package com.zunder.smart.dialog;

import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.ActionFactory;
import com.zunder.smart.dao.impl.factory.ActionParamFactory;
import com.zunder.smart.dao.impl.factory.FunctionFactory;
import com.zunder.smart.dao.impl.factory.FunctionParamFactory;
import com.zunder.smart.model.ActionParam;
import com.zunder.smart.model.DeviceAction;
import com.zunder.smart.model.DeviceFunction;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.FunctionParam;
import com.zunder.smart.model.Products;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.webservice.DeviceTypeServiceUtils;
import com.zunder.smart.webservice.ParamsServiceUtils;
import com.zunder.smart.webservice.ProductsServiceUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.ActionFactory;
import com.zunder.smart.dao.impl.factory.ActionParamFactory;
import com.zunder.smart.dao.impl.factory.FunctionFactory;
import com.zunder.smart.dao.impl.factory.FunctionParamFactory;
import com.zunder.smart.model.ActionParam;
import com.zunder.smart.model.DeviceAction;
import com.zunder.smart.model.DeviceFunction;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.FunctionParam;
import com.zunder.smart.model.Products;
import com.zunder.smart.tools.JSONHelper;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

//ȷ����ʾ��
public class TypeAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView typeValue, productValue,actionValue,actionParamvalue,functionValue,functionParamValue;
	private Button cancleBtn, sureBtn;
	ProgressBar productBar, typeBar,actionBar,actionParamBar,functionBar,functionParamBar;

	public TypeAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_type_verify);
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		typeValue = (TextView) findViewById(R.id.typeValue);
		productValue = (TextView) findViewById(R.id.productValue);
		productBar = (ProgressBar) findViewById(R.id.productPro);
		typeBar = (ProgressBar) findViewById(R.id.typePro);

		actionBar = (ProgressBar) findViewById(R.id.actionPro);
		actionParamBar = (ProgressBar) findViewById(R.id.actionParamPro);
		functionBar = (ProgressBar) findViewById(R.id.functionPro);
		functionParamBar = (ProgressBar) findViewById(R.id.functionParamPro);

		actionValue = (TextView) findViewById(R.id.actionValue);
		actionParamvalue = (TextView) findViewById(R.id.actionParamValue);
		functionValue = (TextView) findViewById(R.id.functionValue);
		functionParamValue = (TextView) findViewById(R.id.functionParamValue);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			dismiss();
			break;
		case R.id.sure_bt:
			new TypeDataTask().execute();
			new ProductDataTask().execute();
			new ActionDataTask().execute();
			new ActionParamsDataTask().execute();
			new FunctionDataTask().execute();
			new FunctionParamDataTask().execute();
			sureBtn.setClickable(false);
			break;

		default:
			break;
		}

	}

	class TypeDataTask extends AsyncTask<String, Integer, List<DeviceType>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<DeviceType> doInBackground(String... params) {
			List<DeviceType> result = null;
			try {
				result = (List<DeviceType>) JSONHelper.parseCollection(
						DeviceTypeServiceUtils.getProductsCode(), List.class,
						DeviceType.class);

			} catch (Exception e) {
				typeBar.setMax(1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<DeviceType> result) {

			if (result == null || result.size() == 0) {
				typeBar.setMax(1);
				publishProgress(1);
			} else {
				typeBar.setMax(result.size());
				for (int i = 0; i < result.size(); i++) {
					MyApplication.getInstance().getWidgetDataBase()
							.updateDeviceType(result.get(i));
					publishProgress(i + 1);
				}

			}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			typeBar.setProgress(values[0]);
			typeValue.setText((values[0] * 100 / typeBar.getMax()) + "%");
		}
	}
	class ProductDataTask extends AsyncTask<String, Integer, List<Products>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<Products> doInBackground(String... params) {
			List<Products> result = null;
			try {
				result = (List<Products>) JSONHelper.parseCollection(
						ProductsServiceUtils.getProducts(), List.class,
						Products.class);

			} catch (Exception e) {
				productBar.setMax(1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<Products> result) {

			if (result == null || result.size() == 0) {
				productBar.setMax(1);
				publishProgress(1);
			} else {
				productBar.setMax(result.size());
				for (int i = 0; i < result.size(); i++) {
					MyApplication.getInstance().getWidgetDataBase()
							.updateProducts(result.get(i));
					publishProgress(i + 1);
				}

			}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			productBar.setProgress(values[0]);
			productValue.setText((values[0] * 100 / productBar.getMax()) + "%");
		}
	}

	class ActionDataTask extends AsyncTask<String, Integer, List<DeviceAction>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<DeviceAction> doInBackground(String... params) {
			List<DeviceAction> result = null;
			try {
				result = (List<DeviceAction>) JSONHelper.parseCollection(
						ParamsServiceUtils.getActions(), List.class,
						DeviceAction.class);

			} catch (Exception e) {
				actionBar.setMax(1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<DeviceAction> result) {
			Sqlite().deleteSyncData("t_device_action");
			if (result == null || result.size() == 0) {
				actionBar.setMax(1);
				publishProgress(1);
			} else {
				actionBar.setMax(result.size());
				for (int i = 0; i < result.size(); i++) {
					MyApplication.getInstance().getWidgetDataBase()
							.insertAction(result.get(i));
					publishProgress(i + 1);
				}
				ActionFactory.clearList();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			actionBar.setProgress(values[0]);
			actionValue.setText((values[0] * 100 / actionBar.getMax()) + "%");
		}
	}

	class ActionParamsDataTask extends AsyncTask<String, Integer, List<ActionParam>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<ActionParam> doInBackground(String... params) {
			List<ActionParam> result = null;
			try {
				result = (List<ActionParam>) JSONHelper.parseCollection(
						ParamsServiceUtils.getActionParams(), List.class,
						ActionParam.class);

			} catch (Exception e) {
				actionParamBar.setMax(1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<ActionParam> result) {
			Sqlite().deleteSyncData("t_action_param");
			if (result == null || result.size() == 0) {
				actionParamBar.setMax(1);
				publishProgress(1);
			} else {
				actionParamBar.setMax(result.size());
				for (int i = 0; i < result.size(); i++) {
					MyApplication.getInstance().getWidgetDataBase()
							.insertActionParam(result.get(i));
					publishProgress(i + 1);
				}
				ActionParamFactory.clearList();
			}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			actionParamBar.setProgress(values[0]);
			actionParamvalue.setText((values[0] * 100 / actionParamBar.getMax()) + "%");
		}
	}

	class FunctionDataTask extends AsyncTask<String, Integer, List<DeviceFunction>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<DeviceFunction> doInBackground(String... params) {
			List<DeviceFunction> result = null;
			try {
				result = (List<DeviceFunction>) JSONHelper.parseCollection(
						ParamsServiceUtils.getFunctions(), List.class,
						DeviceFunction.class);

			} catch (Exception e) {
				functionBar.setMax(1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<DeviceFunction> result) {
			Sqlite().deleteSyncData("t_device_function");
			if (result == null || result.size() == 0) {
				functionBar.setMax(1);
				publishProgress(1);
			} else {
				functionBar.setMax(result.size());
				for (int i = 0; i < result.size(); i++) {
					MyApplication.getInstance().getWidgetDataBase().insetFunction(result.get(i));
					publishProgress(i + 1);
				}
				FunctionFactory.clearList();
			}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			functionBar.setProgress(values[0]);
			functionValue.setText((values[0] * 100 / functionBar.getMax()) + "%");
		}
	}

	class FunctionParamDataTask extends AsyncTask<String, Integer, List<FunctionParam>> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected List<FunctionParam> doInBackground(String... params) {
			List<FunctionParam> result = null;
			try {
				result = (List<FunctionParam>) JSONHelper.parseCollection(
						ParamsServiceUtils.getFunctionParams(), List.class,
						FunctionParam.class);

			} catch (Exception e) {
				functionParamBar.setMax(1);
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<FunctionParam> result) {
			Sqlite().deleteSyncData("t_function_param");
			if (result == null || result.size() == 0) {
				functionParamBar.setMax(1);
				publishProgress(1);
			} else {
				functionParamBar.setMax(result.size());
				for (int i = 0; i < result.size(); i++) {
					MyApplication.getInstance().getWidgetDataBase().insetFunctionParam(result.get(i));
					publishProgress(i + 1);
				}

				FunctionParamFactory.clearList();
			}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			functionParamBar.setProgress(values[0]);
			functionParamValue.setText((values[0] * 100 / functionParamBar.getMax()) + "%");
		}
	}
	IWidgetDAO Sqlite() {
		return MyApplication.getInstance().getWidgetDataBase();
	}
}
