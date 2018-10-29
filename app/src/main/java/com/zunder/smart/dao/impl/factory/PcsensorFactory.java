package com.zunder.smart.dao.impl.factory;

import com.zunder.smart.model.PcSensorInfo;

import java.util.ArrayList;
import java.util.List;

public class PcsensorFactory {
	public  List<PcSensorInfo> list = new ArrayList<PcSensorInfo>();
	private volatile static PcsensorFactory install;
	public static PcsensorFactory getInstance() {
		if (null == install) {
			install = new PcsensorFactory();
		}
		return install;
	}

	public void updateInsideTemp(String[] Str){
		if(Str!=null&&Str.length==8){
			for (int i=0;i<Str.length;i++){
				PcSensorInfo pcSensorInfo=getById(i);
				if(pcSensorInfo!=null){
					pcSensorInfo.setInsideTemp(Str[i]);
				}
			}
		}

	}
	public void updateHumidity(String[] Str){
		if(Str!=null&&Str.length==8){
			for (int i=0;i<Str.length;i++){
				PcSensorInfo pcSensorInfo=getById(i);
				if(pcSensorInfo!=null){
					pcSensorInfo.setHumidity(Str[i]);
				}
			}
		}
	}
	public void updatePm25(String[] Str) {
		if (Str != null && Str.length == 8) {
			for (int i = 0; i < Str.length; i++) {
				PcSensorInfo pcSensorInfo = getById(i);
				if (pcSensorInfo != null) {
					pcSensorInfo.setPm25(Str[i]);
				}
			}
		}
	}
	public void updateFormaldehyde(String[] Str){
		if(Str!=null&&Str.length==8){
			for (int i=0;i<Str.length;i++){
				PcSensorInfo pcSensorInfo=getById(i);
				if(pcSensorInfo!=null){
					pcSensorInfo.setFormaldehyde(Str[i]);
				}
			}
		}
	}
	public void updateIllumination(String[] Str){
		if(Str!=null&&Str.length==8){
			for (int i=0;i<Str.length;i++){
				PcSensorInfo pcSensorInfo=getById(i);
				if(pcSensorInfo!=null){
					pcSensorInfo.setIllumination(Str[i]);
				}
			}
		}
	}
	public PcSensorInfo getById(int Id){
		if(list.size()==0){
			Add();
		}
		PcSensorInfo pcSensorInfo=null;
		for (int i=0;i<list.size();i++){
			if(list.get(i).getId()==Id){
				pcSensorInfo=list.get(i);
				break;
			}
		}
		return pcSensorInfo;
	}
	public void Add(){
		list.add(new PcSensorInfo(0,"0","0","0","0","0"));
		list.add(new PcSensorInfo(1,"0","0","0","0","0"));
		list.add(new PcSensorInfo(2,"0","0","0","0","0"));
		list.add(new PcSensorInfo(3,"0","0","0","0","0"));
		list.add(new PcSensorInfo(4,"0","0","0","0","0"));
		list.add(new PcSensorInfo(5,"0","0","0","0","0"));
		list.add(new PcSensorInfo(6,"0","0","0","0","0"));
		list.add(new PcSensorInfo(7,"0","0","0","0","0"));
	}
}
