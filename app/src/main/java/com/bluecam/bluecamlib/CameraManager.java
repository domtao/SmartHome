package com.bluecam.bluecamlib;

import android.content.Context;
import android.text.TextUtils;

import com.bluecam.bluecamlib.listener.CamNativeCallbackListener;
import com.bluecam.bluecamlib.listener.CameraEventListener;
import com.bluenet.api.DeviceAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import voice.encoder.VoicePlayer;

/**
 * Created by Administrator on 2017/7/25.
 */

public class CameraManager implements CamNativeCallbackListener {
    private Context context;
    private List<BCamera> cameraList = new ArrayList<BCamera>();
    private Vector<CameraEventListener> eventListeners = new Vector<CameraEventListener>();

    private static CameraManager instance;
    private static synchronized  void initDeviceManager(Context context)
    {
        if(instance == null) {
            instance = new CameraManager(context);
        }
    }
    public static CameraManager getDeviceManager(Context context)
    {
        if(instance == null)
        {
            initDeviceManager(context);
        }
        return instance;
    }

    public CameraManager(Context context)
    {
        this.context = context;

    }
    /**
     * 初始化块API
     */
    public void initialize()
    {
        DeviceAPI.initializeApiLib();
        DeviceAPI.registerCallBack(this);
        DeviceAPI.checkNet();
    }

    /**
     * 初始化搜索
     */
    public void initSearchCamera()
    {
        DeviceAPI.initSearchCamera();
        DeviceAPI.registerSearchCallBack(this);
    }

    public void searchCamera(){
        DeviceAPI.searchCamera();
    }
    /**
     *退出搜索
     */
    public void exitSearchCamera()
    {
        DeviceAPI.destorySearchCamera();
    }

    /**
     * 结束app
     */
    public void onDestory()
    {
        for(BCamera camera : cameraList)
        {
            camera.destoryCamera();
        }
        DeviceAPI.exitLib();
        cameraList.clear();
        instance=null;
    }

    //--------------------------------声波对码-------------------

    /**
     * 获取声波对面的实例
     * @return
     */
    public static VoicePlayer startVoicePlayer()
    {
        VoicePlayer player = new VoicePlayer();
        player.setFreqs(new int[]{4000,4200,4400,4600,4800,5000,5200,5400,5600,5800,6000,6200,6400,6600,6800,7000,7200,7400,7600});
        return player;
    }

    /**
     * 注册监听
     * @param listener
     */
    public void registerEventListener(CameraEventListener _listener)
    {
        synchronized (eventListeners)
        {
            eventListeners.add(_listener);
        }

    }
    public void remove(){
    for(BCamera camera : cameraList)
        {
//            camera.destoryCamera();
            DeviceAPI.destoryCamera(camera.getCamHandler());
        }
        cameraList.clear();
        eventListeners.clear();
    }
    /**
     * 取消监听
     * @param listener
     */
    public void unRegisterEventListener(CameraEventListener listener)
    {
        synchronized (eventListeners)
        {
            eventListeners.remove(listener);
        }
    }

    /**
     * 添加设备
     * @param camera
     */
    public void addCamera(BCamera camera)
    {
//        int result=0;
//        if(cameraList.size()>0){
//            for (int i=0;i<cameraList.size();i++){
//                if(camera.getCameraBean().getDevID().equals(cameraList.get(i).getCameraBean().getDevID())){
//                    for (CameraEventListener listener : eventListeners) {
//                        listener.onCameraAddEvent(cameraList.get(i));
//                    }
//                    result=1;
//                    break;
//                }
//            }
//        }
//        if(result==0) {
            camera.setStatus(CameraContants.DeviceStatus.DEVICE_STATUS_NOT_ON_LINE);
            camera.createCamera();
            cameraList.add(camera);
            for (CameraEventListener listener : eventListeners) {
                listener.onCameraAddEvent(camera);
            }
//        }
    }

    /**
     * 删除设备
     * @param camera
     */
    public void deleteCamera(BCamera camera)
    {
        camera.destoryCamera();

        int size = cameraList.size();
        for(int i=0;i<size;i++)
        {
            BCamera cam = cameraList.get(i);
            if(cam.getCameraBean().getDevID().equals(camera.getCameraBean().getDevID()))
            {
                cameraList.remove(cam);
                break;
            }
        }
    }

    /**
     * 获取设备
     * @param camHandler 句柄
     * @return
     */
    public BCamera getCamera(long camHandler)
    {
        for(BCamera camera:cameraList)
        {
            if(camera.getCamHandler() == camHandler)
            {
                return camera;
            }
        }
        return null;
    }

