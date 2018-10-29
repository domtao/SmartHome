package com.zunder.smart.gateway.bean;

public class ApModel
{
  //private static final long serialVersionUID = 1L;
  private String apSharekey;
  private int apchannel;
  private int apenable;
  private int apencrypt;
  private String apmask;
  private String apssid;
  private String endIp;
  private String ipAddr;
  private String startIp;

  public static long getSerialversionuid()
  {
    return 1L;
  }

  public String getApSharekey()
  {
    return this.apSharekey;
  }

  public int getApchannel()
  {
    return this.apchannel;
  }

  public int getApenable()
  {
    return this.apenable;
  }

  public int getApencrypt()
  {
    return this.apencrypt;
  }

  public String getApmask()
  {
    return this.apmask;
  }

  public String getApssid()
  {
    return this.apssid;
  }

  public String getEndIp()
  {
    return this.endIp;
  }

  public String getIpAddr()
  {
    return this.ipAddr;
  }

  public String getStartIp()
  {
    return this.startIp;
  }

  public void setApSharekey(String paramString)
  {
    this.apSharekey = paramString;
  }

  public void setApchannel(int paramInt)
  {
    this.apchannel = paramInt;
  }

  public void setApenable(int paramInt)
  {
    this.apenable = paramInt;
  }

  public void setApencrypt(int paramInt)
  {
    this.apencrypt = paramInt;
  }

  public void setApmask(String paramString)
  {
    this.apmask = paramString;
  }

  public void setApssid(String paramString)
  {
    this.apssid = paramString;
  }

  public void setEndIp(String paramString)
  {
    this.endIp = paramString;
  }

  public void setIpAddr(String paramString)
  {
    this.ipAddr = paramString;
  }

  public void setStartIp(String paramString)
  {
    this.startIp = paramString;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.ApModel
 * JD-Core Version:    0.6.2
 */