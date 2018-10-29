package hsl.p2pipcam.nativecaller;


public class DeviceSDK 
{
	/**
	 * ��ʼ��app
	 * @param serv
	 * @return
	 */
	public static int initialize(String serv)
	{
		return NativeCaller.InitLib(serv);
	}
	
	/**
	 * ����ǰ��ʼ��
	 * @return
	 */
	public static int initSearchDevice()
	{
		return NativeCaller.SearchDeviceInit();
	}
	
	/**
	 * ���������ص�����
	 * @param object
	 */
	public static void setSearchCallback(Object object)
	{
		NativeCaller.SetSearchCallBack(object);
	}
	
	/**
	 * ���ûص�����
	 * @param object
	 */
	public static void setCallback(Object object)
	{
		NativeCaller.SetCallBack(object);
	}
	
	/**
	 * �˳�����
	 * @return
	 */
	public static int unInitSearchDevice()
	{
		return NativeCaller.SearchDeviceUninit();
	}
	
	/**
	 * �����豸
	 * @return
	 */
	public static int searchDevice()
	{
		return NativeCaller.SearchDevice();
	}
	
	/**
	 * �����豸
	 * @param strUsername
	 * @param strPwd
	 * @param strHost
	 * @param nPort
	 * @param StrDid
	 * @param NetType 0=TCP ,1=P2P
	 * @return userid
	 */
	public static long createDevice(String strUsername, String strPwd,String strHost, int nPort, String StrDid, int NetType)
	{
		return NativeCaller.CreateInstance(strUsername, strPwd, strHost, nPort, StrDid, NetType);
	}
	
	/**
	 * �����豸
	 * @param UserID
	 * @return
	 */
	public static int destoryDevice(long UserID)
	{
		return NativeCaller.DestroyInstance(UserID);
	}
	
	/**
	 * �豸����
	 * @param userID
	 * @return
	 */
	public static int openDevice(long userID)
	{
		return NativeCaller.Start(userID);
	}
	
	/**
	 * �豸�ر�
	 * @param userID
	 * @return
	 */
	public static int closeDevice(long userID)
	{
		return NativeCaller.Stop(userID);
	}
	
	/**
	 * ��ȡ�豸����
	 * @param userID
	 * @param nType
	 * @return
	 */
	public static int getDeviceParam(long userID,int nType)
	{
		return NativeCaller.GetParam(userID, nType);
	}
	
	/**
	 * �����豸����
	 * @param userID
	 * @param nType
	 * @param param
	 * @return
	 */
	public static int setDeviceParam(long userID,int nType,String param)
	{
		return NativeCaller.SetParam(userID, nType, param);
	}
	
	/**
	 * ����ʵʱ��Ƶ��
	 * @param userID
	 * @param streamId
	 * @param subStreamId
	 * @return
	 */
	public static int startPlayStream(long userID, int streamId,int subStreamId)
	{
		return NativeCaller.StartStream(userID, streamId, subStreamId);
	}
	
	/**
	 * ����������Ƶ��
	 * @param userID
	 * @return
	 */
	public static int stopPlayStream(long userID)
	{
		return NativeCaller.StopStream(userID);
	}
	
	/**
	 * ���û���
	 * @param userID
	 * @param render
	 */
	public static void setRender(long userID, Object render)
	{
		NativeCaller.SetRender(userID, render);
	}
	
	/**
	 * �����豸
	 * @param UserID
	 * @param nType
	 * @return
	 */
	public static int ptzControl(long UserID, int nType)
	{
		return NativeCaller.PtzControl(UserID, nType);
	}
	
	/**
	 * ������Ƶ
	 * @param UserID
	 * @param AudioId
	 * @return
	 */
	public static int startPlayAudio(long UserID, int AudioId)
	{
		return NativeCaller.StartAudio(UserID, AudioId);
	}
	
	/**
	 * ������Ƶ����
	 * @param UserID
	 * @return
	 */
	public static int stopPlayAudio(long UserID)
	{
		return NativeCaller.StopAudio(UserID);
	}
	
