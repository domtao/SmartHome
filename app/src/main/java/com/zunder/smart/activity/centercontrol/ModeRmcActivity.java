package com.zunder.smart.activity.centercontrol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabModeActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.adapter.ModeTabAdapter;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.List;

public class ModeRmcActivity extends Activity implements OnClickListener {
	ModeTabAdapter adapter;
	List<Mode> listMode;
	private Activity activity;
	private SwipeMenuRecyclerView modeList;
	private TextView editeTxt,backTxt,titleTxt;
	private String modeType="C9";
	EditText autoEdite;
	RelativeLayout autoLayout;
	public static void startActivity(Activity activity,int Id) {
		Intent intent = new Intent(activity, ModeRmcActivity.class);
		activity.startActivityForResult(intent,101);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mode_edite);
		activity = this;
		autoLayout=(RelativeLayout)findViewById(R.id.autoLayout);
		backTxt=(TextView)findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		editeTxt=(TextView)findViewById(R.id.editeTxt);
		editeTxt.setOnClickListener(this);
		titleTxt=(TextView)findViewById(R.id.titleTxt);
		autoEdite=(EditText)findViewById(R.id.autoEdite);
		modeList = (SwipeMenuRecyclerView) findViewById(R.id.modeList);
		modeList.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		modeList.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		modeList.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		modeList.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 设置菜单Item点击监听。

		autoEdite.addTextChangedListener(new MyWatch());
		editeTxt.setVisibility(View.GONE);
		initAdapter();
	}
	public void initAdapter(){
		autoEdite.setText("");
		titleTxt.setText("情景集合列表");

		listMode=ModeFactory.getInstance().getAll();
		if(listMode.size()>0){
			autoLayout.setVisibility(View.VISIBLE);
		}else{
			autoLayout.setVisibility(View.GONE);
		}
		adapter = new ModeTabAdapter(activity,listMode);
		adapter.setOnItemClickListener(onItemClickListener);

		modeList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back("");
			break;
		default:
			break;
		}
	}

	public void back(String data){
		Intent resultIntent = new Intent();
		resultIntent.putExtra("actionValue",data);
		this.setResult(101, resultIntent);
		this.finish();
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
		//	listMode.get(position)
			back(listMode.get(position).getModeName());
		}
	};
	private class MyWatch implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			//joe 0826 搜索条件 用户输入的字符串
			String input = charSequence.toString();
			int length = input.length();
			List<Mode> resultList= new ArrayList<Mode>();
			List<Mode> listMode= new ArrayList<Mode>();
			if (length>0) {
				//listMode=ModeFactory.getInstance().getModesByType(modeType,input);
				resultList=ModeFactory.getInstance().getModesByType(modeType,-1);

				List<ModeList> ModeListArr= new ArrayList<ModeList>();
				List<ModeList> allModeListArr= new ArrayList<ModeList>();
				for (int j=0;j<resultList.size();j++){
					if (ModeListArr.size() == 0) {
						ModeListArr = MyApplication.getInstance().getWidgetDataBase()
								.getModeList();
					}
					for (ModeList modeList : ModeListArr) {
						if (modeList.getModeId() == resultList.get(j).getModeCode()) {
							//// 获取当前情景或者集合的的所有子列表
							allModeListArr.add(modeList);
						}
					}
				}
				int cmdIndex=0;
				//String keyword="+:+:,:，";
				//String[] LogicIf=keyword.split(":");
				String[] LogicIf={"&","|",",","，"};
				for (cmdIndex = 0; cmdIndex < LogicIf.length; cmdIndex++) {
					if(input.contains(LogicIf[cmdIndex])){
						break;
					}
				}
                int isMatch;
				if(cmdIndex<LogicIf.length){
					int ifMode=(cmdIndex<1)?0:1;
					String[] logicIfArr = input.split(LogicIf[cmdIndex]);
					for(int j=0;j<logicIfArr.length;j++){
						if(logicIfArr[j].length()>0) {
							for (Mode modeArr : resultList) {
								if (modeArr.getModeName().contains(logicIfArr[j])) {
									if (!listMode.contains(modeArr)) {
										listMode.add(modeArr);
									}
								}
							}
						}
					}
					for (ModeList model :allModeListArr) {
                        isMatch=0;
						for(int j=0;j<logicIfArr.length;j++) {
							if(logicIfArr[j].length()>0) {
								if (model.getDeviceName().contains(logicIfArr[j])) {
									/////// 包含设备名称
                                    isMatch++;
								}
								if (getLogicTitle(model).contains(logicIfArr[j])) {
									////// 包含逻辑指令
                                    isMatch++;
								}
								if (model.getRoomName().contains(logicIfArr[j])) {
									////// 房间名称
                                    isMatch++;
								}
							}
						}
						if(ifMode>0){
							//[self checkModeModel:model];
							if(isMatch>0) {
								for (Mode modeArr : resultList) {
									if (model.getModeId() == modeArr.getId()) {
										if (!listMode.contains(modeArr)) {
											listMode.add(modeArr);
										}
									}
								}
							}
						}else{
							if(isMatch>=logicIfArr.length) {
								for (Mode modeArr : resultList) {
									if (model.getModeId() == modeArr.getId()) {
										if (!listMode.contains(modeArr)) {
											listMode.add(modeArr);
										}
									}
								}
							}
						}
					}
				}else {
                    for (Mode modeArr : resultList) {
						if(modeArr.getModeName().contains(input)) {
							if(!listMode.contains(modeArr)) {
								listMode.add(modeArr);
							}
						}
					}
					for (ModeList model :allModeListArr) {
                        isMatch=0;
						if (model.getDeviceName().contains(input)) {
							/////// 包含设备名称
                            isMatch=1;
						}
						else if (getLogicTitle(model).contains(input)) {
							////// 包含逻辑指令
                            isMatch=1;
						}
						else if (model.getRoomName().contains(input)) {
               				////// 房间名称
                            isMatch=1;
						}
						if(isMatch>0){
                    		//[self checkModeModel:model];
							for (Mode modeArr : resultList) {
								if(model.getModeId()==modeArr.getId()) {
									if(!listMode.contains(modeArr)) {
										listMode.add(modeArr);
									}
								}
							}
						}
					}
				}
			} else{
				listMode=ModeFactory.getInstance().getModesByType(modeType,-1);
			}
			adapter = new ModeTabAdapter(activity,listMode);
			adapter.setOnItemClickListener(onItemClickListener);
			modeList.setAdapter(adapter);
		}

		@Override
		public void afterTextChanged(Editable editable) {
		}
	}

	///// 获取逻辑指令文本
	String getLogicTitle(ModeList model){
		String deleyTime=model.getModeDelayed().replace("秒","");
		if(Integer.parseInt(deleyTime)>0){
			deleyTime ="延时" + deleyTime + "秒";
		}
		String modePeriod="";
		if((model.getModePeriod().length()>0) && (!model.getModePeriod().equals("00:00--00:00"))){
			modePeriod = model.getModePeriod();
		}
		String month="";
		int statrM=0;
		int endM=0;
		if(model.getBeginMonth().length()>0){
			statrM=Integer.valueOf(model.getBeginMonth().substring(0, 1), 16);
		}
		if(model.getEndMonth().length()>0){
			endM=Integer.valueOf(model.getEndMonth().substring(0, 1), 16);
		}
		if(statrM>0 && endM>0){
			month=model.getBeginMonth()+"~"+model.getEndMonth();
		}
		return model.getModeAction() + model.getModeFunction() + model.getModeTime() + "  " + deleyTime+ "   "+ modePeriod + "    " + month;
	}
}
