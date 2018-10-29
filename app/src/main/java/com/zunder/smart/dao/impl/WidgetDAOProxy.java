package com.zunder.smart.dao.impl;

import java.util.List;

import com.zunder.base.RMCBaseView;
import com.zunder.base.RMSBaseView;
import com.zunder.base.menu.RMCTabView;
import com.zunder.base.menu.RMSTabView;
import com.zunder.cusbutton.RMCBean;
import com.zunder.cusbutton.RMSCustomButton;
import com.zunder.smart.dao.Database;
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
import com.zunder.smart.dao.Database;
import com.zunder.smart.model.Device;

public class WidgetDAOProxy implements IWidgetDAO {
	private static Database dba = null;
	private static IWidgetDAO dao = null;
	public static WidgetDAOProxy instance;

	static {
		instance = new WidgetDAOProxy();
		dba = new Database();
		dao = new WidgetDAOImpl(dba.getSQLiteDatabase());
	}

	public static WidgetDAOProxy getInstance() {
		if (instance == null) {
			instance = new WidgetDAOProxy();
			dba = new Database();
			dao = new WidgetDAOImpl(dba.getSQLiteDatabase());
		}
		return instance;
	}
	public List<GateWay> getGateWay() {
		return dao.getGateWay();
	}

	public List<GatewayType> getGateWayType() {
		return dao.getGateWayType();
	}

	public List<Mode> getMode() {
		return dao.getMode();
	}

	public List<DeviceFunction> getDeviceFunction() {
		return dao.getDeviceFunction();
	}

	public List<DeviceAction> getDeviceAction() {
		return dao.getDeviceAction();
	}

	public List<FunctionParam> getFunctionParam() {
		return dao.getFunctionParam();
	}

	public List<ActionParam> getActionParam() {
		return dao.getActionParam();
	}

	public List<ModeList> getModeList() {
		return dao.getModeList();
	}

	public List<Products> getProducts() {
		return dao.getProducts();
	}
    public List<RMCTabView> findTabViews(){return dao.findTabViews();}
	public List<RMCBean> findRMCBeans(){
		return dao.findRMCBeans();
	}
	public List<RMCBaseView> findSubViews(){
		return dao.findSubViews();
	}

	public List<RMSTabView> findTabSViews(){return dao.findTabSViews();}
	public List<RMSBaseView> findSubSViews(){return dao.findSubSViews();}
	public int insertRMCBean(RMCBean rmcBean){
		return dao.insertRMCBean(rmcBean);
	}
	public int updateRMCBean(RMCBean rmcBean){
		return dao.updateRMCBean(rmcBean);
	}

	public void deleteAllRMS(){
		dao.deleteAllRMS();
	}
	public int updateRMSviews(RMSCustomButton rmsCustomButton){
		return dao.updateRMSviews(rmsCustomButton);
	}
	public int deleteRMCBean(int id){
		return dao.deleteRMCBean(id);
	}
	public int deleteRMCBeanByRoomId(int roomId){
		return dao.deleteRMCBeanByRoomId(roomId);
	}
	public int insertMode(Mode mode) {
		try {
			return dao.insertMode(mode);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int updateMode(Mode mode, String modeName) {
		try {
			return dao.updateMode(mode, modeName);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public void updateDeviceType(DeviceType list) {
		dao.updateDeviceType(list);
	}

	public void updateProducts(Products list) {
		dao.updateProducts(list);
	}

	public int updateDevice(Device device) {
		try {
			return dao.updateDevice(device);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int updateFraID(String deviceID, int infra_index) {
		try {
			return dao.updateFraID(deviceID, infra_index);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int updateRedFra(RedInfra redInfra) {
		try {
			return dao.updateRedFra(redInfra);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int insertDevice(Device device) {
		return dao.insertDevice(device);
	}

	public int insertGateWay(GateWay gateWay) {
		try {
			return dao.insertGateWay(gateWay);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int insertAction(DeviceAction deviceAction) {
		try {
			return dao.insertAction(deviceAction);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	public int insertActionParam(ActionParam deviceAction){
		try {
			return dao.insertActionParam(deviceAction);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	public int insetFunctionParam(FunctionParam deviceFunction){
		try {
			return dao.insetFunctionParam(deviceFunction);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	public int insetFunction(DeviceFunction deviceFunction) {
		try {
			return dao.insetFunction(deviceFunction);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int updateGateWay(GateWay gateWay, String gateWayName) {
		try {
			return dao.updateGateWay(gateWay, gateWayName);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int updateIsCurrent(int id, int current) {
		return dao.updateIsCurrent(id, current);
	}

	public int deleteGateWay(int Id) {
		return dao.deleteGateWay(Id);
	}

	public List<Room> getRoom() {
		return dao.getRoom();
	}

	public int insertRoom(Room room) {
		return dao.insertRoom(room);
	}

	public int updateRoom(Room room, String roomName) {
		return dao.updateRoom(room, roomName);
	}

	public int insertModeList(ModeList modeList) {
		return dao.insertModeList(modeList);
	}

	public int updateModeList(ModeList modeList) {
		return dao.updateModeList(modeList);
	}

	@Override
	public List<Device> getDevices() {
		// TODO Auto-generated method stub
		return dao.getDevices();
	}

	@Override
	public List<Device> getModeDevice(int modeById) {
		return dao.getModeDevice(modeById);
	}

	@Override
	public void updateDeviceSort(List<Device> list){
		dao.updateDeviceSort(list);
	}
	@Override
	public void updateModeSort(List<Mode> list) {
		dao.updateModeSort(list);
	}

	@Override
	public void updateRoomSort(List<Room> list) {
		dao.updateRoomSort(list);
	}

	public void updateModeListSort(List<ModeList> list) {
		dao.updateModeListSort(list);
	}

	@Override
	public void updateRoomHide(int homePageDisplays, int id) {
		dao.updateRoomHide(homePageDisplays, id);
	}

	public void updateRedInfraSort(List<RedInfra> list) {
		dao.updateRedInfraSort(list);
	}

	@Override
	public List<DeviceType> getDeviceTypes() {
		return dao.getDeviceTypes();
	}

	@Override
	public void updateDeviceTypeSort(List<DeviceType> list) {
		dao.updateDeviceTypeSort(list);
	}

	public int deleteRoom(int id) {
		return dao.deleteRoom(id);
	}

	public int deleteRedFra(int id) {
		return dao.deleteRedFra(id);
	}

	public int deleteDevice(int id) {
		return dao.deleteDevice(id);
	}

	@Override
	public int deleteMode(int id) {
		// TODO Auto-generated method stub
		return dao.deleteMode(id);
	}

	public int deleteModelist(int id) {
		return dao.deleteModelist(id);
	}

	@Override
	public List<RedInfra> getInfrareds() {
		// TODO Auto-generated method stub
		return dao.getInfrareds();
	}

	@Override
	public int insertInfrad(RedInfra infrared) {
		// TODO Auto-generated method stub
		return dao.insertInfrad(infrared);
	}

	@Override
	public int deleteInfrad(int id) {
		// TODO Auto-generated method stub
		return dao.deleteInfrad(id);
	}

	public int deleteSyncData(String table) {
		return dao.deleteSyncData(table);
	}

	public void execSQL(String sql) {
		dao.execSQL(sql);
	}
}
