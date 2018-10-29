package com.zunder.smart.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.zunder.smart.MyApplication;

public class ProgressDialogUtils {
	private static ProgressDialog mProgressDialog;

	/**
	 * ProgressDialog
	 * @param activity
	 * @param message
	 */
	public static void showProgressDialog(Activity activity, CharSequence message){
		if(mProgressDialog == null){
			mProgressDialog = ProgressDialog.show(activity, "", message);
		}
		mProgressDialog.show();
	}
	
	/**
	 * �ر�ProgressDialog
	 */
	public static void dismissProgressDialog(){
		if(mProgressDialog != null){
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
}
