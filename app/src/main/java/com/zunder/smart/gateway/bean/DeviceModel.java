package com.zunder.smart.gateway.bean;

import android.text.TextUtils;
import java.io.Serializable;

public class DeviceModel
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String devID = "";
  private String devIP = "";
  private String devMac = "";
  private String devName = "";
  private int deviceType;
  private int id;
  private String password = "";
  private int port;
  private String username = "";

  public String getDevID()
  {
    return this.devID;
  }

  public String getDevIP()
  {
    return this.devIP;
  }

  public String getDevMac()
  {
    return this.devMac;
  }

  public String getDevName()
  {
    return this.devName;
  }

  public int getProductsCode()
  {
    return this.deviceType;
  }

  public int getId()
  {
    return this.id;
  }

  public String getPassword()
  {
    return this.password;
  }

  public int getPort()
  {
    return this.port;
  }

  public String getUsername()
  {
    return this.username;
  }

  public void setDevID(String paramString)
  {
    this.devID = paramString;
  }

  public void setDevIP(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      this.devIP = "";
      return;
    }
    this.devIP = paramString;
  }

  public void setDevMac(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      this.devMac = "";
      return;
    }
    this.devMac = paramString;
  }

  public void setDevName(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = "";
    this.devName = paramString;
  }

  public void setDeviceType(int paramInt)
  {
    this.deviceType = paramInt;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public void setPassword(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = "";
    this.password = paramString;
  }

  public void setPort(int paramInt)
  {
    this.port = paramInt;
  }

  public void setUsername(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = "";
    this.username = paramString;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.DeviceModel
 * JD-Core Version:    0.6.2
 */