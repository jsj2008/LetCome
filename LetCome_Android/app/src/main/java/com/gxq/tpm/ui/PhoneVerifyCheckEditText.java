package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

public class PhoneVerifyCheckEditText extends CheckEditText {

    public final static int MAX_INPUT_SIZE = 6;

    public PhoneVerifyCheckEditText(Context context) {
        this(context, null);
    }

    public PhoneVerifyCheckEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected int getDefaultMaxLength() {
        return MAX_INPUT_SIZE;
    }
    
    @Override
    protected int getDefaultHint() {
        return R.string.phone_number_verify_hint;
    }
    
    @Override
    protected boolean checkEditText() {
    	return Util.checkNumVerify(getText().toString().trim());
    }

    @Override
    protected int getErrorText(Error error) {
    	if (error != Error.NONE)
    		return R.string.phone_vervify_check_error_tips;
        return super.getErrorText(error);
    }
}
