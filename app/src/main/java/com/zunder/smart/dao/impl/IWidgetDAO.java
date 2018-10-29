package com.zunder.smart.dao.impl;

import java.util.List;

import com.zunder.base.RMCBaseView;
import com.zunder.base.RMSBaseView;
import com.zunder.base.menu.RMCTabView;
import com.zunder.base.menu.RMSTabView;
import com.zunder.cusbutton.RMCBean;
import com.zunder.cusbutton.RMSCustomButton;
import com.zunder.smart.model.ActionParam;
import com.zunder.smart.model.Room;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceAction;
import com.zunder.smart.model.DeviceFunction;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.FunctionParam;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.GatewayType;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.model.Products;
import com.zunder.smart.model.Device;

public interface IWidgetDAO {

	public List<GateWay> getGateWay();

	public List<GatewayType> getGateWayType();

	public List<Mode> getMode();

	public List<ModeList> getModeList();

	public List<Room> getRoom();

	public List<Device> getDevices();

	public List<DeviceType> getDeviceTypes();

	public List<DeviceFunction> getDeviceFunction();

	public List<DeviceAction> getDeviceAction();

	public List<ActionParam> getActionParam();

	public List<FunctionParam> getFunctionParam();

	public List<Products> getProducts();

	public List<Device> getModeDevice(int modeById);

	public List<RedInfra> getInfrareds();

	public List<RMCBean> findRMCBeans();
	public List<RMCTabView> findTabViews();
	public List<RMCBaseView> findSubViews();

	public List<RMSTabView> findTabSViews();
	public List<RMSBaseView> findSubSViews();

	public void deleteAllRMS();
	public int updateRMSviews(RMSCustomButton rmsCustomButton);

	public int insertRMCBean(RMCBean rmcBean);
	public int updateRMCBean(RMCBean rmcBean);
	public int deleteRMCBean(int id);
	public int deleteRMCBeanByRoomId(int roomId);

	public int insertRoom(Room room);

	public int insertInfrad(RedInfra infrared);

	public int insertDevice(Device device);

	public int insertMode(Mode mode);

	public int insertGateWay(GateWay gateWay);

	public int insertModeList(ModeList modeList);

	public int updateModeList(ModeList modeList);

	public int insertAction(DeviceAction deviceAction);
	public int insertActionParam(ActionParam deviceAction);

	public int insetFunction(DeviceFunction deviceFunction);
    public int insetFunctionParam(FunctionParam deviceFunction);
	public int updateMode(Mode mode, String modeName);

	public int updateDevice(Device device);

	public int updateRedFra(RedInfra redInfra);

	public void updateDeviceTypeSort(List<DeviceType> list);

	public int updateGateWay(GateWay gateWay, String gateWayName);

	public int updateFraID(String deviceID, int infra_index);
	public void updateDeviceSort(List<Device> list);
	public void updateModeSort(List<Mode> list);

	public void updateRoomSort(List<Room> list);

	public void updateModeListSort(List<ModeList> list);

	public void updateDeviceType(DeviceType list);

	public void updateProducts(Products list);

	public void updateRedInfraSort(List<RedInfra> list);

	public int updateIsCurrent(int id, int current);

	public int updateRoom(Room room, String arceName);

	public void updateRoomHide(int homePageDisplays, int id);

	public int deleteGateWay(int Id);

	public int deleteRoom(int id);

	public int deleteRedFra(int id);

	public int deleteMode(int id);

	public int deleteDevice(int id);

	public int deleteModelist(int id);

	public int deleteInfrad(int id);

	public int deleteSyncData(String table);

	public void execSQL(String sql);
}