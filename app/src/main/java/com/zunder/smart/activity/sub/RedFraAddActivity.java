package com.zunder.smart.activity.sub;

import java.util.List;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.popu.dialog.BandViewWindow;
import com.zunder.smart.activity.popu.dialog.KeyViewWindow;
import com.zunder.smart.activity.popu.dialog.VersionViewWindow;
import com.zunder.smart.adapter.InfraCodeAdapter;
import com.zunder.smart.adapter.InfraNameAdapter;
import com.zunder.smart.adapter.InfraVersionAdapter;
import com.zunder.smart.adapter.TypeAdapter;
import com.zunder.smart.custom.view.MyGridView;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dialog.RedFraAlert;
import com.zunder.smart.dialog.RedFraCloundAlert;
import com.zunder.smart.dialog.TimeAlert;
import com.zunder.smart.listener.RedFralistener;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.InfraCode;
import com.zunder.smart.model.InfraName;
import com.zunder.smart.model.InfraVersion;
import com.zunder.smart.model.ProjectorName;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.SendThread;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.webservice.InfraServiceUtils;
import com.zunder.smart.webservice.ProjectorServiceUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class RedFraAddActivity extends Activity implements OnClickListener,
		RedFralistener {

	private TextView backTxt;
	private TextView editeTxt;
	RelativeLayout typeLayout,bandLayout, versionLayout, keyLayout,codeLayout;
	private TextView typeTxt,bandTxt,versionTxt,keyTxt,codeTxt;
	private Button readCode;

	int id = 0;
	private EditText infraName;
	private TextView infraKey;
	private String Edite = "Add";
	private String infraredName = "";
	private int infraredBrandId ;
	private String infraredBrandName = "";
	private int infraredButtonId;
	private String infraredCode = "";
	private String DeviceId = "";
	private int FatherId;
	private int infraredKey;
	private int infraredStudyType ;
	private int infraredVersionId;
	private String infraredVersionName = "";
	
	public static RedFraAddActivity activity;
	TimeAlert alert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_redfra_add);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle bundle = getIntent().getExtras();
		Edite = bundle.getString("Edite");
		TcpSender.setRedFralistener(this);
		activity = this;
		readCode=(Button)findViewById(R.id.readCode);
		infraName = (EditText) findViewById(R.id.infraName);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		infraKey = (TextView) findViewById(R.id.infraKey);
		codeTxt= (TextView) findViewById(R.id.codeTxt);
		bandTxt= (TextView) findViewById(R.id.bandTxt);
		versionTxt= (TextView) findViewById(R.id.versionTxt);
		keyTxt= (TextView) findViewById(R.id.keyTxt);
		typeTxt=(TextView)findViewById(R.id.typeTxt);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		readCode.setOnClickListener(this);
		typeLayout=(RelativeLayout)findViewById(R.id.typeLayout);
		bandLayout = (RelativeLayout) findViewById(R.id.bandLayout);
		versionLayout = (RelativeLayout) findViewById(R.id.versionLayout);
		keyLayout = (RelativeLayout) findViewById(R.id.keyLayout);
		codeLayout= (RelativeLayout) findViewById(R.id.codeLayout);
		if (Edite.equals("Edite")) {
			id = bundle.getInt("id");
			RedInfra infra = RedInfraFactory.getInstance().getInfra(id);
			if (infra != null) {

                DeviceId = infra.getDeviceId();
				infraKey.setText(infra.getInfraredKey() + "");
				infraName.setText(infra.getInfraredName());
				codeTxt.setText(infra.getInfraredCode());
				infraredKey = infra.getInfraredKey();
				FatherId = infra.getFatherId();
				infraredStudyType = infra.getInfraredStudyType();
				infraredBrandName = infra.getInfraredBrandName();
				infraredVersionName = infra.getInfraredVersionName();
			}
		} else {
			FatherId = bundle.getInt("fatherId");
            DeviceId = bundle.getString("did");
			infraredKey = bundle.getInt("infraKey");
			infraKey.setText(infraredKey + "");
			infraredStudyType=1;
		}
		alert = new TimeAlert(activity);
		alert.setSureListener(new TimeAlert.OnSureListener() {

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
				alert.diss();
			}
		});
		typeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showVideoWindow("类型");
			}
		});

		bandLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new BandDataTask().execute();

			}
		});
		versionLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new VersiobDataTask(infraredBrandId)
						.execute();
			}
		});
		keyLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new CodeDataTask(infraredVersionId)
						.execute();
			}
		});
	}

	private static boolean startflag = false;
	private static int startCount = 0;

	private void startTime() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (startflag) {
					try {
						Thread.sleep(100);
						if (startflag) {
							if (startCount < 200) {
								startCount++;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static String toHex(int number) {
		if (number <= 15) {
			return ("0" + Integer.toHexString(number).toUpperCase());
		} else {
			return Integer.toHexString(number).toUpperCase();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				TcpSender.setRedFralistener(null);
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "Add");
				resultIntent.putExtras(bundle);
				this.setResult(100, resultIntent);
				finish();
				break;
			case R.id.editeTxt:
				save();
				break;
			case R.id.startTime:
				break;
			case R.id.endTime:
				break;
			case R.id.readCode:
				if (Edite.equals("Add")
						&& RedInfraFactory.getInstance().isExite(infraredKey,
						DeviceId) == 1) {
					Toast.makeText(activity, getString(R.string.key_studed), Toast.LENGTH_SHORT)
							.show();
				} else {
					infraredStudyType = 1;
					bandLayout.setVisibility(View.GONE);
					versionLayout.setVisibility(View.GONE);
					keyLayout.setVisibility(View.GONE);
					if (DeviceId.length() == 6) {
						String cmd = "*C0019FA05"
								+ DeviceId + "E0"
								+ toHex(infraredKey + 1)
								+ "0000";
						SendThread send = SendThread.getInstance(cmd);
						new Thread(send).start();
					}
					alert.show(getString(R.string.study_key));
				}
				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TcpSender.setRedFralistener(null);
	}

	public void save() {
		infraredName = infraName.getText().toString();
		infraredKey = Integer.parseInt(infraKey.getText().toString());
		infraredCode = codeTxt.getText().toString();
		RedInfra redInfra = new RedInfra();
		redInfra.setInfraredName(infraredName);
		redInfra.setInfraredImage("");
		redInfra.setDeviceId(DeviceId);
		redInfra.setFatherId(FatherId);
		redInfra.setInfraredIndex(0);
		redInfra.setInfraredBrandId(infraredBrandId);
		redInfra.setInfraredBrandName(infraredBrandName);
		redInfra.setInfraredVersionId(infraredVersionId);
		redInfra.setInfraredVersionName(infraredVersionName);
		redInfra.setInfraredKey(infraredKey);
		redInfra.setInfraredButtonId(infraredButtonId);
		redInfra.setInfraredCode(infraredCode);
		redInfra.setInfraredStudyType(infraredStudyType);
		redInfra.setInfraredX(0);
		redInfra.setInfraredY(0);
		redInfra.setCreationTime("");
		redInfra.setSeqencing(infraredKey);
		if(TextUtils.isEmpty(infraredName)){
			ToastUtils.ShowError(activity, getString(R.string.words_null), Toast.LENGTH_SHORT,true);
			return;
		}
		if (Edite.equals("Add")) {
			if(RedInfraFactory.getInstance().judgeName(infraredName,FatherId)==1){
				ToastUtils.ShowError(activity, getString(R.string.words_exit), Toast.LENGTH_SHORT,true);
				return;
			}
			int result = sql().insertInfrad(redInfra);
			if (result > 0) {
				ToastUtils.ShowSuccess(activity, getString(R.string.red_add_s), Toast.LENGTH_SHORT,true);
				RedInfraFactory.getInstance().clearList();
				// sendNetcode(infrared_key, infraredCode);
				infraredKey = RedInfraFactory.getInstance().getInfraKey(DeviceId);
				infraKey.setText(infraredKey + "");
				codeTxt.setText("");
				infraName.setText("");

					SendCMD cmd = SendCMD.getInstance();
					cmd.sendModeForInfrared(0,redInfra, 2);

			}
		} else {
			if (id > 0) {
				RedInfra r=	RedInfraFactory.getInstance().getInfra(id);
			if(	RedInfraFactory.getInstance().updateName(id,infraredName,r.getInfraredName(),FatherId)>0){
				ToastUtils.ShowError(activity, getString(R.string.words_exit), Toast.LENGTH_SHORT,true);
				return;
			}
				redInfra.setId(id);
				int result = sql().updateRedFra(redInfra);
				if (result > 0) {

					ToastUtils.ShowSuccess(activity, getString(R.string.red_update_s), Toast.LENGTH_SHORT,true);RedInfraFactory.getInstance().clearList();
					SendCMD cmd = SendCMD.getInstance();
					cmd.sendModeForInfrared(0,redInfra, 2);

				} else {
					ToastUtils.ShowError(activity, getString(R.string.red_update_f), Toast.LENGTH_SHORT,true);
				}
			}
		}
	}

	public IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	class BandDataTask extends AsyncTask<String, Integer, List<InfraName>> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected List<InfraName> doInBackground(String... params) {
			List<InfraName> result = null;
			try {
				Thread.sleep(500);
				result = (List<InfraName>) JSONHelper.parseCollection(
						InfraServiceUtils.GetInfraNames(10, 10), List.class,
						InfraName.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<InfraName> result) {
			hideProgressDialog();
			if (result != null && result.size() > 0) {
				final BandViewWindow bandViewWindow=new BandViewWindow(activity,"品牌",result,infraredBrandId);
				bandViewWindow.setAlertViewOnCListener(new BandViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, InfraName infraName) {
						infraredBrandId=infraName.getInfraID();
						infraredBrandName = infraName.getInfraName();
						versionLayout.setVisibility(View.VISIBLE);
						bandTxt.setText(infraName.getInfraName());
						new VersiobDataTask(infraredBrandId)
								.execute();
						bandViewWindow.dismiss();
					}

					@Override
					public void cancle() {

					}
				});
				bandViewWindow.show();
			} else {
				bandLayout.setVisibility(View.GONE);
			}
		}
	}

	class VersiobDataTask extends
			AsyncTask<String, Integer, List<InfraVersion>> {
		int InfraId;

		public VersiobDataTask(int InfraId) {
			this.InfraId = InfraId;
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected List<InfraVersion> doInBackground(String... params) {
			List<InfraVersion> result = null;
			try {
				Thread.sleep(500);
				result = (List<InfraVersion>) JSONHelper.parseCollection(
						InfraServiceUtils.GetInfraVersions(10, 10, InfraId),
						List.class, InfraVersion.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<InfraVersion> result) {
			hideProgressDialog();
			if (result != null && result.size() > 0) {
				final VersionViewWindow versionViewWindow=new VersionViewWindow(activity,"型号",result,0);
				versionViewWindow.setAlertViewOnCListener(new VersionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, InfraVersion infraVersion) {
						infraredVersionId= infraVersion.getId();
						infraredVersionName =infraVersion.getVersionName();
						keyLayout.setVisibility(View.VISIBLE);
						versionTxt.setText(infraVersion.getVersionName());
						new CodeDataTask(infraredVersionId)
								.execute();
						versionViewWindow.dismiss();
					}

					@Override
					public void cancle() {

					}
				});
				versionViewWindow.show();
			} else {
				versionLayout.setVisibility(View.GONE);
			}
		}
	}

	class CodeDataTask extends AsyncTask<String, Integer, List<InfraCode>> {
		private int VersionID;

		public CodeDataTask(int VersionID) {
			this.VersionID = VersionID;
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected List<InfraCode> doInBackground(String... params) {
			List<InfraCode> result = null;
			try {
				Thread.sleep(500);
				result = (List<InfraCode>) JSONHelper.parseCollection(
						InfraServiceUtils.GetInfraCodes(10, 10, VersionID),
						List.class, InfraCode.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<InfraCode> result) {
			hideProgressDialog();
			if (result != null && result.size() > 0) {
				final KeyViewWindow keyViewWindow=new KeyViewWindow(activity,"按键",result,0);
				keyViewWindow.setAlertViewOnCListener(new KeyViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, InfraCode infraCode) {
						infraredButtonId= infraCode.getId();
						infraredCode = infraCode.getInfraCode();
						codeTxt.setText(infraredCode);
						infraName.setText(infraCode.getInfraName());
						keyTxt.setText(infraCode.getInfraName());
						keyViewWindow.dismiss();
					}

					@Override
					public void cancle() {

					}
				});
				keyViewWindow.show();
			} else {
				keyLayout.setVisibility(View.GONE);
			}
		}
	}

	public void showProgressDialog() {
		if (progressDialog == null) {

			progressDialog = ProgressDialog.show(activity,
					activity.getString(R.string.tip),
					activity.getString(R.string.loading), true, false);
		}

		progressDialog.show();

	}

	ProgressDialog progressDialog = null;

	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void setCodeIR(String str) {
		Message msg = handler.obtainMessage();
		msg.what = 0;
		msg.obj = str;
		handler.sendMessage(msg);
	}
	static String[] _IrCode = { "", "", "", "", "", "", "", "" };
	private static String tempStr = "";
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				String getCode = (String) msg.obj;
				int index = Integer.valueOf(getCode.substring(6, 8), 16);
				int len = Integer.valueOf(getCode.substring(8, 10), 16);
				int number = Integer.valueOf(getCode.substring(18, 20), 16);
				if (index == 255) {
					codeTxt.setText("");
					tempStr = "";
					int i = 0;
					for (; i < 8; i++) {
						if (_IrCode[i].length() > 0) {
							tempStr += _IrCode[i];
						} else {
							break;
						}
					}
					if (number == 0 || (number > 0 && number == i)) {
						codeTxt.setText(tempStr);
						alert.diss();
					}
				} else {
					if (index <15) {
						if (index == 0) {
							tempStr = "";
							for (int i = 0; i < 8; i++) {
								_IrCode[i] = "";
							}
						}
						_IrCode[index] = getCode.substring(10, (10 + (len * 2)));
					}
				}
			}
		}
	};
	private void showVideoWindow(final String _title) {

		final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,_title,ListNumBer.getRedFraStudy(),null,0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
			@Override
			public void onItem(int pos, String itemName) {
				switch (pos) {
					case 0: {
						if (Edite.equals("Add")
								&& RedInfraFactory.getInstance().isExite(infraredKey,
								DeviceId) == 1) {
							Toast.makeText(activity, getString(R.string.key_studed), Toast.LENGTH_SHORT)
									.show();
						} else {
							codeTxt.setText("");
							infraredStudyType = 1;
							bandLayout.setVisibility(View.GONE);
							versionLayout.setVisibility(View.GONE);
							keyLayout.setVisibility(View.GONE);
							codeLayout.setVisibility(View.VISIBLE);
							if (DeviceId.length() == 6) {
								String cmd = "*C0019FA05"
										+ DeviceId + "E0"
										+ toHex(infraredKey + 1)
										+ "0000";
								SendThread send = SendThread.getInstance(cmd);
								new Thread(send).start();
							}
							alert.show(getString(R.string.study_key));
						}
					}
					break;
					case 1: {

						codeTxt.setText("");
						infraredStudyType = 2;
						bandLayout.setVisibility(View.VISIBLE);
						versionLayout.setVisibility(View.GONE);
						keyLayout.setVisibility(View.GONE);
						codeLayout.setVisibility(View.GONE);
						new BandDataTask().execute();
						break;
					}
					case 2: {
						infraredStudyType = 3;
						codeTxt.setText("");
						bandLayout.setVisibility(View.GONE);
						versionLayout.setVisibility(View.GONE);
						keyLayout.setVisibility(View.GONE);
						codeLayout.setVisibility(View.GONE);
						final RedFraAlert alert = new RedFraAlert(activity);
						alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.local));
						alert.setOnSureListener(new RedFraAlert.OnSureListener() {
							@Override
							public void onSure(String fileName) {
								// TODO Auto-generated method stub
								String codeStr = fileName.trim();
								codeTxt.setText(toHex(128 + (codeStr.length() / 2)) + codeStr);
								alert.dismiss();
							}

							@Override
							public void onCancle() {
								// TODO Auto-generated method stub

							}
						});
						alert.show();
					}
					break;
					case 3: {
						codeTxt.setText("");
						bandLayout.setVisibility(View.GONE);
						versionLayout.setVisibility(View.GONE);
						keyLayout.setVisibility(View.GONE);
						codeLayout.setVisibility(View.GONE);
						infraredStudyType = 4;
						new NamesAsyncTask().execute();
					}
					break;
				}
				typeTxt.setText(itemName);
				alertViewWindow.dismiss();
			}});
		alertViewWindow.show();
	}
	public class NamesAsyncTask extends AsyncTask<String, Void, List<ProjectorName>> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<ProjectorName> doInBackground(String... params) {
			List<ProjectorName> list = null;
			try {
				String json = ProjectorServiceUtils.getProjectors(2);
				list = (List<ProjectorName>) JSONHelper.parseCollection(json,
						List.class, ProjectorName.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<ProjectorName> result) {
			String msg;
			if(result!=null&&result.size()>0){
			final 	 RedFraCloundAlert alert=new RedFraCloundAlert(activity,result);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.cloud));
				alert.setOnSureListener(new RedFraCloundAlert.OnSureListener() {
					@Override
					public void onSure(String name,String code){
						// TODO Auto-generated method stub
						infraName.setText(name);
						String codeStr=code.trim();
						codeTxt.setText(toHex(128+(codeStr.length()/2))+codeStr);
						alert.dismiss();
					}
					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();
			}else{
				ToastUtils.ShowError(activity,"云端没有上传品牌",Toast.LENGTH_SHORT,true);
			}
		}
	}
}
