package com.zunder.smart.dao.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zunder.base.RMCBaseView;
import com.zunder.base.RMSBaseView;
import com.zunder.base.menu.RMCTabView;
import com.zunder.base.menu.RMSTabView;
import com.zunder.cusbutton.RMCBean;
import com.zunder.cusbutton.RMCCustomButton;
import com.zunder.cusbutton.RMSCustomButton;
import com.zunder.smart.MyApplication;
import com.zunder.smart.image.BitmapService;
import com.zunder.smart.json.Constants;
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

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@SuppressLint("SimpleDateFormat")
public class WidgetDAOImpl implements IWidgetDAO {
	public static final String TABLE_ROOM = "t_room";
	public static final String TABLE_MODE = "t_mode";
	public static final String TABLE_MODE_LIST = "t_mode_list";
	public static final String TABLE_DEVICE = "t_device";
	public static final String TABLE_DEVICE_ACTION = "t_device_action";
	public static final String TABLE_DEVICE_FUNCTION = "t_device_function";
	public static final String TABLE_FUNCTION_PARAM = "t_function_param";

	public static final String TABLE_ACTION_PARAM = "t_action_param";
	public static final String TABLE_DEVICE_TYPE = "t_device_type";
	public static final String TABLE_PRODUCTS = "t_products";

	public static final String TABLE_GATEWAY = "t_gateway";

	public static final String TABLE_GATEWAY_TYPE = "t_gateway_type";
	public static final String TABLE_INFRARED = "t_infrared";

	private SQLiteDatabase db = null;

	public WidgetDAOImpl(SQLiteDatabase db) {

		this.db = db;
	}

