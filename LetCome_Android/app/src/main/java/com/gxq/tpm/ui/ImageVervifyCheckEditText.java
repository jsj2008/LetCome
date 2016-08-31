package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

public class ImageVervifyCheckEditText extends CheckEditText {

    public final static int MAX_INPUT_SIZE = 4;

    public ImageVervifyCheckEditText(Context context) {
        this(context, null);
    }

    public ImageVervifyCheckEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    protected int getDefaultMaxLength() {
        return MAX_INPUT_SIZE;
    }
    
    @Override
    protected int getDefaultHint() {
        return R.string.phone_verify_hint;
    }

    @Override
    protected boolean checkEditText() {
    	return Util.checkImgVerify(getText().toString().trim());
    }
    
    @Override
    protected int getErrorText(Error error) {
    	if (error != Error.NONE)
    		return R.string.phone_image_vervify_check_error_tips;
        return super.getErrorText(error);
    }
}
