package com.zunder.smart.gateway.bean;

import java.io.Serializable;

public class SceneModel
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String alias;
  private int bound;
  private String name;
  private int number;
  private int scene_type;
  private int sit;
  private int tag;
  private int type;
  private int used;

  public String getAlias()
  {
    return this.alias;
  }

  public int getBound()
  {
    return this.bound;
  }

  public String getName()
  {
    return this.name;
  }

  public int getNumber()
  {
    return this.number;
  }

  public int getScene_type()
  {
    return this.scene_type;
  }

  public int getSit()
  {
    return this.sit;
  }

  public int getTag()
  {
    return this.tag;
  }

  public int getType()
  {
    return this.type;
  }

  public int getUsed()
  {
    return this.used;
  }

  public void setAlias(String paramString)
  {
    this.alias = paramString;
  }

  public void setBound(int paramInt)
  {
    this.bound = paramInt;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setNumber(int paramInt)
  {
    this.number = paramInt;
  }

  public void setScene_type(int paramInt)
  {
    this.scene_type = paramInt;
  }

  public void setSit(int paramInt)
  {
    this.sit = paramInt;
  }

  public void setTag(int paramInt)
  {
    this.tag = paramInt;
  }

  public void setType(int paramInt)
  {
    this.type = paramInt;
  }

  public void setUsed(int paramInt)
  {
    this.used = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.SceneModel
 * JD-Core Version:    0.6.2
 */