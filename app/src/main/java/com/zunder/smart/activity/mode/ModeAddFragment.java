package com.zunder.smart.activity.mode;

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
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.image.adapter.ModeImageAdapter;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabModeActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.popu.dialog.TimeViewWindow;
import com.zunder.image.adapter.ImageNetAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.image.model.ImageNet;
import com.zunder.smart.model.Mode;
import com.zunder.smart.popwindow.ModePopupWindow;
import com.zunder.smart.popwindow.listener.PopWindowListener;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.view.ListViewDecoration;
import com.zunder.smart.webservice.ImageServiceUtils;

import java.util.ArrayList;
import java.util.List;

public class ModeAddFragment extends Fragment implements OnClickListener ,OnTouchListener{
	private EditText modeName;
	String bitmap="";
	private RelativeLayout startTime;
	TextView timeTxt;
	private static String modelOpration = "Add";
	private Mode modeParams;
	static int seqencing = 0;
	private int io = 0;
	private int modeCode=0;
	private static int Id = 0;
	private int isShow=1;
	Activity activity;
	private TextView backTxt;
	private TextView editeTxt;
	private Button audioBtn;
	private EditText nickName;
	private RelativeLayout idLayout,ioLayout;
	private TextView idTxt,ioTxt;
	private TextView titleTxt;
	private TextView showTxt;
	ImageButton deviceNameAudio;
	String modeType="C9";
	ImageView icon;
	SwipeMenuRecyclerView gridView;
	private List<ImageNet> listImage=new ArrayList <ImageNet>();
	private ModeImageAdapter imageAdapter;
	TimeViewWindow timeViewWindow;
	ActionViewWindow actionViewWindow;
	ModePopupWindow modePopupWindow;
	SmartImageView imageView;
	private RelativeLayout showLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_mode_add, container,
				false);
		activity = getActivity();
		imageView=(SmartImageView) root.findViewById(R.id.imageIco);
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		timeTxt = (TextView) root.findViewById(R.id.timeTxt);
		idTxt = (TextView) root.findViewById(R.id.idTxt);
		ioTxt = (TextView) root.findViewById(R.id.ioTxt);
		titleTxt=(TextView)root.findViewById(R.id.titleTxt);
		showTxt=(TextView)root.findViewById(R.id.showTxt);
		showLayout=(RelativeLayout)root.findViewById(R.id.showLayout);
		showLayout.setOnClickListener(this);
		deviceNameAudio=(ImageButton) root.findViewById(R.id.deviceNameAudio);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		startTime = (RelativeLayout) root.findViewById(R.id.startTime);

		startTime.setOnClickListener(this);
		deviceNameAudio.setOnClickListener(this);
		modeName = (EditText) root.findViewById(R.id.modeName);
		idLayout = (RelativeLayout) root.findViewById(R.id.idLayout);
		ioLayout = (RelativeLayout) root.findViewById(R.id.ioLayout);
		idLayout.setOnClickListener(this);
		ioLayout.setOnClickListener(this);
		audioBtn = (Button) root.findViewById(R.id.audio);
		nickName = (EditText) root.findViewById(R.id.nickName);
		icon=(ImageView) root.findViewById(R.id.icon);
		icon.setOnClickListener(this);
		root.setOnTouchListener(this);
		initview();
		gridView = (SwipeMenuRecyclerView) root.findViewById(R.id.modeGrid);
		gridView.setLayoutManager(new GridLayoutManager(activity, 3));// 布局管理器。
		gridView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		gridView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//		gridView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		gridView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		new DataTask().execute();
		return root;
	}

	public void setDate(String _modelOpration,String modeType,int _Id) {
		this.modeType=modeType;
        Id = _Id;
		modelOpration=_modelOpration;
		if (modelOpration.equals("Add")) {
			io = 0;
			isShow=1;
			modeName.setText("");
			timeTxt.setText("00:00~00:00");
			ioTxt.setText("回路"+(io+1));
			showTxt.setText("显示");
			if(Id!=0){
				modeCode=Id;
				idTxt.setText(Id+"");
				Id=0;
			}else {
				modeCode=-1;
				idTxt.setText("1");
				for (int i = 1; i < 200; i++) {
					if (ModeFactory.getInstance().useModeId(i) == 1) {
						if (ModeFactory.getInstance().useIO(i) == 1) {
							idTxt.setText("" + i);
							modeCode = i;
							break;
						}
					} else {
						idTxt.setText("" + i);
						modeCode = i;
						break;
					}
				}
			}
			for (int i = 0; i < 8; i++) {
				if(ModeFactory.getInstance().useModeId(modeCode,i)==0) {
					ioTxt.setText("回路" + (i + 1));
					io=i;
					break;
				}
			}
			bitmap=Constants.MODEIMAGEPATH;
			seqencing=ModeFactory.getInstance().getAll().size()+1;
			imageView.setImageUrl(Constants.MODEIMAGEPATH+bitmap);
		} else {
			modeParams = ModeFactory.getInstance().getByIDMode(Id);
			if (modeParams != null) {
				modeName.setText(modeParams.getModeName());
				timeTxt.setText(modeParams.getStartTime()+"~"+modeParams.getEndTime());
				nickName.setText(modeParams.getModeNickName());
				seqencing = modeParams.getSeqencing();
				bitmap = modeParams.getModeImage();
				io = modeParams.getModeLoop();
				modeCode=modeParams.getModeCode();
				isShow=modeParams.getIsShow();
				ioTxt.setText("回路"+(io+1));
				idTxt.setText(modeCode+"");
				showTxt.setText(isShow==1?"显示":"隐藏");
				imageView.setImageUrl(Constants.MODEIMAGEPATH+bitmap);
			}
		}
		if(modeType.equals("FF")){
			idLayout.setVisibility(View.VISIBLE);
			ioLayout.setVisibility(View.VISIBLE);
			titleTxt.setText("情景编辑");
		}else{
			idLayout.setVisibility(View.GONE);
			ioLayout.setVisibility(View.GONE);
			titleTxt.setText("集合编辑");
		}
	}

	private void initview() {
		audioBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AduioService aduioService= AduioService.getInstance();
				aduioService.setEditDevice(activity,nickName,nickName.getText().toString());
			}
		});
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			bitmap=listImage.get(position).getImageUrl();
			if(TextUtils.isEmpty(modeName.getText())){
				modeName.setText(listImage.get(position).getImageName());
			}
			imageView.setImageUrl(Constants.HTTPS+bitmap);
		}
	};

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
				TabModeActivity.getInstance().hideFragMent(1);
			}if(MainActivity.getInstance().mHost.getCurrentTab()	==3) {
			TabMyActivity.getInstance().hideFragMent(11);
		}else{
				TabMainActivity.getInstance().hideFragMent(1);
			}
			break;
		case R.id.editeTxt:
			save();
			break;
		case R.id.startTime:
			timeViewWindow=new TimeViewWindow(activity);
			timeViewWindow.setAlertViewOnCListener(new TimeViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					timeTxt.setText(itemName);
					timeViewWindow.dismiss();
				}

				@Override
				public void cancle() {

				}
			});
			timeViewWindow.show();
			break;
		case R.id.deviceNameAudio:
			AduioService aduioService= AduioService.getInstance();
			aduioService.setEditDevice(activity,modeName,modeName.getText().toString());
			break;
		case R.id.icon:
			break;
		case R.id.idLayout:
				modeCode=Integer.parseInt(idTxt.getText().toString());
				  modePopupWindow=new ModePopupWindow(activity,modeCode);
				modePopupWindow.setListener(new PopWindowListener() {
					@Override
					public void setParams(String params) {
						idTxt.setText(params);
						modePopupWindow.dismiss();
					}
				});
				modePopupWindow.show();
			break;
		case R.id.ioLayout: {
//				io=Integer.parseInt(ioTxt.getText().toString());
			actionViewWindow = new ActionViewWindow(activity, "回路", ListNumBer.getModeIos(Integer.parseInt(idTxt.getText().toString())), io);
			actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String ItemName) {
					if (ItemName.contains("已使用")) {
						ToastUtils.ShowError(activity, "IO回路已被使用", Toast.LENGTH_SHORT, true);
					} else {
						ioTxt.setText(ItemName.replace("已使用", "").replace(" ", ""));
						io = pos;
						actionViewWindow.dismiss();
					}
				}

				@Override
				public void cancle() {
				}
			});
			actionViewWindow.show();
		}
				break;
			case R.id.showLayout:
			{
				actionViewWindow=new ActionViewWindow(activity,"状态",ListNumBer.modeType(),isShow);
				actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
							isShow=pos;
							showTxt.setText(ItemName);
							actionViewWindow.dismiss();
					}
					@Override
					public void cancle() {
					}
				});
				actionViewWindow.show();
			}
			break;
		default:
			break;
		}
	}
	public void save() {
		try {
			String name = modeName.getText().toString().trim();
			if ("".equals(name)) {
				ToastUtils.ShowError(activity,getResources().getString(R.string.modeNameNull),Toast.LENGTH_SHORT,true);
				return;
			}
			if ("".equals(modeType)) {
				ToastUtils.ShowError(activity,getResources().getString(R.string.modeTypeNull),Toast.LENGTH_SHORT,true);
				return;
			}
			modeCode=Integer.parseInt(idTxt.getText().toString());

			Mode mode = new Mode();
			mode.setModeName(name);
			if(bitmap==""||bitmap.equals("")||bitmap==null){
				bitmap=Constants.MODEIMAGEPATH;
			}
			mode.setModeImage(bitmap);
			mode.setModeType(modeType);
			mode.setSeqencing(seqencing);
			String[] times=timeTxt.getText().toString().replace("--","~").split("~");
			mode.setStartTime(times[0]);
			mode.setEndTime(times[1]);
			mode.setModeLoop(io);
			mode.setModeCode(modeCode);
			mode.setIsShow(isShow);
			mode.setModeNickName(nickName.getText().toString());
			if (this.modelOpration.equals("Add")) {

				if (ModeFactory.getInstance().judgeName(name) !=0) {
					ToastUtils.ShowError(activity,getString(R.string.mode_exite)+"["+name+"]",Toast.LENGTH_SHORT,true);
				}else if(!DeviceFactory.getInstance().judgeName(name).equals("0"))
				{
					ToastUtils.ShowError(activity,getString(R.string.device_exite)+"["+name+"]",Toast.LENGTH_SHORT,true);
				}else{
					int result = sql().insertMode(mode);
					if (result > 1) {
						ModeFactory.getInstance().clearList();
						ToastUtils.ShowSuccess(activity,getString(R.string.addSuccess),Toast.LENGTH_SHORT,true);
						if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
							TabModeActivity.getInstance().hideFragMent(1);
							TabModeActivity.getInstance().modeFragment.initAdapter("C9");
							TabModeActivity.getInstance().setAdapter();
						}else{
							TabMainActivity.getInstance().hideFragMent(1);
							TabMainActivity.getInstance().modeFragment.initAdapter("FF");
							TabMainActivity.getInstance().setAdapter();
						}
					}
				}
			} else if (this.modelOpration.equals("Edite")) {
				if(ModeFactory.getInstance().updateName(Id,name,modeParams.getModeName())!=0) {
					ToastUtils.ShowError(activity,getString(R.string.mode_exite)+"["+name+"]",Toast.LENGTH_SHORT,true);
				}else if(!DeviceFactory.getInstance().judgeName(name).equals("0")){
					ToastUtils.ShowError(activity,getString(R.string.device_exite)+"["+name+"]",Toast.LENGTH_SHORT,true);
				}else{
					mode.setId(Id);
					int num = sql().updateMode(mode, modeParams.getModeName());
					if (num == 2) {
						ToastUtils.ShowSuccess(activity, getString(R.string.updateSuccess), Toast.LENGTH_SHORT, true);
					} else if (num == 1) {
						ToastUtils.ShowSuccess(activity, getString(R.string.updateSuccess), Toast.LENGTH_SHORT, true);

					}
					ModeFactory.getInstance().clearList();
					if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
						TabModeActivity.getInstance().hideFragMent(1);
						TabModeActivity.getInstance().modeFragment.initAdapter("C9");
						TabModeActivity.getInstance().setAdapter();
					}else{
						TabMainActivity.getInstance().hideFragMent(1);
						TabMainActivity.getInstance().modeFragment.initAdapter("FF");
						TabMainActivity.getInstance().setAdapter();
					}
				}
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		onDialogDis();
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
				String resultParam = ImageServiceUtils.getImagesByType(23);
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
				imageAdapter = new ModeImageAdapter(activity,listImage);
				imageAdapter.setOnItemClickListener(onItemClickListener);
				gridView.setAdapter(imageAdapter);
			}
		}
	}
	public void onDialogDis(){
		if(timeViewWindow!=null&&timeViewWindow.isShow()){
			timeViewWindow.dismiss();
		}
		if(actionViewWindow!=null&&actionViewWindow.isShow()){
			actionViewWindow.dismiss();
		}
		if(modePopupWindow!=null&&modePopupWindow.isShow()){
			modePopupWindow.dismiss();
		}
	}
}
