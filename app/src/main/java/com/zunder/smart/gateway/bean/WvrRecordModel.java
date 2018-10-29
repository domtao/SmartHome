package com.zunder.smart.gateway.bean;

public class WvrRecordModel
{
  private int alarmType;
  private int channel;
  private String endtime;
  private String fileName;
  private String name;
  private int size;
  private String starttime;

  public int getAlarmType()
  {
    return this.alarmType;
  }

  public int getChannel()
  {
    return this.channel;
  }

  public String getEndtime()
  {
    return this.endtime;
  }

  public String getFileName()
  {
    return this.fileName;
  }

  public String getName()
  {
    return this.name;
  }

  public int getSize()
  {
    return this.size;
  }

  public String getStarttime()
  {
    return this.starttime;
  }

  public void setAlarmType(int paramInt)
  {
    this.alarmType = paramInt;
  }

  public void setChannel(int paramInt)
  {
    this.channel = paramInt;
  }

  public void setEndtime(String paramString)
  {
    this.endtime = paramString;
  }

  public void setFileName(String paramString)
  {
    this.fileName = paramString;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setSize(int paramInt)
  {
    this.size = paramInt;
  }

  public void setStarttime(String paramString)
  {
    this.starttime = paramString;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.WvrRecordModel
 * JD-Core Version:    0.6.2
 */