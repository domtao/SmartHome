package com.zunder.smart.gateway.bean;

public class DateModel
{
  private int now;
  private int ntp_enable;
  private String ntp_ser;
  private int tz;

  public int getNow()
  {
    return this.now;
  }

  public int getNtp_enable()
  {
    return this.ntp_enable;
  }

  public String getNtp_ser()
  {
    return this.ntp_ser;
  }

  public int getTz()
  {
    return this.tz;
  }

  public void setNow(int paramInt)
  {
    this.now = paramInt;
  }

  public void setNtp_enable(int paramInt)
  {
    this.ntp_enable = paramInt;
  }

  public void setNtp_ser(String paramString)
  {
    this.ntp_ser = paramString;
  }

  public void setTz(int paramInt)
  {
    this.tz = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.DateModel
 * JD-Core Version:    0.6.2
 */