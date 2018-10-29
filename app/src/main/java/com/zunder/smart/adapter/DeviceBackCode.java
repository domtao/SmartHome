package com.zunder.smart.adapter;

import com.zunder.smart.service.SendThread;

public class DeviceBackCode {
	public static void getBackCode(String devicesInfo, String _type) {

		String did = devicesInfo.toString();
		String type = _type.replace("A3", "03").replace("B3", "03");
		String mid1 = "00";
		String mid2 = "00";
		String cmd = "";
		if (devicesInfo.toString().length() == 6) {
			did = devicesInfo.toString().substring(0, 2);
			mid1 = devicesInfo.toString().substring(2, 4);
			mid2 = devicesInfo.toString().substring(4, 6);
		}

		int ariortvType = Integer.valueOf(type, 16);
		if (((ariortvType >= 48) && (ariortvType < 101)) || (ariortvType == 33)
				|| (ariortvType == 13)) {
			cmd = "*C0019FA" + type + "00" + did + "AAAF00" + mid1 + mid2;
		} else {
			switch (ariortvType) {
			case 0: // 1DI1DO
			case 1:
			case 3:
			case 6:
			case 7:
			case 11:
			case 39:
				cmd = "*C0009FA" + type +  did + mid1 + mid2 + "000000" ;
				break;
			case 34:
			case 4:
				cmd = "*C0009AA" + type + did + mid1 + mid2 + "000000" ;
				break;
			case 2:
				cmd = "*C0009FA" + type + did + mid1 + mid2 + "000000" ;
				break;
			case 5:
				break;
			case 9:
				break;
			}
		}
		if (cmd.length() > 23) {
			try {
				SendThread send = SendThread.getInstance(cmd);
				new Thread(send).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
