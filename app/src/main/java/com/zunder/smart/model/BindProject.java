package com.zunder.smart.model;

public class BindProject {
    private String tent;
    private String source1;
    private String source2;
    private String source3;
    private String source4;
    private String centreControl;

    public String getTent() {
        if(tent==null||tent=="null"||tent.equals("null")){
            tent="";
        }
        return tent;
    }

    public void setTent(String tent) {
        this.tent = tent;
    }

    public String getSource1() {
        if(source1==null||source1=="null"||source1.equals("null")){
            source1="";
        }
        return source1;
    }

    public void setSource1(String source1) {
        this.source1 = source1;
    }

    public String getSource2() {
        if(source2==null||source2=="null"||source2.equals("null")){
            source2="";
        }
        return source2;
    }

    public void setSource2(String source2) {
        this.source2 = source2;
    }

    public String getSource3() {
        if(source3==null||source3=="null"||source3.equals("null")){
            source3="";
        }
        return source3;
    }

    public void setSource3(String source3) {
        this.source3 = source3;
    }

    public String getSource4() {
        if(source4==null||source4=="null"||source4.equals("null")){
            source4="";
        }
        return source4;
    }

    public void setSource4(String source4) {
        this.source4 = source4;
    }

    public String getCentreControl() {
        if(centreControl==null||centreControl=="null"||centreControl.equals("null")){
            centreControl="";
        }
        return centreControl;
    }

    public void setCentreControl(String centreControl) {
        this.centreControl = centreControl;
    }


}
