package com.zunder.control;
//joe 所有子页面命令
import com.zunder.smart.R;

import java.util.ArrayList;
import java.util.List;

public class SubButtonUtils {
    private volatile static SubButtonUtils install;
    private ButtonBean buttonBean=null;
    public static SubButtonUtils getInstance() {
        if (null == install) {
            install = new SubButtonUtils();
        }
        return install;
    }

//joe
    public ButtonBean getButtonBean(int deviceTypeKey){
        if(buttonBean==null){
            buttonBean=new ButtonBean();
        }
        if (deviceTypeKey==1) { ///灯光

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"离开","1分钟后关闭",0,0));
            list.add(new ButtonInfo(2,"阅读","打开1小时",0,0));
            list.add(new ButtonInfo(3,"会客","打开2小时",0,0));
            list.add(new ButtonInfo(4,"开启","打开",0,0));
            list.add(new ButtonInfo(5,"睡眠","打开1分钟",0,0));
            list.add(new ButtonInfo(6,"关闭","关闭",0,0));

            buttonBean.setDeviceTypeKey(1);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.light_off,R.mipmap.light_on} );
            buttonBean.setImageIndexs(new int[]{});

        }
        else if (deviceTypeKey==2) {  ///窗帘

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"25%","打开四分之一",0,0));
            list.add(new ButtonInfo(2,"50%","打开一半",0,0));
            list.add(new ButtonInfo(3,"75%","打开四分之三",0,0));
            list.add(new ButtonInfo(4,"开窗","打开",0,0));
            list.add(new ButtonInfo(5,"停止","停止",0,0));
            list.add(new ButtonInfo(6,"关窗","关闭",0,0));
            buttonBean.setDeviceTypeKey(2);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.cur_off,R.mipmap.cur_on} );
            buttonBean.setImageIndexs(new int[]{R.mipmap.cur_on,R.mipmap.cur_on,R.mipmap.cur_8,
                    R.mipmap.cur_7,R.mipmap.cur_6,
                    R.mipmap.cur_5,R.mipmap.cur_4,
                    R.mipmap.cur_3,R.mipmap.cur_2,
                    R.mipmap.cur_1,R.mipmap.cur_off});
        }
        else if (deviceTypeKey==3) {  ///插座


            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"煮开水","打开15分钟",0,0));
            list.add(new ButtonInfo(2,"煎中药","打开1小时",0,0));
            list.add(new ButtonInfo(3,"电热器","打开2小时",0,0));
            list.add(new ButtonInfo(4,"开启","打开",0,0));
            list.add(new ButtonInfo(5,"手机充电","打开3小时",0,0));
            list.add(new ButtonInfo(6,"关闭","关闭",0,0));

            buttonBean.setDeviceTypeKey(3);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.zun_chazuo_off,R.mipmap.zun_chazuo_on} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==4) {  ///空调

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"电源","互控",0,0));
            list.add(new ButtonInfo(2,"模式","模式切换",1,0));
            list.add(new ButtonInfo(3,"⬆","温度加一",0,0));
            list.add(new ButtonInfo(4,"⬇","温度减一",0,0));
            buttonBean.setDeviceTypeKey(4);
            buttonBean.setList(list);
            buttonBean.setIsPower(1);
            buttonBean.setState(new int[]{R.mipmap.air_off,R.mipmap.air_on} );
            buttonBean.setImageIndexs(new int[]{});
            buttonBean.setModeImage(new int[]{
                    R.mipmap.mode_auto_black, R.mipmap.mode_cold_black,
                    R.mipmap.mode_drying_black3, R.mipmap.mode_warm_black,
                    R.mipmap.mode_wind_black });
            buttonBean.setSpeedImage(new int[]{R.mipmap.wind_amount_white_one, R.mipmap.wind_amount_white_two,
                    R.mipmap.wind_amount_white_three, R.mipmap.wind_amount_white_four,
                    R.mipmap.wind_amount_white_five});
            buttonBean.setTempImage(new int[]{R.mipmap.sz_0,
                    R.mipmap.sz_1,R.mipmap.sz_2,R.mipmap.sz_3,
                    R.mipmap.sz_4,R.mipmap.sz_5,R.mipmap.sz_6
                    ,R.mipmap.sz_7,R.mipmap.sz_8,R.mipmap.sz_9
            });
        }
        else if (deviceTypeKey==5) {  ///电视
            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"电源","互控",0,0));
            list.add(new ButtonInfo(2,"静音","静音",0,0));
            list.add(new ButtonInfo(3,"⬆","频道加",0,0));
            list.add(new ButtonInfo(4,"⬇","频道加",0,0));
            buttonBean.setDeviceTypeKey(5);
            buttonBean.setList(list);
            buttonBean.setIsPower(1);
            buttonBean.setState(new int[]{R.mipmap.tv_off,R.mipmap.tv_on} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==6) {  ///红外
            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"开","打开",0,0));
            list.add(new ButtonInfo(2,"关","关闭",0,0));

            buttonBean.setDeviceTypeKey(6);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.red_off,R.mipmap.red_on} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==7) {  ///调光

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"电源","互控",0,0));
            list.add(new ButtonInfo(2,"中性光","中性光",0,0));
            list.add(new ButtonInfo(3,"白光","白光",0,0));
            list.add(new ButtonInfo(4,"暖光","暖光",0,0));
            buttonBean.setDeviceTypeKey(7);
            buttonBean.setList(list);
            buttonBean.setIsPower(1);
            buttonBean.setState(new int[]{R.mipmap.dimmer,R.mipmap.dimmer} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==8) {  ////音乐

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"电源","互控",0,0));
            list.add(new ButtonInfo(2,"静音","静音",0,0));
            list.add(new ButtonInfo(3,"暂停","暂停",0,0));
            list.add(new ButtonInfo(4,"播放","播放",0,0));
            buttonBean.setDeviceTypeKey(8);
            buttonBean.setList(list);
            buttonBean.setIsPower(1);
            buttonBean.setState(new int[]{R.mipmap.music_off,R.mipmap.music_on} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==9) {  ///场景

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"场景1","场景1",0,0));
            list.add(new ButtonInfo(2,"场景2","场景2",1,0));
            list.add(new ButtonInfo(3,"场景3","场景3",0,0));
            list.add(new ButtonInfo(4,"场景4","场景4",0,0));

            buttonBean.setDeviceTypeKey(9);
            buttonBean.setList(list);
            buttonBean.setIsPower(1);
            buttonBean.setState(new int[]{R.mipmap.sensor,R.mipmap.sensor} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==10) {  ///电子锁
            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"定时1","打开1小时",0,0));
            list.add(new ButtonInfo(2,"定时2","打开2小时",0,0));
            list.add(new ButtonInfo(3,"定时3","打开3小时",0,0));
            list.add(new ButtonInfo(4,"开启","打开",0,0));
            list.add(new ButtonInfo(5,"定时4","打开4小时",0,0));
            list.add(new ButtonInfo(6,"关闭","关闭",0,0));
            buttonBean.setDeviceTypeKey(10);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.locke_off,R.mipmap.locke_on} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==11) {  ///投影仪

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"电源","互控",0,0));
            list.add(new ButtonInfo(2,"信号源","信号源",0,0));
            buttonBean.setDeviceTypeKey(11);
            buttonBean.setList(list);
            buttonBean.setIsPower(1);
            buttonBean.setState(new int[]{R.mipmap.project_off,R.mipmap.project_off} );
            buttonBean.setImageIndexs(new int[]{});

        }
        else if (deviceTypeKey==12) {  ///智能空开

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"开","打开",0,0));
            list.add(new ButtonInfo(2,"关","关闭",0,0));
            buttonBean.setDeviceTypeKey(12);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.empty_bg,R.mipmap.empty_bg} );
            buttonBean.setImageIndexs(new int[]{R.mipmap.empty_bg});
        }
        else if (deviceTypeKey==13) {  ///时序开关

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"开","打开",0,0));
            list.add(new ButtonInfo(2,"关","关闭",0,0));

            buttonBean.setDeviceTypeKey(13);
            buttonBean.setList(list);
            buttonBean.setImageIndexs(new int[]{});
            buttonBean.setDeviceTypeKey(1);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.seq_off,R.mipmap.seq_on} );
            buttonBean.setImageIndexs(new int[]{R.mipmap.seq_bg});
        }
        else if (deviceTypeKey==14) {  ///新风系统
            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"电源","互控",0,0));
            list.add(new ButtonInfo(2,"模式","模式切换",0,0));
            list.add(new ButtonInfo(3,"⬆","温度加一",0,0));
            list.add(new ButtonInfo(4,"⬇","温度减一",0,0));
            buttonBean.setDeviceTypeKey(14);
            buttonBean.setList(list);
            buttonBean.setIsPower(1);
            buttonBean.setState(new int[]{R.mipmap.new_wind_off,R.mipmap.new_wind_on} );
            buttonBean.setImageIndexs(new int[]{});

        }
        else if (deviceTypeKey==15) {  ///家电
            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"定时1","打开1小时",0,0));
            list.add(new ButtonInfo(2,"定时2","打开2小时",0,0));
            list.add(new ButtonInfo(3,"定时3","打开3小时",0,0));
            list.add(new ButtonInfo(4,"开启","打开",0,0));
            list.add(new ButtonInfo(5,"定时4","打开4小时",0,0));
            list.add(new ButtonInfo(6,"关闭","关闭",0,0));
            buttonBean.setDeviceTypeKey(15);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.elec_off,R.mipmap.ele_no} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==16) {  ///智能传感

            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"定时1","打开1小时",0,0));
            list.add(new ButtonInfo(2,"定时2","打开2小时",0,0));
            list.add(new ButtonInfo(3,"定时3","打开3小时",0,0));
            list.add(new ButtonInfo(4,"开启","打开",0,0));
            list.add(new ButtonInfo(5,"定时4","打开4小时",0,0));
            list.add(new ButtonInfo(6,"关闭","关闭",0,0));

            buttonBean.setDeviceTypeKey(16);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.sensor_off,R.mipmap.sensor_on,R.mipmap.zun_sensor} );
            buttonBean.setImageIndexs(new int[]{});
        }
        else if (deviceTypeKey==17) {
            ///人员计数
            List<ButtonInfo> list=new ArrayList <ButtonInfo>();
            list.add(new ButtonInfo(1,"定时1","打开1小时",0,0));
            list.add(new ButtonInfo(2,"定时2","打开2小时",0,0));
            list.add(new ButtonInfo(3,"定时3","打开3小时",0,0));
            list.add(new ButtonInfo(4,"开启","打开",0,0));
            list.add(new ButtonInfo(5,"定时4","打开4小时",0,0));
            list.add(new ButtonInfo(6,"关闭","关闭",0,0));
            buttonBean.setDeviceTypeKey(17);
            buttonBean.setList(list);
            buttonBean.setIsPower(0);
            buttonBean.setState(new int[]{R.mipmap.zun_pe,R.mipmap.zun_pe} );
            buttonBean.setImageIndexs(new int[]{});
        }
        return  buttonBean;
    }
    public ButtonInfo getButtonInfo(List<ButtonInfo> list,int Id){
        ButtonInfo buttonInfo=null;
        if(list!=null&&list.size()>0){
            for (int i=0;i<list.size();i++){
                if(list.get(i).getId()==Id){
                    buttonInfo=list.get(i);
                    break;
                }
            }
        }
        return buttonInfo;
    }
}
