package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;

public class PwdCheckEditText extends CheckEditText {
    public final static int MIN_INPUT_SIZE = 6;
    public final static int MAX_INPUT_SIZE = 18;

    public PwdCheckEditText(Context context) {
        this(context, null);
    }

    public PwdCheckEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setTransformationMethod(PasswordTransformationMethod.getInstance());
        postInvalidate();

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
        return R.string.phone_pwd_hint;
    }
    
    @Override
    protected boolean checkEditText() {
    	return Util.checkPwd(getText().toString().trim());
    }
    
    @Override
    protected int getErrorText(Error error) {
    	if (error != Error.NONE)
    		return R.string.phone_pwd_check_error_tips;
    	return super.getErrorText(error);
    }

}
