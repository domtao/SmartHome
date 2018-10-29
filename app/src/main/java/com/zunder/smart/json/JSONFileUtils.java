package com.zunder.smart.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import android.util.Log;

public class JSONFileUtils {

	public static void saveJSONToFile(File f, String json) {
		try {
			if (!f.exists())
				f.createNewFile();
			FileWriter writer = new FileWriter(f);
			writer.write(json, 0, json.length());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static String getJsonStringFromFile(File f) {
		String readStr = "";
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(f), 10);
			String len = "";
			while ((len = bufferedReader.readLine()) != null) {
				readStr += len;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return readStr;

	}

	public static StringBuffer openbook(File file) {
		StringBuffer result = null;
		try {
			long lLen = file.length();
			int i = (int) lLen;
			result = new StringBuffer();
			MappedByteBuffer mbb = new RandomAccessFile(file, "r").getChannel()
					.map(FileChannel.MapMode.READ_ONLY, 0, lLen);
			byte[] buf = new byte[i];
			for (int j = 0; j < lLen; j++) {
				if (j >= 0 && j <= lLen) {
					buf[j] = mbb.get(j);
				}

			}
			result.append(new String(buf, 0, buf.length, "UTF-8"));

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public static void readFileByLines(File file) {

		BufferedReader reader = null;
		try {
			System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// ��ʾ�к�
				Log.e("TestCommad", "line " + line + ": " + tempString + "");
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}
