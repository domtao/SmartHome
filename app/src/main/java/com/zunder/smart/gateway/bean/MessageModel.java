package com.zunder.smart.gateway.bean;

import android.graphics.Bitmap;

public class MessageModel
{
  private int alarmType;
  private Bitmap bitmap;
  private int channel;
  private String deviceName;
  private int deviceType;
  private String did;
  private int id;
  private boolean isSelected = false;
  private String message;
  private int msgType;
  private String path;
  private long time;
  private int videoSize = 0;
  private int videoType = 0;

  public int getAlarmType()
  {
    return this.alarmType;
  }

  public Bitmap getBitmap()
  {
    return this.bitmap;
  }

  public int getChannel()
  {
    return this.channel;
  }

  public String getDeviceName()
  {
    return this.deviceName;
  }

  public int getProductsCode()
  {
    return this.deviceType;
  }

  public String getDid()
  {
    return this.did;
  }

  public int getId()
  {
    return this.id;
  }

  public String getMessage()
  {
    return this.message;
  }

  public int getMsgType()
  {
    return this.msgType;
  }

  public String getPath()
  {
    return this.path;
  }

  public long getTime()
  {
    return this.time;
  }

  public int getVideoSize()
  {
    return this.videoSize;
  }

  public int getVideoType()
  {
    return this.videoType;
  }

  public boolean isSelected()
  {
    return this.isSelected;
  }

  public void setAlarmType(int paramInt)
  {
    this.alarmType = paramInt;
  }

  public void setBitmap(Bitmap paramBitmap)
  {
    this.bitmap = paramBitmap;
  }

  public void setChannel(int paramInt)
  {
    this.channel = paramInt;
  }

  public void setDeviceName(String paramString)
  {
    this.deviceName = paramString;
  }

  public void setDeviceType(int paramInt)
  {
    this.deviceType = paramInt;
  }

  public void setDid(String paramString)
  {
    this.did = paramString;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public void setMessage(String paramString)
  {
    this.message = paramString;
  }

  public void setMsgType(int paramInt)
  {
    this.msgType = paramInt;
  }

  public void setPath(String paramString)
  {
    this.path = paramString;
  }

  public void setSelected(boolean paramBoolean)
  {
    this.isSelected = paramBoolean;
  }

  public void setTime(long paramLong)
  {
    this.time = paramLong;
  }

  public void setVideoSize(int paramInt)
  {
    this.videoSize = paramInt;
  }

  public void setVideoType(int paramInt)
  {
    this.videoType = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.MessageModel
 * JD-Core Version:    0.6.2
 */