package com.zunder.smart.gateway.bean;

public class RoomModel
{
  private int audioStatus;
  private int channel;
  private boolean isOpen;
  private String name;
  private int status1;
  private int status2;
  private int status3;
  private int status4;

  public int getAudioStatus()
  {
    return this.audioStatus;
  }

  public int getChannel()
  {
    return this.channel;
  }

  public String getName()
  {
    return this.name;
  }

  public int getStatus1()
  {
    return this.status1;
  }

  public int getStatus2()
  {
    return this.status2;
  }

  public int getStatus3()
  {
    return this.status3;
  }

  public int getStatus4()
  {
    return this.status4;
  }

  public boolean isOpen()
  {
    return this.isOpen;
  }

  public void setAudioStatus(int paramInt)
  {
    this.audioStatus = paramInt;
  }

  public void setChannel(int paramInt)
  {
    this.channel = paramInt;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setOpen(boolean paramBoolean)
  {
    this.isOpen = paramBoolean;
  }

  public void setStatus1(int paramInt)
  {
    this.status1 = paramInt;
  }

  public void setStatus2(int paramInt)
  {
    this.status2 = paramInt;
  }

  public void setStatus3(int paramInt)
  {
    this.status3 = paramInt;
  }

  public void setStatus4(int paramInt)
  {
    this.status4 = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.RoomModel
 * JD-Core Version:    0.6.2
 */