    /**
     * 获取设备
     * @param camID 设备ID
     * @return
     */
    public BCamera getCamera(String camID)
    {
        for(BCamera camera:cameraList)
        {
            if(camera.getCameraBean().getDevID().equals(camID))
            {
                return camera;
            }
        }
        return null;
    }

    /**
     * 获取设备列表
     * @return
     */
    public List<BCamera> getCameraList()
    {
        return cameraList;
    }



    @Override
    public void onSnapImageCallBack(long camHandler, byte[] buff, int len) {
        synchronized (eventListeners){
            for(CameraEventListener listener:eventListeners)
            {
                listener.onCameraSnapShotEvent(camHandler,buff,len);
            }
        }
    }

    @Override
    public void onTFImageCallBack(long camHandler, byte[] buff, int len) {

    }

    @Override
    public void onGetCameraParamCallBack(long camHandler, long paramKey, String param) {
        //LogUtil.printLog("CallBack_GetParam--------------->>param == "+param);
        synchronized (eventListeners){
            for(CameraEventListener listener:eventListeners)
            {
                listener.onGetParamsEvent(camHandler,paramKey,param);
            }
        }

    }

    @Override
    public void onSetCameraParamCallBack(long camHandler, long paramKey, int nResult) {
        synchronized (eventListeners){
            for(CameraEventListener listener:eventListeners)
            {
                listener.onSetParamsEvent(camHandler,paramKey,nResult);
            }
        }

    }

    @Override
    public void onCameraStatusEvent(long camHandler, long nType) {
        synchronized (eventListeners){
            int status = new Long(nType).intValue();
            for(BCamera camera : cameraList)
            {
                if (camera.getCamHandler() == camHandler)
                {
                    camera.setStatus(status);
                }
            }

            for(CameraEventListener listener:eventListeners)
            {
                listener.onCameraStatusChangeEvent(camHandler,status);
            }
        }
    }

    @Override
    public void onAudioDataCallBack(long camHandler, byte[] pcm, int size) {
        for(CameraEventListener listener:eventListeners)
        {
            listener.onAudioDataEvent(camHandler,pcm,size);
        }
    }

    @Override
    public void onRecordFileListCallBack(long camHandler, int filecount, String fname, String strDate, int size) {
        System.out.println("CallBack_GetParam--------------->>filecount == "+filecount+",  fname=="+fname);
        synchronized (eventListeners) {
            for (CameraEventListener listener : eventListeners) {
                listener.onRecordFileList(camHandler, filecount, fname, strDate, size);
            }
        }
    }

    @Override
    public void onP2PTypeCallBack(long camHandler, int nType) {
        for(BCamera camera:cameraList)
        {
            if(camera.getCamHandler() == camHandler)
            {
                camera.setP2pMode(nType);
                break;
            }
        }
    }

    @Override
    public void onRecordPositionCallBack(long camHandler, int pos) {
        synchronized (eventListeners){
            for(CameraEventListener listener:eventListeners)
            {
                listener.onRecordPlayPos(camHandler,pos);
            }
        }

    }

    @Override
    public void onVideoDataCallBack(long camHandler, byte[] data, int type, int size) {

    }

    @Override
    public void onAlarmPushCallBack(long camHandler, int nType)
    {
        synchronized (eventListeners){
            System.out.println("CallBack_AlarmMessage ---->nType==0x"+ Integer.toHexString(nType));
            for(CameraEventListener listener:eventListeners)
            {
                listener.onAlarmEvent(camHandler,nType);
            }
        }

    }

    @Override
    public void onSearchCameraCallBack(String cameraInfo) {
        if(TextUtils.isEmpty(cameraInfo))
            return;
        System.out.println("CallBack_SearchDevice cameraInfo = "+cameraInfo);
        JSONObject obj = null;
        CameraBean cameraBean;
        try {
            obj = new JSONObject(cameraInfo);
            String mac     = obj.getString("Mac");
            String camName = obj.getString("DeviceName");
            String did     = obj.getString("DeviceID");
            String ip      = obj.getString("IP");
            int port    = obj.getInt("Port");
            int type    = obj.getInt("DeviceType");
            int smartConnect = obj.getInt("SmartConnect");
            cameraBean = new CameraBean();
            cameraBean.setDeviceType(type);
            cameraBean.setDevMac(mac);
            cameraBean.setDevID(did);
            cameraBean.setDevName(camName);
            cameraBean.setDevIP(ip);
            cameraBean.setPort(port);
            cameraBean.setUsername("admin");
            cameraBean.setPassword("admin");
            cameraBean.setSmartConnect(smartConnect);

            for(CameraEventListener listener:eventListeners)
            {
                listener.onSerchEvent(cameraBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