	public List<GateWay> getGateWay() {
		List<GateWay> list = new ArrayList<GateWay>();
		if(db!=null) {
			try {
				Cursor cursor = db.query(TABLE_GATEWAY, new String[]{"Id",
								"GatewayName", "GatewayID", "UserName", "UserPassWord",
								"TypeId", "IsCurrent", "State"}, null, null, null,
						null, "TypeId asc");
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					GateWay gateWay = new GateWay();
					gateWay.setId(cursor.getInt(0));
					gateWay.setGatewayName(cursor.getString(1));
					gateWay.setGatewayID(cursor.getString(2));
					gateWay.setUserName(cursor.getString(3));
					gateWay.setUserPassWord(cursor.getString(4));
					gateWay.setTypeId(cursor.getInt(5));
					gateWay.setIsCurrent(cursor.getInt(6));
					gateWay.setGateWayPoint(cursor.getString(7));
					list.add(gateWay);
					cursor.moveToNext();
				}
			} catch (Exception e) {
			}
		}
		return list;
	}

	public List<GatewayType> getGateWayType() {
		List<GatewayType> list = new ArrayList<GatewayType>();
		if(db!=null) {

			try {
				Cursor cursor = db.query(TABLE_GATEWAY_TYPE, new String[]{"Id",
								"GatewayTypeName", "GatewayTypeImage", "CreationTime","TypeId"}, null,
						null, null, null, "TypeId asc");
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					GatewayType gateWayType = new GatewayType();
					gateWayType.setId(cursor.getInt(0));
					gateWayType.setGatewayTypeName(cursor.getString(1));
					gateWayType.setGatewayTypeImage(cursor.getString(2));
					gateWayType.setTypeId(cursor.getInt(4));
					list.add(gateWayType);
					cursor.moveToNext();
				}
			} catch (Exception e) {

			}
		}
		return list;
	}

	public List<DeviceFunction> getDeviceFunction() {
		List<DeviceFunction> list = new ArrayList<DeviceFunction>();
		if(db!=null) {

			try {
				Cursor cursor = db.query(TABLE_DEVICE_FUNCTION, new String[] { "Id",
								"functionName", "functionLanguage", "typeId" }, null, null,
						null, null, null);
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					DeviceFunction gateWayType = new DeviceFunction();
					gateWayType.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					gateWayType.setFunctionName(cursor.getString(cursor
							.getColumnIndex("functionName")));
					gateWayType.setFunctionLanguage(cursor.getString(cursor
							.getColumnIndex("functionLanguage")));
					gateWayType.setTypeId(cursor.getInt(cursor
							.getColumnIndex("typeId")));
					list.add(gateWayType);
					cursor.moveToNext();
				}
			} catch (Exception e) {

			}}
		return list;
	}

	public List<ActionParam> getActionParam() {
		List<ActionParam> list = new ArrayList<ActionParam>();
		if(db!=null) {
			try {
				Cursor cursor = db.query(TABLE_ACTION_PARAM, new String[] { "Id",
								"actionparam", "action_paramLanguage", "typeId" }, null, null,
						null, null, null);
				int resultCount = cursor.getCount();

				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					ActionParam gateWayType = new ActionParam();
					gateWayType.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					gateWayType.setActionparam(cursor.getString(cursor
							.getColumnIndex("actionparam")));
					gateWayType.setAction_paramLanguage(cursor.getString(cursor
							.getColumnIndex("action_paramLanguage")));
					gateWayType.setTypeId(cursor.getInt(cursor
							.getColumnIndex("typeId")));
					list.add(gateWayType);
					cursor.moveToNext();
				}
			} catch (Exception e) {

			}
		}
		return list;
	}

	public List<FunctionParam> getFunctionParam() {
		List<FunctionParam> list = new ArrayList<FunctionParam>();
		if(db!=null) {

			try {
				Cursor cursor = db.query(TABLE_FUNCTION_PARAM, new String[]{"Id",
								"function_param", "function_paramLanguage", "typeId"}, null,
						null, null, null, null);
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					FunctionParam gateWayType = new FunctionParam();
					gateWayType.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					gateWayType.setFunction_param(cursor.getString(cursor
							.getColumnIndex("function_param")));

					gateWayType.setFunction_paramLanguage(cursor.getString(cursor
							.getColumnIndex("function_paramLanguage")));
					gateWayType.setTypeId(cursor.getInt(cursor
							.getColumnIndex("typeId")));
					list.add(gateWayType);
					cursor.moveToNext();
				}
			} catch (Exception e) {

			}
		}
		return list;
	}

	public List<DeviceAction> getDeviceAction() {
		List<DeviceAction> list = new ArrayList<DeviceAction>();
		if(db!=null) {

			try {
				Cursor cursor = db.query(TABLE_DEVICE_ACTION, new String[]{"Id",
								"ActionName", "TypeId"}, null, null, null,
						null, null);
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					DeviceAction gateWayType = new DeviceAction();
					gateWayType.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					gateWayType.setActionName(cursor.getString(cursor
							.getColumnIndex("ActionName")));
					gateWayType.setTypeId(cursor.getInt(cursor
							.getColumnIndex("TypeId")));
					list.add(gateWayType);
					cursor.moveToNext();
				}
			} catch (Exception e) {

			}
		}
		return list;
	}

	public List<Mode> getMode() {
		List<Mode> list = new ArrayList<Mode>();
		if(db!=null) {
			try {
				Cursor cursor = db.query(TABLE_MODE, new String[]{"Id", "ModeName",
								"ModeImage", "ModeType", "ModeCode", "ModeLoop",
								"StartTime", "EndTime","ModeNickName","IsShow", "Seqencing","GatewayID"}, null, null, null, null,
						"Seqencing asc");
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					Mode mode = new Mode();
					mode.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					mode.setModeName(cursor.getString(cursor
							.getColumnIndex("ModeName")));
					String modeImage=cursor
							.getString(cursor.getColumnIndex("ModeImage"));
					if(modeImage==null||modeImage==""||modeImage.equals("")){
						mode.setModeImage(Constants.MODEIMAGEPATH);
					}else {
						mode.setModeImage(modeImage);
					}
					mode.setModeType(cursor.getString(cursor
							.getColumnIndex("ModeType")));
					mode.setModeCode(cursor.getInt(cursor
							.getColumnIndex("ModeCode")));
					mode.setSeqencing(cursor.getInt(cursor
							.getColumnIndex("Seqencing")));
					mode.setModeLoop(cursor.getInt(cursor
							.getColumnIndex("ModeLoop")));
					mode.setStartTime(cursor.getString(cursor
							.getColumnIndex("StartTime")));
					mode.setEndTime(cursor.getString(cursor
							.getColumnIndex("EndTime")));
					mode.setModeNickName(cursor.getString(cursor
							.getColumnIndex("ModeNickName")));
					mode.setCreationTime("");
					mode.setGatewayID(cursor.getString(cursor
							.getColumnIndex("GatewayID")));
					mode.setIsShow(cursor.getInt(cursor
							.getColumnIndex("IsShow")));
					list.add(mode);
					cursor.moveToNext();
				}
			} catch (Exception e) {

			}
		}
		return list;
	}

	public List<ModeList> getModeList() {
		List<ModeList> list = new ArrayList<ModeList>();
		try{
			String sql = "select *,l.Id as m_id,l.CreationTime as mouth,d.DeviceImage,DT.DeviceTypeName,DT.DeviceTypeKey,r.RoomName from t_mode_list l left join t_device d on d.Id=l.DeviceId left join t_room r on r.Id=d.RoomId left join t_products p on d.ProductsKey=p.ProductsKey LEFT JOIN t_device_type dt on d.DeviceTypeKey=dt.DeviceTypeKey where d.Id=l.DeviceId order by l.Seqencing";
			if(db!=null) {
				Cursor cursor = db.rawQuery(sql, null);
				if (cursor != null) {
					int resultCount = cursor.getCount();

					if (resultCount == 0 || !cursor.moveToFirst()) {
						return list;
					}
					for (int i = 0; i < resultCount; i++) {
						ModeList modeList = new ModeList();
						modeList.setId(cursor.getInt(cursor.getColumnIndex("m_id")));

						String ModeAction = cursor.getString(cursor
								.getColumnIndex("ModeAction"));
						String ModeFunction = cursor.getString(cursor
								.getColumnIndex("ModeFunction"));
						String ModeTime = cursor.getString(cursor
								.getColumnIndex("ModeTime"));
						String ModeDelayed = cursor.getString(cursor
								.getColumnIndex("ModeDelayed"));
						String ModePeriod = cursor.getString(cursor
								.getColumnIndex("ModePeriod"));
						modeList.setModeAction(ModeAction);
						modeList.setModeFunction(ModeFunction);
						modeList.setModeTime(ModeTime);
						modeList.setModeDelayed(ModeDelayed);
						modeList.setModePeriod(ModePeriod);

						modeList.setModeId(cursor.getInt(cursor
								.getColumnIndex("ModeId")));
						modeList.setDeviceId(cursor.getInt(cursor
								.getColumnIndex("DeviceId")));

						modeList.setDeviceNickName(cursor.getString(cursor
								.getColumnIndex("DeviceNickName")));
						modeList.setRoomName(cursor.getString(cursor
								.getColumnIndex("RoomName")));
						modeList.setDeviceTypeKey(cursor.getInt(cursor
								.getColumnIndex("DeviceTypeKey")));
						modeList.setDeviceTypeClick(cursor.getInt(cursor
								.getColumnIndex("DeviceTypeClick")));

						modeList.setSeqencing(cursor.getInt(cursor
								.getColumnIndex("Seqencing")));
						modeList.setDeviceOnLine(cursor.getInt(cursor
								.getColumnIndex("DeviceOnLine")));
						modeList.setDeviceTimer(cursor.getString(cursor
								.getColumnIndex("DeviceTimer")));
						modeList.setDeviceOrdered(cursor.getString(cursor
								.getColumnIndex("DeviceOrdered")));
						modeList.setDeviceName(cursor.getString(cursor
								.getColumnIndex("DeviceName")));
						modeList.setDeviceID(cursor.getString(cursor
								.getColumnIndex("DeviceID")));

						String deviceImage=cursor.getString(cursor
								.getColumnIndex("DeviceImage"));
						if(deviceImage==null||deviceImage==""||deviceImage.equals("")){
							deviceImage=cursor.getString(cursor
									.getColumnIndex("ProductsImage"));
                            modeList.setDeviceImage(deviceImage);
						}else{
							modeList.setDeviceImage(deviceImage);
						}
						modeList.setDeviceIO(cursor.getString(cursor
								.getColumnIndex("DeviceIO")));
						modeList.setDeviceTypeName(cursor.getString(cursor
								.getColumnIndex("DeviceTypeName")));
						modeList.setProductsCode(cursor.getString(cursor
								.getColumnIndex("ProductsCode")));
						modeList.setStartTime(cursor.getString(cursor
								.getColumnIndex("StartTime")));
						modeList.setEndTime(cursor.getString(cursor
								.getColumnIndex("EndTime")));
						modeList.setDeviceTypeKey(cursor.getInt(cursor
								.getColumnIndex("DeviceTypeKey")));
						modeList.setProductsKey(cursor.getInt(cursor
								.getColumnIndex("ProductsKey")));
						modeList.setProductsIO(cursor.getInt(cursor
								.getColumnIndex("ProductsIO")));
                        modeList.setBeginMonth(cursor.getString(cursor
                                .getColumnIndex("BeginMonth")));
                        modeList.setEndMonth(cursor.getString(cursor
                                .getColumnIndex("EndMonth")));
						list.add(modeList);
						cursor.moveToNext();
					}
				}
			}
		}catch (Exception e){

		}
		return list;
	}

	public List<Products> getProducts() {
		List<Products> list = new ArrayList<Products>();
		if(db!=null) {

			try {
				Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{"Id",
						"ProductsName", "ProductsImage", "ProductsCode", "ProductsKey", "ProductsIO","DeviceTypeKey", "ActionShow", "FunctionShow", "ActionPramShow",
						"FunctionParamShow"}, null, null, null, null, null);
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					Products products = new Products();
					products.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					products.setProductsName(cursor.getString(cursor
							.getColumnIndex("ProductsName")));
					products.setProductsCode(cursor.getString(cursor
							.getColumnIndex("ProductsCode")));
					products.setProductsIO(cursor.getInt(cursor
							.getColumnIndex("ProductsIO")));
					products.setProductsKey(cursor.getInt(cursor
							.getColumnIndex("ProductsKey")));
					products.setDeviceTypekey(cursor.getInt(cursor
							.getColumnIndex("DeviceTypeKey")));
					products.setProductsImage(cursor.getString(cursor
							.getColumnIndex("ProductsImage")));
					products.setActionShow(cursor.getInt(cursor
							.getColumnIndex("ActionShow")));
					products.setFunctionShow(cursor.getInt(cursor
							.getColumnIndex("FunctionShow")));
					products.setActionPramShow(cursor.getInt(cursor
							.getColumnIndex("ActionPramShow")));
					products.setFunctionParamShow(cursor.getInt(cursor
							.getColumnIndex("FunctionParamShow")));
					list.add(products);
					cursor.moveToNext();
				}
			} catch (Exception e) {
			}
		}
		return list;
	}

	public List<RMCBaseView> findSubViews() {
		// TODO Auto-generated method stub
		List<RMCBaseView> subViews = new ArrayList<RMCBaseView>();
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.toString().contains("findRMCCustom")
					&& method.toString().contains("private")) {
				method.setAccessible(true);
				try {
					subViews.addAll((List<RMCBaseView>) method.invoke(this,
							new Object[] {}));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return subViews;
	}

	private List<RMCCustomButton> findRMCCustomButton(){
		List<RMCCustomButton> list = new ArrayList<RMCCustomButton>();

		if(db!=null) {

			try {
				Cursor cursor = db.query("t_button", new String[]{"Id",
						"BtnName", "BtnImage", "RoomId", "CreationTime", "BtnSeqencing","CodeType", "BtnAction", "BtnX", "BtnY",
						"BtnWidth","BtnHeight","BtnType","CompanyId","BtnSize","BtnColor","LanguageId","Data1","Data2"}, null, null, null, null, null);
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					RMCCustomButton rmcBean = new RMCCustomButton(MyApplication.getInstance());
					rmcBean.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					rmcBean.setText(cursor.getString(cursor
							.getColumnIndex("BtnName")));
					rmcBean.setBtnType(cursor.getInt(cursor
							.getColumnIndex("BtnType")));
					rmcBean.setBackgroundFromProperties(cursor.getString(cursor
							.getColumnIndex("BtnImage")));
					int X=cursor.getInt(cursor
							.getColumnIndex("BtnX"));
					int Y=cursor.getInt(cursor
                            .getColumnIndex("BtnY"));
					int H=cursor.getInt(cursor
                            .getColumnIndex("BtnHeight"));
					int W=cursor.getInt(cursor
                            .getColumnIndex("BtnWidth"));
					rmcBean.setParams(W,H,X,Y);
					rmcBean.setTabViewTag(cursor.getInt(cursor
                            .getColumnIndex("RoomId")));
					rmcBean.setTextColor(cursor.getString(cursor
							.getColumnIndex("BtnColor")));
					rmcBean.setFontSize(cursor.getInt(cursor
							.getColumnIndex("BtnSize")));
					rmcBean.setTextSize(cursor.getInt(cursor
							.getColumnIndex("BtnSize")));
					list.add(rmcBean);
					cursor.moveToNext();
				}
			} catch (Exception e) {
			}
		}
		return list;
	}
	@Override
	public List<RMCTabView> findTabViews() {
		Cursor cursor = db.query(TABLE_ROOM, new String[] { "Id", "RoomName",
						"RoomImage", "IsShow", "CreationTime", "Seqencing","Data1" },
				"IsShow=?", new String[]{String.valueOf(2)}, null, null, "Seqencing asc");
		int resultCounts = cursor.getCount();
		List<RMCTabView> lists = new ArrayList<RMCTabView>();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return lists;
		}
		for (int i = 0; i < resultCounts; i++) {
			RMCTabView tab = new RMCTabView(MyApplication.getInstance());
			tab.setId(cursor.getInt(cursor.getColumnIndex("Id")));
			tab.setText(cursor.getString(cursor.getColumnIndex("RoomName")));
			tab.setTextSize(16);
			tab.setBtnType(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Data1"))));
			tab.setDrawableData("");
			tab.setTag(cursor.getInt(cursor.getColumnIndex("Id")));

			lists.add(tab);
			cursor.moveToNext();
		}
		cursor.close();
		return lists;
	}

	private List<RMSCustomButton> findRMSCustomButton(){
		List<RMSCustomButton> list = new ArrayList<RMSCustomButton>();

		if(db!=null) {

			try {
				Cursor cursor = db.query("t_button", new String[]{"Id",
						"BtnName", "BtnImage", "RoomId", "CreationTime", "BtnSeqencing","CodeType", "BtnAction", "BtnX", "BtnY",
						"BtnWidth","BtnHeight","BtnType","CompanyId","BtnSize","BtnColor","LanguageId","Data1","Data2"}, null, null, null, null, null);
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					RMSCustomButton rmcBean = new RMSCustomButton(MyApplication.getInstance());
					rmcBean.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					rmcBean.setBtnName(cursor.getString(cursor
							.getColumnIndex("BtnName")));
					rmcBean.setBtnImage(cursor.getString(cursor
							.getColumnIndex("BtnImage")));
					rmcBean.setRoomId(cursor.getInt(cursor
							.getColumnIndex("RoomId")));
					rmcBean.setBtnSeqencing(cursor.getInt(cursor
							.getColumnIndex("BtnSeqencing")));
					rmcBean.setCodeType(cursor.getInt(cursor
							.getColumnIndex("CodeType")));
					rmcBean.setBtnAction(cursor.getString(cursor
							.getColumnIndex("BtnAction")));
					rmcBean.setBtnX(cursor.getInt(cursor
							.getColumnIndex("BtnX")));
					rmcBean.setBtnY(cursor.getInt(cursor
							.getColumnIndex("BtnY")));
					rmcBean.setBtnWidth(cursor.getInt(cursor
							.getColumnIndex("BtnWidth")));
					rmcBean.setBtnHeight(cursor.getInt(cursor
							.getColumnIndex("BtnHeight")));
					rmcBean.setBtnType(cursor.getInt(cursor
							.getColumnIndex("BtnType")));
					rmcBean.setBtnSize(cursor.getInt(cursor
							.getColumnIndex("BtnSize")));
					rmcBean.setBtnColor(cursor.getString(cursor
							.getColumnIndex("BtnColor")));
					rmcBean.initParams();
					list.add(rmcBean);
					cursor.moveToNext();
				}
			} catch (Exception e) {
			}
		}
		return list;
	}

	public void deleteAllRMS(){
		 db.delete("t_button", "BtnType=?", new String[]{String.valueOf(0)});
	}

	public int updateRMSviews(RMSCustomButton rmcBean){
		int num = 0;
		try {
			ContentValues contentValues = new ContentValues();

			contentValues.put("Id", rmcBean.getId());
			contentValues.put("BtnName", rmcBean.getText().toString());
			contentValues.put("BtnImage",rmcBean.getBtnImage());
			contentValues.put("RoomId",rmcBean.getTabViewTag());
			contentValues.put("CreationTime",getSysNowTime());
			contentValues.put("BtnSeqencing", rmcBean.getBtnSeqencing());
			contentValues.put("CodeType", rmcBean.getCodeType());
			contentValues.put("BtnAction", rmcBean.getBtnAction());
			contentValues.put("BtnX", rmcBean.getBtnX());
			contentValues.put("BtnY", rmcBean.getBtnY());
			contentValues.put("BtnWidth", rmcBean.getBtnWidth());
			contentValues.put("BtnHeight", rmcBean.getBtnHeight());
			contentValues.put("BtnType",rmcBean.getBtnType());
			contentValues.put("BtnSize",rmcBean.getBtnSize());
			contentValues.put("BtnColor",rmcBean.getBtnColor());
			contentValues.put("CompanyId", 1);
			contentValues.put("LanguageId",1);
			contentValues.put("Data1", "0");
			contentValues.put("Data2","0");
			num += db.insert("t_button", null, contentValues);
			// }
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			num = -1;
		}

		return num;
	}

	public List<RMSTabView> findTabSViews(){
		Cursor cursor = db.query(TABLE_ROOM, new String[] { "Id", "RoomName",
						"RoomImage", "IsShow", "CreationTime", "Seqencing","Data1" },
				"IsShow=? and Data1=?", new String[]{String.valueOf(2),"0"}, null, null, "Seqencing asc");
		int resultCounts = cursor.getCount();
		List<RMSTabView> lists = new ArrayList<RMSTabView>();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return lists;
		}
		for (int i = 0; i < resultCounts; i++) {
			RMSTabView tab = new RMSTabView(MyApplication.getInstance());
			tab.setId(cursor.getInt(cursor.getColumnIndex("Id")));
			tab.setText(cursor.getString(cursor.getColumnIndex("RoomName")));
			tab.setTextSize(16);
			tab.setBtnType(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Data1"))));
			tab.setDrawableData("");
			tab.setTag(cursor.getInt(cursor.getColumnIndex("Id")));

			lists.add(tab);
			cursor.moveToNext();
		}
		cursor.close();
		return lists;
	}
	public List<RMSBaseView> findSubSViews(){
		List<RMSBaseView> subViews = new ArrayList<RMSBaseView>();
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.toString().contains("findRMSCustom")
					&& method.toString().contains("private")) {
				method.setAccessible(true);
				try {
					subViews.addAll((List<RMSBaseView>) method.invoke(this,
							new Object[] {}));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return subViews;
	}

	public List<RMCBean> findRMCBeans(){
		List<RMCBean> list = new ArrayList<RMCBean>();

		if(db!=null) {
			try {
				Cursor cursor = db.query("t_button", new String[]{"Id",
						"BtnName", "BtnImage", "RoomId", "CreationTime", "BtnSeqencing","CodeType", "BtnAction", "BtnX", "BtnY",
						"BtnWidth","BtnHeight","BtnType","CompanyId","BtnSize","BtnColor","LanguageId","Data1","Data2"}, null, null, null, null, null);
				int resultCount = cursor.getCount();
				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					RMCBean rmcBean = new RMCBean();
					rmcBean.setId(cursor.getInt(cursor.getColumnIndex("Id")));
					rmcBean.setBtnName(cursor.getString(cursor
							.getColumnIndex("BtnName")));
					rmcBean.setBtnImage(cursor.getString(cursor
							.getColumnIndex("BtnImage")));
					rmcBean.setRoomId(cursor.getInt(cursor
							.getColumnIndex("RoomId")));
					rmcBean.setBtnSeqencing(cursor.getInt(cursor
							.getColumnIndex("BtnSeqencing")));
					rmcBean.setCodeType(cursor.getInt(cursor
							.getColumnIndex("CodeType")));
					rmcBean.setBtnAction(cursor.getString(cursor
							.getColumnIndex("BtnAction")));
					rmcBean.setBtnX(cursor.getInt(cursor
							.getColumnIndex("BtnX")));
					rmcBean.setBtnY(cursor.getInt(cursor
							.getColumnIndex("BtnY")));
					rmcBean.setBtnWidth(cursor.getInt(cursor
							.getColumnIndex("BtnWidth")));
					rmcBean.setBtnHeight(cursor.getInt(cursor
							.getColumnIndex("BtnHeight")));
					rmcBean.setBtnType(cursor.getInt(cursor
							.getColumnIndex("BtnType")));
					rmcBean.setBtnSize(cursor.getInt(cursor
							.getColumnIndex("BtnSize")));
					rmcBean.setBtnColor(cursor.getString(cursor
							.getColumnIndex("BtnColor")));
					list.add(rmcBean);
					cursor.moveToNext();
				}
			} catch (Exception e) {
			}
		}
		return list;
	}
	public int insertRMCBean(RMCBean rmcBean){

		int num = 0;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put("BtnName", rmcBean.getBtnName());
			contentValues.put("BtnImage",rmcBean.getBtnImage());
			contentValues.put("RoomId",rmcBean.getRoomId());
			contentValues.put("CreationTime",getSysNowTime());
			contentValues.put("BtnSeqencing", rmcBean.getBtnSeqencing());
			contentValues.put("CodeType", rmcBean.getCodeType());
			contentValues.put("BtnAction", rmcBean.getBtnAction());
			contentValues.put("BtnX", rmcBean.getBtnX());
			contentValues.put("BtnY", rmcBean.getBtnY());
			contentValues.put("BtnWidth", rmcBean.getBtnWidth());
			contentValues.put("BtnHeight", rmcBean.getBtnHeight());
			contentValues.put("BtnType",rmcBean.getBtnType());
			contentValues.put("BtnSize",rmcBean.getBtnSize());
			contentValues.put("BtnColor",rmcBean.getBtnColor());
			contentValues.put("CompanyId", 1);
			contentValues.put("LanguageId",1);
			contentValues.put("Data1", "0");
			contentValues.put("Data2","0");
			num += db.insert("t_button", null, contentValues);
			// }
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			num = -1;
		}
		return num;
	}

	public int updateRMCBean(RMCBean rmcBean){
		int num = 0;
		ContentValues contentValues = new ContentValues();
		contentValues.put("BtnName", rmcBean.getBtnName());
		contentValues.put("BtnImage",rmcBean.getBtnImage());
		contentValues.put("RoomId",rmcBean.getRoomId());
		contentValues.put("CreationTime",getSysNowTime());
		contentValues.put("BtnSeqencing", rmcBean.getBtnSeqencing());
		contentValues.put("CodeType", rmcBean.getCodeType());
		contentValues.put("BtnAction", rmcBean.getBtnAction());
		contentValues.put("BtnX", rmcBean.getBtnX());
		contentValues.put("BtnY", rmcBean.getBtnY());
		contentValues.put("BtnWidth", rmcBean.getBtnWidth());
		contentValues.put("BtnHeight", rmcBean.getBtnHeight());
		contentValues.put("BtnType",rmcBean.getBtnType());
		contentValues.put("BtnSize",rmcBean.getBtnSize());
		contentValues.put("BtnColor",rmcBean.getBtnColor());
		contentValues.put("CompanyId", 1);
		contentValues.put("LanguageId",1);
		contentValues.put("Data1", "0");
		contentValues.put("Data2","0");
		if(db!=null) {
			try {
				num = db.update("t_button", contentValues, "Id=?",
							new String[]{String.valueOf(rmcBean.getId())});
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				num = -1;
			}
		}
		return num;
	}

	public int deleteRMCBean(int id){
		int resultCount = 0;
		try {
			resultCount = db.delete("t_button", "Id=?",
					new String[] { String.valueOf(id) });

		} catch (Exception e) {
			// TODO: handle exception
			resultCount = -1;
		}
		return resultCount;
	}
	public int deleteRMCBeanByRoomId(int roomId){
		int resultCount = 0;
		try {
			resultCount = db.delete("t_button", "RoomId=?",
					new String[] { String.valueOf(roomId) });

		} catch (Exception e) {
			// TODO: handle exception
			resultCount = -1;
		}
		return resultCount;
	}

	public int insertMode(Mode mode) {
		int num = 0;
		try {
			ContentValues contentValues = new ContentValues();
			if (mode.getId() != 0) {
				contentValues.put("Id", mode.getId());
			}
			contentValues.put("ModeName", mode.getModeName());
			contentValues.put("ModeImage",mode.getModeImage());
			contentValues.put("ModeType", mode.getModeType());
			contentValues.put("ModeCode", mode.getModeCode());
			contentValues.put("Seqencing", mode.getSeqencing());
			contentValues.put("CreationTime", getSysNowTime());
			contentValues.put("StartTime", mode.getStartTime());
			contentValues.put("EndTime", mode.getEndTime());
			contentValues.put("ModeLoop", mode.getModeLoop());
			contentValues.put("ModeNickName", mode.getModeNickName());
			contentValues.put("GatewayID", mode.getGatewayID());
			contentValues.put("IsShow", mode.getIsShow());
			num += db.insert(TABLE_MODE, null, contentValues);
			// }
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			num = -1;
		}
		return num;
	}

	public int updateMode(Mode mode, String modeName) {
		int num = 0;
		ContentValues contentValues = new ContentValues();
		contentValues.put("ModeName", mode.getModeName());
		contentValues.put("ModeImage",mode.getModeImage());
		contentValues.put("ModeType", mode.getModeType());
		contentValues.put("ModeCode", mode.getModeCode());
		contentValues.put("Seqencing", mode.getSeqencing());
		contentValues.put("CreationTime", getSysNowTime());
		contentValues.put("StartTime", mode.getStartTime());
		contentValues.put("EndTime", mode.getEndTime());
		contentValues.put("ModeLoop", mode.getModeLoop());
		contentValues.put("ModeNickName", mode.getModeNickName());
		contentValues.put("GatewayID", mode.getGatewayID());
		contentValues.put("IsShow", mode.getIsShow());
		if(db!=null) {
			try {
				Cursor cursor = db.query(TABLE_MODE, new String[]{"Id"},
						"ModeName=?", new String[]{mode.getModeName()}, null,
						null, null, null);

				if (cursor.getCount() > 1) {
					num = 2;
				}
				if (cursor.getCount() == 1) {
					cursor.moveToLast();
					int Id = cursor.getInt(cursor.getColumnIndex("Id"));
					cursor.close();

					if (Id == mode.getId()) {
						num = db.update(TABLE_MODE, contentValues, "Id=?",
								new String[]{String.valueOf(mode.getId())});
					} else {
						num = 2;
					}

				} else {
					num = db.update(TABLE_MODE, contentValues, "Id=?",
							new String[]{String.valueOf(mode.getId())});
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				num = -1;
			}
		}
		return num;
	}

	public int updateDevice(Device device) {
		int num = 0;
		ContentValues contentValues = new ContentValues();
		contentValues.put("DeviceName", device.getDeviceName());
		contentValues.put("deviceImage", "");
		contentValues.put("DeviceID", device.getDeviceID());
		contentValues.put("deviceNickName", device.getDeviceNickName());
		contentValues.put("CreationTime", getSysNowTime());
		contentValues.put("Seqencing", device.getSeqencing());
		contentValues.put("DeviceOnLine", device.getDeviceOnLine());
		contentValues.put("DeviceTimer", device.getDeviceTimer());
		contentValues.put("DeviceIO", device.getDeviceIO());
		contentValues.put("DeviceOrdered", device.getDeviceOrdered());
		contentValues.put("StartTime", device.getStartTime());
		contentValues.put("EndTime", device.getEndTime());
		contentValues.put("sceneId", device.getSceneId());
		contentValues.put("DeviceTypeKey", device.getDeviceTypeKey());
		contentValues.put("ProductsKey", device.getProductsKey());
		contentValues.put("GradingId", device.getGradingId());
		contentValues.put("Data1", device.getData1());
		contentValues.put("RoomId", device.getRoomId());
		try {
			num = db.update(TABLE_DEVICE, contentValues, "Id=?",
					new String[] { String.valueOf(device.getId()) });

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			num = -1;
		}
		return num;
	}

	public int updateRedFra(RedInfra infrared) {

		int num = 0;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put("InfraredName", infrared.getInfraredName());
			contentValues.put("InfraredImage", infrared.getInfraredImage());
			contentValues.put("DeviceId", infrared.getDeviceId());
			contentValues.put("FatherId", infrared.getFatherId());
			contentValues.put("InfraredIndex", infrared.getInfraredIndex());
			contentValues.put("InfraredBrandId", infrared.getInfraredBrandId());
			contentValues.put("InfraredBrandName", infrared.getInfraredBrandName());
			contentValues.put("InfraredVersionId",
					infrared.getInfraredVersionId());
			contentValues.put("InfraredVersionName",
					infrared.getInfraredVersionName());
			contentValues.put("InfraredKey",
					infrared.getInfraredKey());
			contentValues.put("InfraredButtonId", infrared.getInfraredButtonId());
			contentValues.put("InfraredCode", infrared.getInfraredCode());
			contentValues.put("InfraredStudyType", infrared.getInfraredStudyType());
			contentValues.put("InfraredX", infrared.getInfraredX());
			contentValues.put("InfraredY", infrared.getInfraredY());
			contentValues.put("CreationTime", getSysNowTime());
			contentValues.put("Seqencing", infrared.getSeqencing());
			num = db.update(TABLE_INFRARED, contentValues, "Id=?",
					new String[] { String.valueOf(infrared.getId()) });
		} catch (Exception e) {
			num = 0;
		}
		return num;
	}

	public int insertGateWay(GateWay gateWay) {

		int num = 0;
		if(db!=null) {
			try {
				Cursor result = db.query(TABLE_GATEWAY,
						new String[]{"GatewayName"}, "GatewayName=?",
						new String[]{gateWay.getGatewayName()}, null, null,
						null, null);

				if (result.getCount() > 0) {
					num = -1;
				} else {
					ContentValues contentValues = new ContentValues();
					if (gateWay.getId() != 0) {
						contentValues.put("Id", gateWay.getId());
					}
					contentValues.put("GatewayName", gateWay.getGatewayName());
					contentValues.put("GatewayID", gateWay.getGatewayID());
					contentValues.put("UserName", gateWay.getUserName());
					contentValues.put("UserPassWord", gateWay.getUserPassWord());
					contentValues.put("TypeId", gateWay.getTypeId());
					contentValues.put("CreationTime", getSysNowTime());
					contentValues.put("IsCurrent", gateWay.getIsCurrent());
					contentValues.put("State", gateWay.getState());
					contentValues.put("Seqencing", gateWay.getSeqencing());
					num += db.insert(TABLE_GATEWAY, null, contentValues);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				num = 0;
			}
		}
		return num;
	}

	public int insertModeList(ModeList modeList) {
		int num = 0;
		try {
			ContentValues contentValues = new ContentValues();
			if (modeList.getId() != 0) {
				contentValues.put("Id", modeList.getId());
			}

			contentValues.put("ModeAction", modeList.getModeAction());
			contentValues.put("ModeFunction",modeList.getModeFunction());
			contentValues.put("ModeTime", modeList.getModeTime());
			contentValues.put("ModeDelayed", modeList.getModeDelayed());
			contentValues.put("ModePeriod", modeList.getModePeriod());
			contentValues.put("CreationTime", getSysNowTime());
			contentValues.put("DeviceId", modeList.getDeviceId());
			contentValues.put("Seqencing", modeList.getSeqencing());
			contentValues.put("ModeId", modeList.getModeId());
			contentValues.put("BeginMonth", modeList.getBeginMonth());
			contentValues.put("EndMonth", modeList.getEndMonth());
			contentValues.put("LanguageId", 1);
			contentValues.put("CompanyId", 1);
			contentValues.put("Data1","0");
			contentValues.put("Data2", "0");

			num = (int) db.insert(TABLE_MODE_LIST, null, contentValues);
		} catch (Exception e) {
			num = -1;
		}
		return num;
	}

	public int updateModeList(ModeList modeList) {
		int num = 0;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put("ModeAction", modeList.getModeAction());
			contentValues.put("ModeFunction",modeList.getModeFunction());
			contentValues.put("ModeTime", modeList.getModeTime());
			contentValues.put("ModeDelayed", modeList.getModeDelayed());
			contentValues.put("ModePeriod", modeList.getModePeriod());
			contentValues.put("CreationTime",modeList.getCreationTime());
			contentValues.put("DeviceId", modeList.getDeviceId());
			 contentValues.put("Seqencing", modeList.getSeqencing());
			contentValues.put("ModeId", modeList.getModeId());
			contentValues.put("BeginMonth", modeList.getBeginMonth());
			contentValues.put("EndMonth", modeList.getEndMonth());
			contentValues.put("CreationTime",getSysNowTime());
			contentValues.put("LanguageId", 1);
			contentValues.put("CompanyId", 1);
			contentValues.put("Data1","0");
			contentValues.put("Data2", "0");
			num = db.update(TABLE_MODE_LIST, contentValues, "Id=?",
					new String[] { String.valueOf(modeList.getId()) });
		} catch (Exception e) {
			num = -1;
		}
		return num;
	}

	public void updateDeviceType(DeviceType deviceType) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("DeviceTypeName", deviceType.getDeviceTypeName());
		contentValues.put("DeviceTypeKey", deviceType.getDeviceTypeKey());
		contentValues.put("DeviceTypeClick", deviceType.getDeviceTypeClick());
		contentValues.put("Seqencing", deviceType.getSeqencing());
		contentValues.put("IsShow", deviceType.getIsShow());

		Cursor result = db.query(TABLE_DEVICE_TYPE,
				new String[]{"DeviceTypeName"}, "Id=?",
				new String[]{String.valueOf(deviceType.getId()) }, null, null,
				null, null);
		if(result.getCount()>0) {
			db.update(TABLE_DEVICE_TYPE, contentValues, "Id=?",
					new String[]{String.valueOf(deviceType.getId())});
		}else{
			contentValues.put("Id",deviceType.getId());
			db.insert(TABLE_DEVICE_TYPE,null,contentValues);
		}
	}

	public void updateProducts(Products product) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("ProductsName", product.getProductsName());
		contentValues.put("ProductsCode", product.getProductsCode());
		contentValues.put("ProductsIO", product.getProductsIO());
		contentValues.put("ProductsKey", product.getProductsKey());
		contentValues.put("ActionShow", product.getActionShow());
		contentValues.put("FunctionShow", product.getFunctionShow());
		contentValues.put("ActionPramShow", product.getActionPramShow());
		contentValues.put("FunctionParamShow", product.getFunctionParamShow());
		contentValues.put("IsSwitch", product.getIsSwitch());

		Cursor result = db.query(TABLE_PRODUCTS,
				new String[]{"ProductsName"}, "Id=?",
				new String[]{String.valueOf(product.getId()) }, null, null,
				null, null);
		if(result.getCount()>0) {
			db.update(TABLE_PRODUCTS, contentValues, "Id=?",
					new String[]{String.valueOf(product.getId())});
		}else{
			contentValues.put("Id",product.getId());
			db.insert(TABLE_PRODUCTS,null,contentValues);
		}
	}
	public int insertDevice(Device device) {
		int num = 0;
		try {
			ContentValues contentValues = new ContentValues();
			if (device.getId() != 0) {
				contentValues.put("Id", device.getId());
			}
			contentValues.put("DeviceName", device.getDeviceName());
			contentValues.put("deviceImage", "");
			contentValues.put("DeviceID", device.getDeviceID());
			contentValues.put("deviceNickName", device.getDeviceNickName());
			contentValues.put("CreationTime", getSysNowTime());
			contentValues.put("Seqencing", device.getSeqencing());
			contentValues.put("DeviceOnLine", device.getDeviceOnLine());
			contentValues.put("DeviceTimer", device.getDeviceTimer());
			contentValues.put("DeviceIO", device.getDeviceIO());
			contentValues.put("DeviceOrdered", device.getDeviceOrdered());
			contentValues.put("StartTime", device.getStartTime());
			contentValues.put("EndTime", device.getEndTime());
			contentValues.put("sceneId", device.getSceneId());
			contentValues.put("DeviceTypeKey", device.getDeviceTypeKey());
			contentValues.put("ProductsKey", device.getProductsKey());
			contentValues.put("GradingId", device.getGradingId());
			contentValues.put("RoomId", device.getRoomId());
			num += db.insert(TABLE_DEVICE, null, contentValues);
			// }
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			num = -1;
		}
		return num;
	}
	public int insertAction(DeviceAction deviceAction) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put("actionName", deviceAction.getActionName());
			contentValues.put("actionLanguage",
					deviceAction.getActionLanguage());
			contentValues.put("typeId", deviceAction.getTypeId());

			db.insert(TABLE_DEVICE_ACTION, null, contentValues);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	public int insertActionParam(ActionParam deviceAction){
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put("actionparam", deviceAction.getActionparam());
			contentValues.put("action_paramLanguage",
					deviceAction.getAction_paramLanguage());
			contentValues.put("typeId", deviceAction.getTypeId());
			db.insert(TABLE_ACTION_PARAM, null, contentValues);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	public int insetFunction(DeviceFunction deviceFunction) {
		try {
			try {
				ContentValues contentValues = new ContentValues();
				contentValues.put("functionName",
						deviceFunction.getFunctionName());
				contentValues.put("functionLanguage",
						deviceFunction.getFunctionLanguage());
				contentValues.put("typeId", deviceFunction.getTypeId());

				db.insert(TABLE_DEVICE_FUNCTION, null, contentValues);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int insetFunctionParam(FunctionParam deviceFunction){
		try {
			try {
				ContentValues contentValues = new ContentValues();
				contentValues.put("function_param",
						deviceFunction.getFunction_param());
				contentValues.put("function_paramLanguage",
						deviceFunction.getFunction_paramLanguage());
				contentValues.put("typeId", deviceFunction.getTypeId());

				db.insert(TABLE_FUNCTION_PARAM, null, contentValues);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public int updateGateWay(GateWay gateWay, String gateWayName) {
		int num = 0;
		ContentValues contentValues = new ContentValues();
		contentValues.put("GatewayName", gateWay.getGatewayName());
		contentValues.put("GatewayID", gateWay.getGatewayID());
		contentValues.put("UserName", gateWay.getUserName());
		contentValues.put("UserPassWord", gateWay.getUserPassWord());
		contentValues.put("TypeId", gateWay.getTypeId());
		contentValues.put("CreationTime", getSysNowTime());
		contentValues.put("IsCurrent", gateWay.getIsCurrent());
		contentValues.put("State", gateWay.getGateWayPoint());
		contentValues.put("Seqencing", gateWay.getSeqencing());
		if(db!=null) {
			try {
				Cursor cursor = db.query(TABLE_GATEWAY, new String[]{"Id"},
						"GatewayName=?", new String[]{gateWay.getGatewayName()},
						null, null, null, null);

				if (cursor.getCount() > 1) {
					num = 2;
				}
				if (cursor.getCount() == 1) {
					cursor.moveToLast();
					int Id = cursor.getInt(cursor.getColumnIndex("Id"));
					cursor.close();

					if (Id == gateWay.getId()) {
						num = db.update(TABLE_GATEWAY, contentValues, "Id=?",
								new String[]{String.valueOf(gateWay.getId())});
					} else {
						num = 2;
					}

				} else {
					num = db.update(TABLE_GATEWAY, contentValues, "Id=?",
							new String[]{String.valueOf(gateWay.getId())});
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				num = -1;
			}
		}
		return num;
	}

	public int updateIsCurrent(int Id, int current) {

		int resultCount = 0;
		try {

			db.execSQL("update t_gateway set IsCurrent=0 where IsCurrent=1");
			db.execSQL("update t_gateway set IsCurrent=? where Id=?",
					new Object[] { current, Id });
			resultCount = 1;
		} catch (Exception e) {
			// TODO: handle exception
			resultCount = 0;
		}

		return resultCount;
	}

	@Override
	public int deleteGateWay(int Id) {
		// TODO Auto-generated method stub
		int resultCount = 0;
		try {
			resultCount = db.delete(TABLE_GATEWAY, "Id=?",
					new String[] { String.valueOf(Id) });

		} catch (Exception e) {
			// TODO: handle exception
			resultCount = -1;
		}
		return resultCount;
	}

	public List<Room> getRoom() {
		List<Room> list = new ArrayList<Room>();

		if(db!=null){
			Cursor cursor = db.query(TABLE_ROOM, new String[] { "Id", "RoomName",
							"RoomImage", "IsShow", "CreationTime", "Seqencing","Data1" },
					null, null, null, null, "Seqencing asc");

			int resultCount = cursor.getCount();
			try {

				if (resultCount == 0 || !cursor.moveToFirst()) {
					return list;
				}
				for (int i = 0; i < resultCount; i++) {
					Room room = new Room();
					room.setId(cursor.getInt(0));
					room.setRoomName(cursor.getString(1));
					room.setRoomImage(cursor.getString(2));
					room.setIsShow(cursor.getInt(3));
					room.setCreationTime(cursor.getString(4));
					room.setSeqencing(cursor.getInt(5));
					room.setData1(cursor.getString(6));
					list.add(room);
					cursor.moveToNext();
				}
			} catch (Exception e) {
			}
		}
		return list;
	}

	public int insertRoom(Room room) {
		int num = 0;
		try {
			Cursor result = db.query(TABLE_ROOM, new String[] { "RoomName" },
					"RoomName=?", new String[] { room.getRoomName() }, null,
					null, null, null);

			if (result.getCount() > 0) {
				num = 1;
			} else {
				ContentValues contentValues = new ContentValues();
				if (room.getId() != 0) {
					contentValues.put("Id", room.getId());
				}
				contentValues.put("RoomName", room.getRoomName());
				contentValues.put("RoomImage",room.getRoomImage());
				contentValues.put("CreationTime", getSysNowTime());
				contentValues.put("IsShow",
						room.getIsShow());
				contentValues.put("Seqencing", room.getSeqencing());
				contentValues.put("Data1", room.getData1());
				num += db.insert(TABLE_ROOM, null, contentValues);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			num = -1;
		}
		return num;
	}

	public int updateRoom(Room room, String RoomName) {
		int num = 0;
		ContentValues contentValues = new ContentValues();
		contentValues.put("RoomName", room.getRoomName());
		contentValues.put("RoomImage",room.getRoomImage());
		contentValues.put("CreationTime", getSysNowTime());
		contentValues.put("IsShow", room.getIsShow());
		contentValues.put("Seqencing", room.getSeqencing());
		contentValues.put("Data1", room.getData1());
		try {
			Cursor cursor = db.query(TABLE_ROOM, new String[] { "Id" },
					"RoomName=?", new String[] { room.getRoomName() }, null,
					null, null, null);

			if (cursor.getCount() > 1) {
				num = 2;
			} else if (cursor.getCount() == 1) {
				cursor.moveToLast();
				int Id = cursor.getInt(cursor.getColumnIndex("Id"));
				cursor.close();

				if (Id == room.getId()) {
					num = db.update(TABLE_ROOM, contentValues, "Id=?",
							new String[] { String.valueOf(room.getId()) });
				} else {
					num = 2;
				}
			} else {
				num = db.update(TABLE_ROOM, contentValues, "Id=?",
						new String[] { String.valueOf(room.getId()) });
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			num = -1;
		}
		return num;
	}

	@Override
	public List<Device> getDevices() {
		// TODO Auto-generated method stub
		List<Device> list = new ArrayList<Device>();
		try {
			String sql = "select D.*,D.Seqencing, a.RoomName, p.ProductsName,p.ProductsImage,p.ProductsCode,p.IsSwitch,p.ProductsKey,p.ProductsIO,p.ActionShow,p.FunctionShow,p.ActionPramShow,p.FunctionParamShow,DT.DeviceTypeKey,DT.IsShow,DT.DeviceTypeClick,DT.DeviceTypeName FROM t_device D LEFT JOIN t_room a on d.RoomId=a.Id left JOIN t_products p on d.ProductsKey=p.ProductsKey LEFT JOIN t_device_type dt on d.DeviceTypeKey=dt.DeviceTypeKey order by dt.Seqencing,d.Seqencing asc";
			if (db != null) {
				Cursor cursor = db.rawQuery(sql, null);
				if (cursor != null) {
					int resultCount = cursor.getCount();
					if (resultCount == 0 || !cursor.moveToFirst()) {
						return list;
					}
					for (int i = 0; i < resultCount; i++) {
						Device device = new Device();
						device.setId(cursor.getInt(cursor.getColumnIndex("Id")));
						device.setDeviceTypeClick(cursor.getInt(cursor
								.getColumnIndex("DeviceTypeClick")));
						device.setDeviceName(cursor.getString(cursor
								.getColumnIndex("DeviceName")));
						device.setDeviceTypeName(cursor.getString(cursor
								.getColumnIndex("DeviceTypeName")));
						device.setProductsName(cursor.getString(cursor
								.getColumnIndex("ProductsName")));
						device.setProductsCode(cursor.getString(cursor
								.getColumnIndex("ProductsCode")));
						device.setDeviceID(cursor.getString(cursor
								.getColumnIndex("DeviceID")));
						device.setIsShow(cursor.getInt(cursor
								.getColumnIndex("IsShow")));
						String deviceImage=cursor.getString(cursor
								.getColumnIndex("DeviceImage"));
						if(deviceImage==null||deviceImage==""||deviceImage.equals("")){
							deviceImage=cursor.getString(cursor
									.getColumnIndex("ProductsImage"));
							device.setDeviceImage(deviceImage);
						}else{
							device.setDeviceImage(deviceImage);
						}
						device.setDeviceTypeKey(cursor.getInt(cursor
                                .getColumnIndex("DeviceTypeKey")));
						device.setDeviceIO(cursor.getString(cursor
								.getColumnIndex("DeviceIO")));
						device.setDeviceNickName(cursor.getString(cursor
								.getColumnIndex("DeviceNickName")));
						device.setSeqencing(cursor.getInt(cursor
								.getColumnIndex("Seqencing")));
						device.setDeviceOnLine(cursor.getInt(cursor
								.getColumnIndex("DeviceOnLine")));
						device.setDeviceTimer(cursor.getString(cursor
								.getColumnIndex("DeviceTimer")));
						device.setDeviceOrdered(cursor.getString(cursor
								.getColumnIndex("DeviceOrdered")));
						device.setStartTime(cursor.getString(cursor
								.getColumnIndex("StartTime")));
						device.setEndTime(cursor.getString(cursor
								.getColumnIndex("EndTime")));
						device.setSceneId(cursor.getInt(cursor
								.getColumnIndex("SceneId")));
						device.setRoomName(cursor.getString(cursor
								.getColumnIndex("RoomName")));
						device.setCreationTime(cursor.getString(cursor
								.getColumnIndex("CreationTime")));
						device.setGradingId(cursor.getInt(cursor
								.getColumnIndex("GradingId")));
						device.setRoomId(cursor.getInt(cursor
								.getColumnIndex("RoomId")));
						device.setDeviceTypeKey(cursor.getInt(cursor
								.getColumnIndex("DeviceTypeKey")));
						device.setIsSwitch(cursor.getInt(cursor
								.getColumnIndex("IsSwitch")));
						device.setProductsKey(cursor.getInt(cursor
								.getColumnIndex("ProductsKey")));
						device.setProductsIO(cursor.getInt(cursor
								.getColumnIndex("ProductsIO")));
						device.setActionShow(cursor.getInt(cursor
								.getColumnIndex("ActionShow")));
						device.setFunctionShow(cursor.getInt(cursor
								.getColumnIndex("FunctionShow")));
						device.setActionPramShow(cursor.getInt(cursor
								.getColumnIndex("ActionPramShow")));
						device.setFunctionParamShow(cursor.getInt(cursor
								.getColumnIndex("FunctionParamShow")));
						device.setData1(cursor.getString(cursor
								.getColumnIndex("Data1")));
						device.setIsBackCode(0);
						list.add(device);
						cursor.moveToNext();
					}
				}
			}
		}catch (Exception e){

		}
		return list;
	}

	@Override
	public List<Device> getModeDevice(int modeById) {
		// TODO Auto-generated method stub
		List<Device> list = new ArrayList<Device>();
		try{
			String sql = "select ml.*,d.DeviceID,d.DeviceIO,d.StartTime,d.EndTime,d.DeviceName,p.ProductsName,p.ProductsCode,p.ActionShow,p.FunctionShow,p.ActionPramShow,p.FunctionParamShow, DT.DeviceTypeName FROM t_mode_list ml left join t_device d on ml.DeviceId=d.Id left join t_products p on d.deviceProductId=p.Id LEFT JOIN t_device_type dt on d.DeviceTypeKey=dt.Id left join t_device_type DT on d.DeviceTypeKey=DT.Id where ml.ModeId="
					+ modeById + " order by dt.Seqencing asc";
			if(db!=null) {
				Cursor cursor = db.rawQuery(sql, null);
				if (cursor != null) {
					int resultCount = cursor.getCount();

					if (resultCount == 0 || !cursor.moveToFirst()) {
						return list;
					}
					for (int i = 0; i < resultCount; i++) {
						Device device = new Device();
						device.setDeviceName(cursor.getString(cursor
								.getColumnIndex("DeviceName")));
						device.setDeviceTypeName(cursor.getString(cursor
								.getColumnIndex("DeviceTypeName")));
						device.setProductsName(cursor.getString(cursor
								.getColumnIndex("ProductsName")));

						device.setProductsCode(cursor.getString(cursor
								.getColumnIndex("ProductsCode")));
						device.setDeviceID(cursor.getString(cursor
								.getColumnIndex("DeviceID")));
						device.setDeviceIO(cursor.getString(cursor
								.getColumnIndex("DeviceIO")));
						device.setStartTime(cursor.getString(cursor
								.getColumnIndex("StartTime")));
						device.setEndTime(cursor.getString(cursor
								.getColumnIndex("EndTime")));
						device.setGradingId(cursor.getInt(cursor
								.getColumnIndex("GradingId")));
						device.setActionShow(cursor.getInt(cursor
								.getColumnIndex("ActionShow")));
						device.setFunctionShow(cursor.getInt(cursor
								.getColumnIndex("FunctionShow")));
						device.setActionPramShow(cursor.getInt(cursor
								.getColumnIndex("ActionPramShow")));
						device.setFunctionParamShow(cursor.getInt(cursor
								.getColumnIndex("FunctionParamShow")));
						list.add(device);
						cursor.moveToNext();

					}
				}
			}
		}catch (Exception e){

		}
		return list;
	}
	@Override
	public void updateDeviceSort(List<Device> list){
		for (int i = 0; i < list.size(); i++) {
			db.execSQL("update t_device set Seqencing=? where Id=?",
					new Object[]{(i + 1), list.get(i).getId()});
		}
	}
	@Override
	public void updateModeSort(List<Mode> list) {
		for (int i = 0; i < list.size(); i++) {
			db.execSQL("update t_mode set Seqencing=? where Id=?",
					new Object[] { (i + 1), list.get(i).getId() });

		}
	}

	@Override
	public void updateRoomSort(List<Room> list) {
		for (int i = 0; i < list.size(); i++) {
			db.execSQL("update t_room set Seqencing=? where Id=?",
					new Object[] { (i + 1), list.get(i).getId() });


		}
	}

	public void updateRedInfraSort(List<RedInfra> list) {

		for (int i = 0; i < list.size(); i++) {
			db.execSQL("update t_infrared set Seqencing=? where Id=?",
					new Object[]{(i+1), list.get(i).getId()});
		}
	}

	public void updateModeListSort(List<ModeList> list) {
		for (int i = 0; i < list.size(); i++) {
			db.execSQL("update t_mode_list set Seqencing=? where Id=?",
					new Object[]{i, list.get(i).getId()});

		}
	}

	@Override
	public void updateRoomHide(int homePageDisplays, int Id) {
		db.execSQL("update t_room set homePageDisplays=? where Id=?",
				new Object[] { homePageDisplays, Id });


	}

	@Override
	public List<DeviceType> getDeviceTypes() {
		List<DeviceType> list = new ArrayList<DeviceType>();
		try {
			Cursor cursor = db.query(TABLE_DEVICE_TYPE, new String[] { "Id",
					"DeviceTypeName","DeviceTypeImage","DeviceTypeKey", "DeviceTypeClick", "IsShow",
					"Seqencing" }, null, null, null, null, "Seqencing asc");
			int resultCount = cursor.getCount();
			if (resultCount == 0 || !cursor.moveToFirst()) {
				return list;
			}
			for (int i = 0; i < resultCount; i++) {
				DeviceType deviceType = new DeviceType();
				deviceType.setId(cursor.getInt(cursor.getColumnIndex("Id")));
				deviceType.setDeviceTypeName(cursor.getString(cursor
						.getColumnIndex("DeviceTypeName")));
				deviceType.setDeviceTypeImage(cursor.getString(cursor
								.getColumnIndex("DeviceTypeImage")));
				deviceType.setSeqencing(cursor.getInt(cursor
						.getColumnIndex("Seqencing")));
				deviceType.setDeviceTypeKey(cursor.getInt(cursor
						.getColumnIndex("DeviceTypeKey")));
				deviceType.setDeviceTypeClick(cursor.getInt(cursor
						.getColumnIndex("DeviceTypeClick")));
				deviceType.setIsShow(cursor.getInt(cursor
						.getColumnIndex("IsShow")));
				list.add(deviceType);
				cursor.moveToNext();
			}
		} catch (Exception e) {
		}
		return list;
	}

	@Override
	public void updateDeviceTypeSort(List<DeviceType> list) {
		for (int i = 0; i < list.size(); i++) {
			db.execSQL("update t_device_type set Seqencing=? where Id=?",
					new Object[] { (i + 1), list.get(i).getId() });
		}
	}

	@Override
	public int deleteRoom(int Id) {
		// TODO Auto-generated method stub
		int resultCount = 0;
		try {
			resultCount += db.delete(TABLE_DEVICE, "RoomId=?",
					new String[] { String.valueOf(Id) });
			resultCount += db.delete(TABLE_ROOM, "Id=?",
					new String[] { String.valueOf(Id) });

		} catch (Exception e) {
			// TODO: handle exception
			resultCount = -1;
		}
		return resultCount;
	}

	public int deleteRedFra(int Id) {// TODO Auto-generated method stub
		int resultCount = 0;
		try {
			resultCount = db.delete(TABLE_INFRARED, "Id=?",
					new String[] { String.valueOf(Id) });
		} catch (Exception e) {
			// TODO: handle exception
			resultCount = -1;
		}
		return resultCount;
	}

	@Override
	public int deleteDevice(int Id) {
		// TODO Auto-generated method stub
		int resultCount = 0;
		try {
			resultCount += db.delete(TABLE_MODE_LIST, "DeviceId=?",
					new String[] { String.valueOf(Id) });
			resultCount += db.delete(TABLE_INFRARED, "FatherId=?",
					new String[] { String.valueOf(Id) });
			resultCount += db.delete(TABLE_DEVICE, "Id=?",
					new String[] { String.valueOf(Id) });
		} catch (Exception e) {
			// TODO: handle exception
			resultCount = -1;
		}
		return resultCount;
	}

	public String getSysNowTime() {
		Date now = new Date();
		java.text.DateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		String formatTime = format.format(now);
		return formatTime;
	}

	@Override
	public int deleteMode(int Id) {
		// TODO Auto-generated method stub
		int resultCount = 0;
		try {
			resultCount += db.delete(TABLE_MODE_LIST, "ModeId=?",
					new String[] { String.valueOf(Id) });

			resultCount += db.delete(TABLE_MODE, "Id=?",
					new String[] { String.valueOf(Id) });

		} catch (Exception e) {
			// TODO: handle exception
			resultCount = -1;
		}
		return resultCount;
	}

	public int deleteModelist(int Id) {
		int resultCount = 0;
		try {
			resultCount = db.delete(TABLE_MODE_LIST, "Id=?",
					new String[] { String.valueOf(Id) });
		} catch (Exception e) {
			// TODO: handle exception
			resultCount = -1;
		}
		return resultCount;
	}

	public int updateFraID(String DeviceID, int infra_index) {
		try {
			String sql = "update t_infrared set infrared_ID = '" + DeviceID
					+ "'" + " where FatherId=?" + infra_index;
			db.execSQL(sql);
		} catch (Exception e) {
			return -1;
		}
		return 0;
	}

	public void execSQL(String sql) {
		db.execSQL(sql);
	}

	@Override
	public List<RedInfra> getInfrareds() {
		// TODO Auto-generated method stub
		List<RedInfra> list = new ArrayList<RedInfra>();
		try {
			String sql = "select *,l.Id as lid,l.Seqencing as lseqencing from t_infrared l left join t_device d on d.Id=l.FatherId where l.FatherId=d.Id order by l.Seqencing";
			Cursor cursor = db.rawQuery(sql, null);
			int resultCount = cursor.getCount();
			if (resultCount == 0 || !cursor.moveToFirst()) {
				return list;
			}
			for (int i = 0; i < resultCount; i++) {
				RedInfra infrared = new RedInfra();
				infrared.setId(cursor.getInt(cursor.getColumnIndex("lid")));
				infrared.setInfraredName(cursor.getString(cursor
						.getColumnIndex("InfraredName")));
				infrared.setInfraredImage(cursor.getString(cursor
						.getColumnIndex("InfraredImage")));
				infrared.setDeviceId(cursor.getString(cursor
						.getColumnIndex("DeviceId")));
				infrared.setFatherId(cursor.getInt(cursor
						.getColumnIndex("FatherId")));
				infrared.setInfraredIndex(cursor.getInt(cursor
						.getColumnIndex("InfraredIndex")));
				infrared.setInfraredBrandId(cursor.getInt(cursor
						.getColumnIndex("InfraredBrandId")));
				infrared.setInfraredBrandName(cursor.getString(cursor
						.getColumnIndex("InfraredBrandName")));
				infrared.setInfraredVersionId(cursor.getInt(cursor
						.getColumnIndex("InfraredVersionId")));
				infrared.setInfraredVersionName(cursor.getString(cursor
						.getColumnIndex("InfraredVersionName")));
				infrared.setInfraredKey(cursor.getInt(cursor
						.getColumnIndex("InfraredKey")));
				infrared.setInfraredButtonId(cursor.getInt(cursor
						.getColumnIndex("InfraredButtonId")));
				infrared.setInfraredCode(cursor.getString(cursor
						.getColumnIndex("InfraredCode")));
				infrared.setInfraredStudyType(cursor.getInt(cursor
						.getColumnIndex("InfraredStudyType")));
				infrared.setInfraredX(cursor.getInt(cursor
						.getColumnIndex("InfraredX")));
				infrared.setInfraredY(cursor.getInt(cursor
						.getColumnIndex("InfraredY")));
				infrared.setCreationTime(cursor.getString(cursor
						.getColumnIndex("CreationTime")));
				infrared.setSeqencing(cursor.getInt(cursor
						.getColumnIndex("lseqencing")));
				list.add(infrared);
				cursor.moveToNext();
			}
		} catch (Exception e) {
		}
		return list;
	}

	@Override
	public int insertInfrad(RedInfra infrared) {
		// TODO Auto-generated method stub
		int num = 0;
		try {
			ContentValues contentValues = new ContentValues();
			if (infrared.getId() != 0) {
				contentValues.put("Id", infrared.getId());
			}
			contentValues.put("InfraredName", infrared.getInfraredName());
			contentValues.put("InfraredImage", infrared.getInfraredImage());
			contentValues.put("DeviceId", infrared.getDeviceId());
			contentValues.put("FatherId", infrared.getFatherId());
			contentValues.put("InfraredIndex", infrared.getInfraredIndex());
			contentValues.put("InfraredBrandId", infrared.getInfraredBrandId());
			contentValues.put("InfraredBrandName", infrared.getInfraredBrandName());
			contentValues.put("InfraredVersionId",
					infrared.getInfraredVersionId());
			contentValues.put("InfraredVersionName",
					infrared.getInfraredVersionName());
			contentValues.put("InfraredKey",
					infrared.getInfraredKey());
			contentValues.put("InfraredButtonId", infrared.getInfraredButtonId());
			contentValues.put("InfraredCode", infrared.getInfraredCode());
			contentValues.put("InfraredStudyType", infrared.getInfraredStudyType());
			contentValues.put("InfraredX", infrared.getInfraredX());
			contentValues.put("InfraredY", infrared.getInfraredY());
			contentValues.put("CreationTime", getSysNowTime());
			contentValues.put("Seqencing", infrared.getSeqencing());
			num += db.insert(TABLE_INFRARED, null, contentValues);
		} catch (Exception e) {
			// TODO: handle exception
			num = -1;
		}
		return num;
	}

	@Override
	public int deleteInfrad(int Id) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int deleteSyncData(String table) {
		return db.delete(table, null, null);
	}
}
