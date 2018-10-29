package com.zunder.smart.utils;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;
import com.zunder.smart.MyApplication;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;

public class ListNumBer {
	public static List<String> list = new ArrayList<String>();
	static {
		for (int i = 1; i < 256; i++) {
			list.add(i + "");
		}
	}

	public static List<String> getList() {
		List<Mode> listMode = MyApplication.getInstance().getWidgetDataBase()
				.getMode();
		for (Mode mode : listMode) {
			list.remove(mode.getModeCode() + "");
		}
		return list;
	}

	public static List<String> getHour() {
		List<String> resultLlist = new ArrayList<String>();
		for (int i = 0; i < 25; i++) {
			if (i < 10) {
				resultLlist.add("0" + i);
			} else {
				resultLlist.add(i + "");
			}
		}

		return resultLlist;

	}

	public static List<String> getTemp(int start ,int end) {
		List<String> resultLlist = new ArrayList<String>();
		for (int i = start; i < end; i++) {
			if (i < 10) {
				resultLlist.add("0" + i);
			} else {
				resultLlist.add(i + "");
			}
		}
		return resultLlist;

	}

	public static List<String> getXinChen(int start ,int end,String units) {
		List<String> resultLlist = new ArrayList<String>();
		for (int i = start; i < end; i++) {
			resultLlist.add(i+units);
		}
		return resultLlist;

	}
	public static List<String> getSwitch() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.open_2));
		resultLlist.add(MyApplication.getInstance().getString(R.string.close_1));
		return resultLlist;
	}
	public static List<String> getButtonType() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("本地场景");
		resultLlist.add("自定义动作");
		return resultLlist;
	}
	public static List<String> getImageType() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("打开图片");
		resultLlist.add("拍摄照片");
		return resultLlist;
	}
	public static List<String> getColors() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("黑色");//#000000
		resultLlist.add("白色");//#FFFFFF
		resultLlist.add("红色");//#FF0000
		resultLlist.add("橙色");//#FF6100
		resultLlist.add("黄色");//#FFFF00
		resultLlist.add("绿色");//#00FF00
		resultLlist.add("蓝色");//#0000FF
		resultLlist.add("紫色");//#A020F0
		resultLlist.add("青色");//#00FFFF
		resultLlist.add("灰色");//#C0C0C0

		return resultLlist;
	}
	public static List<String> getAirSwitch(String value1,String value2,String value3,String value4,String value5,String value6) {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.set_voltage_up)+"_"+value1);
		resultLlist.add(MyApplication.getInstance().getString(R.string.set_voltage_down)+"_"+value2);
		resultLlist.add(MyApplication.getInstance().getString(R.string.leakage_current_up)+"_"+value3);
		resultLlist.add(MyApplication.getInstance().getString(R.string.set_power_down)+"_"+value4);
		resultLlist.add(MyApplication.getInstance().getString(R.string.set_temp_up)+"_"+value5);
		resultLlist.add(MyApplication.getInstance().getString(R.string.set_current_up)+"_"+value6);
