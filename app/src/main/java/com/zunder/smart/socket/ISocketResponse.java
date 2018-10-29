package com.zunder.smart.socket;

/**
 * socket回调
 * 
 * @author DexYang
 * 
 */
public interface ISocketResponse {
	public abstract void onSocketDisConnect(String txt);

	public abstract void onSocketSuccess(String txt);

	public abstract void onSocketResponse(String txt);
}
