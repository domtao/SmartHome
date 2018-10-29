package com.zunder.smart.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.InputSource;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import com.zunder.smart.model.IFileInfo;
import com.zunder.smart.model.ItemName;
import com.zunder.smart.tools.HanziToPinyin.Token;
import com.zunder.smart.MyApplication;
import com.zunder.smart.model.IFileInfo;
import com.zunder.smart.model.ItemName;

/**
 * 
 * @author track
 * @version 1.0
 */
public class AppTools {

	public static final String ACTION = "com.dst.newsmarthome.activity.HelpCodeActivity";
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 10000;

	public static String getCurrentlyDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd");
		return dateFormat.format(new Date());
	}

	public static int  getCurrentMonth() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
		return Integer.parseInt(dateFormat.format(new Date()));
	}
	public static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}

	public static boolean isNumeric(String str) {
		if(str==null) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static String isNumer(String str) {
		if(str==null) {
			return "0";
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		if(pattern.matcher(str).matches()){
			return str;
		}else {
			return "0";
		}
	}
	public static boolean isCharNum(String str) {
		Pattern pattern = Pattern.compile("^[0-9A-F]+$");
		return pattern.matcher(str).matches();
	}

	public static InputSource getNetInputSource(String site, String format)
			throws IOException {
		site += "&random=" + Math.random();
		URL url = new URL(site);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, format);
		InputSource source = new InputSource(isr);
		return source;
	}

	public static String getLocaleLanguage() {
		Locale l = Locale.getDefault();
		return String.format("%s", l.getLanguage(), l.getCountry())
				.toUpperCase();
	}


	// 此方法直接照搬自网络上的�?个下拉刷新的demo，此处是“估计�?�headView的width以及height
	public static void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public static String howTimeAgo(Context context, long t) {
		String msg = "";
		long nowTime = System.currentTimeMillis();
		long time = (nowTime - t) / (60 * 1000);
		if (time > 0 && time < 60) {
			msg = time + context.getString(R.string.minuteago);
		} else if (time == 0) {
			msg = context.getString(R.string.at_now);
		}
		time = (nowTime - t) / (60 * 1000 * 60);
		if (time > 0 && time < 24) {
			msg = time + context.getString(R.string.hourago);
		}
		time = (nowTime - t) / (60 * 1000 * 60 * 24);
		if (time > 0) {
			msg = time + context.getString(R.string.dayago);
		}
		return msg;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getDateTime(String createTime) {

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long
				.parseLong(createTime.replace("/Date(", "").replace(")/", "")));
	}

	public static String getCurrentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	public static String getSystemTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}

	public static String getTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(new Date());
	}

	public static List<String> convertToArray(String[] strings){
		List<String>  list=new ArrayList<String>();
		for (int i=0;i<strings.length;i++){
			list.add(strings[i]);
		}
		return list;
	}
	public static String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd");
		return dateFormat.format(new Date());
	}

	public static String getTime(String createTime) {

		return new SimpleDateFormat(" HH:mm:ss").format(Long
				.parseLong(createTime.replace("/Date(", "").replace(")/", "")));
	}

	public static String getAlias() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date);
	}

	private static String hexString = "0123456789ABCDEF";

	public static String toStringHex(String s) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = s.getBytes();
		int bit;

		for (int i = 0; i < bs.length; i++)
		{
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}

	public static String hexStr2Str(String hexStr)
	{
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++)
		{
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}


	public static String toHex(int number) {
		if (number <= 15) {
			return ("0" + Integer.toHexString(number).toUpperCase());
		} else {
			return Integer.toHexString(number).toUpperCase();
		}
	}

	public static String twos(String number) {
		String result="00";
		if (number!=null&&number.length()==0) {
			result= "0"+number;
		}
		return result;
	}
	public static String getWeeks(int week) {
		String[] arr = MyApplication.getInstance().getString(R.string.week).split(",");
		String weekStr = "";
		if ((week & 0x7f) == 0x7f) {
			weekStr = MyApplication.getInstance().getString(R.string.everyday);
		} else {
			for (int i = 0; i < arr.length; i++) {
				if ((week & (1 << i)) > 0) {
					weekStr += arr[i] + " ";
				}
			}
		}
		return weekStr;
	}
	public static boolean PinYinComparison(String source1, String source2) {
		boolean result = false;
		String pinyingDev = getPinYin(source2); // getSelling(CampStr);
		if ((!source1.equals("") && (source1.length() > 0))) {
			if ((!pinyingDev.equals("") && (pinyingDev.length() > 0))) {
				if (source1.indexOf(pinyingDev) != -1) {
					result = true;
				}
			}
		}
		return result;
	}	public static String getPinYin(String hanzi) {
		ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(hanzi);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (HanziToPinyin.Token token : tokens) {
				if (HanziToPinyin.Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toUpperCase();
	}
	public static int timeComprartion(int select, int iPos, String str) {

		String[] time = { "秒", "分", "时", ";" };
		int ContValue = 0;
		int LimitPos = 0;
		String UnitStr = "";
		boolean bFlag = true;
		boolean bFirstOk = false;

		if (select == 0) {// Time
			bFlag = false;
			for (int i = iPos; i < str.length(); i++) {
				for (int n = 0; n < time.length; n++) {
					if (str.substring(i, i + 1).equals(time[n])) {
						if (str.substring(i, i + 1).equals(time[n])) {
							if ((n < 2) || ((n == 2) && (UnitStr.equals("小")))
									|| (n == 3)) {
								LimitPos = n;
								if (n < 3) {
									bFlag = true;
								}
								break;
							}
						}
					}
				}
				UnitStr = str.substring(i, i + 1);
				if ((bFlag) || (LimitPos == 3)) {
					break;
				}
			}
		}
		String[] number = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "两", "拾",
				"十", "百", ";" };
		if (bFlag) {
			int MemAdd = 0, k = 0;
			int j = 0;
			bFlag = false;
			for (j = iPos; j < str.length(); j++) {
				for (k = 0; k < number.length; k++) {
					if (str.substring(j, j + 1).equals(number[k])) {
						break;
					}
				}
				if (k < number.length) //
				{
					if (k == 24) {
						break;
					} else {
						if (bFirstOk) {
							bFirstOk = false;
							ContValue = 0;
						}
						if ((k == 21) || (k == 22)) // ??
						{
							if (ContValue == 0) {
								ContValue = 1;
							}
							MemAdd = 1;
						} else if (k == 23) // ∞?
						{
							ContValue = (ContValue * 10);
							MemAdd = 1;
						} else {
							if (k == 20) {
								k = 2;
							} else if (k > 9) {
								k -= 10;
							}
							ContValue = (ContValue * 10) + k;
							MemAdd = 0;
						}
						bFlag = true;
					}
				} else {
					if (bFlag) {
						if (str.substring(j, j + 1).equals(UnitStr)) {
							break;
						} else {
							if (MemAdd == 1) {
								MemAdd = 0;
								ContValue = (ContValue * 10);
							}
							bFirstOk = true; // break;
						}
					}
				}
			}
			if (MemAdd == 1) {
				ContValue = (ContValue * 10);
			}
			if (ContValue > 0) {
				UnitStr = ",已设定 " + ContValue;
				if (select == 0) {
					switch (LimitPos) {
						case 0: // √?
							if (ContValue > 63) {
								ContValue = 63;
							}
							UnitStr = UnitStr + "秒";
							break;
						case 1: // ∑÷
							if (ContValue > 60) {
								ContValue = 60;
							}
							ContValue = ContValue + 64;
							UnitStr = UnitStr + "分";
							break;
						case 2: // ?r
							if (ContValue > 12) {
								ContValue = 12;
							}
							ContValue = ContValue + 128;
							UnitStr = UnitStr + "小时";
							break;
					}
				}
			}
		}

		return ContValue;
	}
	public static String enUnicode(String content){//将汉字转换为16进制数
		String enUnicode="";
		for(int i=0;i<content.length();i++){
			if(i==0){
				enUnicode+=getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
			}else{
				enUnicode+=enUnicode+getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
			}
		}

		return enUnicode;
	}

	private static String getHexString(String hexString){
		String hexStr="";
		for(int i=hexString.length();i<4;i++){
			if(i==hexString.length())
				hexStr="0";
			else
				hexStr=hexStr+"0";
		}
		return hexStr+hexString;
	}
	public static String convertUTF8ToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}

		try {
			s = s.toUpperCase();

			int total = s.length() / 2;
			int pos = 0;

			byte[] buffer = new byte[total];
			for (int i = 0; i < total; i++) {

				int start = i * 2;

				buffer[i] = (byte) Integer.parseInt(
						s.substring(start, start + 2), 16);
				pos++;
			}
			return new String(buffer, 0, pos, "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 *
	 * @param s 原串
	 * @return
	 */
	public static String convertStringToUTF8(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			char c;
			for (int i = 0; i < s.length(); i++) {
				c = s.charAt(i);
				if (c >= 0 && c <= 255) {
					//sb.append(c);
					sb.append(Integer.toHexString(c).toUpperCase());
				} else {
					byte[] b;
					b = Character.toString(c).getBytes("GBK");
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if (k < 0)
							k += 256;
						sb.append(Integer.toHexString(k).toUpperCase());
						// sb.append("%" +Integer.toHexString(k).toUpperCase());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static boolean validPhoneNum(String phoneNum){
		boolean flag=false;
		Pattern p1 = null;

		Matcher m = null;
		p1 = Pattern.compile("^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\\d{8})?$");

		if(phoneNum.length()!=11){
			return false;
		}else{
			m = p1.matcher(phoneNum);
			flag = m.matches();
		}
		return flag;
	}
public static boolean isEmpty(String text){
	String regx = "[A-Za-z0-9]*";
	Matcher m = Pattern.compile(regx).matcher(text);
	return m.find();
}
	public static boolean validTwPhoneNum(String phoneNum){
		boolean flag=false;
		Pattern p1 = null;

		Matcher m = null;
		p1 = Pattern.compile("^(((0[0-9]{1}))+\\d{8})?$");

		if(phoneNum.length()!=10){
			return false;
		}else{
			m = p1.matcher(phoneNum);
			flag = m.matches();
		}
		return flag;
	}
	public static int timeConvert(String timeStr) {
		if (timeStr == "" || timeStr.equals("")) {
			return 0;
		}
		String[] timeStrs = timeStr.split(":");
		return Integer.parseInt(timeStrs[0]) * 60
				+ Integer.parseInt(timeStrs[1]);
	}
	public static String StringToHex16(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		//将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2) {
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		}
		return new String(baos.toByteArray());
	}
	public static List<ItemName> convertItemNames(String[] str){
		List<ItemName> list=new ArrayList<ItemName>();
		for (int i=0;i<str.length;i++){
			ItemName itemName=new ItemName();
			itemName.setId(i+1);
			itemName.setItemName(str[i]);
			list.add(itemName);
		}
		return list;
	}
	public static List<ItemName> convertItemNames(List<IFileInfo> _list){
		List<ItemName> list=new ArrayList<ItemName>();
		for (int i=0;i<_list.size();i++){
			IFileInfo iFileInfo=_list.get(i);

			ItemName itemName=new ItemName();
			itemName.setId(iFileInfo.getId());
			itemName.setItemName(iFileInfo.getFileName());
			list.add(itemName);
		}
		return list;
	}
	public static InputStream getNetInputStream(String url) throws IOException {

		URLConnection conn = new URL(url).openConnection();
		conn.setConnectTimeout(CONNECT_TIMEOUT);
		conn.setReadTimeout(READ_TIMEOUT);
		InputStream is =(InputStream) conn.getContent();

		return is;
	}
	public static String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "0";
	}
	public static int getHour() {
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
		int Hour= Integer.parseInt(hourFormat.format(new Date()));

		SimpleDateFormat dateFormat = new SimpleDateFormat("mm");
		int Minit= Integer.parseInt(dateFormat.format(new Date()));
		return Hour*60+Minit;
	}
}
