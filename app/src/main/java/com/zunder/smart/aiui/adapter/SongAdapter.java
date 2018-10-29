package com.zunder.smart.aiui.adapter;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.aiui.activity.SongActivity;
import com.zunder.smart.aiui.info.Song;
import com.zunder.smart.dialog.ButtonAlert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter {

	List<Song> list;
	int mSelect = -1;
	int mEdite = -1;
	SongActivity context;

	public SongAdapter(SongActivity context, List<Song> list) {
		this.list = list;
		this.context = context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public void changeIndex(int index) {
		if (mSelect != index) {
			mSelect = index;
			notifyDataSetChanged();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewCach viewCach = null;
		if (convertView == null) {
			viewCach = new ViewCach();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_song, null);
			viewCach.songAuthor = (TextView) convertView
					.findViewById(R.id.songAuthor);
			viewCach.songTitle = (TextView) convertView
					.findViewById(R.id.songTitle);
			viewCach.img = (ImageView) convertView.findViewById(R.id.img);
			viewCach.songPlay = (ImageView) convertView
					.findViewById(R.id.songPlay);
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}
		viewCach.songAuthor.setText(list.get(position).getSongAuthor());
		viewCach.songTitle.setText(list.get(position).getSongName());
		String songType = list.get(position).getSongType();
		if (songType.equals("poetry")) {
			viewCach.img.setImageResource(R.mipmap.poetry_img);
		}
		if (songType.equals("joke")) {
			viewCach.img.setImageResource(R.mipmap.chart_image);
		}
		if (songType.equals("strory")) {
			viewCach.img.setImageResource(R.mipmap.story_image);
		}
		if (songType.equals("music")) {
			viewCach.img.setImageResource(R.mipmap.music_image);
		}
		if (mSelect == position) {
			viewCach.songPlay.setImageResource(R.mipmap.stop_play);
		} else {
			viewCach.songPlay.setImageResource(R.mipmap.music_play);
		}

		viewCach.songPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					if (list.get(position).getSongType().equals("poetry")) {
//						String result = ISocketCode.setForwardSong("poetry:"+list.get(position).getSongName()+list.get(position).getSongUrl(),
//								AiuiMainActivity.deviceID);
//						MainActivity.getInstance().sendCode(result);
//						changeIndex(position);
//						ToastUtils.ShowSuccess(context,"推送到叮咚", Toast.LENGTH_SHORT,true);
						context.play(list.get(position).getSongName(),
								list.get(position).SongUrl, position,1);
					} else {
						final ButtonAlert buttonAlert = new ButtonAlert(context);
						buttonAlert.setTitle(R.mipmap.info_systemset,context.getString(R.string.tip), context.getString(R.string.play));
						buttonAlert.setButton(context.getString(R.string.localplay), context.getString(R.string.remoteplay), context.getString(R.string.cancle));
						buttonAlert.setVisible(View.VISIBLE, View.VISIBLE,
								View.VISIBLE);
						buttonAlert
								.setOnSureListener(new ButtonAlert.OnSureListener() {
									@Override
									public void onSure() {
										// TODO Auto-generated method stub
								;
										context.play(list.get(position).getSongName(),
												list.get(position).SongUrl, position,0);
										buttonAlert.dismiss();
									}
									@Override
									public void onSearch() {
										// TODO Auto-generated method stub

										context.play(list.get(position).getSongName(),
												list.get(position).SongUrl, position,1);
										buttonAlert.dismiss();
									}
									@Override
									public void onCancle() {
										// TODO Auto-generated method stub
									}
								});
						buttonAlert.show();


					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return convertView;
	}

	private final class ViewCach {

		TextView songAuthor;
		TextView songTitle;
		TextView songUrl;

		ImageView img;
		ImageView songPlay;
	}

}
