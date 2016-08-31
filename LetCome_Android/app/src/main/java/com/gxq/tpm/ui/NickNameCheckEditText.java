package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

public class NickNameCheckEditText extends CheckEditText {

    public final static int MAX_INPUT_SIZE = 6;
    public final static int MIN_INPUT_SIZE = 2;

    public NickNameCheckEditText(Context context) {
        this(context, null);
    }

    public NickNameCheckEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    protected int getDefaultMaxLength() {
        return MAX_INPUT_SIZE;
    }
    
    @Override
    protected int getDefaultMinLength() {
    	return MIN_INPUT_SIZE;
    }
    
    @Override
    protected int getDefaultHint() {
        return R.string.nick_name_hint;
    }
    
    @Override
    protected boolean checkEditText() {
    	return Util.checkNickName(getText().toString().trim());
    }

    @Override
    protected int getErrorText(Error error) {
    	if (error != Error.NONE)
    		return R.string.nick_name_check_error_tips;
        return super.getErrorText(error);
    }
}
