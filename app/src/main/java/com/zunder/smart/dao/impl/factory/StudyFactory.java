package com.zunder.smart.dao.impl.factory;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.ItemName;

import java.util.ArrayList;
import java.util.List;

public class StudyFactory {
    private volatile static StudyFactory install;
    public static StudyFactory getInstance() {
        if (null == install) {
            install = new StudyFactory();
        }
        return install;
    }
    public List<ItemName> getAll(int deviceTypeKey){
        List<ItemName> list=new ArrayList <ItemName>();

        if(deviceTypeKey==4){
            final String[] strings = (String[]) MyApplication.getInstance().getResources().getStringArray(
                    R.array.studys);
            for (int i=0;i<strings.length;i++){
                ItemName itemName=new ItemName();
                itemName.setId(i);
                itemName.setItemName(strings[i]);
                list.add(itemName);
            }
        }else if(deviceTypeKey==5){
            String[] strings = (String[]) MyApplication.getInstance().getResources().getStringArray(
                    R.array.tvStudy);
            for (int i=0;i<strings.length;i++){
                ItemName itemName=new ItemName();
                itemName.setId(i);
                itemName.setItemName(strings[i]);
                list.add(itemName);
            }
        }
        return list;
    }

}
