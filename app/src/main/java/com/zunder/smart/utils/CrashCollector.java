package com.zunder.smart.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import com.zunder.smart.MyApplication;
import com.zunder.smart.activity.StartActivity;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.utils.FileUtil.DataFileHelper;
import com.zunder.smart.webservice.forward.AiuiErrServiceUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.SystemInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

/**
 * 崩溃日志收集器。
 * 
 * @author <a href="http://www.xfyun.cn">讯飞开放平台</a>
 * @date 2016年6月27日 下午7:48:02 
 *
 */
public class CrashCollector implements UncaughtExceptionHandler {
	private static final String TAG = "CrashCollector";
	
	private static final String CRASH_LOG_DIR =MyApplication.getInstance().getRootPath()+ "/crash/";
	
	private UncaughtExceptionHandler mDefaultHandler;
	
	private static CrashCollector INSTANCE = new CrashCollector();
	
	private FileUtil.DataFileHelper mCrashLogHelper;
	
	private CrashCollector() {
		
	}
	
	public static CrashCollector getInstance() {
		return INSTANCE;
	}
	
	public void init(Context context) {
		
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(CrashCollector.this);
		
		mCrashLogHelper = FileUtil.createFileHelper(CRASH_LOG_DIR);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!saveCrashLog(ex) && null != mDefaultHandler) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {  
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}  
			
			// 退出程序
            Intent intent = new Intent(MyApplication.getInstance(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			MyApplication.getInstance().startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());  
			System.exit(1);  
		}
	}
	
	private boolean saveCrashLog(Throwable ex) {
		if (null == ex) {
			return false;
		}
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		
		while (null != cause) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		String crashLog = writer.toString();
		printWriter.close();
		AiuiErrServiceUtils.instance.postString1(SystemInfo.getMasterID(MyApplication.getInstance()),"云知声日志",crashLog, AppTools.getCurrentTime());
		mCrashLogHelper.createFile("", FileUtil.SURFFIX_TXT, false);
		mCrashLogHelper.write(crashLog.getBytes(), true);
		mCrashLogHelper.closeWriteFile();
		return true;
	}

}
