package com.zunder.smart.gateway.bean;

public class WvrSearchDeviceModel
{
  private String alias;
  private String deviceid;
  private int dhcp_enable;
  private String ipaddr;
  private boolean isChecked;
  private int keyValue;
  private String mac;
  private String password;
  private int port;
  private int streamid;
  private String username;

  public String getAlias()
  {
    return this.alias;
  }

  public String getDeviceid()
  {
    return this.deviceid;
  }

  public int getDhcp_enable()
  {
    return this.dhcp_enable;
  }

  public String getIpaddr()
  {
    return this.ipaddr;
  }

  public int getKeyValue()
  {
    return this.keyValue;
  }

  public String getMac()
  {
    return this.mac;
  }

  public String getPassword()
  {
    return this.password;
  }

  public int getPort()
  {
    return this.port;
  }

  public int getStreamid()
  {
    return this.streamid;
  }

  public String getUsername()
  {
    return this.username;
  }

  public boolean isChecked()
  {
    return this.isChecked;
  }

  public void setAlias(String paramString)
  {
    this.alias = paramString;
  }

  public void setChecked(boolean paramBoolean)
  {
    this.isChecked = paramBoolean;
  }

  public void setDeviceid(String paramString)
  {
    this.deviceid = paramString;
  }

  public void setDhcp_enable(int paramInt)
  {
    this.dhcp_enable = paramInt;
  }

  public void setIpaddr(String paramString)
  {
    this.ipaddr = paramString;
  }

  public void setKeyValue(int paramInt)
  {
    this.keyValue = paramInt;
  }

  public void setMac(String paramString)
  {
    this.mac = paramString;
  }

  public void setPassword(String paramString)
  {
    this.password = paramString;
  }

  public void setPort(int paramInt)
  {
    this.port = paramInt;
  }

  public void setStreamid(int paramInt)
  {
    this.streamid = paramInt;
  }

  public void setUsername(String paramString)
  {
    this.username = paramString;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.WvrSearchDeviceModel
 * JD-Core Version:    0.6.2
 */