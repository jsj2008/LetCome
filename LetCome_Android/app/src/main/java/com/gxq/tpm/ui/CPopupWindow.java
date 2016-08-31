package com.gxq.tpm.ui;

import com.letcome.R;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class CPopupWindow {
	private Activity mContext;
	
	private PopupWindow mPopupWindow;
	private View mView;
	private OnDismissListener mDismissListener;
	private float mAlpha = 0.5f;
	
	public CPopupWindow(Activity context) {
		this.mContext = context;
		mPopupWindow = new PopupWindow(context);
		
		mPopupWindow.setFocusable(true);  
		mPopupWindow.setOutsideTouchable(true);
		
		mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		
		ColorDrawable drawable = new ColorDrawable(mContext.getResources().getColor(R.color.transparent));
		mPopupWindow.setBackgroundDrawable(drawable);
	}
	
	public void setContentView(int layoutResId) {
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		mView = layoutInflater.inflate(layoutResId, null, false);
		mPopupWindow.setContentView(mView);
	}
	
	public void setContentView(View view) {
		mPopupWindow.setContentView(view);
		mView = view;
	}
	
	public void setAnimationStyle(int animationStyle) {
		mPopupWindow.setAnimationStyle(animationStyle);
	}
	
	public void setWidth(int width) {
		mPopupWindow.setWidth(width);
	}
	
	public void setHeight(int height) {
		mPopupWindow.setHeight(height);
	}
	
	public View findViewById(int id) {
		if (mView == null) return null;
		
		return mView.findViewById(id);
	}
	
	public void setOutsideTouchable(boolean touchable) {
		mPopupWindow.setOutsideTouchable(touchable);
	}
	
	public void showAtLocation(View parent, int gravity, int x, int y) {
		addDismissListener();
		mPopupWindow.showAtLocation(parent, gravity, x, y); 
	}
	
	public void showAsDropDown(View anchor) {
		addDismissListener();
		mPopupWindow.showAsDropDown(anchor);
	}
	
	public void setPopupWindowOnClickListener(int id, OnPopupWindowClickListener listener) {
		View view = findViewById(id);
		
		if (view != null) {
			view.setOnClickListener(new PopupWindowClickListener(listener));
		}
	}
	
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		addDismissListener();
		mPopupWindow.showAsDropDown(anchor, xoff, yoff);
	}
	
	private void addDismissListener() {
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				if (mDismissListener != null) {
					mDismissListener.onDismiss();
				}
				
				backgroundAlpha(1f);
				mPopupWindow = null;
			}
		});
		backgroundAlpha(mAlpha);
	}
	
	private void backgroundAlpha(float bgAlpha) {  
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();  
        lp.alpha = bgAlpha; //0.0-1.0
        mContext.getWindow().setAttributes(lp);  
        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
	
	public void dismiss() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			backgroundAlpha(1f);

			mPopupWindow = null;
		} 
	}
	
	public void setOnDismissListener (OnDismissListener listener) {
		mDismissListener = listener;
	}
	
	private static class PopupWindowClickListener implements View.OnClickListener {
		private OnPopupWindowClickListener mListener;
		
		public PopupWindowClickListener(OnPopupWindowClickListener listener) {
			this.mListener = listener;
		}
		
		@Override
		public void onClick(View v) {
			if (mListener != null)
				mListener.onPopupWindowClick(v);
		}
	}
	
	public void setAlpha(float alpha) {
		this.mAlpha = alpha;
	}
	
	public static interface OnPopupWindowClickListener {
		public void onPopupWindowClick(View view);
	}
	
}
