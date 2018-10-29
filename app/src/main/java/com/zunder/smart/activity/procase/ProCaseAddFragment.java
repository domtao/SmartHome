package com.zunder.smart.activity.procase;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.image.adapter.ProcaseImageAdapter;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.image.adapter.ImageNetAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.json.Constants;
import com.zunder.smart.json.ProCaseUtils;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.image.model.ImageNet;
import com.zunder.smart.model.ProCase;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.view.ListViewDecoration;
import com.zunder.smart.webservice.ImageServiceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProCaseAddFragment extends Fragment implements OnClickListener,View.OnTouchListener {


	private EditText proCaseName;
	private static String modelOpration = "Add";
	private static int Id = 0;
	Activity activity;
	private TextView backTxt;
	private TextView editeTxt;
	private RelativeLayout imageLayout;
	private SmartImageView imageView;
	private String proCaseKey;
	private String proCaseImage= Constants.PROCASEIMAGEPATH;
	SwipeMenuRecyclerView gridView;
	private List<ImageNet> listImage=new ArrayList<ImageNet>();
	private ProcaseImageAdapter imageAdapter;
	String oldName="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_pro_case_add, container,
				false);
		activity = getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		proCaseName = (EditText) root.findViewById(R.id.proCaseName);
		imageLayout=(RelativeLayout)root.findViewById(R.id.imageLayout);
		imageView=(SmartImageView) root.findViewById(R.id.imageIco);
		imageLayout.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		root.setOnTouchListener(this);
		gridView = (SwipeMenuRecyclerView) root.findViewById(R.id.proCaseGrid);
		gridView.setLayoutManager(new GridLayoutManager(activity, 3));// 布局管理器。
		gridView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		gridView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		gridView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		gridView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		new DataTask().execute();
		return root;
	}
	ProCase proCaseParam;

	public void setInit(String _modelOpration,ProCase _proCase) {
		this.modelOpration=_modelOpration;
		this.proCaseParam=_proCase;
		if (modelOpration.equals("Add")) {
			proCaseName.setText("");
			proCaseKey=AppTools.getSystemTime();;
		} else {
			if (proCaseParam != null) {
				oldName=proCaseParam.getProCaseName();
				proCaseName.setText(proCaseParam.getProCaseName());
				proCaseKey=proCaseParam.getProCaseKey();
				imageView.setImageUrl(Constants.HTTPS+proCaseParam.getProCaseImage());
			}
		}
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){

		}
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				if(MainActivity.getInstance().mHost.getCurrentTab()==0) {
					TabMainActivity.getInstance().hideFragMent(6);
				}else{
					TabHomeActivity.getInstance().hideFragMent(6);
				}
				break;
			case R.id.editeTxt:
				save();
				break;
			default:
				break;
		}
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			proCaseImage=listImage.get(position).getImageUrl();
			if(TextUtils.isEmpty(proCaseName.getText())){
				proCaseName.setText(listImage.get(position).getImageName());
			}
			imageView.setImageUrl(Constants.HTTPS+proCaseImage);
		}
	};
	public void save() {
		try {
			String name = proCaseName.getText().toString().trim();
			if ("".equals(name)) {
				Toast.makeText(activity,"专案名称不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}

			ProCase proCase = new ProCase();
			proCase.setProCaseName(name);
			proCase.setProCaseAlia(name);
			proCase.setProCaseKey(proCaseKey);
			proCase.setProCaseImage(proCaseImage);
			if (this.modelOpration.equals("Add")) {
				if (ProCaseUtils.getInstance().judgeName(name,proCaseKey)>0) {
					ToastUtils.ShowError(activity, getString(R.string.exist),
							Toast.LENGTH_SHORT,true);
				}else{
					ProCaseUtils.getInstance().addProCase(proCase);
					String path =MyApplication.getInstance().getRootPath();
					File f = new File(path);
					if (!f.exists()) {
						f.mkdir();
					}
					File sd = new File(path, name);
					if (!sd.exists()) {
						sd.mkdir();
					}
					String fileName =path
							+ File.separator + name + File.separator
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
					ToastUtils.ShowSuccess(activity, getString(R.string.addSuccess),
							Toast.LENGTH_SHORT,true);
					if(MainActivity.getInstance().mHost.getCurrentTab()==0) {
						TabMainActivity.getInstance().proCaseFragment.setAdpapter();
					}else{
						TabHomeActivity.getInstance().proCaseFragment.setAdpapter();
					}
				}
			} else if (this.modelOpration.equals("Edite")) {

				if (ProCaseUtils.getInstance().updateName(oldName,name,proCaseKey)>0) {
					ToastUtils.ShowError(activity, getString(R.string.exist),
							Toast.LENGTH_SHORT,true);
				} else {
					String path= MyApplication.getInstance().getRootPath()+File.separator+oldName;
					File oleFile = new File(path); //要重命名的文件或文件夹
					if(oleFile.exists()){
						File newFile = new File(MyApplication.getInstance().getRootPath()+File.separator+name + File.separator);  //重命名为zhidian1
						oleFile.renameTo(newFile);
					}

					ProCaseUtils.getInstance().updateProCase(proCase);
					ToastUtils.ShowSuccess(activity, getString(R.string.updateSuccess),
							Toast.LENGTH_SHORT,true);
					if(proCaseParam.getProCaseIndex()==1){
						ProjectUtils.getRootPath().setRootImage(proCaseImage);
						ProjectUtils.saveRootPath();
					}
					if(MainActivity.getInstance().mHost.getCurrentTab()==0) {
						TabMainActivity.getInstance().proCaseFragment.setAdpapter();
					}else{
						TabHomeActivity.getInstance().proCaseFragment.setAdpapter();
					}
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	class DataTask extends AsyncTask<String, Void, List<ImageNet>> {
		@Override
		protected void onPreExecute() {

		}
		@Override
		protected  List<ImageNet> doInBackground(String... params) {
			List<ImageNet> result = null;
			try {
				String resultParam = ImageServiceUtils.getImagesByType(24);
				result = (List<ImageNet>) JSONHelper.parseCollection(
						resultParam, List.class, ImageNet.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute( List<ImageNet> result) {
			if (result != null && result.size() > 0) {
				listImage.addAll(result);
				imageAdapter = new ProcaseImageAdapter(activity,listImage);
				imageAdapter.setOnItemClickListener(onItemClickListener);
				gridView.setAdapter(imageAdapter);
			}
		}
	}
}
