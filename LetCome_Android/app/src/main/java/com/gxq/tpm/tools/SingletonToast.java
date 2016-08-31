package com.gxq.tpm.tools;


import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;

public class SingletonToast {
	private static SingletonToast instance = null;
	private Toast mToast = null;
	private Context mContext;

	private SingletonToast() {

	}

	public static SingletonToast getInstance() {
		if(instance == null) {
			instance = new SingletonToast();
		}
		return instance;
	}

	public Toast makeText(Context context, String text,int duration) {
		if(mToast == null || mContext != context) {
			mToast = Toast.makeText(context, text, duration);
			mContext = context;
		} else {
			mToast.setDuration(duration);
			mToast.setText(text);
		}

		return mToast;
	}

	public Toast makeText(Context context, int resId,int duration) {
		String text = context.getString(resId);
		return makeText(context, text, duration);
	}

	public Toast makeText(Context context, String text,int duration,int gravity, int xOffset,int yOffset) {
		mToast = makeText(context, text, duration);
		if(mToast != null) {
			mToast.setGravity(gravity, xOffset, yOffset);
		}
		return mToast;
	}

	public Toast makeText(Context context, int resId, int duration,int gravity, int xOffset, int yOffset) {
		String text = context.getString(resId);
		return makeText(context,text,duration,gravity,xOffset,yOffset);
	}

	public Toast makeTextWithSpecificGravity(Context context, String text,int duration) {
		mToast = makeText(context, text, duration);
		if(mToast != null) {
			mToast.setGravity(Gravity.BOTTOM, 0, App.instance().dip2px(GlobalConstant.TOAST_OFFSET_Y));
		}
		return mToast;
	}

	public Toast makeTextWithSpecificGravity(Context context, int resId,int duration) {
		String text = context.getString(resId);
		return makeTextWithSpecificGravity(context,text,duration);
	}

}
