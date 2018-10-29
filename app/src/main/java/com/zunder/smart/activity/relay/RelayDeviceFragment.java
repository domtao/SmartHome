package com.zunder.smart.activity.relay;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.Products;
import com.zunder.smart.model.Room;

import com.zunder.smart.service.SendCMD;
import com.zunder.smart.spinner.ArceSpinerAdapter;
import com.zunder.smart.spinner.ArceSpinerPopWindow;
import com.zunder.smart.spinner.DeviceTypeSpinerAdapter;
import com.zunder.smart.spinner.ProductSpinerAdapter;
import com.zunder.smart.spinner.ProductSpinerPopWindow;
import com.zunder.smart.spinner.TypeSpinerPopWindow;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.List;

public class RelayDeviceFragment extends Fragment implements OnClickListener{
	DeviceAdapter adapter;
	List<Device> listDevice;
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

	//0 已添加 1未添加

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_relay_device, container,
				false);
		activity = getActivity();
		arceLayout = (RelativeLayout) root.findViewById(R.id.arceLayout);
		typeLayout = (RelativeLayout) root.findViewById(R.id.typeLayout);
		productLayout = (RelativeLayout) root.findViewById(R.id.productLayout);
		arceValue = (TextView) root.findViewById(R.id.arce_value);
		typeValue = (TextView) root.findViewById(R.id.type_value);
		yyValue = (TextView) root.findViewById(R.id.yy_value);
		moreBtn=(Button)root.findViewById(R.id.moreBtn);
		moreBtn.setOnClickListener(this);
		arceLayout.setOnClickListener(this);
		typeLayout.setOnClickListener(this);
		productLayout.setOnClickListener(this);
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
//		setPinerAdapter();
		return root;
	}
	public void initAdapter(){
		setPinerAdapter();
		listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, typeId, productId,1);
		adapter = new DeviceAdapter(activity, listDevice,roomId);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);

    }


	public void setDeviceAdapter() {
		listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, typeId, productId,1);
		adapter = new DeviceAdapter(activity,listDevice,roomId);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);
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
						serchDevice();
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
						serchDevice();
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
			TabMyActivity.getInstance().hideFragMent(9);
			break;
			case R.id.editeTxt:
				if (editeTxt.getText().toString().equals(getString(R.string.more_select))) {
					adapter.muCheck(0);
					editeTxt.setText(getString(R.string.cancle));
					moreBtn.setVisibility(View.VISIBLE);
				} else if (editeTxt.getText().toString().equals(getString(R.string.cancle))) {
					adapter.muCheck(-1);
					editeTxt.setText(getString(R.string.more_select));
					moreBtn.setVisibility(View.GONE);
				}
				break;
			case R.id.moreBtn:
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
//			TabMyActivity.getInstance().relayFragment.initAdpapter();
		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			DialogAlert alert = new DialogAlert(activity);
			alert.init(
					getString(R.string.tip),
					getString(R.string.isSetRelay)
							+ listDevice.get(position).getDeviceName()
							+ getString(R.string.Relay));
			alert.setSureListener(new DialogAlert.OnSureListener() {

				@Override
				public void onSure() {
					// TODO Auto-generated method stub
					Device device=listDevice.get(position);
					if(DeviceFactory.getInstance().getGateWayDevice()!=null){
						device.setDeviceBackCode(DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
						device.setCmdDecodeType(DeviceFactory.getInstance().getGateWayDevice().getCmdDecodeType());
					}
					SendCMD.getInstance().sendCMD(247, "1", device);
					TabMyActivity.getInstance().hideFragMent(9);
					SendCMD.getInstance().sendCMD(247, "2", DeviceFactory.getInstance().getGateWayDevice());
				}
				@Override
				public void onCancle() {
					// TODO Auto-generated method stub
				}
			});
			alert.show();

			}
	};

}
