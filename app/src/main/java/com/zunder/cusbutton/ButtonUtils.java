package com.zunder.cusbutton;
//joe 所有子页面命令
import com.zunder.control.ButtonBean;
import com.zunder.control.ButtonInfo;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;

import java.util.ArrayList;
import java.util.List;

public class ButtonUtils {
    private volatile static ButtonUtils install;
    private List<RMCCustomButton> buttonBean=null;
    public static ButtonUtils getInstance() {
        if (null == install) {
            install = new ButtonUtils();
        }
        return install;
    }




    public List<RMCBean> getRMCBean1() {
        List<RMCBean>  buttonBean=new ArrayList <RMCBean>();
        buttonBean.add(new RMCBean(1,"按钮1", "",160,160,60,60));
        buttonBean.add(new RMCBean(2,"按钮2", "",160,160,300,60));
        buttonBean.add(new RMCBean(3,"按钮3", "",160,160,540,60));
        buttonBean.add(new RMCBean(4,"按钮4", "",160,160,780,60));
//        buttonBean.add(new RMCBean(5,"", Constants.CENTER1,150,150,1000,80));
        buttonBean.add(new RMCBean(6,"", Constants.CENTER2,160,160,1120,60));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER3,160,160,1360,60));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER4,160,160,1600,60));
//        buttonBean.add(new RMCBean(6,"", Constants.CENTER5,150,150,1750,80));

        buttonBean.add(new RMCBean(7,"按钮5", "",160,160,60,320));
        buttonBean.add(new RMCBean(8,"按钮6", "",160,160,300,320));
        buttonBean.add(new RMCBean(9,"按钮7", "",160,160,540,320));
        buttonBean.add(new RMCBean(10,"按钮8", "",160,160,780,320));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER6,160,160,1120,320));
        buttonBean.add(new RMCBean(6,"", Constants.CENTER7,160,160,1360,320));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER8,160,160,1600,320));
        
        buttonBean.add(new RMCBean(13,"按钮9", "",160,160,60,580));
        buttonBean.add(new RMCBean(14,"按钮10", "",160,160,300,580));
        buttonBean.add(new RMCBean(15,"按钮11", "",160,160,540,580));
        buttonBean.add(new RMCBean(16,"按钮12", "",160,160,780,580));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER9,160,160,1120,580));
        buttonBean.add(new RMCBean(6,"", Constants.CENTER10,160,160,1360,580));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER11,160,160,1600,580));
        return  buttonBean;
    }

    public List<RMCBean> getRMCBean2() {
        List<RMCBean>  buttonBean=new ArrayList <RMCBean>();
        buttonBean.add(new RMCBean(1,"按钮1", "",160,160,60,60));
        buttonBean.add(new RMCBean(2,"按钮2", "",160,160,300,60));
        buttonBean.add(new RMCBean(3,"按钮3", "",160,160,540,60));
        buttonBean.add(new RMCBean(4,"按钮4", "",160,160,780,60));
        buttonBean.add(new RMCBean(6,"按钮5", "",120,120,1180,60));
        buttonBean.add(new RMCBean(5,"按钮6", "",120,120,1350,60));
        buttonBean.add(new RMCBean(6,"按钮7", "",120,120,1520,60));



        buttonBean.add(new RMCBean(7,"按钮8", "",160,160,60,320));
        buttonBean.add(new RMCBean(8,"按钮9", "",160,160,300,320));
        buttonBean.add(new RMCBean(9,"按钮10", "",160,160,540,320));
        buttonBean.add(new RMCBean(10,"按钮11", "",160,160,780,320));
        buttonBean.add(new RMCBean(6,"按钮12", "",120,120,1180,260));
        buttonBean.add(new RMCBean(5,"按钮13", "",120,120,1350,260));
        buttonBean.add(new RMCBean(6,"按钮14", "",120,120,1520,260));


        buttonBean.add(new RMCBean(13,"按钮15", "",160,160,60,580));
        buttonBean.add(new RMCBean(14,"按钮16", "",160,160,300,580));
        buttonBean.add(new RMCBean(15,"按钮17", "",160,160,540,580));
        buttonBean.add(new RMCBean(16,"按钮18", "",160,160,780,580));

        buttonBean.add(new RMCBean(5,"", Constants.CENTER12,420,420,1180,420));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER13,134,420,1180,420));
        buttonBean.add(new RMCBean(6,"", Constants.CENTER14,156,132,1314,420));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER15,156,156,1314,552));
        buttonBean.add(new RMCBean(6,"", Constants.CENTER16,156,132,1314,707));
        buttonBean.add(new RMCBean(5,"", Constants.CENTER17,134,420,1470,420));
        return  buttonBean;
    }
    public List<RMCBean> getRMCBean3() {
        List<RMCBean>  buttonBean=new ArrayList <RMCBean>();
        buttonBean.add(new RMCBean(1,"按钮1", "",270,200,60,60));
        buttonBean.add(new RMCBean(2,"按钮2", "",270,200,360,60));
        buttonBean.add(new RMCBean(3,"按钮3", "",270,200,660,60));
        buttonBean.add(new RMCBean(4,"按钮4", "",270,200,960,60));
        buttonBean.add(new RMCBean(5,"按钮5", "",270,200,1260,60));
        buttonBean.add(new RMCBean(6,"按钮6", "",270,200,1560,60));

        buttonBean.add(new RMCBean(7,"按钮7", "",270,200,60,320));
        buttonBean.add(new RMCBean(8,"按钮8", "",270,200,360,320));
        buttonBean.add(new RMCBean(9,"按钮9", "",270,200,660,320));
        buttonBean.add(new RMCBean(10,"按钮10", "",270,200,960,320));
        buttonBean.add(new RMCBean(11,"按钮11", "",270,200,1260,320));
        buttonBean.add(new RMCBean(12,"按钮12", "",270,200,1560,320));

        buttonBean.add(new RMCBean(13,"按钮13", "",270,200,60,580));
        buttonBean.add(new RMCBean(14,"按钮14", "",270,200,360,580));
        buttonBean.add(new RMCBean(15,"按钮15", "",270,200,660,580));
        buttonBean.add(new RMCBean(16,"按钮16", "",270,200,960,580));
        buttonBean.add(new RMCBean(17,"按钮17", "",270,200,1260,580));
        buttonBean.add(new RMCBean(18,"按钮18", "",270,200,1560,580));
        return  buttonBean;
    }
}
