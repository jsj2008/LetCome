package com.gxq.tpm.tools;

import com.letcome.App;

import android.util.Log;

public class Print {
	private static final boolean isTest = App.instance().isTest();
	public static void e(String tag,String msg) {
		if (isTest) Log.e(tag, msg);
	}

	public static void e(String tag,String msg,Throwable tr) {
		if (isTest) Log.e(tag, msg, tr);
	}

	public static void d(String tag,String msg) {
		if (isTest)  Log.d(tag, msg);
	}

	public static void w(String tag,String msg) {
		if (isTest)  Log.w(tag, msg);
	}
	public static void w(String tag,String msg,Throwable tr) {
		if (isTest)  Log.w(tag, msg, tr);
	}
	public static void v(String tag,String msg) {
		if (isTest)  Log.v(tag, msg);
	}

	public static void i(String tag,String msg) {
		if (isTest)  Log.i(tag, msg);
	}
}
