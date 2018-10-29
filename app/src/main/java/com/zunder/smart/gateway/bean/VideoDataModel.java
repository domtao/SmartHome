package com.zunder.smart.gateway.bean;

public class VideoDataModel
{
  private byte[] data;
  private int size;
  private int type;

  public byte[] getData()
  {
    return this.data;
  }

  public int getSize()
  {
    return this.size;
  }

  public int getType()
  {
    return this.type;
  }

  public void setData(byte[] paramArrayOfByte)
  {
    this.data = paramArrayOfByte;
  }

  public void setSize(int paramInt)
  {
    this.size = paramInt;
  }

  public void setType(int paramInt)
  {
    this.type = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.VideoDataModel
 * JD-Core Version:    0.6.2
 */