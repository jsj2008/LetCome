package com.letcome.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.flyco.animation.Attention.Tada;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.letcome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by rjt on 16/10/18.
 */

public class QQDialog extends BaseDialog<QQDialog> implements TextWatcher{
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;

    @BindView(R.id.qq_et)
    EditText mQQEt;


    public QQDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new Tada());

        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.dialog_qq_base, null);
        ButterKnife.bind(this, inflate);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mQQEt.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        refreshBtnState();
    }

    public void refreshBtnState(){
        if (mQQEt.getText().length()>0){
            mBtnSubmit.setEnabled(true);
        }else{
            mBtnSubmit.setEnabled(false);
        }
    }

    public String getQQ(){
        return mQQEt.getText().toString();
    }
}