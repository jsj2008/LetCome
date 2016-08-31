package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CCheckBox extends RelativeLayout{
	private boolean mIsChecked;
	
	private OnCheckListener mCheckListener;
	private ImageView mCheckBtn;
	private TextView mLeftText;
	private TextView mRightText;

	private int mCheckBoxUnChecked /*= R.drawable.check_box*/;
	private int mCheckBoxChecked /*= R.drawable.check_box_checked*/;
	private int mCheckBoxDisable;
	
	public CCheckBox(Context context) {
		this(context, null);
	}

	public CCheckBox(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CCheckBox);
		Resources resources = getResources();
		int textSize = a.getDimensionPixelSize(R.styleable.CCheckBox_cb_textSize, 
				resources.getDimensionPixelSize(R.dimen.font_size_xhdpi_14));
		
		int textColor = a.getColor(R.styleable.CCheckBox_cb_textColor, 
				resources.getColor(R.color.text_color_title));
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.c_check_box, this, true);
		
		mCheckBtn = (ImageView) findViewById(R.id.checkBtn);
		mLeftText = (TextView) findViewById(R.id.leftCheckText);
		mRightText = (TextView) findViewById(R.id.rightCheckText);
		
		String textValue = a.getString(R.styleable.CCheckBox_cb_leftText);
		if (textValue != null) {
			mLeftText.setVisibility(View.VISIBLE);
			mLeftText.setText(textValue);
			mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			mLeftText.setTextColor(textColor);	
		} else {
			if (!isInEditMode()) {
				mLeftText.setVisibility(View.GONE);				
			}
		}
		
		textValue = a.getString(R.styleable.CCheckBox_cb_rightText);
		if (textValue != null) {
			mRightText.setVisibility(View.VISIBLE);
			mRightText.setText(textValue);
			mRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			mRightText.setTextColor(textColor);	
		} else {
			if (!isInEditMode()) {
				mRightText.setVisibility(View.GONE);
			}
		}
		
		mIsChecked = a.getBoolean(R.styleable.CCheckBox_checked, false);
		mCheckBoxChecked = a.getResourceId(R.styleable.CCheckBox_checkedBackground, 
				R.drawable.checkbox_s);
		mCheckBoxUnChecked = a.getResourceId(R.styleable.CCheckBox_uncheckedBackground,
				R.drawable.checkbox);
		mCheckBoxDisable = a.getResourceId(R.styleable.CCheckBox_uncheckedBackground,
				R.drawable.checkbox);
		setChecked(mIsChecked);
		
		a.recycle();
		
		if (isInEditMode())
			return;
		
		setClick();
	}
	
	private void setClick() {
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mIsChecked = !mIsChecked;
				if (mIsChecked) {
					mCheckBtn.setBackgroundResource(mCheckBoxChecked);
				}else {
					mCheckBtn.setBackgroundResource(mCheckBoxUnChecked);
				}
				if (mCheckListener != null) mCheckListener.checkListener(mIsChecked);
			}
		});
		mCheckBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mIsChecked = !mIsChecked;
				if (mIsChecked) {
					mCheckBtn.setBackgroundResource(mCheckBoxChecked);
				}else {
					mCheckBtn.setBackgroundResource(mCheckBoxUnChecked);
				}
				if (mCheckListener != null) mCheckListener.checkListener(mIsChecked);
			}
		});
	}
//	public void setInvisible(){
//		checkBtn.setBackgroundResource(R.drawable.lib_check_box_not_check);
//		checkBtn.setEnabled(false);
//
//	}
	public boolean isChecked() {
		return mIsChecked;
	}

	public void setChecked(boolean isChecked) {
		this.mIsChecked = isChecked;
		if (isChecked) {
			mCheckBtn.setBackgroundResource(mCheckBoxChecked);
		}else {
			if (!isInEditMode())
				mCheckBtn.setBackgroundResource(mCheckBoxUnChecked);
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			mCheckBtn.setBackgroundResource(mCheckBoxUnChecked);
			mLeftText.setTextColor(Util.transformColor(R.color.text_color_title));
		} else {
			mCheckBtn.setBackgroundResource(mCheckBoxDisable);
			mLeftText.setTextColor(Util.transformColor(R.color.color_c5c5c5));
		}
		mCheckBtn.setEnabled(enabled);
		this.mIsChecked = false;
		super.setEnabled(enabled);
	}

	public void setCheckBoxUnchecked(int check_box) {
		this.mCheckBoxUnChecked = check_box;
	}

	public void setCheckBoxChecked(int check_box_checked) {
		this.mCheckBoxChecked = check_box_checked;
	}
	
	public void setLeftText(int resId) {
		mLeftText.setText(resId);
	}
	
	public void setLeftText(String text) {
		mLeftText.setText(text);
	}
	
	public void setRightText(int resId) {
		mRightText.setText(resId);
	}
	
	public void setRightText(String text, OnClickListener listener) {
		mRightText.setText(text);
		mRightText.setOnClickListener(listener);
	}
	
	public void setRightText(String text) {
		mRightText.setText(text);
	}

	public OnCheckListener getCheckListener() {
		return mCheckListener;
	}

	public void setCheckListener(OnCheckListener checkListener) {
		this.mCheckListener = checkListener;
	}

	public static interface OnCheckListener {
		void checkListener(boolean isChecked);
	}
}
