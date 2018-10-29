package com.zunder.smart.activity.safe;

import com.zunder.smart.model.ComModel;

import java.util.ArrayList;
import java.util.List;

public class SafeDeviceUtils {
    private volatile static SafeDeviceUtils install;
    private List<ComModel> list=new ArrayList <ComModel>();
    public static SafeDeviceUtils getInstance() {
        if (null == install) {
            install = new SafeDeviceUtils();
        }
        return install;
    }

    public List<ComModel> getAll(){
        if(list.size()==0){
            list.add(new ComModel(1,12,"断路器","0"));
            list.add(new ComModel(2,12,"断路器1路","8"));
            list.add(new ComModel(3,12,"断路器2路","9"));
            list.add(new ComModel(4,12,"断路器3路","A"));
            list.add(new ComModel(5,12,"断路器4路","B"));
            list.add(new ComModel(6,12,"断路器5路","C"));
            list.add(new ComModel(7,12,"断路器6路","D"));
            list.add(new ComModel(8,12,"断路器7路","E"));
            list.add(new ComModel(9,12,"断路器8路","F"));
            list.add(new ComModel(10,13,"时序器","0"));
            list.add(new ComModel(11,13,"时序1路","8"));
            list.add(new ComModel(12,13,"时序2路","9"));
            list.add(new ComModel(13,13,"时序3路","A"));
            list.add(new ComModel(14,13,"时序4路","B"));
            list.add(new ComModel(15,13,"时序5路","C"));
            list.add(new ComModel(16,13,"时序6路","D"));
            list.add(new ComModel(17,13,"时序7路","DE"));
            list.add(new ComModel(18,13,"时序8路","F"));
            list.add(new ComModel(19,16,"输出DO 1","0"));
            list.add(new ComModel(20,16,"输出DO 2","1"));
            list.add(new ComModel(21,16,"输入DI 1","2"));
            list.add(new ComModel(22,16,"输入DI 2","3"));
        }
        return list;
    }

    public List<String> getDeviceStrings(int deviceTypeKey){
        if(list.size()==0){
            getAll();
        }
        List<String> resultList=new ArrayList <String>();
        for (int i=0;i<list.size();i++){
            if(list.get(i).getDeviceTypekey()==deviceTypeKey){
                resultList.add(list.get(i).getName());
            }
        }
        return resultList;
    }

    public ComModel getComModels(String Name){
        if(list.size()==0){
            getAll();
        }
        ComModel comModel=null;
        for (int i=0;i<list.size();i++){
            if(list.get(i).getName().equals(Name)){
                comModel=list.get(i);
                break;
            }
        }
        return comModel;
    }
}
