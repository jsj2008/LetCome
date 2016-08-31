package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.TextWatcherAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;

public abstract class CheckEditText extends EditText {

	public final static int DEFAULT_VALUE = 0;

    public static enum Error {
    	NONE, EMPTY, RANGE, CHECK
    };
    
    protected int mMinLength;
    protected boolean isPhoneNumFormat = false;
    protected String space = " ";
    protected int AddSpaceLen = 0;
    protected final static int CDD = 1;
    protected final static int CCE = 2;
    protected final static int DDC = 3;
    protected int stop1 = DEFAULT_VALUE;
    protected int stop2 = DEFAULT_VALUE;

    private TextWatcher mWatcher = new EditTextWatcher(this);
    private OnCheckEditTextChangeListener mChangeListener;

    public CheckEditText(Context context) {
        this(context, null);
    }

    public CheckEditText(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckEditText);
        Resources resource = getResources();

        int textSize = a.getDimensionPixelSize(R.styleable.CheckEditText_android_textSize, DEFAULT_VALUE);
        if (DEFAULT_VALUE == textSize) setTextSize(TypedValue.COMPLEX_UNIT_PX, getDefaultTextSize());

        int textColor = a.getColor(R.styleable.CheckEditText_android_textColor, DEFAULT_VALUE);
        if (DEFAULT_VALUE == textColor)  setTextColor(resource.getColor(getDefaultTextColor()));

        int hintTextColor = a.getColor(R.styleable.CheckEditText_android_textColorHint, DEFAULT_VALUE);
        if (DEFAULT_VALUE == hintTextColor) setHintTextColor(resource.getColor(getDefaultTextColorHint()));

        int background = a.getColor(R.styleable.CheckEditText_android_background, DEFAULT_VALUE);
        if (DEFAULT_VALUE == background) setBackgroundResource(getDefaultBackgroundResource());

        String editHintText = a.getString(R.styleable.CheckEditText_android_hint);
        if (editHintText == null) setHint(getDefaultHint());
        
        int phoneformat = a.getInt(R.styleable.CheckEditText_phoneFormat, DEFAULT_VALUE);
    	setPhoneFormat(phoneformat);
        
        int maxLength = a.getInt(R.styleable.CheckEditText_android_maxLength, DEFAULT_VALUE);
        if (DEFAULT_VALUE == maxLength) setMaxLength(getDefaultMaxLength());
        
        mMinLength = a.getInt(R.styleable.CheckEditText_minLength, getDefaultMinLength());
        
        a.recycle();

        setSingleLine(true);
        setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        addTextChangedListener(mWatcher);
    }

    public CheckEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, null);

    }
    
    protected int getDefaultTextSize() {
    	return getResources().getDimensionPixelSize(R.dimen.font_size_xhdpi_16);
    }
    
    protected int getDefaultTextColor() {
    	return R.color.text_color_title;
    }
    
    protected int getDefaultTextColorHint() {
    	return R.color.text_color_info;
    }
    
    protected int getDefaultHint() {
    	return DEFAULT_VALUE;
    }
    
    protected int getDefaultBackgroundResource() {
    	return R.color.white_color;
    }
    
    protected int getDefaultMaxLength() {
    	return DEFAULT_VALUE;
    }
    
    protected int getDefaultMinLength() {
        return getDefaultMaxLength();
    }
    
    protected void setPhoneFormat(int phoneFormat) {
    }

    private void setMaxLength(int maxlength) {
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlength)});
    }
    
    public final Error checkText() {
    	String s = getText().toString();
        if (null == s || s.length() == 0) {
            return Error.EMPTY;
        } else if (s.length() < mMinLength) {
        	return Error.RANGE;
        } else if (!checkEditText()) {
        	return Error.CHECK;
        } else {
        	return Error.NONE;
        }
    }

    protected boolean checkEditText() {
    	return true;
    }
    
    protected int getErrorText(Error error) {
    	return 0;
    }
    
    public final int getErrorText() {
    	return getErrorText(checkText());
    }
    
    private class EditTextWatcher extends TextWatcherAdapter {
        private EditText mEditText;

        public EditTextWatcher(EditText phoneEditText) {
            this.mEditText = phoneEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editOnTextChanged(s, start, before, count, mEditText);
            if (mChangeListener != null) {
        		mChangeListener.onCheckEditTextChanged(checkText());
        	}
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    protected void editOnTextChanged(CharSequence s, int start, int before, int count, EditText twEditText) {
    }
    
    public void setOnCheckEditTextChangeListener(OnCheckEditTextChangeListener listener) {
    	this.mChangeListener = listener;
    }
    
    public static interface OnCheckEditTextChangeListener {
    	public void onCheckEditTextChanged(Error error);
    }

}