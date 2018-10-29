package com.zunder.smart.activity;

import java.util.List;


import com.zunder.smart.R;
import com.zunder.smart.adapter.ImageAdapter;
import com.zunder.smart.gridview.DragGridView;
import com.zunder.image.model.ImageNet;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.ImageServiceUtils;

import android.app.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

public class ImageActivity extends Activity implements
		OnClickListener {

	private Activity context;
	ImageAdapter adapter;
	DragGridView gridView;
	TextView backTxt;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_img);
		context = this;
		gridView = (DragGridView) findViewById(R.id.imgGrid);
		backTxt=(TextView)findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", ((ImageNet)parent.getAdapter().getItem(position)).getImageUrl());
//				bundle.putParcelable("bitmap", barcode);
				resultIntent.putExtras(bundle);
//				this.setResult(RESULT_OK, resultIntent);

				setResult(RESULT_OK, resultIntent);
				ImageActivity.this.finish();
			}
		});
		new DataTask().execute();
	}

	class DataTask extends AsyncTask<String, Void, List<ImageNet>> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<ImageNet> doInBackground(String... params) {
			List<ImageNet> result = null;
			try {
				String resultParam = ImageServiceUtils.getImages(6);
				result = (List<ImageNet>) JSONHelper.parseCollection(
						resultParam, List.class, ImageNet.class);
			} catch (Exception e) {
				e.printStackTrace();

			}
			return result;
		}

		@Override
		protected void onPostExecute(List<ImageNet> result) {
			if (result != null && result.size() > 0) {
				adapter = new ImageAdapter(context, result);
				gridView.setAdapter(adapter);

			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
			case R.id.backTxt:
				this.finish();
				break;
		}
	}

}
