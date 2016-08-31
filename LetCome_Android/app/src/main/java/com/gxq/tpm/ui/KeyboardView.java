package com.gxq.tpm.ui;

import com.letcome.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class KeyboardView extends LinearLayout implements View.OnClickListener {

	private final static int SUM = 10;
	private final static int DECIMAL = 2;
	
	public final static int EDIT_MODE_BUY 		= 1;
	public final static int EDIT_MODE_SELL 	= 2;
	
	private String mText = "";

	private OnKeyboardListener mListener;
	
	public KeyboardView(Context context) {
		this(context, null);
	}

	public KeyboardView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public KeyboardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		findViewById(R.id.btn_key_one).setOnClickListener(this);
		findViewById(R.id.btn_key_two).setOnClickListener(this);
		findViewById(R.id.btn_key_three).setOnClickListener(this);
		findViewById(R.id.btn_key_four).setOnClickListener(this);
		findViewById(R.id.btn_key_five).setOnClickListener(this);
		findViewById(R.id.btn_key_six).setOnClickListener(this);
		findViewById(R.id.btn_key_seven).setOnClickListener(this);
		findViewById(R.id.btn_key_eight).setOnClickListener(this);
		findViewById(R.id.btn_key_nine).setOnClickListener(this);
		findViewById(R.id.btn_key_zero).setOnClickListener(this);
		findViewById(R.id.btn_key_hundred).setOnClickListener(this);
		findViewById(R.id.btn_key_delete).setOnClickListener(this);
		findViewById(R.id.btn_key_full_fund).setOnClickListener(this);
		findViewById(R.id.btn_key_confirm).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		Button btn;
		switch (id) {
		case R.id.btn_key_one:
		case R.id.btn_key_two:
		case R.id.btn_key_three:
		case R.id.btn_key_four:
		case R.id.btn_key_five:
		case R.id.btn_key_six:
		case R.id.btn_key_seven:
		case R.id.btn_key_eight:
		case R.id.btn_key_nine:
		case R.id.btn_key_zero:
		case R.id.btn_key_hundred:
			btn = (Button) v;
			if (mText.length() == 0) {
				mText = btn.getText().toString();
				if (id == R.id.btn_key_zero || id == R.id.btn_key_hundred) {
					mText = "0";
				}
			} else if (mText.length() == 1 && mText.equals("0")) {
				mText = btn.getText().toString();
				if (id == R.id.btn_key_zero || id == R.id.btn_key_hundred) {
					mText = "0";
				}
			} else if (mText.contains(".") && mText.indexOf(".") < mText.length() - DECIMAL) {
				break;
			} else if (mText.length() >= SUM) {
				break;
			} else {
				mText += btn.getText().toString();
				if (mText.length() > SUM) {
					mText = mText.substring(0, SUM);
				}
			}
			if (mListener != null)
				mListener.setText(mText);
			break;
		case R.id.btn_key_delete:
			if (mText.length() > 1) {
				mText = mText.substring(0, mText.length() - 1);
			} else {
				mText = "0";
			}
			if (mListener != null)
				mListener.setText(mText);
			break;
		case R.id.btn_key_full_fund:
			if (mListener != null)
				mListener.onKeyboardFinish(true);
			break;
		case R.id.btn_key_confirm:
			if (mListener != null){
				mListener.onKeyboardFinish(false);
			}
			break;
		}
	}
	
	public void setOnKeyboardListener(OnKeyboardListener listener) {
		mListener = listener;
	}
	
	public void setText(String text, int editMode) {
		mText = text;
		if (editMode == EDIT_MODE_BUY) {
			((Button) findViewById(R.id.btn_key_zero)).setText(R.string.keyboard_zero);
			((Button) findViewById(R.id.btn_key_full_fund)).setText(R.string.keyboard_full_fund);
		} else if (editMode == EDIT_MODE_SELL) {
			((Button) findViewById(R.id.btn_key_zero)).setText(R.string.keyboard_thousand);
			((Button) findViewById(R.id.btn_key_full_fund)).setText(R.string.strategy_selling_total_amount);
		}
	}
	
	public void setConfirmEnable(boolean enable) {
		findViewById(R.id.btn_key_confirm).setEnabled(enable);
	}

	public static interface OnKeyboardListener {
		public void setText(String text);
		public void onKeyboardFinish(boolean fullFund);
	}
	
}
