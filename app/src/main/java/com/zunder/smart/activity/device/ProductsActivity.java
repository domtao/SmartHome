package com.zunder.smart.activity.device;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.ProductsAdapter;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Products;
import com.zunder.smart.service.SendThread;

import java.util.List;

public class ProductsActivity extends Activity implements OnClickListener {
	ProductsAdapter adapter;
	List<Products> listProduct;
	private TextView backTxt;
	private TextView editeTxt;
	GridView gridView;
	Products products;
	private Activity activity;

	private static int deviceTypeKey;
	private static Device device;


	public static void startActivity(Activity activity, int _deviceTypeKey, Device _device) {
		 deviceTypeKey=_deviceTypeKey;
		device=_device;
		Intent intent = new Intent(activity, ProductsActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		activity = this;

		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.productsGrid);
		listProduct= ProductFactory.getProducts(deviceTypeKey);
		adapter=new ProductsAdapter(activity,listProduct);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				adapter.changeSelected(position);
				products=listProduct.get(position);
				if(device!=null) {
					String cmd="*C0019FA"+products.getProductsCode()+device.getDeviceID()+"00000000";
					SendThread send = SendThread.getInstance(cmd);
					new Thread(send).start();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			finish();
			break;
		case R.id.editeTxt:
			if(products!=null) {
				TabMyActivity.getInstance().deviceAddFragment.setproduct(products);
				finish();
			}else{
				ToastUtils.ShowError(activity,"没有选择产品",Toast.LENGTH_SHORT,true);
			}
			break;
		default:
			break;
		}
	}
}
