package com.zunder.smart.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.TextUtils;

import com.zunder.smart.MyApplication;
import com.zunder.smart.MyApplication;

public class SystemInfo {

	private static String mac = "00:00:00:00";

	/** ��ȡ����mac��ַ ��ҪȨ��android:name="android.permission.ACCESS_WIFI_STATE" */
	public static String getLocalMacAddress(Context context) {

		try {
			mac = loadFileAsString("/sys/class/net/eth0/address").toUpperCase()
					.substring(0, 17);
			if (mac == null) {
				mac = "δ֪mac��ַ";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mac;
	}

	public static String getSSID(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();
		return wifiInfo.getSSID().replace("\"", "");
	}

	public static String loadFileAsString(String filePath)
			throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	/** Get the STB MacAddress */

	public static String getMacAddress() {
		try {
			return loadFileAsString("/sys/class/net/eth0/address")
					.toUpperCase().substring(0, 17);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getMasterID(Context context) {
		return Settings.System.getString(context.getContentResolver(),
				Settings.System.ANDROID_ID).toUpperCase();
	}

	private static String cpuInfo = "";

	/** ��ȡcpu��Ϣ */
	public static String getCpuInfo() {
		if (TextUtils.isEmpty(cpuInfo)) {
			String str1 = "/proc/cpuinfo";
			String str2 = "";
			String[] arrayOfString;
			try {
				FileReader fr = new FileReader(str1);
				BufferedReader localBufferedReader = new BufferedReader(fr,
						8192);
				str2 = localBufferedReader.readLine();
				arrayOfString = str2.split("\\s+");
				for (int i = 2; i < arrayOfString.length; i++) {
					cpuInfo = cpuInfo + arrayOfString[i] + " ";
				}
				localBufferedReader.close();
			} catch (IOException e) {
			}
		}
		return cpuInfo;
	}

	/**
	 * ��ȡCPU���к�
	 * 
	 * @return CPU���к�(16λ) ��ȡʧ��Ϊ"0000000000000000"
	 */
	public static String getCPUSerial() {

		String str = "", strCPU = "", cpuAddress = "0000000000000000";

		try {
			// ��ȡCPU��Ϣ

			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");

			InputStreamReader ir = new InputStreamReader(pp.getInputStream());

			LineNumberReader input = new LineNumberReader(ir);

			// ����CPU���к�

			for (int i = 1; i < 100; i++) {

				str = input.readLine();

				if (str != null) {

					// ���ҵ����к�������

					if (str.indexOf("Serial") > -1) {

						// ��ȡ���к�

						strCPU = str.substring(str.indexOf(":") + 1,
								str.length());

						// ȥ�ո�

						cpuAddress = strCPU.trim();

						break;

					}
				} else {

					// �ļ���β
					break;

				}
			}

		} catch (IOException ex) {

			// ����Ĭ��ֵ

			ex.printStackTrace();
		}
		return cpuAddress;
	}

	private static String dervice_ip = "";

	/** ��ȡ�豸IP��ַ */
	public static String getIPString(Context context) {

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// �ж�wifi�Ƿ���
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);
		return ip;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	/** �ж��Ƿ�ƽ�� */
	public static boolean isTabletDevice(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb
			// test screen size, use reflection because isLayoutSizeAtLeast is
			// only available since 11
			Configuration con = context.getResources().getConfiguration();
			try {
				Method mIsLayoutSizeAtLeast = con.getClass().getMethod(
						"isLayoutSizeAtLeast", int.class);
				Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con,
						0x00000004); // Configuration.SCREENLAYOUT_SIZE_XLARGE
				return r;
			} catch (Exception x) {
				x.printStackTrace();
				return false;
			}
		}
		return false;
	}

	private static ConnectivityManager manager;
	private static final String TAG = "ConnectityTools";

	public static boolean isConnectity(Context context) {
		boolean flag = false;
		manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// ���ж��Ƿ���2G��3G�ź�
		NetworkInfo mobileInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	public static long getMemorySize() {
		// File root = Environment.getRootDirectory();
		// StatFs sf = new StatFs(root.getPath());
		// long blockSize = sf.getBlockSize();
		// long availCount = sf.getAvailableBlocks();
		// return (availCount * blockSize) / (1024 * 1024);
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize) / (1024 * 1024);
	}

	public static int excuteSuCMD(String cmd) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream dos = new DataOutputStream(
					(OutputStream) process.getOutputStream());
			// �����ֻ�Root֮��Library path ��ʧ������library path�ɽ��������
			dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
			cmd = String.valueOf(cmd);
			dos.writeBytes((String) (cmd + "\n"));
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			process.waitFor();
			int result = process.exitValue();
			return (Integer) result;
		} catch (Exception localException) {
			localException.printStackTrace();
			return -1;
		}
	}
	public static boolean hasWifiConnection()
	{
		final ConnectivityManager connectivityManager= (ConnectivityManager) MyApplication.getInstance().
				getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		//是否有网络并且已经连接
		return (networkInfo!=null&& networkInfo.isConnectedOrConnecting());
	}
	public static boolean isLocationEnabled() {
		int locationMode = 0;
		String locationProviders;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				locationMode = Settings.Secure.getInt(MyApplication.getInstance().getContentResolver(), Settings.Secure.LOCATION_MODE);
			} catch (Settings.SettingNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			return locationMode != Settings.Secure.LOCATION_MODE_OFF;
		} else {
			locationProviders = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}
	}
}
