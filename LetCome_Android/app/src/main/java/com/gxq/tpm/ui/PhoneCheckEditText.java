package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

public class PhoneCheckEditText extends CheckEditText {
	public final static int MAX_INPUT_SIZE_1 = 11;
	public final static int MAX_INPUT_SIZE_2 = 13;

	// isinput
	private boolean isRun = false;
	
	public PhoneCheckEditText(Context context) {
		this(context, null);
	}

	public PhoneCheckEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setInputType(InputType.TYPE_CLASS_PHONE);
	}

	@Override
	protected int getDefaultMaxLength() {
		if(isPhoneNumFormat)
		    return MAX_INPUT_SIZE_2;
		else 
			return MAX_INPUT_SIZE_1;
	}

	@Override
	protected int getDefaultHint() {
		return R.string.phone_number_hint;
	}

	@Override
	protected boolean checkEditText() {
		String s = getText().toString().trim();
		s = s.replace(" ", "");
		return Util.checkMoblie(s);
	}

	@Override
	protected int getErrorText(Error error) {
		if (error != Error.NONE)
			return R.string.phone_number_check_error_tips;
		return super.getErrorText(error);
	}

	@Override
	protected void setPhoneFormat(int phoneFormat) {
		isPhoneNumFormat = true;
    	AddSpaceLen=space.length();
		// self_define style
		switch (phoneFormat) {
		case CDD:
			stop1 = 3;
			stop2 = 3 + 4 + AddSpaceLen;
			break;
		case CCE:
			stop1 = 3;
			stop2 = 3 + 3 + AddSpaceLen;
			break;
		case DDC:
			stop1 = 4;
			stop2 = 4 + 4 + AddSpaceLen;
			break;
			//default_format_style
		default:
			stop1 = 3;
			stop2 = 3 + 4 + AddSpaceLen;
			break;
		}
	}

	@Override
	protected void editOnTextChanged(CharSequence s, int start, int before, int count, EditText twEditText) {
		super.editOnTextChanged(s, start, before, count, twEditText);
		if (!isPhoneNumFormat)
			return;

		if (isRun) {
			isRun = false;
			return;
		}
		
		isRun = true;
		String sourceStr = s.toString();
		
		// remove space input
		String emptyStr = sourceStr.substring(start, start + count);
		if (emptyStr.contains(" ") & (before < count)) {
			setTextSel(getShowFormtPhoneStr(sourceStr), start + emptyStr.replace(" ", "").length(), twEditText);
			return;
		}
		
		//selection_position
		int sel = 0;
		
		// add_char
		if ((before < count) && (start == stop1 || start == stop2)) {
			sel = sel + AddSpaceLen;
			
		// delete_char
		} else if ((before > count) && (start == stop1 + AddSpaceLen || start == stop2 + AddSpaceLen)) {
			sel = sel - AddSpaceLen;
		}
		sel = sel + start + count;
		setTextSel(getShowFormtPhoneStr(sourceStr), sel, twEditText);

	}
	// showFomatPhoneNum
	private String getShowFormtPhoneStr(String sourceStr) {
		String str = sourceStr.replace(" ", "");
		String inputstr = "";

		if (str.length() > stop1 && str.length() < stop2 - AddSpaceLen) {
			
			inputstr = str.substring(0, stop1) 
					+ space + 
					str.substring(stop1, str.length());

		} else if (str.length() >= stop2 - AddSpaceLen && str.length() <= mMinLength) {
			
			inputstr = str.substring(0, stop1)
					+ space + 
					str.substring(stop1, stop2 - AddSpaceLen) 
			        + space + 
			        str.substring(stop2 - AddSpaceLen, str.length());
			
		} else {
			
			inputstr = str;
		}
		return inputstr;
	}

	private void setTextSel(String str, int length, EditText twEditText) {
		twEditText.setText(str);
		twEditText.setSelection(length);
	}

	public String getPhoneText() {	
		return super.getText().toString().replace(space, "");
	}

	
}
