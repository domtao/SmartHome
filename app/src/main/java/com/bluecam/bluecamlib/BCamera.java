package com.bluecam.bluecamlib;

import java.io.Serializable;


public abstract class BCamera implements Serializable {
	//摄像机信息
	protected CameraBean cameraBean;
	//设备状态
	protected int        status;
	//设备句柄
	protected long       camHandler;
	//p2p模式
	protected int        p2pMode = 0;


	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCamHandler() {
		return camHandler;
	}
	public void setCamHandler(long camHandler) {
		this.camHandler = camHandler;
	}
	public CameraBean getCameraBean() {
		return cameraBean;
	}
	public void setCameraBean(CameraBean cameraBean) {
		this.cameraBean = cameraBean;
	}

	public int getP2pMode() {
		return p2pMode;
	}

	public void setP2pMode(int p2pMode) {
		this.p2pMode = p2pMode;
	}

	/**
	 * 创建摄像机
	 */
	public abstract void createCamera();

	/**
	 * 销毁摄像机
	 */
	public abstract void destoryCamera();

	/**
	 * 连接摄像机
	 */
	public abstract void connectCamera();

	/**
	 * 获取参数 具体参考CameraContants.ParamKey类里的参数
	 * @param paramKey key值
	 */
	public abstract void getCameraParam(int paramKey);

	/**
	 * 设置参数 具体参考CameraContants.ParamKey类里的参数
	 * @param paramKey key值
	 * @param param    json串
	 */
	public abstract void setCameraParam(int paramKey,String param);

	/**
	 *  设置参数 具体参考CameraContants.ParamKey类里的参数
	 * @param param key值
	 * @param value 数值
	 */
	public abstract void setCameraParam(int param,int value);

	/**
	 * 播放视频流
	 * @param resolution 分辨率  0高清， 1标清， 2普通
	 *
	 */
	public abstract void playStream(int resolution);

	/**
	 * 关闭视频流
	 */
	public abstract void closeStream();

	/**
	 * 设置 视频流回调
	 * @param streamCallBack
	 */
	public abstract void setStreamCallBack(Object streamCallBack);

	/**
	 * 打开音频通道
	 */
	public abstract void openAudio();

	/**
	 * 关闭音频通道
	 */
	public abstract void closeAudio();

	/**
	 * 看视频的时候拍照
	 * @param path 要保存的路径
	 */
	public abstract void takePicture(String path);

	/**
	 * 看视频的时候录像开始
	 * @param path  要保存的路径
	 * @param width 视频的宽度
	 * @param higth 视频的高度
	 */
	public abstract void startRecordVideo(String path, int width, int higth);

	/**
	 * 结束视频录制
	 */
	public abstract void stopRecordVideo();

	/**
	 * 打开说话通道
	 */
	public abstract void openTalk();

	/**
	 * 关闭说话通道
	 */
	public abstract void closeTalk();

	/**
	 * 发送说话数据
	 * @param data
	 * @param size
	 */
	public abstract void sendTalkData(byte[] data, int size);

	/**
	 * 获取TF卡录像列表
	 * @param bYear 开始年
	 * @param bMon  开始月
	 * @param bDay  开始日
	 * @param eYear 结束年
	 * @param eMon  结束月
	 * @param eDay  结束日
	 */
	public abstract void getTfVideoList(int bYear, int bMon, int bDay,int eYear, int eMon, int eDay);

	/**
	 * 打开录像文件进行观看
	 * @param fileName 文件名
	 * @param pos     视频位置，默认为0
	 */
	public abstract void openTfRecord(String fileName, int pos);

	/**
	 * 关闭TF视频流
	 * @param fileName 文件名
	 */
	public abstract void closeTfRecord(String fileName);

	/**
	 * 设置录像视频流回调
	 * @param recordCallBack
	 */
	public abstract void setRecordCallBack(Object recordCallBack);

	/**
	 * 可以任意播放录像视频的位置
	 * @param fileName
	 * @param pos
	 */
	public abstract void playTfPos(String fileName, int pos);
	/**
	 * 控制摄像机接口
	 * @param cmd 控制指令
	 */
	public abstract void controlCamera(int cmd);

	/**
	 * 下载录像文件成mp4
	 * @param srcFilename 源文件
	 * @param dstfilename 保存的目标文件
	 */
	public abstract void downloadFile(String srcFilename, String dstfilename);

	/**
	 * 获取当前下载位置
	 * @return 返回当前位置
	 */
	public abstract int getDownloadPos();

	/**
	 * 停止下载
	 */
	public abstract void stopDownloadFile();
}
