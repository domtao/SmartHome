package com.zunder.smart.activity.backup;

import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.MyApplication;
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
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
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

public class BackUpActivity extends Activity implements OnClickListener {
	private SwipeRefreshLayout freshlayout;
	private ProgressAlert progressAlert;
	TextView backTxt, editeTxt;
	BackupAdapter adapter;
	private BackUpActivity context;
	SwipeMenuRecyclerView listView;
	private List<Archive> list=new ArrayList <Archive>();
	private String primaryKey = "";
	private String projectPwd = "0000";
	private int projectNum = 5;
	private String projectKey = "00000000";
	private String projectName = "00000000";
	private String createTime = "00000000";
	public static int oprition = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_backup_list);
		this.context = this;
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		editeTxt.setOnClickListener(this);
		oprition = 0;
		backTxt = (TextView) findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		primaryKey = MyApplication.getInstance().PrimaryKey();
		freshlayout = (SwipeRefreshLayout) findViewById(R.id.freshlayout);
		freshlayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
		freshlayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#BBFFFF"));
		freshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				new DataTask().execute();

			}
		});
		listView = (SwipeMenuRecyclerView) findViewById(R.id.backupList);
		listView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		if(oprition==0) {
			listView.setSwipeMenuCreator(swipeMenuCreator);
			listView.setSwipeMenuItemClickListener(menuItemClickListener);
		}
		// 设置菜单Item点击监听。
		new DataTask().execute();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return false;
	}

	class DataTask extends AsyncTask<String, Integer, List<Archive>> {
		@Override
		protected void onPreExecute() {
		freshlayout.setRefreshing(true);
		list.clear();
		}

		@Override
		protected List<Archive> doInBackground(String... params) {
			List<Archive> result = null;
			try {
				String json = ProjectServiceUtils.getProjects(primaryKey);
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
			if (freshlayout.isRefreshing()) {
				freshlayout.setRefreshing(false);
			}
			if (result != null) {
				list = result;
				if (list.size() == 0) {
					TipAlert alert = new TipAlert(context,
							getString(R.string.tip), getString(R.string.noBack)
									+ "!");
					alert.show();
				}
				adapter = new BackupAdapter(list);
				adapter.setOnItemClickListener(onItemClickListener);
				listView.setAdapter(adapter);
			}
		}
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
					oprition = 1;
					listView.setSwipeMenuCreator(null);
					adapter = new BackupAdapter( list);
					adapter.setOnItemClickListener(onItemClickListener);
					listView.setAdapter(adapter);
				}
			}
		}
	}

	class UpDataTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			showProgressDialog("数据上传中");
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = ProjectServiceUtils.createProject(projectName,
						projectPwd, projectNum, primaryKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			hideProgressDialog();
			if (result != null) {
				try {
					JSONObject object = new JSONObject(result);
					if (object.getInt("ResultCode") == 1) {
						projectKey = object.getString("Msg");
						Archive backupBea = new Archive();
						backupBea.setProjectName(projectName);
						backupBea.setProjectKey(projectKey);
						backupBea.setProjectTime(createTime);
						backupBea.setProjectPwd(projectPwd);
						backupBea.setProjectNum(projectNum);
						list.add(backupBea);
						adapter = new BackupAdapter(list);
						adapter.setOnItemClickListener(onItemClickListener);
						listView.setAdapter(adapter);
						DataUpActivity dataUpActivity = new DataUpActivity(
								context, projectKey);
						dataUpActivity
								.setSureListener(new DataUpActivity.OnSureListener() {
									@Override
									public void onSure() {
										// TODO Auto-generated method stub
										TipAlert alert1 = new TipAlert(context, getString(R.string.tip), getString(R.string.upsuccess));
										alert1.show();
									}

									@Override
									public void onCancle() {
										// TODO Auto-generated method stub
										delBack(projectKey);
										TipAlert alert1 = new TipAlert(context,
												getString(R.string.tip), getString(R.string.upfail));
										alert1.show();
									}
								});
						dataUpActivity.show();
					} else {
						// domtao
					}
				} catch (Exception e) {
				}
			}
		}
	}

	EditText et = null, pwdTxt = null;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.editeTxt:
			final EditTxtTwoAlert alert = new EditTxtTwoAlert(context);
			alert.setTitle(R.mipmap.info_systemset,
					getString(R.string.fileUpBack));
			alert.setHint("请输入家庭名称", getString(R.string.roleinfo));
			alert.setVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE);
			alert.setOnSureListener(new EditTxtTwoAlert.OnSureListener() {
				@Override
				public void onSure(String editName, String editValue) {

					String fileName = editName;
					projectPwd = editValue;
					if (fileName.equals("")) {
						ToastUtils.ShowError(context,
								getString(R.string.inputBackName),
								Toast.LENGTH_SHORT,true);
						return;
					}
					if (projectPwd.equals("")||projectPwd.length()>6) {
						ToastUtils.ShowError(context,
								getString(R.string.powerPwd),
								Toast.LENGTH_SHORT,true);
						return;
					}
					alert.dismiss();
					final Archive archive = getBackUpBean(fileName);
					if (archive != null) {
						DialogAlert dialogAlert = new DialogAlert(
								context);
						dialogAlert.init(
								getString(R.string.fileUpBack),
								getString(R.string.fileIsExite));
						dialogAlert
								.setSureListener(new DialogAlert.OnSureListener() {

									@Override
									public void onSure() {

										// TODO Auto-generated method
										// stub
										projectName = archive
												.getProjectName();
										projectPwd = archive
												.getProjectPwd();
										projectNum = archive
												.getProjectNum();
										projectKey = archive
												.getProjectKey();
										createTime = archive
												.getProjectTime();
										DataUpActivity dataUpActivity = new DataUpActivity(
												context, projectKey);
										dataUpActivity
												.setSureListener(new DataUpActivity.OnSureListener() {

													@Override
													public void onSure() {
														// TODO
														// Auto-generated
														// method stub

														TipAlert alert1 = new TipAlert(
																context,
																getString(R.string.tip),
																getString(R.string.upsuccess));
														alert1.setSureListener(new TipAlert.OnSureListener() {
															@Override
															public void onSure() {
																new DataTask().execute();
															}
														});
														alert1.show();

													}

													@Override
													public void onCancle() {
														// TODO
														// Auto-generated
														// method stub
														TipAlert alert1 = new TipAlert(
																context, getString(R.string.tip),
																getString(R.string.upfail));
														alert1.show();
													}
												});
										dataUpActivity.show();
									}

									@Override
									public void onCancle() {
										// TODO Auto-generated method
										// stub
										alert.dismiss();
									}
								});
						dialogAlert.show();

					} else {
						projectName = fileName;
						projectKey = "DST" + AppTools.getAlias();
						createTime = AppTools.getCurrentlyDate();

						projectNum = 5;
						new UpDataTask().execute();
					}
				}

				@Override
				public void onSearch() {
					// TODO Auto-generated method stub
					quryfilename(alert);
				}

				@Override
				public void onCancle() {
					// TODO Auto-generated method stub
				}
			});
			alert.show();
			break;
		case R.id.backTxt:
			this.finish();
			break;
		default:
			break;
		}
	}

	String[] names;

	private void quryfilename(final EditTxtTwoAlert alert) {

		if (list != null && list.size() > 0) {
			BackQueryAdapter bacAdapter=new BackQueryAdapter(context,list);
			new AlertDialog.Builder(context)
					.setTitle(getString(R.string.backName))
					.setAdapter(bacAdapter, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							alert.setText(list.get(which).getProjectName(),
									list.get(which).getProjectPwd());
							projectKey = list.get(which).getProjectKey();
							createTime = list.get(which).getProjectTime();
							projectName = list.get(which).getProjectName();
						}
					}).show();
		} else {
			Toast.makeText(context, getString(R.string.noBackFile),
					Toast.LENGTH_SHORT).show();
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

	public Archive getBackUpBean(String backName) {
		Archive backupBean = null;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getProjectName().equals(backName)) {
					backupBean = list.get(i);
					break;
				}
			}
		}
		return backupBean;
	}
	public void machineDialog() {

		final EditTxtTwoAlert alert = new EditTxtTwoAlert(context);
		alert.setTitle(R.mipmap.info_systemset, getString(R.string.backList));
		alert.setHint(getString(R.string.inputUserCount),
				getString(R.string.powerPwd));
		alert.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
		alert.setOnSureListener(new EditTxtTwoAlert.OnSureListener() {

			@Override
			public void onSure(String editName, String editValue) {
				final String machineStr = editName;
				final String pwdStr = editValue;
				if ("".equals(machineStr)) {
					ToastUtils.ShowError(context,
							getString(R.string.noUserNull) + "!",
							Toast.LENGTH_SHORT,true);
					return;
				}
				if ("".equals(pwdStr)) {
					ToastUtils.ShowError(context, getString(R.string.pwd_no_empty), Toast.LENGTH_SHORT,true);

				} else {
					new ClientDataTask(machineStr, pwdStr).execute();
					alert.dismiss();
				}
			}

			@Override
			public void onSearch() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub

			}
		});
		alert.show();

	}

	public void updateShow(String pwdStr, int number, int isShow, String bAlia) {
		new DataShowTask(pwdStr, number, isShow, bAlia).execute();
	}
