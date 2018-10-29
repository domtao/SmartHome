package com.bluecam.bluecamlib;


import com.bluenet.api.DeviceAPI;

import org.json.JSONException;
import org.json.JSONObject;


public class Camera extends BCamera
{
	@Override
	public void createCamera()
	{
		camHandler = DeviceAPI.createCamera(cameraBean.getUsername(), cameraBean.getPassword(), cameraBean.getDevIP(), cameraBean.getPort(), cameraBean.getDevID());
		if(camHandler >=0)
		{
			int start = DeviceAPI.openCamera(camHandler);
			if(start<0)
			{
				status = CameraContants.DeviceStatus.DEVICE_STATUS_OPEN_ERR;
				return ;
			}
		}
		else
		{
			status = CameraContants.DeviceStatus.DEVICE_STATUS_CREATE_ERR;
			return;
		}
	}

	@Override
	public void destoryCamera() {
		DeviceAPI.closeCamera(camHandler);
		DeviceAPI.destoryCamera(camHandler);
	}

	@Override
	public void connectCamera() {
		if(status == CameraContants.DeviceStatus.DEVICE_STATUS_CONNECTING)
			return;
		if(status != CameraContants.DeviceStatus.DEVICE_STATUS_ON_LINE)
		{
			DeviceAPI.closeCamera(camHandler);
			DeviceAPI.openCamera(camHandler);
		}
	}

	@Override
	public void getCameraParam(int paramKey)
	{
		DeviceAPI.getCameraParam(camHandler, paramKey);
	}

	@Override
	public void setCameraParam(int type, String param) {
		DeviceAPI.setCameraParam(camHandler, type, param);
	}

	@Override
	public void setCameraParam(int param, int value) {
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("param", param);
			obj.put("value", value);
			DeviceAPI.setCameraParam(camHandler, CameraContants.ParamKey.SET_CAMERA_PARAM_KEY, obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playStream(int resolution) {
		int streamType = 10;
		DeviceAPI.openStream(camHandler, streamType, resolution);
	}

	@Override
	public void closeStream() {
		DeviceAPI.closeStream(camHandler);
	}

	@Override
	public void setStreamCallBack(Object render) {
		DeviceAPI.registerStreamCallBack(camHandler, render);
	}

	@Override
	public void openAudio() {
		DeviceAPI.openAudio(camHandler,1);
	}

	@Override
	public void closeAudio() {
		DeviceAPI.closeAudio(camHandler);
	}

	@Override
	public void takePicture( String path) {
		DeviceAPI.takePicture(camHandler,path);
	}

	@Override
	public void startRecordVideo(String path, int width, int higth) {
		DeviceAPI.startRecorder(camHandler,path,width,higth,15);
	}

	@Override
	public void stopRecordVideo() {
		DeviceAPI.stopRecorder(camHandler);
	}

	@Override
	public void openTalk() {
		DeviceAPI.openTalk(camHandler);
	}

	@Override
	public void closeTalk() {
		DeviceAPI.closeTalk(camHandler);
	}

	@Override
	public void sendTalkData(byte[] data, int size) {
		DeviceAPI.sendTalkData(camHandler,data,size);
	}

	@Override
	public void getTfVideoList(int bYear, int bMon, int bDay, int eYear, int eMon, int eDay) {
		DeviceAPI.queryRecordFileList(camHandler, bYear, bMon, bDay, eYear, eMon, eDay);
	}

	@Override
	public void openTfRecord(String fileName, int pos) {
		DeviceAPI.openPlayRecord(camHandler,fileName,pos);
	}

	@Override
	public void closeTfRecord(String fileName) {
		DeviceAPI.closePlayRecord(camHandler,fileName);
	}

	@Override
	public void setRecordCallBack(Object render) {
		DeviceAPI.registerRecordCallBack(camHandler,render);
	}

	@Override
	public void controlCamera(int cmd) {
		DeviceAPI.control(camHandler,cmd);
	}

	@Override
	public void playTfPos(String fileName, int pos) {
		DeviceAPI.playRecordPosition(camHandler,fileName,pos);
	}

	@Override
	public void downloadFile(String srcFilename, String dstfilename) {
		DeviceAPI.downloadFile(camHandler,srcFilename,dstfilename);
	}

	@Override
	public int getDownloadPos() {
		return DeviceAPI.queryDownloadPosition(camHandler);
	}

	@Override
	public void stopDownloadFile() {
		DeviceAPI.stopDownloadFile(camHandler);
	}
}
