package com.zunder.smart.service;

import com.zunder.smart.service.TcpSender;

//import java.io.IOException;
//import java.nio.CharBuffer;
//import com.sun.corba.se.impl.naming.pcosnaming.NameServer;

public class SendThread implements Runnable {
	private volatile static SendThread install;
	private static String ctrollCMD;

	public static SendThread getInstance(String cmd) {
		ctrollCMD = cmd;
		if (null == install) {
			synchronized (SendThread.class) {
				if (null == install) {
					install = new SendThread();
				}
			}
		}
		return install;
	}

	@Override
	public void run() {
			TcpSender.sendMssageAF(ctrollCMD);

//		TcpSender.sendMssageAF(ctrollCMD);
		// TcpSender.testSend(ctrollCMD);
	}
}
