package com.letcome.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.letcome.R;
import com.letcome.mode.ProductsRes;
import com.letcome.mode.UpdateProductRes;
import com.letcome.ui.ProductDetailDlg;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyProductsActivity extends SuperActivity {

    ImageView mImageView;
    Button mCloseBtn,mFavoriteBtn,mChatBtn;
    TextView mProductName,mProductPrice,mSellerFirst,mSellerName;
    LinearLayout mProductLayout;
    List<ProductsRes.Record> mRecords;
    Long mCurrentID;
    Integer mPosition;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mRecords = (List<ProductsRes.Record>)getIntent().getSerializableExtra("records");
        mCurrentID = (Long)getIntent().getSerializableExtra("id");
        mPosition = (Integer)getIntent().getSerializableExtra("position");
        mBitmap = (Bitmap)getIntent().getParcelableExtra("bitmap");



        initView();
        initAction();
    }

    private void initView() {
        mImageView = (ImageView)findViewById(R.id.image_view);
        mCloseBtn = (Button)findViewById(R.id.close_btn);
        mFavoriteBtn = (Button)findViewById(R.id.favorite_btn);
        mChatBtn = (Button)findViewById(R.id.chat_btn);
        mProductName = (TextView)findViewById(R.id.product_name);
        mProductPrice = (TextView)findViewById(R.id.product_price);
        mSellerFirst = (TextView)findViewById(R.id.seller_first);
        mSellerName = (TextView)findViewById(R.id.seller_name);
        mProductLayout = (LinearLayout)findViewById(R.id.product_layout);

    }

    private void initAction() {

        final ProductsRes.Record record = mRecords.get(mPosition);
        mImageView.setImageBitmap(mBitmap);

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

        GradientDrawable mGroupDrawable= (GradientDrawable)mChatBtn.getBackground();

        if (record.getStatus().equals(ProductsRes.STATUS_SELLING)){
            mGroupDrawable.setColor(ContextCompat.getColor(this, R.color.color_009aab));
            mChatBtn.setText("标记为已售出");

        }else {
            mGroupDrawable.setColor(ContextCompat.getColor(this, R.color.nav_red));
            mChatBtn.setText("再次卖出");
        }
        mChatBtn.setBackground(mGroupDrawable);
        mChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (record.getStatus().equals(ProductsRes.STATUS_SELLING)) {
                    changeSold();
                } else {
                    changeSelling();
                }
            }
        });

        mFavoriteBtn.setVisibility(View.GONE);

        mProductName.setText(record.getDescription());
        mProductPrice.setText("¥"+record.getPrice());
        mSellerName.setText(record.getFullname());
        mSellerFirst.setText(record.getFullname().substring(0, 1).toUpperCase());

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProductsActivity.this.finish();

            }
        });

        mProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailDlg dlg = new ProductDetailDlg(MyProductsActivity.this,record);
                dlg.show();
            }
        });

        String imageUri = record.getImagepath();
        imageLoader.displayImage(imageUri, mImageView);
    }

    public void changeStatus(String status){
        ProductsRes.Record record = mRecords.get(mPosition);
        UpdateProductRes.Params param = new UpdateProductRes.Params();
        param.setId(record.getId());
        param.setStatus(status);
        UpdateProductRes.doRequest(param, this);
        showWaitDialog(RequestInfo.UPDATEPRODUCT);
    }

    public void changeSold(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("请确认")
                .setContentText("您确定要将此宝贝标记为已售出？")
                .setCancelText("不，我再想想")
                .setConfirmText("是，立即标记!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        changeStatus(ProductsRes.STATUS_SOLD);
                    }
                })
                .show();
    }

    void changeSelling(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("请确认")
                .setContentText("您确定要再次出售这个宝贝？")
                .setCancelText("不，我再想想")
                .setConfirmText("是，再次出售!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        changeStatus(ProductsRes.STATUS_SELLING);

                    }
                })
                .show();
    }

    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
        if(RequestInfo.UPDATEPRODUCT == info){
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("成功")
                    .setContentText("您的宝贝已成功更改状态！")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            MyProductsActivity.this.finish();

                        }
                    })
                    .show();
        }
    }
}