//		resultLlist.add(MyApplication.getInstance().getString(R.string.alrm_voltage_up));
//		resultLlist.add(MyApplication.getInstance().getString(R.string.alrm_voltage_down));
		resultLlist.add(MyApplication.getInstance().getString(R.string.set_voltage_value));
		return resultLlist;
	}
	public static List<String> getMachineCode() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("机码设置");
		resultLlist.add("机码配对");
		return resultLlist;
	}
	public static List<String> getClearCode() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("清空列表");
		resultLlist.add("清空缓存");
		return resultLlist;
	}
	public static List<String> getCommand(int sfromBtn) {
		String[] str = MyApplication.getInstance().getString((sfromBtn==0)?R.string.command:R.string.SymbolCommand).split(";");
		List<String> resultLlist = new ArrayList<String>();
		for (int i=0;i<str.length;i++)
		{
			resultLlist.add(str[i]);
		}
		return resultLlist;
	}
	public static List<String> getPeople() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("");
		for (int i=1;i<9;i++) {
			if(i==8){
				resultLlist.add(MyApplication.getInstance().getString(R.string.cureentpepole)+">" + i);
			}else {
				resultLlist.add(MyApplication.getInstance().getString(R.string.cureentpepole)+":" + i);
			}
		}return resultLlist;
	}
	public static List<String> getStudy() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.onestudy));
		resultLlist.add(MyApplication.getInstance().getString(R.string.twoopen));
		resultLlist.add(MyApplication.getInstance().getString(R.string.twoclose));

		return resultLlist;
	}
	public static List<String> getProStudy() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.opencode));
		resultLlist.add(MyApplication.getInstance().getString(R.string.closecode));
		return resultLlist;
	}
	public static List<String> getRedFraStudy() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("红外本地学习");
		resultLlist.add("红外云端下载");
		resultLlist.add("串口本地编程");
		resultLlist.add("串口云端下载");
		return resultLlist;
	}
	public static int getIndex(List<String> _list, String str) {
		int index = 0;
		for (int i = 0; i < _list.size(); i++) {
			if (_list.get(i).equals(str)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public static List<String> getChange() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.nomalstate));
		resultLlist.add(MyApplication.getInstance().getString(R.string.hstate));
		resultLlist.add(MyApplication.getInstance().getString(R.string.vstate));
		resultLlist.add(MyApplication.getInstance().getString(R.string.allstate));
		return resultLlist;

	}

	public static List<String> getClarity() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.high));
		resultLlist.add(MyApplication.getInstance().getString(R.string.biao));
		resultLlist.add(MyApplication.getInstance().getString(R.string.liu));
		return resultLlist;

	}

	public static List<String> getTimming() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("开启5秒");
		resultLlist.add("开启15分钟");
		resultLlist.add("开启30分钟");
		resultLlist.add("开启1小时");
		resultLlist.add("开启2小时");
		resultLlist.add("开启3小时");
		resultLlist.add("开启4小时");
		resultLlist.add("开启5小时");
		resultLlist.add("开启6小时");
		resultLlist.add("开启7小时");
		resultLlist.add("开启8小时");

		return resultLlist;

	}

	public static List<String> getMinit() {
		List<String> resultLlist = new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			if(i<10){
				resultLlist.add("0"+i);
			}else{
				resultLlist.add(i + "");
		}}
		return resultLlist;
	}
	public static List<String> getNumbers() {
		List<String> resultLlist = new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			resultLlist.add(i + "");
	}
		return resultLlist;
	}
	public static List<String> getMinit60() {
		List<String> resultLlist = new ArrayList<String>();

		for (int i = 0; i < 60; i++) {
			resultLlist.add(i + "");
		}
		return resultLlist;
	}

	public static List<String> getAction() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("打开");
		resultLlist.add("关闭");
