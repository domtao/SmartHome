/**
 * 
 */
package com.zunder.smart.aiui.activity;

import java.util.ArrayList;
import java.util.List;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.adapter.SongAdapter;
import com.zunder.smart.aiui.info.Song;
import com.zunder.smart.custom.view.SuperListView;
import com.zunder.smart.custom.view.SuperListView.OnRefreshListener;
import com.zunder.smart.dialog.MusicPlayMedia;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.forward.SongServiceUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
public class SongActivity extends Activity implements OnRefreshListener,
		OnClickListener {
	SuperListView listView;
	SongActivity activity;
	private List<Song> list = new ArrayList<Song>();
	SongAdapter adapter;
	private static String songType = "song";
	private static String title = "音乐";
	TextView titleView;
	TextView backTxt, tipTxt;
	static ImageView musicPre, musicNext, musicStop;
//	LinearLayout playLayout;
	int index = 0;
	int playType=1;

	public static void startActivity(Activity activity, String _songType,
			String _title) {
		songType = _songType;
		title = _title;
		Intent intent = new Intent(activity, SongActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aiui_song_list);
		activity = this;
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// startTime();
	}

	private void initView() {
		tipTxt = (TextView) findViewById(R.id.tipTxt);
		backTxt = (TextView) findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
//		playLayout = (LinearLayout) findViewById(R.id.playLayout);
		titleView = (TextView) findViewById(R.id.titleTxt);
		musicPre = (ImageView) findViewById(R.id.musicPre);
		musicStop = (ImageView) findViewById(R.id.musicStop);
		musicNext = (ImageView) findViewById(R.id.musicNext);
		listView = (SuperListView) findViewById(R.id.songList);
		listView.setOnRefreshListener(this);
		adapter = new SongAdapter(activity, list);
		listView.setAdapter(adapter);
		titleView.setText(title);
		listView.doRefresh();
		musicPre.setOnClickListener(this);
		musicNext.setOnClickListener(this);
		musicStop.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
		case R.id.musicPre:
			if (index <= 0) {
				Toast.makeText(activity, getString(R.string.isOne), Toast.LENGTH_SHORT).show();
			} else {
				index--;
				play(list.get(index).getSongName(), list.get(index)
						.getSongUrl(), index,playType);
			}
			break;
		case R.id.musicStop:
			if(playType==0) {
				if (MusicPlayMedia.getInstansMedia().isPlaying()) {
					MusicPlayMedia.getInstansMedia().stop();
					musicStop.setImageResource(R.mipmap.stop_pause);
				}
			}else {
				String result = ISocketCode.setForwardSong("stop:"+getString(R.string.stopPlay),
						AiuiMainActivity.deviceID);
				MainActivity.getInstance().sendCode(result);
				ToastUtils.ShowSuccess(activity, getString(R.string.stopPlay), Toast.LENGTH_SHORT, true);
				musicStop.setImageResource(R.mipmap.stop_pause);
			}
			adapter.changeIndex(-1);
			break;
		case R.id.musicNext:
			if (index >= list.size()) {
				Toast.makeText(activity, getString(R.string.isLast), Toast.LENGTH_SHORT).show();
			} else {
				index++;
				play(list.get(index).getSongName(), list.get(index)
						.getSongUrl(), index,playType);
			}
			break;
		default:
			break;
		}
	}

	public void play(String titleName, String url, int index,int _playType) {
		try {
			playType=_playType;

			musicStop.setImageResource(R.mipmap.stop_play);
			tipTxt.setText(getString(R.string.playing) + titleName);
			if(_playType==0) {
				MusicPlayMedia.getInstansMedia().play(url);
			}else {
				if (songType.equals("poetry")) {
					String result = ISocketCode.setForwardSong("poetry:" +titleName+ url,
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
				}else{
					String result = ISocketCode.setForwardSong("song:" +url,
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
				}
				ToastUtils.ShowSuccess(activity, getString(R.string.push_dd), Toast.LENGTH_SHORT, true);
			}
			adapter.changeIndex(index);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		list.clear();
		new SongAsyncTask(1).execute();
	}

	private ProgressDialog progressDialog;

	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(activity, getString(R.string.tip), getString(R.string.dataloading), true,
					false);
		}
		progressDialog.show();

	}

	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	@Override
	public void onShowNextPage(int pageNum) {
		// TODO Auto-generated method stub
		new SongAsyncTask(pageNum).execute();

	}
	public class SongAsyncTask extends AsyncTask<String, Void, List<Song>> {
		private int pageNum;
		SongAsyncTask(int _pageNum) {
			this.pageNum = _pageNum;
		}
		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected List<Song> doInBackground(String... params) {
			List<Song> listUser = null;
			try {
				String json = SongServiceUtils.getSongs(songType, pageNum, 20);
				listUser = (List<Song>) JSONHelper.parseCollection(json,
						List.class, Song.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return listUser;
		}

		@Override
		protected void onPostExecute(List<Song> result) {
			hideProgressDialog();
			listView.refreshComplete();
			if (result != null && result.size() > 0) {

				list.addAll(result);
				adapter.notifyDataSetChanged();

			} else {
				if (list.size() == 0) {

					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip),
							getString(R.string.noupdata));
					verifyAlert.show();
				}
			}

		}
	}

	public void back() {
		MusicPlayMedia.getInstansMedia().stop();
		finish();
	}
}