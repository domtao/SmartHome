package com.zunder.smart.listener;

import android.os.Message;

public interface GateWayListener {
	public void receiveGateWayStatus(int type,long userid, int status);
}
