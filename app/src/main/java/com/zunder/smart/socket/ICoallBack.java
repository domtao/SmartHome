package com.zunder.smart.socket;

import java.net.Socket;

/**
 * Created by Administrator on 2017/6/27.
 */

public interface ICoallBack {
	/**
	 * 连接成功事件
	 * **/
	public void OnSuccess(Socket socket);

	public void OnDisConnect(String e);
	public void OnGateWay() ;
}