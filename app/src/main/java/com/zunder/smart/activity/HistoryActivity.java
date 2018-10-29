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
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.adapter.HistoryAdapter;
import com.zunder.smart.custom.view.SuperListView;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.History;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.HistoryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class HistoryActivity extends Activity implements SuperListView.OnRefreshListener,
		OnClickListener {
	SuperListView listView;
	HistoryActivity activity;
	private List<History> list = new ArrayList<History>();
	HistoryAdapter adapter;
	TextView titleView;
	TextView backTxt,editeTxt;
	private SharedPreferences spf;
	private String primaryKey = "";
	private String MasterMac="";
	private String MasterName="";
	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, HistoryActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_history);
		spf = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		primaryKey = spf.getString("PrimaryKey", "000000");
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
		listView = (SuperListView) findViewById(R.id.songList);
		listView.setOnRefreshListener(this);
		adapter = new HistoryAdapter(activity, list);
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
				final List<String> list= GateWayFactory.getInstance().getGateWay();
				list.add("全部网关");
				final ActionViewWindow actionViewWindow=new ActionViewWindow(activity,"网关列表",list,0);
				actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void cancle() {

					}

					@Override
					public void onItem(final int pos, String ItemName) {
						actionViewWindow.dismiss();

						GateWay gateWay=GateWayFactory.getInstance().getGateWayByName(ItemName);
						if(gateWay!=null) {
							MasterMac=gateWay.getGatewayID();
							DialogAlert alert = new DialogAlert(activity);
							alert.init(activity.getString(R.string.tip), getString(R.string.cleanallhis));

							alert.setSureListener(new DialogAlert.OnSureListener() {

								@Override
								public void onSure() {
									// TODO Auto-generated method stub
									if(pos==list.size()-1){

										new AsyncTask() {

											@Override
											protected Object doInBackground(Object[] params) {
												try {
													String result = HistoryUtils.deleteHistorys(primaryKey);
												} catch (Exception e) {
													e.printStackTrace();
												}
												return "";
											}

											@Override
											protected void onPostExecute(Object o) {
												super.onPostExecute(o);
												list.clear();
												listView.doRefresh();
											}
										}.execute();
									}else {
										new AsyncTask() {

											@Override
											protected Object doInBackground(Object[] params) {
												try {
													String result = HistoryUtils.deleteHistorysByMasterMac(MasterMac, primaryKey);
												} catch (Exception e) {
													e.printStackTrace();
												}
												return "";
											}

											@Override
											protected void onPostExecute(Object o) {
												super.onPostExecute(o);
												list.clear();
												listView.doRefresh();
											}
										}.execute();
									}
								}

								@Override
								public void onCancle() {
									// TODO Auto-generated method stub

								}
							});
							alert.show();
						}
					}
				});
				actionViewWindow.show();
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
				String json = HistoryUtils.getHistorys(pageNum, 10,primaryKey);
				listUser = (List<History>) JSONHelper.parseCollection(json,
						List.class, History.class);
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
							getString(R.string.no_rec_line));
					verifyAlert.show();
				}
			}

		}
	}

	public void back() {


		finish();
	}
}