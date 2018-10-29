package com.zunder.smart.gateway.bean;

import java.io.Serializable;

public class SitModel
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int alarm_bound;
  private String alias;
  private int code;
  private int key;
  private int link_channel;
  private int ptz;
  private int sit;
  private String str_key;
  private int type;

  public int getAlarm_bound()
  {
    return this.alarm_bound;
  }

  public String getAlias()
  {
    return this.alias;
  }

  public int getCode()
  {
    return this.code;
  }

  public int getKey()
  {
    return this.key;
  }

  public int getLink_channel()
  {
    return this.link_channel;
  }

  public int getPtz()
  {
    return this.ptz;
  }

  public int getSit()
  {
    return this.sit;
  }

  public String getStr_key()
  {
    return this.str_key;
  }

  public int getType()
  {
    return this.type;
  }

  public void setAlarm_bound(int paramInt)
  {
    this.alarm_bound = paramInt;
  }

  public void setAlias(String paramString)
  {
    this.alias = paramString;
  }

  public void setCode(int paramInt)
  {
    this.code = paramInt;
  }

  public void setKey(int paramInt)
  {
    this.key = paramInt;
  }

  public void setLink_channel(int paramInt)
  {
    this.link_channel = paramInt;
  }

  public void setPtz(int paramInt)
  {
    this.ptz = paramInt;
  }

  public void setSit(int paramInt)
  {
    this.sit = paramInt;
  }

  public void setStr_key(String paramString)
  {
    this.str_key = paramString;
  }

  public void setType(int paramInt)
  {
    this.type = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.SitModel
 * JD-Core Version:    0.6.2
 */