package com.zunder.smart.gateway.bean;

import java.io.Serializable;

public class SceneImageModel
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String name;
  private int scene_type;

  public String getName()
  {
    return this.name;
  }

  public int getScene_type()
  {
    return this.scene_type;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setScene_type(int paramInt)
  {
    this.scene_type = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.SceneImageModel
 * JD-Core Version:    0.6.2
 */