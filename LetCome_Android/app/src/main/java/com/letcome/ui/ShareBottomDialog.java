package com.letcome.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.letcome.App;
import com.letcome.R;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareBottomDialog extends BottomBaseDialog<ShareBottomDialog> implements IUiListener{
//    @BindView(R.id.ll_wechat_friend_circle) LinearLayout mLlWechatFriendCircle;
//    @BindView(R.id.ll_wechat_friend) LinearLayout mLlWechatFriend;
    @BindView(R.id.ll_qq) LinearLayout mLlQq;
//    @BindView(R.id.ll_sms) LinearLayout mLlSms;
    String mDesc,mImageUrl;

    public ShareBottomDialog(Context context, View animateView) {
        super(context, animateView);

    }

    public ShareBottomDialog(Context context,String desc,String imageUrl) {
        super(context);
        mDesc = desc;
        mImageUrl = imageUrl;
    }

    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        View inflate = View.inflate(mContext, R.layout.dialog_share, null);
        ButterKnife.bind(this, inflate);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {

//        mLlWechatFriendCircle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                T.showShort(mContext, "朋友圈");
//                dismiss();
//            }
//        });
//        mLlWechatFriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                T.showShort(mContext, "微信");
//                dismiss();
//            }
//        });
        mLlQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                T.showShort(mContext, "QQ");
                dismiss();
                shareQQ();
            }
        });
//        mLlSms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                T.showShort(mContext, "短信");
//                dismiss();
//            }
//        });
    }

    void shareQQ(){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);

        params.putString(QQShare.SHARE_TO_QQ_TITLE, "来自毫买卖的分享");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mDesc);
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mImageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "毫买卖");
//        //分享的类型(必填)
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//        //需要分享的本地图片路径(必填)
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,  mImageUrl);
//        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "毫买卖");
//        //分享额外选项
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

        App.instance().getTencent().shareToQQ((Activity)mContext, params, ShareBottomDialog.this);
    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
