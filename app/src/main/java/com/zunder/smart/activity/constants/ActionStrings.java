package com.zunder.smart.activity.constants;


import com.zunder.smart.activity.sub.RedFraActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;

import java.util.ArrayList;
import java.util.List;

public class ActionStrings {
    private volatile static ActionStrings install;
    public static ActionStrings getInstance() {
        if (null == install) {
            install = new ActionStrings();
        }
        return install;
    }

    public List<String> getLinkageStrings(){
        List<String> list=new ArrayList <String>();
            list.add("取消正相联动");list.add("关闭正相联动");list.add("开启正相联动");list.add("全部正相联动");
            list.add("取消反相联动");list.add("关闭反相联动");list.add("开启反相联动");list.add("全部反相联动");
        return list;
    }
    public List<String> getActionStrings(int deviceTypeKey){
        List<String> list=new ArrayList <String>();
        list.add("");
        if(deviceTypeKey==4){
            list.add("副控");list.add("打开");
            list.add("关闭");list.add("停止");list.add("假如输出开");
            list.add("假如输出关");list.add("假如大于");list.add("假如小于");
            list.add("假如等于");list.add("假如是优");list.add("假如一般");list.add("假如是差");
            list.add("或者输出开");list.add("或者输出关");list.add("或者大于");list.add("或者小于");
            list.add("或者等于");list.add("或者是优");list.add("或者一般");list.add("或者是差");
            list.add("而且输出开");list.add("而且输出关");list.add("而且大于");list.add("而且小于");
            list.add("而且等于");list.add("而且是优");list.add("而且一般");list.add("条件不成立");list.add("逻辑结束");
        } else  if(deviceTypeKey==9||deviceTypeKey==17){
            list.add("假如输出开");list.add("假如输出关");list.add("假如输入开");list.add("假如输入关");
            list.add("假如大于");list.add("假如小于");list.add("假如等于");list.add("假如是优");list.add("假如一般");
            list.add("假如是差");list.add("或者输出开");list.add("或者输出关");list.add("或者输入开");list.add("或者输入关");
            list.add("或者大于");list.add("或者小于");list.add("或者等于");list.add("或者是优");list.add("或者一般");list.add("或者是差");
            list.add("而且输出开");list.add("而且输出关");list.add("而且输入开");list.add("而且输入关");list.add("而且大于");list.add("而且小于");
            list.add("而且等于");list.add("而且是优");list.add("而且一般");list.add("而且是差");list.add("条件不成立");list.add("逻辑结束");
        } else  if((deviceTypeKey==18)||(deviceTypeKey==20)){ //安防 ,
            list.add("副控");list.add("打开");
            list.add("关闭");
        } else{
            list.add("副控");list.add("打开");
            list.add("关闭");list.add("停止");
            list.add("假如输出开");list.add("假如输出关");
            list.add("或者输出开");list.add("或者输出关");
            list.add("而且输出开");list.add("而且输出关");
            list.add("条件不成立");list.add("逻辑结束");
        }
        return list;
    }
    public List<String> getFunctionStrings(int deviceTypeKey,int id){
        List<String> list=new ArrayList <String>();
        list.add("");
        if (deviceTypeKey==2) {
            list.add("一半");list.add("三分之一");list.add("三分之二");list.add("四分之一");list.add("四分之三");
        }else if(deviceTypeKey==4){  ///空调

            list.add("制冷");list.add("制热");
            list.add("除湿");list.add("送风");
            list.add("自动");list.add("高速");list.add("中速");list.add("低速");
        }
        else if (deviceTypeKey==5) {  ///电视
            list.add("频道加");list.add("频道减");list.add("静音");
           }
        else if (deviceTypeKey==6) {    ///红外

            list.addAll(RedInfraFactory.getInstance().getInfraNames(id));
        }
        else if (deviceTypeKey==7) {  ///调光
          list.add("白光");list.add("暖光");list.add("中性光");list.add("蓝光");list.add("红光");
            list.add("绿光");list.add("紫光");list.add("橙光");list.add("黄光");list.add("青色");list.add("粉色光");list.add("闪烁");

        }
        else if (deviceTypeKey==8) {   ///音乐
             list.add("播放");list.add("暂停");list.add("停止");list.add("打开静音");list.add("关闭静音");
        }
        else if (deviceTypeKey==11) {   ///投影仪
            list.add("信号源1");list.add("信号源2");list.add("信号源3");list.add("信号源4");
        }
        else if (deviceTypeKey==12) {   ///智能空开
            list.add("断路器1路");list.add("断路器2路");list.add("断路器3路");list.add("断路器4路");list.add("断路器5路");list.add("断路器6路");list.add("断路器7路");list.add("断路器8路");
        }
        else if (deviceTypeKey==13) {  ///时序开关
            list.add("时序1路");list.add("时序2路");list.add("时序3路");list.add("时序4路");list.add("时序5路");list.add("时序6路");list.add("时序7路");list.add("时序8路");
        }else if (deviceTypeKey==20) {    ///快捷控制
            list.addAll(RoomFactory.getInstance().getRoomName(1));
        }
        return list;
    }

    public List<String> getFunctionParamString(int deviceTypeKey){
        List<String> list=new ArrayList <String>();
        list.add("");
       if(deviceTypeKey==4){  ///空调
           list.add("温度太高");list.add("温度太低");list.add("温度");
        }
        else if (deviceTypeKey==5) {  ///电视
           list.add("音量加");list.add("音量减");list.add("音量大一点");list.add("音量小一点");list.add("音量");
        }
        else if (deviceTypeKey==7) {  ///调光
           list.add("场景");list.add("最亮");list.add("最暗");
        }
        else if (deviceTypeKey==8) {   ///音乐
           list.add("音量加");list.add("音量减");list.add("音量大一点");list.add("音量小一点");list.add("音量");
        }else if (deviceTypeKey==20) {    ///快捷控制
            list.add("灯光全开");list.add("灯光全关");list.add("设备全开");list.add("设备全关");
            list.add("设置在家安防");list.add("设置离家安防");list.add("设置睡眠安防");
            list.add("假如在家安防");list.add("假如离家安防");list.add("假如睡眠安防");
            list.add("安防解除");list.add("条件不成立");list.add("逻辑结束");
        }
        return list;
    }
}
