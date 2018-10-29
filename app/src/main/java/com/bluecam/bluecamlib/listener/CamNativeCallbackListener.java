/**
 * 
 */
package com.bluecam.bluecamlib.listener;

public interface CamNativeCallbackListener
{
	/**
	 * 从摄像机那抓拍回调接口
	 * @param camHandler 设备句柄
	 * @param buff       图片流
	 * @param len        图片大小
	 */
	public void onSnapImageCallBack(long camHandler, byte[] buff, int len);

	/**
	 * TF卡图片下载回调接口
	 * @param camHandler 设备句柄
	 * @param buff       图片流
	 * @param len        图片大小
	 */
	public void onTFImageCallBack(long camHandler, byte[] buff, int len);

	/**
	 * 获取设备参数的结果回调接口
	 * @param camHandler  设备句柄
	 * @param paramKey    参数KEY值 参考CameraContants.ParamKey
	 * @param param       参数值（JSON格式）
	 */
	public void onGetCameraParamCallBack(long camHandler, long paramKey, String param);

	/**
	 * 设置设备参数的结果的回调接口
	 * @param camHandler  设备句柄
	 * @param paramKey    参数KEY值 参考CameraContants.ParamKey
	 * @param nResult     返回1表示成功，0表示失败
	 */
	public void onSetCameraParamCallBack(long camHandler, long paramKey, int nResult);

	/**
	 * 设备状态变化回调接口
	 * @param camHandler 设备句柄
	 * @param status     设备状态
	 */
	public void onCameraStatusEvent(long camHandler, long status);

	/**
	 * 音频播放回调接口
	 * @param camHandler 设备句柄
	 * @param pcm        音频Buf
	 * @param size       音频大小
	 */
	public void onAudioDataCallBack(long camHandler, byte[] pcm, int size);

	/**
	 * 查询TF卡录像文件列表结果的回调接口
	 * @param camHandler   设备句柄
	 * @param filecount    文件总量
	 * @param fname        文件名称
	 * @param strDate      文件日期时间
	 * @param size         文件总大小
	 */
	public void onRecordFileListCallBack(long camHandler, int filecount, String fname, String strDate, int size);

	/**
	 * P2P 类型
	 * @param camHandler 设备句柄
	 * @param nType      类型
	 */
	public void onP2PTypeCallBack(long camHandler, int nType);

	/**
	 * TF卡录像回放的当前位置
	 * @param camHandler        设备句柄
	 * @param position          当前位置
	 */
	public void onRecordPositionCallBack(long camHandler, int position);

	/**
	 * 暂时不用
	 * @param camHandler
	 * @param data
	 * @param type
	 * @param size
	 */
	public void onVideoDataCallBack(long camHandler, byte[] data, int type, int size);

	/**
	 * 报警推送回调接口
	 * @param camHandler  设备句柄
	 * @param nType       报警类型
	 */
	public void onAlarmPushCallBack(long camHandler, int nType);

	/**
	 * 搜索结果回调接口
	 * @param cameraInfo  设备信息（JSON格式）
	 */
	public void onSearchCameraCallBack(String cameraInfo);
}