	/**
	 * ��ʼ˵��
	 * @param nUserID
	 * @return
	 */
	public static int startTalk(long UserID)
	{
		return NativeCaller.StartTalk(UserID);
	}
	
	/**
	 * ����˵��
	 * @param UserID
	 * @return
	 */
	public static int stopTalk(long UserID)
	{
		return NativeCaller.StopTalk(UserID);
	}
	
	/**
	 * ����˵������
	 * @param nUserID
	 * @param data
	 * @param size
	 * @return
	 */
	public static int SendTalkData(long userID, byte[] data, int size)
	{
		return NativeCaller.SendTalkData(userID, data, size);
	}
	
	/**
	 * ͼƬ��ת��
	 * @param yuv
	 * @param rgb
	 * @param width
	 * @param height
	 * @return
	 */
	public static int YUV420ToRGB565(byte[] yuv, byte[] rgb, int width,int height)
	{
		return NativeCaller.YUV420ToRGB565(yuv, rgb, width, height);
	}
	
	/**
	 * ��ʼ¼��
	 * @param filename
	 * @param forcc
	 * @param height
	 * @param width
	 * @param framerate
	 * @return
	 */
	public static int startRecordVideo(String filename, String forcc, int height,int width, int framerate)
	{
		return object.p2pipcam.nativecaller.NativeCaller.OpenAvi(filename, forcc, height, width, framerate);
	}
	
	/**
	 * ¼��������д�뵽�ļ�
	 * @param data
	 * @param len
	 * @param keyframe
	 * @return
	 */
	public static int writeVideoData(byte[] data, int len, int keyframe)
	{
		return object.p2pipcam.nativecaller.NativeCaller.WriteData(data, len, keyframe);
	}
	
	/**
	 * ����¼��
	 * @return
	 */
	public static int stopRecordVideo()
	{
		return object.p2pipcam.nativecaller.NativeCaller.CloseAvi();
	}
	
	/**
	 * ��ȡԶ��¼��
	 * @param userid
	 * @return
	 */
	public static int getRecordVideo(long userid,int bYear, int bMon, int bDay,int eYear, int eMon, int eDay)
	{
		return NativeCaller.SearchRecordFile(userid, bYear, bMon, bDay, 0, 0, 0, eYear, eMon, eDay, 23, 59, 59);
	}
	
	/**
	 * ����¼�񲥷�Render
	 * @param nUserID
	 * @param render
	 * @return
	 */
	public static int setRecordRender(long nUserID, Object render)
	{
		return NativeCaller.SetRecordRender(nUserID, render);
	}
	
	/**
	 * ��ʼ����¼��
	 * @param nUserID
	 * @param filename
	 * @return
	 */
	public static int startPlayRecord(long nUserID, String filename,int pos)
	{
		return NativeCaller.StartPlayRecord(nUserID, filename,pos);
	}
	
	/**
	 * ��������¼��
	 * @param nUserID
	 * @param filename
	 * @return
	 */
	public static int stopPlayRecord(long nUserID, String filename)
	{
		return NativeCaller.StopPlayRecord(nUserID, filename);
	}
	/**
	 * ��������λ��
	 * @param nUserID
	 * @param filename
	 * @param pos
	 * @return
	 */
	public static int playRecordPos(long nUserID, String filename, int pos)
	{
		return NativeCaller.PlayRecordPos(nUserID, filename, pos);
	}
	
	/**
	 * ��ͣ����
	 * @param nUserID
	 * @param filename
	 * @return
	 */
	public static int pausePlayRecord(long nUserID, String filename)
	{
		return NativeCaller.PausePlayRecord(nUserID, filename);
	}
	

	public static void networkDetect()
	{
		NativeCaller.NetworkDetect();
	}

	public static int unInitialize()
	{
		return NativeCaller.UnInitLib();
	}

	public static int SearchDeviceInit() {
		// TODO Auto-generated method stub
		return NativeCaller.SearchDeviceInit();
	}
	public static int SetParam(int userid, int type, String s){
		return NativeCaller.SetParam(userid,type,s);
	}
}
