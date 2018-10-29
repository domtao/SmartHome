package com.zunder.smart.activity.backup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.adapter.BackQueryAdapter;
import com.zunder.smart.adapter.BackupAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.WidgetDAOProxy;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.dialog.EditTxtTwoAlert;
import com.zunder.smart.dialog.ProgressAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.json.Constants;
import com.zunder.smart.json.ProCaseUtils;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Archive;
import com.zunder.smart.model.ProCase;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.view.ListViewDecoration;
import com.zunder.smart.webservice.ProjectServiceUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BackClientActivity extends Activity implements OnClickListener {

	private ProgressAlert progressAlert;
	TextView backTxt, editeTxt;
	BackupAdapter adapter;
	private BackClientActivity context;
	SwipeMenuRecyclerView listView;
	private List<Archive> list=new ArrayList <Archive>();
	private EditText phoneEdit;
	private EditText pwdEdit;
	private ImageView sureBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_backclient_list);
		this.context = this;
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		editeTxt.setOnClickListener(this);
		backTxt = (TextView) findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		listView = (SwipeMenuRecyclerView) findViewById(R.id.backupList);
		listView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		phoneEdit = (EditText) findViewById(R.id.phoneEdit);
		pwdEdit = (EditText) findViewById(R.id.pwdEdit);
		sureBtn = (ImageView) findViewById(R.id.sureBtn);
		sureBtn.setOnClickListener(this);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return false;
	}


	class ClientDataTask extends AsyncTask<String, Integer, List<Archive>> {
		private String userName;
		private String pwd;
		ClientDataTask(String userNsme, String pwd) {
			this.userName = userNsme;
			this.pwd = pwd;
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog("数据加载中");
		}

		@Override
		protected List<Archive> doInBackground(String... params) {
			List<Archive> result = null;
			try {
				String json = ProjectServiceUtils.getClientProjects(userName,
						pwd);
				JSONObject object = new JSONObject(json);
				if (object.getInt("ResultCode") == 1) {
					result = (List<Archive>) JSONHelper.parseCollection(
							object.getString("Msg"), List.class, Archive.class);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<Archive> result) {
			hideProgressDialog();
			if (result != null) {
				list = result;
				if (list.size() == 0) {
					TipAlert alert = new TipAlert(context,
							getString(R.string.tip), getString(R.string.noBack)
									+ "!");
					alert.show();
				} else {
					listView.setSwipeMenuCreator(null);
					adapter = new BackupAdapter( list);
					adapter.setOnItemClickListener(onItemClickListener);
					listView.setAdapter(adapter);
				}
			}
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			this.finish();
			break;
		case R.id.sureBtn:
			if (TextUtils.isEmpty(phoneEdit.getText())) {
				ToastUtils.ShowError(context,
						getString(R.string.noUserNull) + "!",
						Toast.LENGTH_SHORT,true);
			}else if (TextUtils.isEmpty(pwdEdit.getText())) {
				ToastUtils.ShowError(context, getString(R.string.pwd_no_empty), Toast.LENGTH_SHORT,true);

			} else {
				new ClientDataTask(phoneEdit.getText().toString(), pwdEdit.getText().toString()).execute();

			}
			break;
		default:
			break;
		}
	}

	public IWidgetDAO Sqlite() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	public void showProgressDialog(String msg) {
		if (progressAlert == null) {
			progressAlert=new ProgressAlert(context);
			progressAlert.setProgressAlertFace(new ProgressAlert.ProgressAlertFace() {
				@Override
				public void close() {
					progressAlert.dismiss();
				}
			});
		}
		progressAlert.showAlert(msg);
		progressAlert.show();
	}

	public void hideProgressDialog() {
		if (progressAlert != null && progressAlert.isShowing()) {
			progressAlert.dismiss();
		}

	}

	private void unhideenDiaog(DialogInterface dialog, Boolean flg) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, flg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			downData(list.get(position).getProjectName(),list.get(position).getProjectKey());
		}
	};

	ProCase proCase=null;
	void downData(final String ProjectName,final String ProjectKey) {
		proCase = ProCaseUtils.getInstance().judgeName(ProjectName);
		if (proCase != null) {
			final ButtonAlert buttonAlert=new ButtonAlert(context);
			buttonAlert.setButton("确定","新建","取消");
			buttonAlert.setTitle(0,context.getString(R.string.tip), "[" + ProjectName + "]已经存在,是否覆盖所有数据");
			buttonAlert.setOnSureListener(new ButtonAlert.OnSureListener() {
				@Override
				public void onCancle() {
					buttonAlert.dismiss();
				}

				@Override
				public void onSearch() {
					buttonAlert.dismiss();
					final EditTxtAlert editTxtAlert=new EditTxtAlert(context);
					editTxtAlert.setEditTextType(InputType.TYPE_CLASS_TEXT);
					editTxtAlert.setHint("请输入专案名称");
					editTxtAlert.setOnSureListener(new EditTxtAlert.OnSureListener() {
						@Override
						public void onCancle() {

						}

						@Override
						public void onSure(String str) {
							proCase = ProCaseUtils.getInstance().judgeName(str);
							if(proCase!=null){
								ToastUtils.ShowError(context,"名称已经存在,请重新输入",Toast.LENGTH_SHORT,true);
								return;
							}
							newFile(str,ProjectKey);
							editTxtAlert.dismiss();
						}
					});
					editTxtAlert.show();
				}

				@Override
				public void onSure() {
					buttonAlert.dismiss();
					ProCaseUtils.getInstance().updateProCaseIndex(1, proCase.getProCaseKey());
					WidgetDAOProxy.instance = null;
					String fileName = proCase.getProCaseAlia();
					String rootPath = MyApplication.getInstance()
							.getRootPath()
							+ File.separator
							+ fileName
							+ File.separator + "homedatabases.db";
					ProjectUtils.getRootPath().setRootPath(rootPath);
					ProjectUtils.getRootPath().setRootName(proCase.getProCaseName());
					ProjectUtils.getRootPath().setRootImage(proCase.getProCaseImage());
					ProjectUtils.saveRootPath();
					DataDownActivity downActivity = new DataDownActivity(
							context, ProjectKey);

					downActivity
							.setSureListener(new DataDownActivity.OnSureListener() {

								@Override
								public void onSure() {
									// TODO Auto-generated method
									// stub
									TipAlert alert1 = new TipAlert(context,
											getString(R.string.tip), getString(R.string.downsuccess));
									alert1.show();
									MainActivity.getInstance().updateFresh();
								}

								@Override
								public void onCancle() {
									// TODO Auto-generated method
									// stub
									TipAlert alert1 = new TipAlert(context,
											getString(R.string.tip), getString(R.string.downfail));
									alert1.show();
									MainActivity.getInstance().updateFresh();
								}
							});
					downActivity.show();
				}
			});
			buttonAlert.show();
		} else {
			String proCaseKey = AppTools.getSystemTime();
			proCase = new ProCase();
			proCase.setProCaseName(ProjectName);
			proCase.setProCaseAlia(ProjectName);
			proCase.setProCaseKey(proCaseKey);
			proCase.setProCaseImage(Constants.PROCASEIMAGEPATH);
			DialogAlert alert = new DialogAlert(context);
			alert.init(context.getString(R.string.tip), "是否下载数据[" + ProjectName + "]");
			alert.setSureListener(new DialogAlert.OnSureListener() {
				@Override
				public void onCancle() {

				}

				@Override
				public void onSure() {
					ProCaseUtils.getInstance().addProCase(proCase);
					String path = MyApplication.getInstance().getRootPath();
					File f = new File(path);
					if (!f.exists()) {
						f.mkdir();
					}
					File sd = new File(path, ProjectName);
					if (!sd.exists()) {
						sd.mkdir();
					}
					String fileName = path
							+ File.separator + ProjectName + File.separator
							+ "homedatabases.db";
					FileOutputStream fos = null;
					InputStream is = null;
					try {
						is = MyApplication.getInstance().getResources()
								.openRawResource(R.raw.homedatabases);
						fos = new FileOutputStream(fileName);
						byte[] buffer = new byte[8192];
						int count = 0;
						while ((count = is.read(buffer)) > 0) {
							fos.write(buffer, 0, count);
							fos.flush();
						}
					} catch (Exception e) {
						System.out.println(e.toString());
					}
					ProCaseUtils.getInstance().updateProCaseIndex(1, proCase.getProCaseKey());
					WidgetDAOProxy.instance = null;

					String rootPath = MyApplication.getInstance()
							.getRootPath()
							+ File.separator
							+  proCase.getProCaseAlia()
							+ File.separator + "homedatabases.db";
					ProjectUtils.getRootPath().setRootPath(rootPath);
					ProjectUtils.getRootPath().setRootName(proCase.getProCaseName());
					ProjectUtils.getRootPath().setRootImage(proCase.getProCaseImage());
					ProjectUtils.saveRootPath();
//					MainActivity.getInstance().updateFresh();

					DataDownActivity downActivity = new DataDownActivity(
							context, ProjectKey);

					downActivity
							.setSureListener(new DataDownActivity.OnSureListener() {

								@Override
								public void onSure() {
									// TODO Auto-generated method
									// stub
									MainActivity.getInstance().updateFresh();
									TipAlert alert1 = new TipAlert(context,
											getString(R.string.tip), getString(R.string.downsuccess));
									alert1.show();
								}

								@Override
								public void onCancle() {
									// TODO Auto-generated method
									// stub
									MainActivity.getInstance().updateFresh();
									TipAlert alert1 = new TipAlert(context,
											getString(R.string.tip), getString(R.string.downfail));
									alert1.show();
								}
							});
					downActivity.show();
				}
			});
			alert.show();

			ToastUtils.ShowSuccess(context, getString(R.string.addSuccess),
					Toast.LENGTH_SHORT, true);
			if (MainActivity.getInstance().mHost.getCurrentTab() == 0) {
				TabMainActivity.getInstance().proCaseFragment.setAdpapter();
			} else {
				TabHomeActivity.getInstance().proCaseFragment.setAdpapter();
			}
		}
	}
	public void newFile(final String ProjectName,final String ProjectKey){
		String proCaseKey = AppTools.getSystemTime();
		proCase = new ProCase();
		proCase.setProCaseName(ProjectName);
		proCase.setProCaseAlia(ProjectName);
		proCase.setProCaseKey(proCaseKey);
		proCase.setProCaseImage(Constants.PROCASEIMAGEPATH);

		ProCaseUtils.getInstance().addProCase(proCase);
		String path = MyApplication.getInstance().getRootPath();
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		File sd = new File(path, ProjectName);
		if (!sd.exists()) {
			sd.mkdir();
		}
		String fileName = path
				+ File.separator + ProjectName + File.separator
				+ "homedatabases.db";
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			is = MyApplication.getInstance().getResources()
					.openRawResource(R.raw.homedatabases);
			fos = new FileOutputStream(fileName);
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
				fos.flush();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		ProCaseUtils.getInstance().updateProCaseIndex(1, proCase.getProCaseKey());
		WidgetDAOProxy.instance = null;

		String rootPath = MyApplication.getInstance()
				.getRootPath()
				+ File.separator
				+  proCase.getProCaseAlia()
				+ File.separator + "homedatabases.db";
		ProjectUtils.getRootPath().setRootPath(rootPath);
		ProjectUtils.getRootPath().setRootName(proCase.getProCaseName());
		ProjectUtils.getRootPath().setRootImage(proCase.getProCaseImage());
		ProjectUtils.saveRootPath();
//					MainActivity.getInstance().updateFresh();

		DataDownActivity downActivity = new DataDownActivity(
				context, ProjectKey);

		downActivity
				.setSureListener(new DataDownActivity.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method
						// stub
						MainActivity.getInstance().updateFresh();
						TipAlert alert1 = new TipAlert(context,
								getString(R.string.tip), getString(R.string.downsuccess));
						alert1.show();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method
						// stub
						MainActivity.getInstance().updateFresh();
						TipAlert alert1 = new TipAlert(context,
								getString(R.string.tip), getString(R.string.downfail));
						alert1.show();
					}
				});
		downActivity.show();
		if (MainActivity.getInstance().mHost.getCurrentTab() == 0) {
			TabMainActivity.getInstance().proCaseFragment.setAdpapter();
		} else {
			TabHomeActivity.getInstance().proCaseFragment.setAdpapter();
		}
	}

}