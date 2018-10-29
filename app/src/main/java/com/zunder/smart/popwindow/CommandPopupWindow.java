package com.zunder.smart.popwindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.aiui.activity.SubscribeAddActivity;
import com.zunder.smart.aiui.activity.VoiceAddActivity;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dialog.CommandEditTxtAlert;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.aiui.activity.SubscribeAddActivity;
import com.zunder.smart.aiui.activity.VoiceAddActivity;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dialog.CommandEditTxtAlert;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.utils.ListNumBer;

import java.util.List;

public class CommandPopupWindow implements OnClickListener {

	private Activity activity;
	private PopupWindow popupWindow;
	View pupView;
	int index = 0;
	int sfromBtn;
	private List<String> list;

	public CommandPopupWindow(Activity activity, String name,int fromBtn) {
		super();
		this.activity = activity;
		sfromBtn=fromBtn;
		init(name);
	}
	private OnOCListener onOCListene=null;
	public boolean isShow() {
		return popupWindow.isShowing();
	}

	private void init(String name) {
		// TODO Auto-generated method stub
		pupView = ((Activity) activity).getLayoutInflater().inflate(
				R.layout.popwindow_camara_layout, null);
		TextView save = (TextView) pupView.findViewById(R.id.save);
		TextView title = (TextView) pupView.findViewById(R.id.titleTxt);
		TextView back=(TextView)pupView.findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		title.setText(name);
		TextView showTxt = (TextView) pupView.findViewById(R.id.showTxt);
		if(sfromBtn==2){
			list= ModeFactory.getInstance().getModeName();
		}else {
			list = ListNumBer.getCommand(sfromBtn);
		}
		WheelView wheel_camera = (WheelView) pupView
				.findViewById(R.id.wheel_camera);
		wheel_camera.setOffset(2);
		wheel_camera.setItems(list);
		wheel_camera.setSeletion(0);
		wheel_camera.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						index=selectedIndex-2;
					}
				});
		save.setOnClickListener(this);
		popupWindow = new PopupWindow(pupView,
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow.setOutsideTouchable(true);
	}

	public void show() {
		popupWindow.showAtLocation(pupView, Gravity.BOTTOM, 0, 0);
	}

	public void dismiss() {
		popupWindow.dismiss();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.back:
				dismiss();
				break;
		case R.id.save:
			String str=list.get(index);
			if(sfromBtn==0) {
				switch (index) {
					case 0:
                    case 7:
						if(activity instanceof VoiceAddActivity) {
							((VoiceAddActivity)activity).setTxt(str);
						}	else if(activity instanceof SubscribeAddActivity) {
							((SubscribeAddActivity)activity).setTxt(str);
						}
						dismiss();
						break;
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					{
						CommandEditTxtAlert cm = new CommandEditTxtAlert(activity);
						cm.setTitle(R.mipmap.info_systemset, str, index, null, null);
						cm.setOnSureListener(new CommandEditTxtAlert.OnSureListener() {
							@Override
							public void onCancle() {
							}
							@Override
							public void onSure(String str) {
								if (activity instanceof VoiceAddActivity) {
									((VoiceAddActivity) activity).setTxt(str);
								} else if (activity instanceof SubscribeAddActivity) {
									((SubscribeAddActivity) activity).setTxt(str);
								}
								dismiss();
							}
						});
						cm.show();
					}
						break;
					case 6:{
						CommandEditTxtAlert cm = new CommandEditTxtAlert(activity);
						cm.setTitle(R.mipmap.info_systemset, str, index, null, null);
						cm.setOnSureListener(new CommandEditTxtAlert.OnSureListener() {
							@Override
							public void onCancle() {
							}
							@Override
							public void onSure(String str) {
								if(activity instanceof  VoiceAddActivity ) {
									((VoiceAddActivity)activity).setTxt(str);
								}	else if(activity instanceof SubscribeAddActivity) {
									((SubscribeAddActivity)activity).setTxt(str);
								}
								dismiss();
							}
						});
						cm.show();
					}
					break;
					case 8: {
						CommandEditTxtAlert pm = new CommandEditTxtAlert(activity);
						pm.setTitle(R.mipmap.info_systemset, str, index, activity.getString(R.string.excellent), activity.getString(R.string.good));
						pm.setOnSureListener(new CommandEditTxtAlert.OnSureListener() {
							@Override
							public void onCancle() {
							}
							@Override
							public void onSure(String str) {
								if (activity instanceof VoiceAddActivity) {
									((VoiceAddActivity) activity).setTxt(str);
								} else if (activity instanceof SubscribeAddActivity) {
									((SubscribeAddActivity) activity).setTxt(str);
								}
								dismiss();
							}
						});
						pm.show();
					}
						break;
					case 9:

						showPopupWindow(str);
						dismiss();
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						CommandEditTxtAlert time = new CommandEditTxtAlert(activity);
						time.setTitle(R.mipmap.info_systemset, str, index, "17", null);
						time.setOnSureListener(new CommandEditTxtAlert.OnSureListener() {
							@Override
							public void onCancle() {
							}
							@Override
							public void onSure(String str) {
								if(activity instanceof  VoiceAddActivity ) {
									((VoiceAddActivity)activity).setTxt(str);
								}	else if(activity instanceof SubscribeAddActivity) {
									((SubscribeAddActivity)activity).setTxt(str);
								}dismiss();
							}
						});
						time.show();
						break;
					case 16:
						AskTimeSch(str);
						break;
					case 17:
					case 18:
						CommandEditTxtAlert cm1 = new CommandEditTxtAlert(activity);
						cm1.setTitle(R.mipmap.info_systemset, str, index, "17", null);
						cm1.setOnSureListener(new CommandEditTxtAlert.OnSureListener() {
							@Override
							public void onCancle() {
							}
							@Override
							public void onSure(String str) {
								if(activity instanceof  VoiceAddActivity ) {
									((VoiceAddActivity)activity).setTxt(str);
								}	else if(activity instanceof SubscribeAddActivity) {
									((SubscribeAddActivity)activity).setTxt(str);
								}
								dismiss();
							}
						});
						cm1.show();
						break;
					default:
						break;
				}
			}else if(sfromBtn==1){
				if(str.equals("#")){
					CommandEditTxtAlert cm1 = new CommandEditTxtAlert(activity);
					cm1.setTitle(R.mipmap.info_systemset, str, 16, activity.getString(R.string.hour), activity.getString(R.string.minute));
					cm1.setOnSureListener(new CommandEditTxtAlert.OnSureListener() {
						@Override
						public void onCancle() {
						}
						@Override
						public void onSure(String str) {
							if(activity instanceof  VoiceAddActivity ) {
								((VoiceAddActivity)activity).setTxt(str);
							}	else if(activity instanceof SubscribeAddActivity) {
								((SubscribeAddActivity)activity).setTxt(str);
							}
							dismiss();
						}
					});
					cm1.show();
				}else {
					if (activity instanceof VoiceAddActivity) {
						((VoiceAddActivity) activity).setTxt(str);
					} else if (activity instanceof SubscribeAddActivity) {
						((SubscribeAddActivity) activity).setTxt(str);
					}
					dismiss();
				}
			}else if(sfromBtn==2){
				if (activity instanceof VoiceAddActivity) {
					((VoiceAddActivity) activity).setTxt(str);
				} else if (activity instanceof SubscribeAddActivity) {
					((SubscribeAddActivity) activity).setTxt(str);
				}
				dismiss();
			}
			break;
		case R.id.backTxt:
			dismiss();
			break;
		default:
			break;
		}
	}

	public void setOnOCListene(OnOCListener onOCListene) {
		this.onOCListene = onOCListene;
	}

	public interface OnOCListener {
		public void onOpen();
		public void onClose();
	}
	private void AskTimeSch(final String str) {
		final String items[] = (String[]) activity.getResources()
				.getStringArray(R.array.weeken);
		final boolean[] selected = new boolean[items.length];
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(R.mipmap.img_air_time_meihong);
		builder.setTitle(str);
		builder.setMultiChoiceItems(items, selected,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
										boolean isChecked) {
						// TODO Auto-generated method stub
						selected[which] = isChecked;
					}
				});
		builder.setPositiveButton(activity.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

								String tempWeek = "";
								for (int i = 0; i < selected.length; i++) {
									if (selected[i]) {
										tempWeek += items[i] ;
									}
								}
								if (tempWeek == "") {
									return;
								}
						if(activity instanceof  VoiceAddActivity ) {
							((VoiceAddActivity)activity).setTxt(str.substring(0,2)+tempWeek);
						}	else if(activity instanceof SubscribeAddActivity) {
							((SubscribeAddActivity)activity).setTxt(str.substring(0,2)+tempWeek);
						}
						dismiss();

					}
				});
		builder.setNegativeButton(activity.getString(R.string.cancle),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
		builder.create().show();

	}

	PopupWindow popWindow = null;
	PopupWindow popModeWindow = null;
	String startHourStr = "00";
	String startMinuteStr = "00";
	String endHourStr = "00";
	String endMinuteStr = "00";
	View timeview;
	private void showPopupWindow(final String str) {
		if (popWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			timeview = layoutInflater.inflate(R.layout.popwindow_time_layout,
					null);
			popWindow = new PopupWindow(timeview,
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			TextView save = (TextView) timeview.findViewById(R.id.save);

			WheelView startHour = (WheelView) timeview
					.findViewById(R.id.start_hour);
			final WheelView startSecond = (WheelView) timeview
					.findViewById(R.id.start_second);
			startHour.setItems(ListNumBer.getHour());
			startHour.setOffset(2);
			startHour.setSeletion(0);

			startSecond.setItems(ListNumBer.getMinit());
			startSecond.setOffset(2);
			startSecond.setSeletion(0);
			startHour
					.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

						@Override
						public void onSelected(int selectedIndex, String item) {
							startHourStr = item;
						}
					});
			startSecond
					.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
						@Override
						public void onSelected(int selectedIndex, String item) {
							startMinuteStr = item;
						}
					});

			final WheelView endHour = (WheelView) timeview.findViewById(R.id.end_hour);
			final WheelView endSecond = (WheelView) timeview
					.findViewById(R.id.end_second);
			endHour.setItems(ListNumBer.getHour());
			endHour.setOffset(2);
			endHour.setSeletion(0);

			endSecond.setItems(ListNumBer.getMinit());
			endSecond.setOffset(2);
			endSecond.setSeletion(0);
			endHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

				@Override
				public void onSelected(int selectedIndex, String item) {
					endHourStr = item;
				}
			});
			endSecond
					.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
						@Override
						public void onSelected(int selectedIndex, String item) {
							endMinuteStr = item;
						}
					});
			save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String sart = startHourStr + ":" + startMinuteStr;
					String end = endHourStr + ":" + endMinuteStr;
//					if (timeConvert(end) >= timeConvert(sart)) {
						if(activity instanceof  VoiceAddActivity ) {
							((VoiceAddActivity)activity).setTxt(str+"("+sart+"--"+end+")");
						}	else if(activity instanceof SubscribeAddActivity) {
							((SubscribeAddActivity)activity).setTxt(str+"("+sart+"--"+end+")");
						}
						///dismiss();
						popWindow.dismiss();

				}
			});
		}
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});

		popWindow.setTouchInterceptor(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				return false;
			}
		});
		popWindow.showAtLocation(timeview, Gravity.BOTTOM, 0, 0);
	}
	private int timeConvert(String timeStr) {
		if (timeStr == "" || timeStr.equals("")) {
			return 0;
		}
		String[] timeStrs = timeStr.split(":");
		return Integer.parseInt(timeStrs[0]) * 60
				+ Integer.parseInt(timeStrs[1]);
	}
}
