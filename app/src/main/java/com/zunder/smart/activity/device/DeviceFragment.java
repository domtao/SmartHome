package com.zunder.smart.activity.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;

import com.zunder.smart.activity.constants.ActionStrings;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.tv.ChannelAddActivity;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.adapter.SearchDeviceAdapter;

import com.zunder.smart.aiui.info.AnHong;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;

import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.dialog.DialogAlert;

import com.zunder.smart.listener.DeviceListener;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Room;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.model.Products;

import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.spinner.ArceSpinerAdapter;
import com.zunder.smart.spinner.ArceSpinerPopWindow;
import com.zunder.smart.spinner.DeviceTypeSpinerAdapter;
import com.zunder.smart.spinner.ProductSpinerAdapter;
import com.zunder.smart.spinner.ProductSpinerPopWindow;
import com.zunder.smart.spinner.TypeSpinerPopWindow;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceFragment extends Fragment implements OnClickListener,
        DeviceStateListener, DeviceListener, View.OnTouchListener {
    DeviceAdapter adapter;
    List<Device> listDevice;
    List<Device> searchListDevice = new ArrayList<Device>();
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
    private static String selectCmd = "";
    private static int productId = -1;
    private static String productCode = "FF";
    public Activity activity;
    RadioButton unAddRadio, addRadio;
    SearchDeviceAdapter searchAdapter;
    private Button handAdd;
    //0 已添加 1未添加
    private int deviceStyle = 0;
    private LinearLayout checkLayout;
    private CheckBox checkAll;
    private Button moreBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_device, container,
                false);
        activity = getActivity();
        arceLayout = (RelativeLayout) root.findViewById(R.id.arceLayout);
        typeLayout = (RelativeLayout) root.findViewById(R.id.typeLayout);
        productLayout = (RelativeLayout) root.findViewById(R.id.productLayout);
        arceValue = (TextView) root.findViewById(R.id.arce_value);
        typeValue = (TextView) root.findViewById(R.id.type_value);
        yyValue = (TextView) root.findViewById(R.id.yy_value);
        unAddRadio = (RadioButton) root.findViewById(R.id.unAddRadio);
        addRadio = (RadioButton) root.findViewById(R.id.addRadio);
        handAdd = (Button) root.findViewById(R.id.handAdd);
        checkAll = (CheckBox) root.findViewById(R.id.checkAll);
        checkAll.setOnClickListener(this);
        checkLayout = (LinearLayout) root.findViewById(R.id.checkLayout);
        checkLayout.setOnClickListener(this);
        moreBtn = (Button) root.findViewById(R.id.moreBtn);
        moreBtn.setOnClickListener(this);
        handAdd.setOnClickListener(this);
        arceLayout.setOnClickListener(this);
        typeLayout.setOnClickListener(this);
        productLayout.setOnClickListener(this);
        unAddRadio.setOnClickListener(this);
        addRadio.setOnClickListener(this);
        root.setOnTouchListener(this);
        roomId = 0;
        typeId = 0;
        productId = -1;
        checkAll = (CheckBox) root.findViewById(R.id.checkAll);
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
        listView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        listView.setSwipeMenuItemClickListener(menuItemClickListener);
        listView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
        listView.setOnItemMoveListener(onItemMoveListener);

