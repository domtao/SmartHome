package com.zunder.smart.gateway.bean;

public class FtpModel
{
  private String dir;
  private int mode;
  private String passwd;
  private int port;
  private String server;
  private int upload_interval;
  private String user;

  public String getDir()
  {
    return this.dir;
  }

  public int getMode()
  {
    return this.mode;
  }

  public String getPasswd()
  {
    return this.passwd;
  }

  public int getPort()
  {
    return this.port;
  }

  public String getServer()
  {
    return this.server;
  }

  public int getUpload_interval()
  {
    return this.upload_interval;
  }

  public String getUser()
  {
    return this.user;
  }

  public void setDir(String paramString)
  {
    this.dir = paramString;
  }

  public void setMode(int paramInt)
  {
    this.mode = paramInt;
  }

  public void setPasswd(String paramString)
  {
    this.passwd = paramString;
  }

  public void setPort(int paramInt)
  {
    this.port = paramInt;
  }

  public void setServer(String paramString)
  {
    this.server = paramString;
  }

  public void setUpload_interval(int paramInt)
  {
    this.upload_interval = paramInt;
  }

  public void setUser(String paramString)
  {
    this.user = paramString;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.FtpModel
 * JD-Core Version:    0.6.2
 */