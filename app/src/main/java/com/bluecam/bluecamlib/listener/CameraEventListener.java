package com.bluecam.bluecamlib.listener;



import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.CameraBean;

/**
 * Created by Administrator on 2017/6/15.
 */

public interface CameraEventListener {
    /**
     * 监听获取参数结果
     * @param camHandler 设备句柄
     * @param paramKey   参数KEY
     * @param params     参数（JSON）
     */
    void onGetParamsEvent(long camHandler, long paramKey, String params);

    /**
     * 监听设置参数结果
     * @param camHandler 设备句柄
     * @param paramKey   参数KEY
     * @param nResult    1表示成功，0表示失败
     */
    void onSetParamsEvent(long camHandler, long paramKey, int nResult);

    /**
     * 监听设备状态变化
     * @param camHandler 设备句柄
     * @param status     设备状态
     */
    void onCameraStatusChangeEvent(long camHandler, int status);

    /**
     * 监听设备抓拍图片数据流
     * @param camHandler 设备句柄
     * @param imgBuf     图片流
     * @param len        图片长度
     */
    void onCameraSnapShotEvent(long camHandler, byte[] imgBuf, int len);

    /**
     *监听设备搜索结果
     * @param cameraBean 设备信息
     */
    void onSerchEvent(CameraBean cameraBean);

    /**
     * 监听添加设备结果
     * @param camera
     */
    void onCameraAddEvent(BCamera camera);

    /**
     * 监听音频数据结果
     * @param camHandler 设备句柄
     * @param pcm        音频流
     * @param size       音频大小
     */
    void onAudioDataEvent(long camHandler, byte[] pcm, int size);

    /**
     * 监听报警消息
     * @param camHandler  设备句柄
     * @param nType       参考 CameraContants.AlarmType
     */
    void onAlarmEvent(long camHandler, int nType);

    /**
     * 监听获取录像文件结果
     * @param camHandler  设备句柄
     * @param filecount   文件总数
     * @param fileName    文件名称
     * @param strDate     时间
     * @param size        大小
     */
    void onRecordFileList(long camHandler, int filecount, String fileName, String strDate, int size);

    /**
     * TF卡当前播放位置
     * @param camHandler 设备句柄
     * @param pos        当前位置
     */
    void onRecordPlayPos(long camHandler, int pos);
}
