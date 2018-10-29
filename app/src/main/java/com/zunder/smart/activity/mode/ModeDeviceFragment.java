package com.zunder.smart.activity.mode;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zunder.smart.activity.constants.ActionStrings;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.model.ModeList;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabModeActivity;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Products;
import com.zunder.smart.model.Room;
import com.zunder.smart.popwindow.DoorPopupWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.spinner.ArceSpinerAdapter;
import com.zunder.smart.spinner.ArceSpinerPopWindow;
import com.zunder.smart.spinner.DeviceTypeSpinerAdapter;
import com.zunder.smart.spinner.ProductSpinerAdapter;
import com.zunder.smart.spinner.ProductSpinerPopWindow;
import com.zunder.smart.spinner.TypeSpinerPopWindow;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModeDeviceFragment extends Fragment implements OnClickListener,View.OnTouchListener{
	DeviceAdapter adapter;
	List<Device> listDevice;
	List<Device> searchListDevice=new ArrayList <Device>();
	private TextView backTxt;
	private TextView editeTxt;
	private TextView arceValue;
	private TextView typeValue;
	private TextView yyValue;
	RelativeLayout arceLayout, typeLayout, productLayout;
	private List<Room> listArce = new ArrayList<Room>();
	private List<DeviceType> listType = new ArrayList<DeviceType>();
	private List<Products> listProduct = new ArrayList<Products>();
	private ArceSpinerAdapter arceSpinerAdapter;
	private DeviceTypeSpinerAdapter typeSpinerAdapter;
	private ProductSpinerAdapter productSpinerAdapter;
	private SwipeMenuRecyclerView listView;
	private static int roomId = 0;
	private static int typeId = 0;
	private static int productId = -1;
	private static String productCode="FF";
	public Activity activity;
	private Button moreBtn;
	private CheckBox checkAll;
	private LinearLayout checkLayout;
	int modeId=0;
	//0 已添加 1未添加

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_mode_device, container,
				false);
		activity = getActivity();
		arceLayout = (RelativeLayout) root.findViewById(R.id.arceLayout);
		typeLayout = (RelativeLayout) root.findViewById(R.id.typeLayout);
		productLayout = (RelativeLayout) root.findViewById(R.id.productLayout);
		arceValue = (TextView) root.findViewById(R.id.arce_value);
		typeValue = (TextView) root.findViewById(R.id.type_value);
		yyValue = (TextView) root.findViewById(R.id.yy_value);
		checkAll=(CheckBox)root.findViewById(R.id.checkAll);
		checkLayout=(LinearLayout) root.findViewById(R.id.checkLayout);
		moreBtn=(Button)root.findViewById(R.id.moreBtn);
		moreBtn.setOnClickListener(this);
		checkAll.setOnClickListener(this);
		arceLayout.setOnClickListener(this);
		typeLayout.setOnClickListener(this);
		productLayout.setOnClickListener(this);
		root.setOnTouchListener(this);
		 roomId = 0;
		typeId = 0;
		productId=-1;
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		listView = (SwipeMenuRecyclerView) root.findViewById(R.id.deviceList);
		listView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		setPinerAdapter();
		return root;
	}
	public void initAdapter(int modeId){
		this.modeId=modeId;
		listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, typeId, 1,1);
			adapter = new DeviceAdapter(activity, listDevice,roomId);
			adapter.setOnItemClickListener(onItemClickListener);
			listView.setAdapter(adapter);
		checkLayout.setVisibility(View.GONE);
		checkAll.setChecked(false);
    }

	public void setDeviceAdapter() {

		listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, typeId, productId,1);
		adapter = new DeviceAdapter(activity,listDevice,roomId);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);
		if (DeviceAdapter.edite == 0) {
			editeTxt.setVisibility(View.VISIBLE);
		} else {
			editeTxt.setVisibility(View.GONE);
		}
	}
	public void setPinerAdapter() {
		Room room = new Room();
		room.setId(0);
		room.setRoomName(getString(R.string.allArce));
		listArce.add(room);
		listArce.addAll(RoomFactory.getInstance().getAll());
		DeviceType deviceType = new DeviceType();
		deviceType.setId(0);
		deviceType.setDeviceTypeName(getString(R.string.allType));
		listType.add(deviceType);
		listType.addAll(DeviceTypeFactory.getInstance().getDeviceTypes(1));
		if (listArce != null) {
			arceSpinerAdapter = new ArceSpinerAdapter(activity);
			arceSpinerAdapter.refreshData(listArce, 0);
			arceSpinerPopWindow = new ArceSpinerPopWindow(activity);
			arceSpinerPopWindow.setAdatper(arceSpinerAdapter);
			arceSpinerPopWindow.setItemListener(new ArceSpinerAdapter.IOnItemSelectListener() {

				@Override
				public void onItemClick(int pos) {
					// TODO Auto-generated method stub
					if (pos >= 0 && pos <= listArce.size()) {
						Room value = listArce.get(pos);
						arceValue.setText(value.getRoomName());
						roomId = value.getId();
						setDeviceAdapter();
					}
				}
			});
		}

		if (listType != null) {
			typeSpinerAdapter = new DeviceTypeSpinerAdapter(activity);
			typeSpinerAdapter.refreshData(listType, 0);
			typeSpinerPopWindow = new TypeSpinerPopWindow(activity);
			typeSpinerPopWindow.setAdatper(typeSpinerAdapter);
			typeSpinerPopWindow.setItemListener(new ArceSpinerAdapter.IOnItemSelectListener() {

				@Override
				public void onItemClick(int pos) {
					// TODO Auto-generated method stub
					if (pos >= 0 && pos <= listType.size()) {
						DeviceType value = listType.get(pos);
						typeValue.setText(value.getDeviceTypeName());
						typeId = value.getDeviceTypeKey();

						listProduct = ProductFactory.getProducts(typeId);
						productSpinerAdapter.refreshData(listProduct, 0);
						productSpinerPopWindow.setAdatper(productSpinerAdapter);
						if (listProduct.size() > 0) {
							yyValue.setText(listProduct.get(0)
									.getProductsName());
							productCode = listProduct.get(0).getProductsCode();
						} else {
							yyValue.setText("全部产品");
							productCode="FF";
						}
					setDeviceAdapter();
					}
				}
			});
		}
		listProduct = ProductFactory.getMenu();
		if (listProduct != null) {
			productSpinerAdapter = new ProductSpinerAdapter(activity);
			productSpinerAdapter.refreshData(listProduct, 0);

			productSpinerPopWindow = new ProductSpinerPopWindow(activity);
			productSpinerPopWindow.setAdatper(productSpinerAdapter);
			productSpinerPopWindow.setItemListener(new ArceSpinerAdapter.IOnItemSelectListener() {

				@Override
				public void onItemClick(int pos) {
					// TODO Auto-generated method stub
					if (pos >= 0 && pos <= listProduct.size()) {
						Products value = listProduct.get(pos);
						yyValue.setText(value.getProductsName());
						productId = value.getProductsKey();
						productCode=value.getProductsCode();
						setDeviceAdapter();
					}
				}
			});
		}
	}

	private ArceSpinerPopWindow arceSpinerPopWindow;

	private void showArceSpinWindow() {
		arceSpinerPopWindow.setWidth(LayoutParams.FILL_PARENT);
		arceSpinerPopWindow.showAsDropDown(arceLayout);
	}

	private TypeSpinerPopWindow typeSpinerPopWindow;

	private void showTypeSpinWindow() {
		typeSpinerPopWindow.setWidth(LayoutParams.FILL_PARENT);
		typeSpinerPopWindow.showAsDropDown(typeLayout);
	}

	private ProductSpinerPopWindow productSpinerPopWindow;

	private void showProiductSpinWindow() {
		productSpinerPopWindow.setWidth(LayoutParams.FILL_PARENT);
		productSpinerPopWindow.showAsDropDown(yyValue);
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
			case R.id.editeTxt:
				if (editeTxt.getText().toString().equals(getString(R.string.more_select))) {
					adapter.muCheck(0);
					editeTxt.setText(getString(R.string.cancle));
					checkLayout.setVisibility(View.VISIBLE);
				} else if (editeTxt.getText().toString().equals(getString(R.string.cancle))) {
					adapter.muCheck(-1);
					editeTxt.setText(getString(R.string.more_select));
					checkLayout.setVisibility(View.GONE);
				}
				break;
			case R.id.moreBtn:
				final ActionViewWindow actionViewWindow=new ActionViewWindow(activity,"快捷控制", ActionStrings.getInstance().getActionStrings(1),1);

              actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
				  @Override
				  public void onItem(int pos, String ItemName) {
					  save(ItemName);
					  checkLayout.setVisibility(View.GONE);
					  actionViewWindow.dismiss();
				  }

				  @Override
				  public void cancle() {
					  actionViewWindow.dismiss();
				  }
			  });actionViewWindow.show();
				break;
			case R.id.checkAll:
				if(checkAll.isChecked()){
					adapter.muCheck(1);
				}else{
					adapter.muCheck(0);
				}
				break;
			case R.id.arceLayout:
			showArceSpinWindow();
			break;
		case R.id.typeLayout:
			showTypeSpinWindow();
			break;
		case R.id.productLayout:
			showProiductSpinWindow();
			break;
		default:
			break;
		}
	}
	//joe 搜索设备
	public void serchDevice(){
		if(productCode.equals("FF")){
			SendCMD.getInstance().sendCMD(244, "FF", null);
		}else{
			Device device=new DeviceFactory().getDevice();
			device.setProductsCode(productCode);
			String hexStr= AppTools.toHex(typeId);
			if(typeId==1){
				device.setProductsCode("01");
				SendCMD.getInstance().sendCMD(244, hexStr, device);
				device.setProductsCode("02");
				SendCMD.getInstance().sendCMD(244, hexStr, device);
				device.setProductsCode("03");
				SendCMD.getInstance().sendCMD(244, hexStr, device);
			}else{
				SendCMD.getInstance().sendCMD(244, hexStr, device);
			}
		}
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
				TabModeActivity.getInstance().modeListFragment.initAdapter(modeId);
			}else{
				TabMainActivity.getInstance().modeListFragment.initAdapter(modeId);
			}
		}else{
			editeTxt.setText(getString(R.string.more_select));
		}
	}
	public void back() {

		if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
			TabModeActivity.getInstance().hideFragMent(3);
		}else{
			TabMainActivity.getInstance().hideFragMent(3);
		}
	}


	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
				TabModeActivity.getInstance().showFragMent(4);
				TabModeActivity.getInstance().modeListActionFragment.initAdapter(0, modeId, listDevice.get(position).getId());
			}else{
				TabMainActivity.getInstance().showFragMent(4);
				TabMainActivity.getInstance().modeListActionFragment.initAdapter(0, modeId, listDevice.get(position).getId());
			}
		}
	};
    public void save(String strValue) {
        HashMap<Integer, Boolean> isSelected = DeviceAdapter.getIsSelected();
        for (int i = 0; i < listDevice.size(); i++) {
            if (isSelected.get(i) == true) {
                Device device = listDevice.get(i);
				ModeList modeList = new ModeList();
                modeList.setModeAction(strValue);
                modeList.setModeFunction("");
                modeList.setModeTime("0");
                modeList.setModeDelayed("0");
                modeList.setModePeriod("00:00~00:00");
                modeList.setDeviceId(device.getId());
                modeList.setBeginMonth("0");
				modeList.setEndMonth("0");
                modeList.setSeqencing(ModeListFactory.getInstance().getAll().size() + 1);
                modeList.setModeId(modeId);
                if (sql().insertModeList(modeList) > 0) {
                    ModeListFactory.getInstance().clearList();
                }
            }
        }
		editeTxt.setText(getString(R.string.more_select));
        adapter.muCheck(-1);
		checkLayout.setVisibility(View.GONE);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
