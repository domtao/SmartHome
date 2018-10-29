package com.zunder.smart.gateway.bean;

import android.graphics.Bitmap;
import java.util.HashMap;
import java.util.Map;

public class TerminalModel
{
  private int audioStatus;
  private Map<Integer, Object> data = new HashMap();
  private Map<Integer, Object> data1 = new HashMap();
  private Bitmap icon;
  private int index;
  private boolean isShowing = false;
  private String name;
  private int size;

  public int getAudioStatus()
  {
    return this.audioStatus;
  }

  public Map<Integer, Object> getData()
  {
    return this.data;
  }

  public Map<Integer, Object> getData1()
  {
    return this.data1;
  }

  public Bitmap getIcon()
  {
    return this.icon;
  }

  public int getIndex()
  {
    return this.index;
  }

  public String getName()
  {
    return this.name;
  }

  public int getSize()
  {
    return this.size;
  }

  public boolean isShowing()
  {
    return this.isShowing;
  }

  public void setAudioStatus(int paramInt)
  {
    this.audioStatus = paramInt;
  }

  public void setData(Map<Integer, Object> paramMap)
  {
    this.data = paramMap;
  }

  public void setData1(Map<Integer, Object> paramMap)
  {
    this.data1 = paramMap;
  }

  public void setIcon(Bitmap paramBitmap)
  {
    this.icon = paramBitmap;
  }

  public void setIndex(int paramInt)
  {
    this.index = paramInt;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setShowing(boolean paramBoolean)
  {
    this.isShowing = paramBoolean;
  }

  public void setSize(int paramInt)
  {
    this.size = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.TerminalModel
 * JD-Core Version:    0.6.2
 */