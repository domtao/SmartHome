package com.zunder.smart.dialog;

import java.util.List;

import com.zunder.scrollview.ProgressWheel;
import com.zunder.smart.R;
import com.zunder.smart.adapter.InfraVersionAdapter;
import com.zunder.smart.model.InfraName;
import com.zunder.smart.model.InfraVersion;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.InfraServiceUtils;
import com.zunder.smart.model.InfraVersion;
import com.zunder.smart.tools.JSONHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
//import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
//import android.widget.Button;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class InfraVersionDialog extends Dialog {
	private Activity mContext;
	List<InfraVersion> list;
	ListView listView;
	InfraVersionAdapter adapter;
	InfraVersionInterFace infraInterFace;
	TextView infraTxt;
	Button fresh;
	int infraID = 0;
	ProgressWheel progressWheel;
	public InfraVersionDialog(Activity context, String name, int infraID) {
		super(context,R.style.MyDialog);
		this.mContext = context;
		this.infraID = infraID;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.infra_dialog);
		findView(name);
	}

	private void findView(String name) {
		progressWheel=(ProgressWheel)findViewById(R.id.proGress);
		fresh = (Button) findViewById(R.id.refresh);
		listView = (ListView) findViewById(R.id.infraList);

		infraTxt = (TextView) findViewById(R.id.infraTxt);
		infraTxt.setText(name);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (infraInterFace != null) {
					infraInterFace.setInfraID(list.get(position)
							.getVersionName(), list.get(position).getId());
					dismiss();
				}
			}
		});
		fresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new DataTask().execute();
			}
		});

		new DataTask().execute();
	}

	public void setInfraInterFace(InfraVersionInterFace infraInterFace) {
		this.infraInterFace = infraInterFace;
	}

	public interface InfraVersionInterFace {
		public void setInfraID(String infraNAme, int infraID);

	}



	class DataTask extends AsyncTask<String, Integer, List<InfraVersion>> {

		@Override
		protected void onPreExecute() {

			progressWheel.setVisibility(View.VISIBLE);
			progressWheel.resetCount();
			progressWheel.setText("加载中……");
			progressWheel.startSpinning();
		}

		@Override
		protected List<InfraVersion> doInBackground(String... params) {
			List<InfraVersion> result = null;
			try {
				result = (List<InfraVersion>) JSONHelper.parseCollection(
						InfraServiceUtils.GetInfraVersions(1, 10, infraID),
						List.class, InfraVersion.class);

			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<InfraVersion> result) {

			if (result != null && result.size() > 0) {
				progressWheel.setVisibility(View.GONE);
				progressWheel.setText("");
				progressWheel.stopSpinning();
				list = result;
				adapter = new InfraVersionAdapter(mContext, list);
				listView.setAdapter(adapter);
			} else {
				progressWheel.setText("加载失败");
				progressWheel.stopSpinning();
			}
		}
	}
}
