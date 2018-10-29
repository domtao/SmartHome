package com.zunder.smart.setting;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DownAlert;
import com.zunder.smart.socket.info.ISocketCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SmartHomeSetting extends Activity {
	private static Activity activity;
	// private SlidingMenu sm;

	int InOutNetSel = 0;
	RadioButton checkAduioLocal = null;
	String jm;
	FrameLayout helpCode;
	TextView backTxt, titleTxt, funTxt, readWords;
	ImageView readBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.system_net);
		activity = this;
		setshow();

	}

	FrameLayout but_upload, but_device;

	private void setshow() {
		readWords = (TextView) findViewById(R.id.readWords);
		readBtn = (ImageView) findViewById(R.id.readBtn);
		backTxt = (TextView) findViewById(R.id.back_item);
		titleTxt = (TextView) findViewById(R.id.title_item);
		funTxt = (TextView) findViewById(R.id.fun_item);
		backTxt.setVisibility(View.GONE);
		titleTxt.setText(getString(R.string.action_settings));
		funTxt.setVisibility(View.GONE);
		FrameLayout but_ip_ok = (FrameLayout) activity
				.findViewById(R.id.bt_normal_dialog_ok);
		FrameLayout but_ip_cal = (FrameLayout) activity
				.findViewById(R.id.bt_normal_dialog_cancle);
		FrameLayout buildDivice = (FrameLayout) activity
				.findViewById(R.id.build_device);
		FrameLayout buildaduio = (FrameLayout) activity
				.findViewById(R.id.build_aduio);
		but_device = (FrameLayout) activity.findViewById(R.id.set_device);
		but_upload = (FrameLayout) activity.findViewById(R.id.set_upload);
		FrameLayout set_luguage = (FrameLayout) activity
				.findViewById(R.id.set_luanguage);
		FrameLayout setting_backup = (FrameLayout) activity
				.findViewById(R.id.setting_backup);
		setting_backup.setOnClickListener(new backup_Onclick());

		FrameLayout setting_video = (FrameLayout) findViewById(R.id.setting_video);

		FrameLayout setting_file = (FrameLayout) findViewById(R.id.setting_file);
		setting_file.setOnClickListener(new file_Onclick());

		helpCode = (FrameLayout) findViewById(R.id.helpCode);
		helpCode.setOnClickListener(new help_OnClick());
		FrameLayout gwayBtn = (FrameLayout) activity.findViewById(R.id.gwayBtn);
		gwayBtn.setOnClickListener(new gway_OnClick());
		CheckBox checkBox = (CheckBox) activity.findViewById(R.id.checkBox);
		checkAduioLocal = (RadioButton) activity.findViewById(R.id.aduio_local);

		final CheckBox mangerBox = (CheckBox) activity
				.findViewById(R.id.mangerBox);
		mangerBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mangerBox.isChecked()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							activity);
					builder.setTitle(getString(R.string.isOpen));
					builder.setPositiveButton(
							activity.getString(R.string.sure),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ProjectUtils.getRootPath()
											.setRootVersion(1);
									ProjectUtils.saveRootPath();
								}
							});
					builder.setNegativeButton(
							activity.getString(R.string.cancle),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									mangerBox.setChecked(false);
								}
							});
					builder.show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							activity);
					builder.setTitle(getString(R.string.isClose));
					builder.setPositiveButton(
							activity.getString(R.string.sure),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ProjectUtils.getRootPath()
											.setRootVersion(0);
									ProjectUtils.saveRootPath();

								}
							});
					builder.setNegativeButton(
							activity.getString(R.string.cancle),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mangerBox.setChecked(true);
								}
							});
					builder.show();

				}
			}
		});
		if (ProjectUtils.rootPath.getRootVersion() == 1) {
			mangerBox.setChecked(true);
		}
		RadioButton checkAduionet = (RadioButton) activity
				.findViewById(R.id.aduio_net);
		final CheckBox checkBox_hx = (CheckBox) activity
				.findViewById(R.id.checkBox_hx);

		if (ProjectUtils.getRootPath().getConnectState() == 0) {
			checkAduioLocal.setChecked(true);
		} else {
			checkAduionet.setChecked(true);
		}
		checkAduioLocal
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						ProjectUtils.getRootPath().setConnectState(
								isChecked ? 0 : 1);
						ProjectUtils.saveRootPath();
					}
				});
		but_ip_ok.setOnClickListener(new Firstbt_okOnClick());
		but_ip_cal.setOnClickListener(new Firstbt_cancleOnClick());
		buildDivice.setOnClickListener(new buildDivice_OnClick());
		buildaduio.setOnClickListener(new buildaduio_OnClick());
		set_luguage.setOnClickListener(new lunguage_OnClick());
		checkBox.setOnCheckedChangeListener(new checkBoxOnCheckedChangeListener());

		if (ProjectUtils.getRootPath().getMessageBack() == 1) {
			checkBox_hx.setChecked(true);
		} else {
			checkBox_hx.setChecked(false);
		}
		checkBox_hx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				ProjectUtils.getRootPath().setMessageBack(isChecked ? 1 : 0);
			}
		});

		readBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	}

	private final class Firstbt_okOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
		}
	}

	private final class Firstbt_cancleOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder2 = new AlertDialog.Builder(
					SmartHomeSetting.this);
			builder2.setTitle(MyApplication.getInstance().getString(
					R.string.timeBroad));
			builder2.setPositiveButton(
					MyApplication.getInstance().getString(R.string.open_2),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) { //

							// sendcmd("*C000AFA1C00FF000401000003");
						}
					});
			builder2.setNeutralButton(
					MyApplication.getInstance().getString(R.string.close_1),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) { //
							// sendcmd("*C000AFA1C00FF000400000003");
						}
					});
			builder2.show();
		}
	}

	private final class buildDivice_OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {

			new AlertDialog.Builder(activity)
					.setTitle(
							MyApplication.getInstance().getString(
									R.string.isSetMachie))
					.setPositiveButton(
							MyApplication.getInstance().getString(
									R.string.action_settings),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TcpSender.sendMssageAF("*AL3");
									// sendcmd("*AL3");
								}
							})
					.setNeutralButton(
							MyApplication.getInstance()
									.getString(R.string.edit),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							})
					.setNegativeButton(
							MyApplication.getInstance().getString(
									R.string.cancle),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		}
		// }
	}

	EditText etpwd;
	String oldPassword = "";

	private final class buildaduio_OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (InOutNetSel == 0) {
			} else {
				Toast.makeText(activity,
						MyApplication.getInstance().getString(R.string.setPwd),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private final class lunguage_OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {

			String[] strings = { getResources().getString(R.string.mandarin),
					getResources().getString(R.string.cantonese),
					getResources().getString(R.string.henanese),
					getResources().getString(R.string.en_us) };
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("��������ʶ������");
			builder.setItems(strings, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						ProjectUtils.getRootPath().setLanguage(0);
						ProjectUtils.saveRootPath();
						break;
					case 1:
						ProjectUtils.getRootPath().setLanguage(1);
						ProjectUtils.saveRootPath();
						break;
					case 2:
						ProjectUtils.getRootPath().setLanguage(2);
						ProjectUtils.saveRootPath();
						break;
					case 3:
						ProjectUtils.getRootPath().setLanguage(3);
						ProjectUtils.saveRootPath();
						break;
					default:
						break;
					}
				}
			}).show();
		}
	}

	private final class gway_OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(activity)
					.setTitle(
							MyApplication.getInstance().getString(
									R.string.isConect))
					.setPositiveButton(
							MyApplication.getInstance().getString(
									R.string.open_2),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// sendcmd("*C000AFA1C00FF00060100003");
								}
							})
					.setNegativeButton(
							MyApplication.getInstance().getString(
									R.string.close_1),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// sendcmd("*C000AFA1C00FF00060000003");
								}
							}).show();
		}
	}

	private final class backup_Onclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DownAlert alert = new DownAlert(activity);
			alert.show();
		}

	}

	private final class file_Onclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			DialogAlert alert = new DialogAlert(activity);

			alert.init(getString(R.string.tip),
					getString(R.string.isCleanDevice));
			alert.setSureListener(new DialogAlert.OnSureListener() {

				@Override
				public void onSure() {
					// TODO Auto-generated method stub
					String deviceID = ProjectUtils.getRootPath().getRootID();
					String result = ISocketCode
							.setDelAllDevice("del", deviceID);
					MainActivity.getInstance().sendCode(result);
				}
				@Override
				public void onCancle() {
					// TODO Auto-generated method stub
				}
			});
			alert.show();
		}
	};

	private final class help_OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {

		}
	}

	private final class test_OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {

		}
	}

	private final class mangerBoxOnCheckedChangeListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(final CompoundButton cb,
				final boolean cheked) {

		}
	}

	private final class checkBoxOnCheckedChangeListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton cb, boolean cheked) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("��������");
			builder.setPositiveButton("����",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

//							MyApplication.getInstance().sendDataCode(
//									ActionCommandParam.openSound());
						}
					});
			builder.setNegativeButton("�ر�",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

//							MyApplication.getInstance().sendDataCode(
//									ActionCommandParam.closeSound());
						}
					});
			builder.show();
		}
	}

}
