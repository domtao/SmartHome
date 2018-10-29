package com.zunder.smart.tools;

import android.R.integer;

public class EncodeHex {


	private static String hexStr = "0123456789ABCDEF";

	public static byte[] HexStringToByte(String str) {
		str = str.replace("\r", "");
		str = str.replace("\n", "");
		str = str.replace("\t", "");
		String[] strings = str.split(" ");
		byte[] bytes = new byte[strings.length];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(strings[i], 16);
		}

		return bytes;

	}

	public static String FlagNumber(String str) {
		String result = "";
		if (str.length() == 1) {
			result = "000000" + str;
		} else if (str.length() == 2) {
			result = "00000" + str;
		} else if (str.length() == 3) {
			result = "0000" + str;
		} else if (str.length() == 4) {
			result = "000" + str;
		} else if (str.length() == 5) {
			result = "00" + str;
		} else if (str.length() == 6) {
			result = "0" + str;
		} else if (str.length() == 7) {
			result = str;
		} else {
			result = "0000000";
		}
		return result;

	}

	public static String BinaryToHexString(byte[] bytes) {

		String result = "";
		String hex = "";
		for (int i = 0; i < bytes.length; i++) {
			// �ֽڸ�4λ
			hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
			// �ֽڵ�4λ
			hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
			result += hex + " ";
		}
		String[] results = result.split(" ");
		int sum = 2 * 2 * 2 * 2 * 2 * 2 * Integer.parseInt(results[6]) + 2 * 2
				* 2 * 2 * 2 * Integer.parseInt(results[5]) + 2 * 2 * 2 * 2
				* Integer.parseInt(results[4]) + 2 * 2 * 2
				* Integer.parseInt(results[3]) + 2 * 2
				* Integer.parseInt(results[2]) + 2
				* Integer.parseInt(results[1]) + Integer.parseInt(results[0]);
		String str = Integer.toHexString(sum);
		if (str.length() == 1) {
			str = "0" + str;
		}
		return str.toUpperCase();
	}

	/**
	 * �ѽ��յ����ֽ�����ת��Ϊʮ�������ַ���
	 * 
	 * @param b
	 *            ��Ҫת�����ֽ�����
	 * @param len
	 *            �ֽ����鳤��
	 */
	public static String ByteToHexString(byte[] b, int len) {
		StringBuffer sb = new StringBuffer();
		String hex = "";
		for (int i = 0; i < len; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase() + " ");
		}
		String result = sb.toString();
		return result.trim();
	}

	public static byte[] HexStringToBinary(String hexString) {
		// hexString�ĳ��ȶ�2ȡ������Ϊbytes�ĳ���
		int len = hexString.length() / 2;
		byte[] bytes = new byte[len];
		byte high = 0;// �ֽڸ���λ
		byte low = 0;// �ֽڵ���λ

		for (int i = 0; i < len; i++) {
			// ������λ�õ���λ
			high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
			low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
			bytes[i] = (byte) (high | low);// �ߵ�λ��������
		}
		return bytes;
	}

	// public static String getHexNumber(String Code) {
	// String result = "";
	// if (Code.equals("A")) {
	// result = "10";
	// } else if (Code.equals("B")) {
	// result = "11";
	// } else if (Code.equals("C")) {
	// result = "12";
	// } else if (Code.equals("D")) {
	// result = "13";
	// } else if (Code.equals("E")) {
	// result = "14";
	// } else if (Code.equals("F")) {
	// result = "15";
	// } else {
	// result = Code;
	// }
	// return result;
	// }

	public static String getTypeNumber(String Code) {
		String result = "";
		if (Code.equals("0A") || Code.equals("A")) {
			result = "10";
		} else if (Code.equals("0B") || Code.equals("B")) {
			result = "11";
		} else if (Code.equals("0C") || Code.equals("C")) {
			result = "12";
		} else if (Code.equals("0D") || Code.equals("D")) {
			result = "13";
		} else if (Code.equals("0E") || Code.equals("E")) {
			result = "14";
		} else if (Code.equals("0F") || Code.equals("F")) {
			result = "15";
		} else if (Code.equals("10")) {
			result = "16";
		} else if (Code.equals("11")) {
			result = "17";
		} else if (Code.equals("12")) {
			result = "18";
		} else if (Code.equals("13")) {
			result = "19";
		} else if (Code.equals("14")) {
			result = "20";
		} else if (Code.equals("15")) {
			result = "21";
		} else if (Code.equals("16")) {
			result = "22";
		} else if (Code.equals("17")) {
			result = "23";
		} else if (Code.equals("18")) {
			result = "24";
		} else if (Code.equals("19")) {
			result = "25";
		} else if (Code.equals("1A")) {
			result = "26";
		} else if (Code.equals("1B")) {
			result = "27";
		} else if (Code.equals("1C")) {
			result = "28";
		} else if (Code.equals("1D")) {
			result = "29";
		} else if (Code.equals("1E")) {
			result = "30";
		} else if (Code.equals("1F")) {
			result = "31";
		} else if (Code.equals("20")) {
			result = "32";
		} else if (Code.equals("21")) {
			result = "33";
		} else if (Code.equals("22")) {
			result = "34";
		} else if (Code.equals("23")) {
			result = "35";
		} else if (Code.equals("24")) {
			result = "36";
		} else if (Code.equals("25")) {
			result = "37";
		} else if (Code.equals("26")) {
			result = "38";
		} else if (Code.equals("27")) {
			result = "39";
		} else if (Code.equals("28")) {
			result = "40";
		} else if (Code.equals("29")) {
			result = "41";
		} else if (Code.equals("2A")) {
			result = "42";
		} else if (Code.equals("2B")) {
			result = "43";
		} else if (Code.equals("2C")) {
			result = "44";
		} else if (Code.equals("2D")) {
			result = "45";
		} else if (Code.equals("2E")) {
			result = "46";
		} else if (Code.equals("2F")) {
			result = "47";
		} else if (Code.equals("30")) {
			result = "48";
		} else if (Code.equals("31")) {
			result = "49";
		} else if (Code.equals("32")) {
			result = "50";
		} else if (Code.equals("33")) {
			result = "51";
		} else if (Code.equals("34")) {
			result = "52";
		} else if (Code.equals("35")) {
			result = "53";
		} else if (Code.equals("36")) {
			result = "54";
		} else if (Code.equals("37")) {
			result = "55";
		} else if (Code.equals("38")) {
			result = "56";
		} else if (Code.equals("39")) {
			result = "57";
		} else if (Code.equals("3A")) {
			result = "58";
		} else if (Code.equals("3B")) {
			result = "59";
		} else if (Code.equals("3C")) {
			result = "60";
		} else if (Code.equals("3D")) {
			result = "61";
		} else if (Code.equals("3E")) {
			result = "62";
		} else if (Code.equals("3F")) {
			result = "63";
		} else if (Code.equals("40")) {
			result = "64";
		} else if (Code.equals("41")) {
			result = "65";
		} else if (Code.equals("42")) {
			result = "66";
		} else if (Code.equals("43")) {
			result = "67";
		} else if (Code.equals("44")) {
			result = "68";
		} else if (Code.equals("45")) {
			result = "69";
		} else if (Code.equals("46")) {
			result = "70";
		} else if (Code.equals("47")) {
			result = "71";
		} else if (Code.equals("48")) {
			result = "72";
		} else if (Code.equals("49")) {
			result = "73";
		} else if (Code.equals("4A")) {
			result = "74";
		} else if (Code.equals("4B")) {
			result = "75";
		} else if (Code.equals("4C")) {
			result = "76";
		} else if (Code.equals("4D")) {
			result = "77";
		} else if (Code.equals("4E")) {
			result = "78";
		} else if (Code.equals("4F")) {
			result = "79";
		} else if (Code.equals("50")) {
			result = "80";
		} else if (Code.equals("51")) {
			result = "81";
		} else if (Code.equals("52")) {
			result = "82";
		} else if (Code.equals("53")) {
			result = "83";
		} else if (Code.equals("54")) {
			result = "84";
		} else if (Code.equals("55")) {
			result = "85";
		} else if (Code.equals("56")) {
			result = "86";
		} else if (Code.equals("57")) {
			result = "87";
		} else if (Code.equals("58")) {
			result = "88";
		} else if (Code.equals("59")) {
			result = "89";
		} else if (Code.equals("5A")) {
			result = "90";
		} else if (Code.equals("5B")) {
			result = "91";
		} else if (Code.equals("5C")) {
			result = "92";
		} else if (Code.equals("5D")) {
			result = "93";
		} else if (Code.equals("5E")) {
			result = "94";
		} else if (Code.equals("5F")) {
			result = "95";
		} else if (Code.equals("60")) {
			result = "96";
		} else if (Code.equals("61")) {
			result = "97";
		} else if (Code.equals("62")) {
			result = "98";
		} else if (Code.equals("63")) {
			result = "99";
		} else if (Code.equals("64")) {
			result = "100";
		} else if (Code.equals("65")) {
			result = "101";
		} else if (Code.equals("66")) {
			result = "102";
		} else if (Code.equals("67")) {
			result = "103";
		} else if (Code.equals("68")) {
			result = "104";
		} else if (Code.equals("69")) {
			result = "105";
		} else if (Code.equals("6A")) {
			result = "106";
		} else if (Code.equals("6B")) {
			result = "107";
		} else if (Code.equals("6C")) {
			result = "108";
		} else if (Code.equals("6D")) {
			result = "109";
		} else if (Code.equals("6E")) {
			result = "110";
		} else if (Code.equals("6F")) {
			result = "111";
		} else if (Code.equals("70")) {
			result = "112";
		} else if (Code.equals("71")) {
			result = "113";
		} else if (Code.equals("72")) {
			result = "114";
		} else if (Code.equals("73")) {
			result = "115";
		} else if (Code.equals("74")) {
			result = "116";
		} else if (Code.equals("75")) {
			result = "117";
		} else if (Code.equals("76")) {
			result = "118";
		} else if (Code.equals("77")) {
			result = "119";
		} else if (Code.equals("78")) {
			result = "120";
		} else if (Code.equals("79")) {
			result = "121";
		} else if (Code.equals("7A")) {
			result = "122";
		} else if (Code.equals("7B")) {
			result = "123";
		} else if (Code.equals("7C")) {
			result = "124";
		} else if (Code.equals("7D")) {
			result = "125";
		} else if (Code.equals("7E")) {
			result = "126";
		} else if (Code.equals("7F")) {
			result = "127";
		} else if (Code.equals("80")) {
			result = "128";
		} else if (Code.equals("81")) {
			result = "129";
		} else if (Code.equals("82")) {
			result = "130";
		} else if (Code.equals("83")) {
			result = "131";
		} else if (Code.equals("84")) {
			result = "132";
		} else if (Code.equals("85")) {
			result = "133";
		} else if (Code.equals("86")) {
			result = "134";
		} else if (Code.equals("87")) {
			result = "135";
		} else if (Code.equals("88")) {
			result = "136";
		} else if (Code.equals("89")) {
			result = "137";
		} else if (Code.equals("8A")) {
			result = "138";
		} else if (Code.equals("8B")) {
			result = "139";
		} else if (Code.equals("8C")) {
			result = "140";
		} else if (Code.equals("8D")) {
			result = "141";
		} else if (Code.equals("8E")) {
			result = "142";
		} else if (Code.equals("8F")) {
			result = "143";
		} else if (Code.equals("90")) {
			result = "144";
		} else if (Code.equals("91")) {
			result = "145";
		} else if (Code.equals("92")) {
			result = "146";
		} else if (Code.equals("93")) {
			result = "147";
		} else if (Code.equals("94")) {
			result = "148";
		} else if (Code.equals("95")) {
			result = "149";
		} else if (Code.equals("96")) {
			result = "150";
		} else if (Code.equals("97")) {
			result = "151";
		} else if (Code.equals("98")) {
			result = "152";
		} else if (Code.equals("99")) {
			result = "153";
		} else if (Code.equals("154")) {
			result = "154";
		} else if (Code.equals("9B")) {
			result = "155";
		} else if (Code.equals("9C")) {
			result = "156";
		} else if (Code.equals("9D")) {
			result = "157";
		} else if (Code.equals("9E")) {
			result = "158";
		} else if (Code.equals("9F")) {
			result = "159";
		} else if (Code.equals("A0")) {
			result = "160";
		} else if (Code.equals("A1")) {
			result = "161";
		} else if (Code.equals("A2")) {
			result = "162";
		} else if (Code.equals("A3")) {
			result = "163";
		} else if (Code.equals("A4")) {
			result = "164";
		} else if (Code.equals("A5")) {
			result = "165";
		} else if (Code.equals("A6")) {
			result = "166";
		} else if (Code.equals("A7")) {
			result = "167";
		} else if (Code.equals("A8")) {
			result = "168";
		} else if (Code.equals("A9")) {
			result = "169";
		} else if (Code.equals("AA")) {
			result = "170";
		} else if (Code.equals("AB")) {
			result = "171";
		} else if (Code.equals("AC")) {
			result = "172";
		} else if (Code.equals("AD")) {
			result = "173";
		} else if (Code.equals("AE")) {
			result = "174";
		} else if (Code.equals("AF")) {
			result = "175";
		} else if (Code.equals("B0")) {
			result = "176";
		} else if (Code.equals("B1")) {
			result = "177";
		} else if (Code.equals("B2")) {
			result = "178";
		} else if (Code.equals("B3")) {
			result = "179";
		} else if (Code.equals("B4")) {
			result = "180";
		} else if (Code.equals("B5")) {
			result = "181";
		} else if (Code.equals("B6")) {
			result = "182";
		} else if (Code.equals("B7")) {
			result = "183";
		} else if (Code.equals("B8")) {
			result = "184";
		} else if (Code.equals("B9")) {
			result = "185";
		} else if (Code.equals("BA")) {
			result = "186";
		} else if (Code.equals("BB")) {
			result = "187";
		} else if (Code.equals("BC")) {
			result = "188";
		} else if (Code.equals("BD")) {
			result = "189";
		} else if (Code.equals("BE")) {
			result = "190";
		} else if (Code.equals("BF")) {
			result = "191";
		} else if (Code.equals("C0")) {
			result = "192";
		} else if (Code.equals("C1")) {
			result = "193";
		} else if (Code.equals("C2")) {
			result = "194";
		} else if (Code.equals("C3")) {
			result = "195";
		} else if (Code.equals("C4")) {
			result = "196";
		} else if (Code.equals("C5")) {
			result = "197";
		} else if (Code.equals("C6")) {
			result = "198";
		} else if (Code.equals("C7")) {
			result = "199";
		} else if (Code.equals("C8")) {
			result = "200";
		} else if (Code.equals("C9")) {
			result = "201";
		} else if (Code.equals("CA")) {
			result = "202";
		} else if (Code.equals("CB")) {
			result = "203";
		} else if (Code.equals("CC")) {
			result = "204";
		} else if (Code.equals("CD")) {
			result = "205";
		} else if (Code.equals("CE")) {
			result = "206";
		} else if (Code.equals("CF")) {
			result = "207";
		} else if (Code.equals("D0")) {
			result = "208";
		} else if (Code.equals("D1")) {
			result = "209";
		} else if (Code.equals("D2")) {
			result = "210";
		} else if (Code.equals("D3")) {
			result = "211";
		} else if (Code.equals("D4")) {
			result = "212";
		} else if (Code.equals("D5")) {
			result = "213";
		} else if (Code.equals("D6")) {
			result = "214";
		} else if (Code.equals("D7")) {
			result = "215";
		} else if (Code.equals("D8")) {
			result = "216";
		} else if (Code.equals("D9")) {
			result = "217";
		} else if (Code.equals("DA")) {
			result = "218";
		} else if (Code.equals("DB")) {
			result = "219";
		} else if (Code.equals("DC")) {
			result = "220";
		} else if (Code.equals("DD")) {
			result = "221";
		} else if (Code.equals("DE")) {
			result = "222";
		} else if (Code.equals("DF")) {
			result = "223";
		} else if (Code.equals("E0")) {
			result = "224";
		} else if (Code.equals("E1")) {
			result = "225";
		} else if (Code.equals("E2")) {
			result = "226";
		} else if (Code.equals("E3")) {
			result = "227";
		} else if (Code.equals("E4")) {
			result = "228";
		} else if (Code.equals("E5")) {
			result = "229";
		} else if (Code.equals("E6")) {
			result = "230";
		} else if (Code.equals("E7")) {
			result = "231";
		} else if (Code.equals("E8")) {
			result = "232";
		} else if (Code.equals("E9")) {
			result = "233";
		} else if (Code.equals("EA")) {
			result = "234";
		} else if (Code.equals("EB")) {
			result = "235";
		} else if (Code.equals("EC")) {
			result = "236";
		} else if (Code.equals("ED")) {
			result = "237";
		} else if (Code.equals("EE")) {
			result = "238";
		} else if (Code.equals("EF")) {
			result = "239";
		} else if (Code.equals("F0")) {
			result = "240";
		} else if (Code.equals("F1")) {
			result = "241";
		} else if (Code.equals("F2")) {
			result = "242";
		} else if (Code.equals("F3")) {
			result = "243";
		} else if (Code.equals("F4")) {
			result = "244";
		} else if (Code.equals("F5")) {
			result = "245";
		} else if (Code.equals("F6")) {
			result = "246";
		} else if (Code.equals("F7")) {
			result = "247";
		} else if (Code.equals("F8")) {
			result = "248";
		} else if (Code.equals("F9")) {
			result = "249";
		} else if (Code.equals("FA")) {
			result = "250";
		} else if (Code.equals("FB")) {
			result = "251";
		} else if (Code.equals("FC")) {
			result = "252";
		} else if (Code.equals("FD")) {
			result = "253";
		} else if (Code.equals("FE")) {
			result = "254";
		} else if (Code.equals("FF")) {
			result = "255";
		} else {
			result = Code;
		}
		return result;
	}

	public static Boolean isAir(String value) {
		if (value.startsWith("30") || value.startsWith("31")
				|| value.startsWith("32") || value.startsWith("33")
				|| value.startsWith("34") || value.startsWith("35")
				|| value.startsWith("36") || value.startsWith("37")
				|| value.startsWith("38") || value.startsWith("39")
				|| value.startsWith("3A") || value.startsWith("3B")
				|| value.startsWith("3C") || value.startsWith("3D")
				|| value.startsWith("3E") || value.startsWith("3F")
				|| value.startsWith("40") || value.startsWith("40")
				|| value.startsWith("41") || value.startsWith("42")
				|| value.startsWith("43") || value.startsWith("44")
				|| value.startsWith("45") || value.startsWith("46")
				|| value.startsWith("47") || value.startsWith("48")
				|| value.startsWith("49") || value.startsWith("4A")
				|| value.startsWith("4B") || value.startsWith("4C")
				|| value.startsWith("4D") || value.startsWith("4E")
				|| value.startsWith("4F")) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isTV(String value) {
		if (value.startsWith("50") || value.startsWith("51")
				|| value.startsWith("52") || value.startsWith("53")
				|| value.startsWith("54") || value.startsWith("55")
				|| value.startsWith("56") || value.startsWith("57")
				|| value.startsWith("58") || value.startsWith("59")
				|| value.startsWith("5A") || value.startsWith("5B")
				|| value.startsWith("5C") || value.startsWith("5D")
				|| value.startsWith("5E") || value.startsWith("5F")
				|| value.startsWith("60") || value.startsWith("61")
				|| value.startsWith("62") || value.startsWith("63")
				|| value.startsWith("64") || value.startsWith("65")
				|| value.startsWith("66") || value.startsWith("67")
				|| value.startsWith("68") || value.startsWith("69")
				|| value.startsWith("6A") || value.startsWith("6B")
				|| value.startsWith("6C") || value.startsWith("6D")
				|| value.startsWith("6E") || value.startsWith("6F")
				|| value.startsWith("70") || value.startsWith("71")
				|| value.startsWith("72") || value.startsWith("73")
				|| value.startsWith("74") || value.startsWith("75")
				|| value.startsWith("76") || value.startsWith("77")
				|| value.startsWith("78") || value.startsWith("79")
				|| value.startsWith("7A") || value.startsWith("7B")
				|| value.startsWith("7C") || value.startsWith("7D")
				|| value.startsWith("7E") || value.startsWith("7F")
				|| value.startsWith("80")) {
			return true;
		} else {
			return false;
		}
	}

	public static String getStringIO(String Code) {
		String result = "";
		if (Code.equals("00")) {
			result = "0";
		} else if (Code.equals("01")) {
			result = "1";
		} else if (Code.equals("02")) {
			result = "2";
		} else if (Code.equals("03")) {
			result = "3";
		} else if (Code.equals("04")) {
			result = "4";
		} else if (Code.equals("05")) {
			result = "5";
		} else if (Code.equals("06")) {
			result = "6";
		} else if (Code.equals("07")) {
			result = "7";
		} else if (Code.equals("08")) {
			result = "8";
		} else if (Code.equals("09")) {
			result = "9";
		} else if (Code.equals("0A")) {
			result = "A";
		} else if (Code.equals("0B")) {
			result = "B";
		} else if (Code.equals("0C")) {
			result = "C";
		} else if (Code.equals("0D")) {
			result = "D";
		} else if (Code.equals("0E")) {
			result = "E";
		} else if (Code.equals("0F")) {
			result = "F";
		} else {
			result = Code;
		}
		return result;
	}
}
