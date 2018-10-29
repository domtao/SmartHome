package com.zunder.smart.gateway.bean;

public class MailModel
{
  private String receiver1;
  private String receiver2;
  private String receiver3;
  private String receiver4;
  private String sender;
  private String smtpPasswd;
  private int smtpPort;
  private String smtpServer;
  private int smtpUpload;
  private String smtpUser;
  private int ssl;

  public String getReceiver1()
  {
    return this.receiver1;
  }

  public String getReceiver2()
  {
    return this.receiver2;
  }

  public String getReceiver3()
  {
    return this.receiver3;
  }

  public String getReceiver4()
  {
    return this.receiver4;
  }

  public String getSender()
  {
    return this.sender;
  }

  public String getSmtpPasswd()
  {
    return this.smtpPasswd;
  }

  public int getSmtpPort()
  {
    return this.smtpPort;
  }

  public String getSmtpServer()
  {
    return this.smtpServer;
  }

  public int getSmtpUpload()
  {
    return this.smtpUpload;
  }

  public String getSmtpUser()
  {
    return this.smtpUser;
  }

  public int getSsl()
  {
    return this.ssl;
  }

  public void setReceiver1(String paramString)
  {
    this.receiver1 = paramString;
  }

  public void setReceiver2(String paramString)
  {
    this.receiver2 = paramString;
  }

  public void setReceiver3(String paramString)
  {
    this.receiver3 = paramString;
  }

  public void setReceiver4(String paramString)
  {
    this.receiver4 = paramString;
  }

  public void setSender(String paramString)
  {
    this.sender = paramString;
  }

  public void setSmtpPasswd(String paramString)
  {
    this.smtpPasswd = paramString;
  }

  public void setSmtpPort(int paramInt)
  {
    this.smtpPort = paramInt;
  }

  public void setSmtpServer(String paramString)
  {
    this.smtpServer = paramString;
  }

  public void setSmtpUpload(int paramInt)
  {
    this.smtpUpload = paramInt;
  }

  public void setSmtpUser(String paramString)
  {
    this.smtpUser = paramString;
  }

  public void setSsl(int paramInt)
  {
    this.ssl = paramInt;
  }
}

/* Location:           E:\apktool\classes_dex2jar.jar
 * Qualified Name:     hsl.p2pipcam.bean.MailModel
 * JD-Core Version:    0.6.2
 */