//		setPinerAdapter();
        return root;
    }

    public void initAdapter() {
        TcpSender.setDeviceStateListener(this);
        TcpSender.setDeviceListener(this);
        editeTxt.setText(getString(R.string.more_select));
        checkAll.setChecked(false);
        if (deviceStyle == 0) {
            addRadio.setTextColor(getResources().getColor(R.color.green));
            unAddRadio.setTextColor(getResources().getColor(R.color.black));
            roomId = 0;
            typeId = 0;
            productId = -1;
            setPinerAdapter();
            addRadio.setChecked(true);
            editeTxt.setVisibility(View.GONE);
            listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, typeId, 0, 1);
            adapter = new DeviceAdapter(activity, listDevice, roomId);
            adapter.setOnItemClickListener(onItemClickListener);
            listView.setAdapter(adapter);
        } else {
            addRadio.setTextColor(getResources().getColor(R.color.black));
            unAddRadio.setTextColor(getResources().getColor(R.color.green));
        }
    }

    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            // 当Item被拖拽的时候。
            Device _from = listDevice.get(fromPosition);
            if (fromPosition < toPosition) {
                listDevice.add(toPosition + 1, _from);
                listDevice.remove(fromPosition);
            } else if (fromPosition > toPosition) {
                listDevice.add(toPosition, _from);
                listDevice.remove(fromPosition + 1);
            }
            adapter.notifyItemMoved(fromPosition, toPosition);
            MyApplication.getInstance().getWidgetDataBase().updateDeviceSort(listDevice);
            DeviceFactory.getInstance().clearList();
            return true;// 返回true表示处理了，返回false表示你没有处理。
        }

        @Override
        public void onItemDismiss(int position) {
            // 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
            // 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
        }
    };

    public void setDeviceAdapter() {
        listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, typeId, 0, 1);
        adapter = new DeviceAdapter(activity, listDevice, roomId);
        adapter.setOnItemClickListener(onItemClickListener);
        listView.setAdapter(adapter);
    }

    public void setPinerAdapter() {
        listArce.clear();
        listType.clear();
        Room room = new Room();
        room.setId(0);
        room.setRoomName(getString(R.string.allArce));
        listArce.add(room);
        listArce.addAll(RoomFactory.getInstance().getAll());
        DeviceType deviceType = new DeviceType();
        deviceType.setId(0);
        deviceType.setDeviceTypeName(getString(R.string.allType));
        arceValue.setText("全部区域");
        typeValue.setText("全部类型");
        yyValue.setText("默认全部");
        listType.add(deviceType);
        listType.addAll(DeviceTypeFactory.getInstance().getDeviceTypes(1));

        Log.d("测试", "setPinerAdapter: 进入判断前");


        if (listArce != null) {
            arceSpinerAdapter = new ArceSpinerAdapter(activity);
            Log.d("测试", "setPinerAdapter: 看看列表 listArce" + listArce);
            arceSpinerAdapter.refreshData(listArce, 0);
            arceSpinerPopWindow = new ArceSpinerPopWindow(activity);
            arceSpinerPopWindow.setAdatper(arceSpinerAdapter);
            arceSpinerPopWindow.setItemListener(new ArceSpinerAdapter.IOnItemSelectListener() {
                @Override
                public void onItemClick(int pos) {
                    // TODO Auto-generated method stub
                    Log.d("测试", "setPinerAdapter: 点击的是第" + pos + "项");

                    if (pos >= 0 && pos <= listArce.size()) {
                        Room value = listArce.get(pos);
                        arceValue.setText(value.getRoomName());
                        if (deviceStyle == 0) { //已添加
                            roomId = value.getId();
                            setDeviceAdapter();
                        } else {  //未添加
                            searchListDevice.clear();
                            listView.setAdapter(searchAdapter);
                            selectCmd = value.getPrimary_Key();
                        }
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
                            productCode = "FF";
                        }
                        if (deviceStyle == 0) {
                            setDeviceAdapter();
                        } else {
                            serchDevice();
                        }
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
                        productCode = value.getProductsCode();
                        if (deviceStyle == 0) {
                            setDeviceAdapter();
                        } else {
                            serchDevice();
                        }
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
            case R.id.arceLayout:
                showArceSpinWindow();
                break;
            case R.id.typeLayout:
                showTypeSpinWindow();
                break;
            case R.id.productLayout:
                showProiductSpinWindow();
                break;
            case R.id.addRadio:
                //joe 已添加
                deviceStyle = 0;
                addRadio.setTextColor(getResources().getColor(R.color.green));
                unAddRadio.setTextColor(getResources().getColor(R.color.black));
                editeTxt.setVisibility(View.GONE);
                checkLayout.setVisibility(View.GONE);
                listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, typeId, 0, 1);
                adapter = new DeviceAdapter(activity, listDevice, roomId);
                adapter.setOnItemClickListener(onItemClickListener);
                listView.setAdapter(adapter);
                arceValue.setText("全部区域");
                listArce.clear();
                Room room = new Room();
                room.setId(0);
                room.setRoomName(getString(R.string.allArce));
                listArce.add(room);
                listArce.addAll(RoomFactory.getInstance().getAll());
                arceSpinerAdapter = new ArceSpinerAdapter(activity);
                arceSpinerAdapter.refreshData(listArce, 0);
                if (arceSpinerPopWindow == null) {
                    arceSpinerPopWindow = new ArceSpinerPopWindow(activity);
                }
                arceSpinerPopWindow.setAdatper(arceSpinerAdapter);

                break;
            case R.id.unAddRadio:
                //joe 未添加
                addRadio.setTextColor(getResources().getColor(R.color.black));
                unAddRadio.setTextColor(getResources().getColor(R.color.green));
                editeTxt.setText(getString(R.string.more_select));
                editeTxt.setVisibility(View.VISIBLE);
                deviceStyle = 1;
                searchListDevice.clear();
                searchAdapter = new SearchDeviceAdapter(activity, searchListDevice);
                searchAdapter.setOnItemClickListener(onItemSearchListener);
                listView.setAdapter(searchAdapter);
                arceValue.setText("全部命令");
                listArce.clear();
                listArce.addAll(RoomFactory.getInstance().getSelect());
                arceSpinerAdapter = new ArceSpinerAdapter(activity);
                arceSpinerAdapter.refreshData(listArce, 0);
//				arceSpinerPopWindow = new ArceSpinerPopWindow(activity);
                arceSpinerPopWindow.setAdatper(arceSpinerAdapter);
                serchDevice();
                break;
            case R.id.handAdd:
                TabMyActivity.getInstance().showFragMent(-1);
                TabMyActivity.getInstance().deviceAddFragment.setAdpapter("Add", 0, null);
                break;
            case R.id.editeTxt:
                if (editeTxt.getText().toString().equals(getString(R.string.more_select))) {
                    searchAdapter.muCheck(0);
                    editeTxt.setText(getString(R.string.cancle));
                    checkLayout.setVisibility(View.VISIBLE);
                } else if (editeTxt.getText().toString().equals(getString(R.string.cancle))) {
                    searchAdapter.muCheck(-1);
                    editeTxt.setText(getString(R.string.more_select));
                    checkAll.setChecked(false);
                    checkLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.moreBtn:
                for (int i = 0; i < searchListDevice.size(); i++) {
                    Device device = new Device();
                    device.setId(0);
                    device.setDeviceName(searchListDevice.get(i).getProductsName() + searchListDevice.get(i).getDeviceID() + searchListDevice.get(i).getDeviceIO());
                    device.setDeviceID(searchListDevice.get(i).getDeviceID());
                    device.setDeviceIO(searchListDevice.get(i).getDeviceIO());
                    device.setDeviceNickName("");
                    device.setDeviceImage("");
                    device.setSeqencing(DeviceFactory.getInstance().getAll().size() + 1);
                    device.setDeviceOnLine(1);
                    device.setDeviceTimer("0");
                    device.setDeviceOrdered("0");
                    device.setStartTime("00:00");
                    device.setEndTime("00:00");
                    device.setSceneId(0);
                    device.setDeviceTypeKey(searchListDevice.get(i).getDeviceTypeKey());
                    device.setProductsKey(searchListDevice.get(i).getProductsKey());
                    device.setGradingId(1);
                    device.setRoomId(1);
                    int result = sql().insertDevice(device);
                    DeviceFactory.getInstance().clearList();
                }
                DeviceFactory.getInstance().clearList();
                ToastUtils.ShowSuccess(activity, "设备创建成成功", Toast.LENGTH_SHORT, true);
                editeTxt.setText(getString(R.string.more_select));
                checkAll.setChecked(false);
                searchAdapter.muCheck(-1);
                checkLayout.setVisibility(View.GONE);
                break;
            case R.id.checkAll:
                if (checkAll.isChecked()) {
                    searchAdapter.muCheck(1);
                } else {
                    searchAdapter.muCheck(0);
                }
                searchAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    //joe 搜索设备
    public void serchDevice() {
        TcpSender.setDeviceListener(this);
        searchListDevice.clear();
        if (productCode.equals("FF")) {
            SendCMD.getInstance().sendCMD(244, "FF", null);
        } else {
            Device device = new DeviceFactory().getDevice();
            device.setProductsCode(productCode);
            String hexStr = AppTools.toHex(typeId);
            if (typeId == 1) {
                device.setProductsCode("01");
                SendCMD.getInstance().sendCMD(244, hexStr, device);
                device.setProductsCode("02");
                SendCMD.getInstance().sendCMD(244, hexStr, device);
                device.setProductsCode("03");
                SendCMD.getInstance().sendCMD(244, hexStr, device);
                device.setProductsCode("04");
                SendCMD.getInstance().sendCMD(244, hexStr, device);
            } else {
                SendCMD.getInstance().sendCMD(244, hexStr, device);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            TcpSender.setDeviceStateListener(null);
            TcpSender.setDeviceListener(null);
            deviceStyle = 0;
        }
    }

    public void back() {
        TabMyActivity.getInstance().hideFragMent(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, typeId, productId, 1);
        adapter = new DeviceAdapter(activity, listDevice, roomId);
        adapter.setOnItemClickListener(onItemClickListener);
        listView.setAdapter(adapter);
        //
    }

    @Override
    public void receiveDeviceStatus(String cmd) {
        // TODO Auto-generated method stub
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } else if (msg.what == 1) {
                if (adapter != null) {
                    sort(searchListDevice);
                    searchAdapter.notifyDataSetChanged();
                }
            }else if (msg.what == 2) {
                if (adapter != null) {
                    searchAdapter.notifyDataSetChanged();
                }
            }
        }

        ;
    };

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int hSize = getResources().getDimensionPixelSize(R.dimen.item_height);
            int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
            SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
                    .setBackgroundDrawable(R.color.red)
                    .setText(getString(R.string.delete))
                    .setWidth(wSize)
                    .setHeight(hSize);
            swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。


        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(final int position) {
            final Device device = adapter.getItems().get(position);
            TabMyActivity.getInstance().showFragMent(-1);
            TabMyActivity.getInstance().deviceAddFragment.setAdpapter("Edite", device.getId(), null);

        }
    };
    private OnItemClickListener onItemSearchListener = new OnItemClickListener() {
        @Override
        public void onItemClick(final int position) {
            if(selectCmd.length()==0||selectCmd.equals("FF")) {
                TabMyActivity.getInstance().showFragMent(-1);
                TabMyActivity.getInstance().deviceAddFragment.setAdpapter("newDevice", 0, searchListDevice
                        .get(position));
            }
        }
    };
    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {

                final Device device = adapter.getItems().get(adapterPosition);
                switch (menuPosition) {
                    case 0:
                        DialogAlert alert = new DialogAlert(activity);
                        //joe 未顯示 device.getDeviceName()
                        alert.init("",
                                "是否删除[" + device.getRoomName() + device.getDeviceName() + "]及关联的数据");
                        alert.setSureListener(new DialogAlert.OnSureListener() {

                            @Override
                            public void onSure() {
                                // TODO Auto-generated method stub
                                int id = device.getId();
                                int result = MyApplication.getInstance()
                                        .getWidgetDataBase().deleteDevice(id);
                                if (result > 0) {
                                    if (device.getDeviceTypeKey() == 5) {
                                        List<Device> deviceList = DeviceFactory.getInstance().getDeviceByAction(device.getRoomId(), 19, 0, 0);
                                        for (int i = 0; i < deviceList.size(); i++) {
                                            result = MyApplication.getInstance()
                                                    .getWidgetDataBase().deleteDevice(deviceList.get(i).getId());
                                        }
                                    }
                                    ToastUtils.ShowSuccess(activity,
                                            activity.getString(R.string.deleteSuccess),
                                            Toast.LENGTH_SHORT, true);
                                    adapter.getItems().remove(adapterPosition);
                                    DeviceFactory.getInstance().delete(id);
                                    ModeListFactory.getInstance().clearList();
                                    adapter.notifyDataSetChanged();
                                    //joe 這行

                                    SendCMD._GetCmdArr.clear();
                                    SendCMD.getInstance().sendCMD(254, "11:00:07:0" + device.getDeviceIO(), device);
                                    if (TabHomeActivity.getInstance() != null) {
                                        TabHomeActivity.getInstance().RoomChange();
                                    }
                                }
                            }

                            @Override
                            public void onCancle() {
                                // TODO Auto-generated method stub
                            }
                        });
                        alert.show();
                        break;
                }
            }
        }
    };

    //joe 搜索设备回码处理
    @Override
    public void searchDevice(String cmd) {
        if (deviceStyle == 0) {
            return;
        }
        String string = cmd.substring(4);
        boolean flag = true;
        if (typeId > 0) {
            String source, dest;
            flag = false;
            //*C(00)(cmd)(發送方)(接收方)(ID)(MID1)(MID2)
            //source = string.substring(2, 4);
            dest = string.substring(4, 6);
            switch (typeId) {
                //case 6:
                //	if (dest.equals("0E")) {
                //		flag = true;
                //	}
                //	break;
                default:
                    String hexStr = AppTools.toHex(typeId);
                    if (typeId == 1) {
                        if (dest.equals("01") || dest.equals("02") || dest.equals("03") || dest.equals("04")) {
                            flag = true;
                        }
                    } else {
                        if (dest.equals(productCode)) {
                            flag = true;
                        }
                    }
                    break;
            }
        }
        if (flag && (selectCmd.length() > 0) && (!selectCmd.equals("FF"))) {
            if (!string.substring(0, 2).equals(selectCmd)) {
                flag = false;
            }
        } else {
            if (!string.substring(0, 2).equals("12")) {
                flag = false;
            }
        }
        if (flag) {
            mutableThread(string);
        }
    }

    public void mutableThread(String invocationOperation) {

        Device backDeviceModel = returnCodeLabel(invocationOperation);
        if (backDeviceModel != null) {
            for (Device backModel : searchListDevice) {
                if (backModel.getPrimary_Key().equals(
                        backDeviceModel.getPrimary_Key())) {
                    searchListDevice.remove(backModel);
                    break;
                }
            }
            searchListDevice.add(backDeviceModel);
            handler.sendEmptyMessage(1);
        }
    }

    public Device returnCodeLabel(String string) {
        boolean flag = true;
        Device device = null;
        String cmdType = "";
        String deviceID = "";
        int CommandValue = Integer.valueOf(string.substring(0, 2), 16);
        if (CommandValue == 18) {
            device = new Device();
            device.setPrimary_Key(string);
            cmdType = string.substring(4, 6);
            deviceID = string.substring(6, 12);
            int routeIndex = Integer.parseInt(string.substring(14, 16), 16);
            int relayIndex = Integer.parseInt(string.substring(16, 18), 16);

            device.setDeviceID(deviceID);
            device.setProductsCode(cmdType);
            Products products = ProductFactory.getProduct(cmdType);
            if (products != null) {
                String productName = products.getProductsName();
                if (productName == "" || productName == null || productName.equals("")) {
                    productName = cmdType;
                }
                int IO = products.getProductsIO();
                device.setDeviceImage(products.getProductsImage());
                device.setCreationTime(getString(R.string.deviceId) + ":" + deviceID);
                device.setProductsIO(IO);
                device.setDeviceTypeKey(products.getDeviceTypekey());
                device.setProductsKey(products.getProductsKey());
                device.setRouteIndex(routeIndex & (1 << IO));
                device.setRelayIndex(relayIndex & 16);
                device.setTranceType(relayIndex & 3);
                String checkIoStr = string.substring(2, 4);
                device.setProductsName(productName);
                if (checkIoStr.equals("FA")) {
                    device.setDeviceIO("0");
                    device.setDeviceTimer(productName);
                } else {
                    device.setDeviceIO(string.substring(3, 4));
                    device.setDeviceTimer(productName + " 回路" + (Integer.parseInt(string.substring(3, 4)) + 1));
                }
                device.setCmdDecodeType(1);
                List<Device> deviceList = DeviceFactory.getInstance().getAll();
                for (Device deviceModel : deviceList) {
                    if (deviceModel.getDeviceTypeKey() < 4 || deviceModel.getDeviceTypeKey() > 6) {
                        if (deviceModel.getProductsCode().equals(device.getProductsCode()) && deviceModel.getDeviceID().equals(device.getDeviceID()) && deviceModel.getDeviceIO().equals(device.getDeviceIO())) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
        } else if (CommandValue == 0 || CommandValue == 3 || CommandValue == 4 || CommandValue == 25) {
            device = new Device();
            device.setPrimary_Key(string);
            cmdType = string.substring(4, 6);
            deviceID = string.substring(6, 12);
            int routeIndex = Integer.parseInt(string.substring(14, 16), 16);
            int relayIndex = Integer.parseInt(string.substring(16, 18), 16);

            device.setDeviceID(deviceID);
            device.setProductsCode(cmdType);
            Products products = ProductFactory.getProduct(cmdType);
            if (products != null) {
                String productName = products.getProductsName();
                if (productName == "" || productName == null || productName.equals("")) {
                    productName = cmdType;
                }
                int IO = products.getProductsIO();
                device.setDeviceImage(products.getProductsImage());
                device.setCreationTime(getString(R.string.deviceId) + ":" + deviceID);
                device.setProductsIO(IO);
                device.setDeviceTypeKey(products.getDeviceTypekey());
                device.setProductsKey(products.getProductsKey());
                device.setRouteIndex(routeIndex & (1 << IO));
                device.setRelayIndex(relayIndex & 16);
                device.setTranceType(relayIndex & 3);
                String checkIoStr = string.substring(2, 4);
                device.setProductsName(productName);
                if (checkIoStr.equals("FA")) {
                    device.setDeviceIO("0");
                    device.setDeviceTimer(productName);
                } else {
                    device.setDeviceIO(string.substring(3, 4));
                    device.setDeviceTimer(productName + " 回路" + (Integer.parseInt(string.substring(3, 4)) + 1));
                }
                device.setCmdDecodeType(1);
            }
        } else if (CommandValue == 50) {
            String modeCode = string.substring(10, 12);
            String modeIO = string.substring(12, 14);
            searchListDevice.clear();
            listView.setAdapter(searchAdapter);
            /**
             * 测试显示情景
             */
//            device.setDeviceName("情景id：" + modeCode);
//            device.setDeviceID("设备id：" + modeIO);
            device = new Device();
            device.setPrimary_Key(modeCode);
            device.setDeviceID(modeIO);
            device.setCmdDecodeType(0);
            searchListDevice.add(device);
            handler.sendEmptyMessage(2);

            flag=false;
        }
        return flag ? device : null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public void sort(List<Device> list) {
        Collections.sort(list, new Comparator<Device>() {

            @Override
            public int compare(Device o1, Device o2) {
                int i = o1.getProductsKey() - o2.getProductsKey();
                if (i == 0) {
                    return o1.getDeviceID().compareTo(o2.getDeviceID());
                }
                return i;
            }
        });
    }
}
