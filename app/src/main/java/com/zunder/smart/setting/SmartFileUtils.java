package com.zunder.smart.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.model.SmartFile;
import com.zunder.smart.MyApplication;
import com.zunder.smart.model.SmartFile;

import android.os.Environment;

public class SmartFileUtils {

	public static List<SmartFile> list = new ArrayList<SmartFile>();

	public static List<SmartFile> getFiles() {
		list.clear();
//		File sdcardDir = Environment.getExternalStorageDirectory();
		String pathSD = MyApplication.getInstance().getRootPath();

		File file = new File(pathSD);
		final File[] files = file.listFiles();
		if ((files != null) && (files.length > 0)) {
			int len = files.length;
			for (int i = 0; i < len; i++) {
				if (files[i].isDirectory()) {

					SmartFile smartFile = new SmartFile();
					smartFile.setFileName(files[i].getName());
					list.add(smartFile);
				}
			}

		}

		return list;
	}
	public static List<String> getFileNames() {
		List<String> list = new ArrayList<String>();
		String pathSD = MyApplication.getInstance().getRootPath();
		File file = new File(pathSD);
		final File[] files = file.listFiles();
		if ((files != null) && (files.length > 0)) {
			int len = files.length;
			for (int i = 0; i < len; i++) {
				if (files[i].isDirectory()) {
					String fileName=files[i].getName();
					if(fileName.equals("Root")){
						list.add("默认家庭");
					}else if(fileName.equals("image_cache")||fileName.equals("crash")){
						continue;
					}
				}
			}
		}
		list.add("家庭管理");
		return list;
	}
	public static int addFile(String fileName) {

		int result = 0;
		for (SmartFile smartFile : list) {
			if (smartFile.getFileName().equals(fileName)) {
				result = 1;
				break;
			}
		}
		if (result == 0) {
			SmartFile file = new SmartFile();
			file.setFileName(fileName);
			list.add(file);
		}
		return result;
	}

	public static void delFile(String fileName) {

		for (SmartFile smartFile : list) {
			if (smartFile.getFileName().equals(fileName)) {
				list.remove(smartFile);
				break;
			}
		}
	}

	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			if (!file.getName().equals("aduio")) {
				file.delete();
			}
		}
	}
}
