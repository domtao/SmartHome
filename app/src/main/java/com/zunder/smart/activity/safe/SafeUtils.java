package com.zunder.smart.activity.safe;

import com.zunder.smart.json.ProCaseUtils;

import java.util.ArrayList;
import java.util.List;

public class SafeUtils {
    private volatile static SafeUtils install;
    public static SafeUtils getInstance() {
        if (null == install) {
            install = new SafeUtils();
        }
        return install;
    }

    public List<String> safeTypes(int safeType){
        List <String> list = new ArrayList <String>();
        if(safeType==0) {
            list.add("火警");
            list.add("瓦斯");
            list.add("求救");
            list.add("其它");
        }else{
            list.add("遥控");
        }
        return list;
    }
    public List<String> safeIos(int pos){
        List<String> list=new ArrayList <String>();
        if(pos==0){
            list.add("回路1");
            list.add("回路2");
        }
        else if(pos==1){
            list.add("回路3");
            list.add("回路4");
        }
        else if(pos==2){
            list.add("回路5");
            list.add("回路6");
            list.add("回路7");
            list.add("回路8");
        }
        else if(pos==3){
            for (int i=9;i<=28;i++){
                list.add("回路"+i);
            }
        }  else if(pos==4){
            for (int i=29;i<=32;i++){
                list.add("设定"+i);
            }
        }
        return list;
    }
}
