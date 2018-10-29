/**
 * 
 */
package com.zunder.smart.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.adapter.HistoryAdapter;
import com.zunder.smart.adapter.SafeInfoAdapter;
import com.zunder.smart.custom.view.SuperListView;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.json.SafeInfoUtils;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.History;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.HistoryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class SafeInfoActivity extends Activity implements SuperListView.OnRefreshListener,
		OnClickListener {
	SuperListView listView;
	SafeInfoActivity activity;
	private List<History> list = new ArrayList<History>();
	SafeInfoAdapter adapter;
	TextView titleView;
	TextView backTxt,editeTxt;
	private String primaryKey = "";
	private String MasterMac="";
	private String MasterName="";
	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, SafeInfoActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_history);

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
		editeTxt=(TextView)findViewById(R.id.editeTxt);
		backTxt = (TextView) findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		titleView = (TextView) findViewById(R.id.titleTxt);
		titleView.setText("安防记录");
				listView = (SuperListView) findViewById(R.id.songList);
		listView.setOnRefreshListener(this);
		adapter = new SafeInfoAdapter(activity, list);
		listView.setAdapter(adapter);
	listView.doRefresh();

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
			case R.id.editeTxt:
				DialogAlert alert = new DialogAlert(activity);
				alert.init(activity.getString(R.string.tip), "是否清历史记录");
				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						SafeInfoUtils.clear();
						list.clear();
						adapter = new SafeInfoAdapter(activity, list);
						listView.setAdapter(adapter);
						listView.doRefresh();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();
				break;
		default:
			break;
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

			progressDialog = ProgressDialog.show(activity, getString(R.string.tip), getString(R.string.load_data), true,
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

	public class SongAsyncTask extends AsyncTask<String, Void, List<History>> {
		private int pageNum;

		SongAsyncTask(int _pageNum) {
			this.pageNum = _pageNum;
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected List<History> doInBackground(String... params) {
			List<History> listUser = null;
			try {
				listUser = SafeInfoUtils.list1;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return listUser;
		}

		@Override
		protected void onPostExecute(List<History> result) {
			hideProgressDialog();
			listView.refreshComplete();
			if (result != null && result.size() > 0) {

				list.addAll(result);
				adapter.notifyDataSetChanged();

			} else {
				if (list.size() == 0) {

					TipAlert verifyAlert = new TipAlert(activity,
							getResources().getString(R.string.tip),
							"没有历史记录");
					verifyAlert.show();
				}
			}

		}
	}

	public void back() {


		finish();
	}
}