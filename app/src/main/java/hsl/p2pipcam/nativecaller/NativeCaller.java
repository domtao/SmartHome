package hsl.p2pipcam.nativecaller;

public class NativeCaller {

	static {
		System.loadLibrary("PPPP_API");
		System.loadLibrary("IPCClientNetLib");
		System.loadLibrary("StreamPlayLib");
		System.loadLibrary("jni_ipc");
	}

	/**
	 * init server
	 */
	public native static int InitLib(String serv);

	public native static int UnInitLib();

	/**
	 * @param NetType
	 * @return
	 */
	public native static long CreateInstance(String strUsername, String strPwd,
			String strHost, int nPort, String StrDid, int NetType);

	public native static int DestroyInstance(long UserID);

	public native static int SetCallBack(Object UserContext);

	public native static int GetParam(long UserID, int nType);

	public native static int StartStream(long UserID, int StreamId,
			int subStreamId);

	public native static int StopStream(long UserID);

	public native static int SetRender(long UserID, Object render);

	/**
	 * cloudPan contrul 瀵瑰簲涔嬪墠鐨凱PPPPTZControl(strDID, 61);
	 */
	public native static int PtzControl(long UserID, int nType);

	/**
	 * set camera params 瀵瑰簲涔嬪墠鐨凱PPPCameraControl(strDID, 6, 10);
	 */
	public native static int SetParam(long UserID, int nType, String param);

	public native static int SetSearchCallBack(Object objSearch);

	/**
	 * 鍒濆鍖栨悳绱�
	 * 
	 * @return
	 */
	public native static int SearchDeviceInit();

	/**
	 * 鍙嶅垵濮嬪寲鎼滅储
	 * 
	 * @return
	 */
	public native static int SearchDeviceUninit();

	/**
	 * 寮�濮嬫悳绱�
	 * 
	 * @return
	 */
	public native static int SearchDevice();

	/**
	 * 
	 * @param UserID
	 * @param AudioId
	 * @return
	 */
	public native static int StartAudio(long UserID, int AudioId);

	/**
	 * @param UserID
	 * @return
	 */
	public native static int StopAudio(long UserID);

	/**
	 * 鍙戦�佽闊虫暟鎹埌搴曞眰
	 * 
	 * @param nUserID
	 * @param data
	 * @param size
	 * @return
	 */
	public native static int StartTalk(long nUserID);

	//
	public native static int StopTalk(long nUserID);

	public native static int SendTalkData(long nUserID, byte[] data, int size);

	//
	public native static int YUV420ToRGB565(byte[] yuv, byte[] rgb, int width,
			int height);

	public native static int SearchRecordFile(long nUserID, int bYear,
			int bMon, int bDay, int bHour, int bMin, int bSec, int eYear,
			int eMon, int eDay, int eHour, int eMin, int eSec);

	public native static int RecordPlayControl(long nUserID, String filename,
			int pos);// 姝ゆ帴鍙ｅ仠姝娇鐢�

	// 寮�濮嬫挱鏀�
	public native static int StartPlayRecord(long nUserID, String filename,
			int pos);

	// 缁撴潫鎾斁
	public native static int StopPlayRecord(long nUserID, String filename);

	// 璋冩暣鎾斁浣嶇疆
	public native static int PlayRecordPos(long nUserID, String filename,
			int pos);

	// 鏆傚仠鎾斁
	public native static int PausePlayRecord(long nUserID, String filename);

	// 璁剧疆褰曞儚鏁版嵁娓叉煋瀵硅薄
	public native static int SetRecordRender(long nUserID, Object render);

	public native static int Start(long UserID);

	public native static int Stop(long UserID);

	// 妫�鏌ョ綉缁�
	public native static int NetworkDetect();

	public native static int CapturePicture(long userid, String filename);

	public native static int StartRecord(long userid, String filename,
			int width, int height, int framerate);

	public native static int StopRecord(long userid);

	public native static int TransmitFileToDevice(long userid, String param);

	public native static int DownloadFile(long userid, String srcFilename,
			String dstfilename);

	public native static int StopDownloadFile(long userid);

	public native static int GetDownloadPos(long userid);

	public native static int GetParamEx(long UserID, int nType, String param);

}