//2017022819201288
//p_2018030710195880
	public void delBack(String projectKey) {
		new DataDelTask(projectKey).execute();
	}
	class DataShowTask extends AsyncTask<String, Integer, String> {
		String projectKey;
		String pwdStr;
		int number;
		DataShowTask(String pwdStr, int number, int isShow, String projectKey) {
			this.pwdStr = pwdStr;
			this.number = number;
			this.projectKey = projectKey;
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog("加载数据中");
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "-1";
			try {
				result = ProjectServiceUtils.updateProject(pwdStr, number,
						projectKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			hideProgressDialog();
			try {
				JSONObject object = new JSONObject(result);
				if (object.getInt("ResultCode") == 1) {
					 new DataTask().execute();
//					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(context, object.getString("Msg") + "!",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {

			}
		}

	}

	class DataDelTask extends AsyncTask<String, Integer, String> {
		String projectKey;

		DataDelTask(String projectKey) {
			this.projectKey = projectKey;
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog("数据删除中");

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "-1";
			try {
				result = ProjectServiceUtils.deleteProject(projectKey,
						primaryKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			hideProgressDialog();
			try {
				JSONObject object = new JSONObject(result);
				if (object.getInt("ResultCode") == 1) {
					new DataTask().execute();
				} else {
					Toast.makeText(context,
							getString(R.string.updateFail) + "!",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
			}

		}
	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			SwipeMenuItem closeItem = new SwipeMenuItem(context)
					.setBackgroundDrawable(R.color.orange)
					.setText(getString(R.string.edit))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

			SwipeMenuItem addItem = new SwipeMenuItem(context)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(addItem);

		}
	};

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
	/**
	 * 菜单点击监听。
	 */
	private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
				switch (menuPosition){
					case 0:
						final EditTxtAlert alert = new EditTxtAlert(context);
						alert.setTitle(android.R.drawable.ic_dialog_info,
								list.get(adapterPosition).getProjectName(),0);
						alert.setHint(context.getString(R.string.powerPwd));

						alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

							@Override
							public void onSure(String fileName) {
								// TODO Auto-generated method stub
								if (fileName.equals("")) {
									Toast.makeText(context,
											context.getString(R.string.powerPwd) + "!",
											Toast.LENGTH_SHORT).show();
									return;
								}
								list.get(adapterPosition).setProjectPwd(fileName);
								list.get(adapterPosition).setProjectNum(10);
								context.updateShow(fileName, 10, 1, list.get(adapterPosition)
										.getProjectKey());
								alert.dismiss();
							}

							@Override
							public void onCancle() {
								// TODO Auto-generated method stub

							}
						});
						alert.show();
						break;
					case 1:
						DialogAlert alert1 = new DialogAlert(context);
						alert1.init("温馨提示",
								context.getString(R.string.delBack)+list.get(adapterPosition).getProjectName());
						alert1.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								delBack(list.get(adapterPosition).getProjectKey());

							}

							@Override
							public void onCancle() {
								// TODO Auto-generated method stub

							}
						});
						alert1.show();
						break;
				}
			}
		}
	};
}