//		resultLlist.add("副控");
//		resultLlist.add("停止");
//		resultLlist.add("假如输出开");
//		resultLlist.add("假如输出关");
//		resultLlist.add("假如输入开");
//		resultLlist.add("假如输入关");
//		resultLlist.add("假如大于");
//		resultLlist.add("假如小于");
//		resultLlist.add("假如等于");
//		resultLlist.add("假如是优");
//		resultLlist.add("假如一般");
//		resultLlist.add("假如是差");
//		resultLlist.add("或者输出开");
//		resultLlist.add("或者输出关");
//		resultLlist.add("或者输入开");
//		resultLlist.add("或者输入关");
//		resultLlist.add("或者大于");
//		resultLlist.add("或者小于");
//		resultLlist.add("或者等于");
//		resultLlist.add("或者是优");
//		resultLlist.add("或者一般");
//		resultLlist.add("或者是差");
//		resultLlist.add("而且输出开");
//		resultLlist.add("而且输出关");
//		resultLlist.add("而且输入开");
//		resultLlist.add("而且输入关");
//		resultLlist.add("而且大于");
//		resultLlist.add("而且小于");
//		resultLlist.add("而且等于");
//		resultLlist.add("而且是优");
//		resultLlist.add("而且一般");
//		resultLlist.add("而且是差");
//		resultLlist.add("条件不成立");
//		resultLlist.add("逻辑结束");
		return resultLlist;
	}

	public static List<String> getUnits() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.Second));
		resultLlist.add(MyApplication.getInstance().getString(R.string.Munits));
		resultLlist.add(MyApplication.getInstance().getString(R.string.Hour));


		return resultLlist;

	}

	public static List<String> getModes() {
		List<String> resultLlist = new ArrayList<String>();
		for (int i = 1; i < 200; i++) {
			String number = "ID:" + i;
			if(ModeFactory.getInstance().useModeId(i)==1){
				if(ModeFactory.getInstance().useIO(i)==0) {
					number = "ID:" + i + "已使用";
				}else{
					number = "ID:" + i+"可以使用其它回路";
				}
			}
			if (i > 200 && i <= 232) {
				number = "门磁红外联动:" + i;
			} else if (i == 240) {
				number = "液位过高:" + i;
			} else if (i == 241) {
				number = "液位过低:" + i;
			} else if (i == 242) {
				number = "噪音过高:" + i;
			} else if (i == 244) {
				number = "温度过高:" + i;
			} else if (i == 245) {
				number = "温度过低:" + i;
			} else if (i == 246) {
				number = "湿度过高:" + i;
			} else if (i == 247) {
				number = "湿度过低:" + i;
			} else if (i == 248) {
				number = "甲醛浓度过高:" + i;
			} else if (i == 249) {
				number = "PH2.5过高:" + i;
			} else if (i == 250) {
				number = "安防解除联动:" + i;
			} else if (i == 251) {
				number = "安防警报联动:" + i;
			}
			resultLlist.add(number);
		}
		return resultLlist;
	}

	public static List<String> getPoint(GateWay point) {
		List<String> resultLlist = new ArrayList<String>();
		String[] str = null;
		if (point != null && point.getGateWayPoint().length() > 16) {
			str = point.getGateWayPoint().split(",");
		} else {
			str = new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0",
					"0", "0", "0", "0", "0", "0", "0" };
		}
		for (int i = 0; i < str.length; i++) {
			String result = DeviceFactory.getInstance()
					.getDeviceName(Integer.parseInt(str[i]));
			if (result.equals("")) {
				resultLlist.add(MyApplication.getInstance().getString(R.string.point) + (i + 1));
			} else {
				resultLlist.add(result);
			}

		}
		return resultLlist;
	}
	public static List<String> getControl() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.all_open));
		resultLlist.add(MyApplication.getInstance().getString(R.string.all_close));
		resultLlist.add(MyApplication.getInstance().getString(R.string.device_close));
		return resultLlist;
	}

	public static List<String> getMotor() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.motor_pos));
		resultLlist.add(MyApplication.getInstance().getString(R.string.motor_neg));
		return resultLlist;
	}

	public static List<String> getOnNo() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.full_on_yes));
		resultLlist.add(MyApplication.getInstance().getString(R.string.full_on_no));
		return resultLlist;
	}
	public static List<String> getStudyTv() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.cloudown));
		resultLlist.add(MyApplication.getInstance().getString(R.string.local_study));
		return resultLlist;
	}
	public static List<String> getArce(){
		String[] str = MyApplication.getInstance().getResources().getStringArray(R.array.arces);
		List<String> resultLlist = new ArrayList<String>();
		for (int i=0;i<str.length;i++)
		{
			resultLlist.add(str[i]);
		}
		return resultLlist;
	}
	public static List<String> getSensor(){
		String[] str = MyApplication.getInstance().getResources().getStringArray(R.array.sensors);
		List<String> resultLlist = new ArrayList<String>();
		for (int i=0;i<str.length;i++)
		{
			resultLlist.add(str[i]);
		}
		return resultLlist;
	}
	public static List<String> getWorks(){
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.work1));
		resultLlist.add(MyApplication.getInstance().getString(R.string.work2));
		resultLlist.add(MyApplication.getInstance().getString(R.string.work3));
		return resultLlist;
	}
	public static List<String> getSpeeds(){
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.speed0));
		resultLlist.add(MyApplication.getInstance().getString(R.string.speed1));
		resultLlist.add(MyApplication.getInstance().getString(R.string.speed2));
		resultLlist.add(MyApplication.getInstance().getString(R.string.speed3));
		return resultLlist;
	}
	public static List<String> getValues(){
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(MyApplication.getInstance().getString(R.string.closeValue));
		resultLlist.add(MyApplication.getInstance().getString(R.string.openValue));
		return resultLlist;
	}
	public static List<String> geSaOperator(){
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("学习");
		resultLlist.add("指定设备");
		return resultLlist;
	}
	public static List<String> geSequRoad(String name){
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(name);
		for (int i=1;i<=8;i++) {
			resultLlist.add(MyApplication.getInstance().getString(R.string.sequ)+i+MyApplication.getInstance().getString(R.string.road));

		}
		return resultLlist;
	}
	public static List<String> geAirRoad(String name){

		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add(name);
		for (int i=1;i<=8;i++) {
			resultLlist.add(MyApplication.getInstance().getString(R.string.air_on)+i+MyApplication.getInstance().getString(R.string.road));

		}
		resultLlist.add(MyApplication.getInstance().getString(R.string.air_alrm1));
		resultLlist.add(MyApplication.getInstance().getString(R.string.air_alrm2));
		resultLlist.add(MyApplication.getInstance().getString(R.string.air_alrm3));
		resultLlist.add(MyApplication.getInstance().getString(R.string.air_alrm4));
		resultLlist.add(MyApplication.getInstance().getString(R.string.air_alrm5));
		resultLlist.add(MyApplication.getInstance().getString(R.string.air_alrm6));
		resultLlist.add(MyApplication.getInstance().getString(R.string.air_alrm7));
		resultLlist.add(MyApplication.getInstance().getString(R.string.air_alrm8));
		return resultLlist;
	}
	public static List<String> getMonth() {
		List<String> resultLlist = new ArrayList<String>();

		for (int i = 0; i < 13; i++) {
			resultLlist.add(i+"月");
		}
		return resultLlist;
	}
	public static List<String> getIos() {
		List<String> resultLlist = new ArrayList<String>();

		for (int i = 0; i < 8; i++) {
			resultLlist.add("回路"+(i+1));
		}
		return resultLlist;
	}

	public static List<String> getModeIos(int modeCode) {
		List<String> resultLlist = new ArrayList<String>();

		for (int i = 0; i < 8; i++) {
			if(ModeFactory.getInstance().useModeId(modeCode,i)==1) {
				resultLlist.add("回路" + (i + 1)+" 已使用");
			}else{
				resultLlist.add("回路" + (i + 1));
			}
		}
		return resultLlist;
	}

	public static List<String> roomType() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("隐藏");
		resultLlist.add("家居");
		resultLlist.add("中控");
		return resultLlist;
	}
	public static List<String> modeType() {
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("隐藏");
		resultLlist.add("显示");
		return resultLlist;
	}

	public static List<String> getAirSwitchs(){
		List<String> resultLlist = new ArrayList<String>();
		resultLlist.add("点歌机");
		resultLlist.add("解码器");
		resultLlist.add("前级效果器");resultLlist.add("功率放大器");resultLlist.add("蓝光DVD");resultLlist.add("硬盘播放器");
		resultLlist.add("投影机");resultLlist.add("电视");resultLlist.add("胆机");resultLlist.add("AV功放");resultLlist.add("话筒");
		resultLlist.add("低音炮");resultLlist.add("网络盒子");resultLlist.add("液晶显示器");resultLlist.add("路由器");resultLlist.add("风扇");
		resultLlist.add("电脑");resultLlist.add("音频处理器");resultLlist.add("均衡器");resultLlist.add("调音台");resultLlist.add("多媒体矩阵");
		resultLlist.add("视频追踪器");resultLlist.add("滤波器");resultLlist.add("反馈抑制器");resultLlist.add("话筒主机");resultLlist.add("调光台");
		resultLlist.add("512控台");resultLlist.add("灯光控制器");resultLlist.add("自定义");
	return resultLlist;
	}
}
