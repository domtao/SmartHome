package com.zunder.smart.activity.room;

import java.util.ArrayList;
import java.util.List;

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.cusbutton.ButtonUtils;
import com.zunder.cusbutton.RMCBean;
import com.zunder.image.adapter.RoomImageAdapter;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.R.id;

import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.image.adapter.ImageNetAdapter;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.RmcBeanFactory;
import com.zunder.smart.dao.impl.factory.RmcTabFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.json.Constants;
import com.zunder.image.model.ImageNet;
import com.zunder.smart.model.Room;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.view.ListViewDecoration;
import com.zunder.smart.webservice.ImageServiceUtils;

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

public class RoomAddFragment extends Fragment implements OnClickListener,View.OnTouchListener {
	private EditText roomName;
	private static String modelOpration = "Add";
	static int seqencing = 0;
	private int pos = -1;
	private static int Id = 0;
	Activity activity;
	private TextView backTxt;
	private TextView editeTxt;
	private RelativeLayout imageLayout;
	private SmartImageView imageView;
	private String imagePath=Constants.ROOMIMAGEPATH;
	private List<ImageNet> listImage=new ArrayList <ImageNet>();
	private RoomImageAdapter imageAdapter;
	SwipeMenuRecyclerView gridView;
	RelativeLayout typeLayout;
	private TextView typeTxt;
	private int isShow=1;
	ActionViewWindow typeWindow;
	String centername="0";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_room_add, container,
				false);

		activity = getActivity();
		backTxt = (TextView) root.findViewById(id.backTxt);
		editeTxt = (TextView) root.findViewById(id.editeTxt);
		roomName = (EditText) root.findViewById(id.roomName);
		imageLayout=(RelativeLayout)root.findViewById(id.imageLayout);
		imageView=(SmartImageView) root.findViewById(id.imageIco);
		typeLayout=(RelativeLayout)root.findViewById(id.typeLayout);
		typeTxt = (TextView) root.findViewById(id.typeTxt);
		imageLayout.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		root.setOnTouchListener(this);
		typeLayout.setOnClickListener(this);
		gridView = (SwipeMenuRecyclerView) root.findViewById(R.id.roomGrid);
		gridView.setLayoutManager(new GridLayoutManager(activity, 3));// 布局管理器。
		gridView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		gridView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		gridView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		gridView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		new DataTask(21).execute();
		return root;
	}

	Room room;

	public void setInit(String _modelOpration,Room _room) {
		this.modelOpration=_modelOpration;
		this.room=_room;
		if (modelOpration.equals("Add")) {
			roomName.setText("");
			isShow=1;
			typeTxt.setText(getisShow(isShow));
			imageView.setImageResource(R.mipmap.zun_add);
			seqencing = RoomFactory.getInstance().getAll().size()+1;
			new DataTask(21).execute();
		} else {
			if (room != null) {
				this.Id=room.getId();
				roomName.setText(room.getRoomName());
                imagePath=room.getRoomImage();
				seqencing = room.getSeqencing();
				imageView.setImageUrl(Constants.HTTPS+imagePath);
				isShow=room.getIsShow();
				typeTxt.setText(getisShow(isShow));
				centername=room.getData1();
				if(isShow==2){
					new DataTask(25).execute();
				}else{
					new DataTask(21).execute();
				}
			}

		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			TabHomeActivity.getInstance().roomFragment.setAdpapter();
			onDialogDis();
		}
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case id.backTxt:
			TabHomeActivity.getInstance().hideFragMent(2);
			break;
		case id.editeTxt:
			save();
			break;
			case id.typeLayout:
				typeWindow=new ActionViewWindow(activity,"类型", ListNumBer.roomType(),isShow);
				typeWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
						isShow=pos;
						typeTxt.setText(ItemName);
						typeWindow.dismiss();
						if(pos==2){
							new DataTask(25).execute();
						}else{
							new DataTask(21).execute();
						}
					}
					@Override
					public void cancle() {
					}
				});
				typeWindow.show();
				break;
		default:
			break;
		}

	}

	public void save() {
		try {

			String name = roomName.getText().toString().trim();
			if ("".equals(name)) {
				Toast.makeText(activity,"房间名称不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}

			Room room = new Room();
			room.setRoomName(name);
			room.setRoomImage(imagePath);
			room.setSeqencing(seqencing);
			room.setIsShow(isShow);
			room.setData1(AppTools.getNumbers(centername));
			if (this.modelOpration.equals("Add")) {
				int result = sql().insertRoom(room);
				if (result > 1) {
					RoomFactory.getInstance().clearList();
					if(isShow==2){
						insertRmcBean(name);
					}
					ToastUtils.ShowSuccess(activity, getString(R.string.addSuccess),
							Toast.LENGTH_SHORT,true);
					if(TabHomeActivity.getInstance()!=null) {
						TabHomeActivity.getInstance().RoomChange();
					}
				} else if (result == 1) {
					ToastUtils.ShowError(activity, getString(R.string.exist),
							Toast.LENGTH_SHORT,true);
				}
			} else if (this.modelOpration.equals("Edite")) {
				room.setId(Id);
				int num = sql().updateRoom(room, room.getRoomName());
				if (num == 2) {
					ToastUtils.ShowError(activity, getString(R.string.exist),
							Toast.LENGTH_SHORT,true);
				} else if (num == 1) {
					if(isShow==2){
						int centerIndex=Integer.parseInt(AppTools.getNumbers(centername));
						RMCBean rmcBean=RmcBeanFactory.getInstance().junRMC(Id);
						if(rmcBean!=null){
							if(rmcBean.getBtnType()!=centerIndex){
								sql().deleteRMCBeanByRoomId(Id);
								insertRmcBean(name);
							}
						}else{
							insertRmcBean(name);
						}
					}
					RoomFactory.getInstance().clearList();
					ToastUtils.ShowSuccess(activity, getString(R.string.updateSuccess),
							Toast.LENGTH_SHORT,true);
					if(TabHomeActivity.getInstance()!=null) {
						TabHomeActivity.getInstance().RoomChange();
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public void insertRmcBean(String name){
		Room roomCenter=RoomFactory.getInstance().getRoomByName(name);
		if(roomCenter!=null){
			int centerIndex=Integer.parseInt(AppTools.getNumbers(centername));
			List <RMCBean> list=new ArrayList <RMCBean>();
			if(centerIndex==1) {
				list = ButtonUtils.getInstance().getRMCBean1();
			}else	if(centerIndex==2) {
				list = ButtonUtils.getInstance().getRMCBean2();
			}else	if(centerIndex==3) {
				list = ButtonUtils.getInstance().getRMCBean3();
			}
			if(list.size()>0){
				for (int i=0;i<list.size();i++){
					RMCBean rmcBean=list.get(i);
					rmcBean.setRoomId(roomCenter.getId());
					rmcBean.setCodeType(-1);
					rmcBean.setBtnAction("");
					rmcBean.setBtnSeqencing(i);
					rmcBean.setBtnType(centerIndex);
					rmcBean.setBtnSize(14);
					rmcBean.setBtnColor("000000");
					MyApplication.getInstance().getWidgetDataBase().insertRMCBean(rmcBean);
				}
			}
		}
		RmcBeanFactory.getInstance().clearList();
        RmcTabFactory.getInstance().clear();
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	class DataTask extends AsyncTask<String, Void, List<ImageNet>> {

		private int typeId;
		public DataTask(int typeId){
			this.typeId=typeId;
		}

		@Override
		protected void onPreExecute() {

		}
		@Override
		protected  List<ImageNet> doInBackground(String... params) {
			List<ImageNet> result = null;
			try {
				String resultParam = ImageServiceUtils.getImagesByType(typeId);
				result = (List<ImageNet>) JSONHelper.parseCollection(
						resultParam, List.class, ImageNet.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute( List<ImageNet> result) {
			listImage.clear();
			if (result != null && result.size() > 0) {
				listImage.addAll(result);
				imageAdapter = new RoomImageAdapter(activity,listImage);
				imageAdapter.setOnItemClickListener(onItemClickListener);
				gridView.setAdapter(imageAdapter);
			}
		}
	}
	private com.zunder.smart.listener.OnItemClickListener onItemClickListener = new com.zunder.smart.listener.OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
            imagePath=listImage.get(position).getImageUrl();
			if(TextUtils.isEmpty(roomName.getText())){
				roomName.setText(listImage.get(position).getImageName());
			}
			centername=listImage.get(position).getImageName();
            imageView.setImageUrl(Constants.HTTPS+imagePath);
		}
	};
	public void onDialogDis(){
		if(typeWindow!=null&&typeWindow.isShow()){
			typeWindow.dismiss();
		}
	}
	public String getisShow(int isShow){
		if(isShow==0){
			return "隐藏";
		}else if(isShow==1){
			return "家居";
		}else {
			return "中控";
		}
	}
}
