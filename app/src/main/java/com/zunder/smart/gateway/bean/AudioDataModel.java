package com.zunder.smart.gateway.bean;

public class AudioDataModel
{
  private byte[] data;
  private int length;
  private int startcode;

  public byte[] getData()
  {
    return this.data;
  }

  public int getLength()
  {
    return this.length;
  }

  public int getStartcode()
  {
    return this.startcode;
  }

  public void setData(byte[] paramArrayOfByte)
  {
    this.data = paramArrayOfByte;
  }

  public void setLength(int paramInt)
  {
    this.length = paramInt;
  }

  public void setStartcode(int paramInt)
  {
    this.startcode = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.AudioDataModel
 * JD-Core Version:    0.6.2
 */