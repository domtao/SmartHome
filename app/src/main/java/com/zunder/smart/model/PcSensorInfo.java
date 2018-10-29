package com.zunder.smart.model;

/**
 * Created by Administrator on 2018/1/16/016.
 */

public class PcSensorInfo {
    private int Id;
    private  String InsideTemp;
    private  String Humidity;
    private  String Pm25;
    private  String Formaldehyde;
    private  String Illumination;
    public PcSensorInfo(int Id,String InsideTemp,String Humidity,String Pm25,String Formaldehyde,String Illumination){
        this.Id=Id;
        this.InsideTemp=InsideTemp;
        this.Humidity=Humidity;
        this.Pm25=Pm25;
        this.Formaldehyde=Formaldehyde;
        this.Illumination=Illumination;
    }
    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getInsideTemp() {
        return InsideTemp;
    }

    public void setInsideTemp(String insideTemp) {
        InsideTemp = insideTemp;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }

    public String getPm25() {
        return Pm25;
    }

    public void setPm25(String pm25) {
        Pm25 = pm25;
    }

    public String getFormaldehyde() {
        return Formaldehyde;
    }

    public void setFormaldehyde(String formaldehyde) {
        Formaldehyde = formaldehyde;
    }

    public String getIllumination() {
        return Illumination;
    }

    public void setIllumination(String illumination) {
        Illumination = illumination;
    }
}
