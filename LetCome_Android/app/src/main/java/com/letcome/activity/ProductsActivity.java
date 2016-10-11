package com.letcome.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.letcome.R;
import com.letcome.mode.DoFavoriteRes;
import com.letcome.mode.ProductsRes;
import com.letcome.mode.UnFavoriteRes;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ProductsActivity extends SuperActivity {

    ImageView mImageView;
    Button mCloseBtn, mFavoriteBtn, mChatBtn,mUnFavoriteBtn;
    TextView mProductName, mProductPrice, mSellerFirst, mSellerName;
    LinearLayout mProductLayout;
    List<ProductsRes.Record> mRecords;
    Long mCurrentID;
    Integer mPosition;
    Bitmap mBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mRecords = (List<ProductsRes.Record>) getIntent().getSerializableExtra("records");
        mCurrentID = (Long) getIntent().getSerializableExtra("id");
        mPosition = (Integer) getIntent().getSerializableExtra("position");
        mBitmap = (Bitmap) getIntent().getParcelableExtra("bitmap");
        initView();
        initAction();

    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.image_view);
        mCloseBtn = (Button) findViewById(R.id.close_btn);
        mFavoriteBtn = (Button) findViewById(R.id.favorite_btn);
        mUnFavoriteBtn = (Button) findViewById(R.id.unfavorite_btn);
        mChatBtn = (Button) findViewById(R.id.chat_btn);
        mProductName = (TextView) findViewById(R.id.product_name);
        mProductPrice = (TextView) findViewById(R.id.product_price);
        mSellerFirst = (TextView) findViewById(R.id.seller_first);
        mSellerName = (TextView) findViewById(R.id.seller_name);
        mProductLayout = (LinearLayout) findViewById(R.id.product_layout);



    }

    private void initAction() {

        final ProductsRes.Record record = mRecords.get(mPosition);
        mImageView.setImageBitmap(mBitmap);

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance


        mProductName.setText(record.getDescription());
        mProductPrice.setText("¥" + record.getPrice());
        mSellerName.setText(record.getFullname());
        mSellerFirst.setText(record.getFullname().substring(0, 1).toUpperCase());

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsActivity.this.finish();

            }
        });

        mProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.create(ProductsActivity.this, record);
            }
        });

        if (record.getIs_favorite().equals("Y")){
            mUnFavoriteBtn.setVisibility(View.VISIBLE);
            mFavoriteBtn.setVisibility(View.GONE);
        }else{
            mUnFavoriteBtn.setVisibility(View.GONE);
            mFavoriteBtn.setVisibility(View.VISIBLE);
        }

        mChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="mqqwpa://im/chat?chat_type=wpa&uin="+record.getQq();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                //指定的QQ号只需要修改uin后的值即可。
            }
        });

        mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFavorite();
            }
        });

        mUnFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unFavorite();
            }
        });

        String imageUri = record.getImagepath();
        imageLoader.displayImage(imageUri, mImageView);

    }

    void doFavorite(){
        ProductsRes.Record record = mRecords.get(mPosition);
        DoFavoriteRes.Params params = new DoFavoriteRes.Params();
        params.setPid(record.getId());
        DoFavoriteRes.doRequest(params, this);
        mFavoriteBtn.setEnabled(false);
    }

    void unFavorite(){
        ProductsRes.Record record = mRecords.get(mPosition);
        UnFavoriteRes.Params params = new UnFavoriteRes.Params();
        params.setPid(record.getId());
        UnFavoriteRes.doRequest(params, this);
        mUnFavoriteBtn.setEnabled(false);
    }

    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
        super.netFinishOk(info, res, tag);
        if (info == RequestInfo.DOFAVORITE){
            ProductsRes.Record record = mRecords.get(mPosition);
            record.setIs_favorite("Y");
            Intent intent = new Intent();
            intent.putExtra("id",record.getId());
            intent.putExtra("is_favorite", record.getIs_favorite());
            setResult(RESULT_OK,intent);
            mFavoriteBtn.setEnabled(true);
            mFavoriteBtn.setVisibility(View.GONE);
            mUnFavoriteBtn.setVisibility(View.VISIBLE);
        }else if (info == RequestInfo.UNFAVORITE){
            ProductsRes.Record record = mRecords.get(mPosition);
            record.setIs_favorite("N");
            Intent intent = new Intent();
            intent.putExtra("id",record.getId());
            intent.putExtra("is_favorite", record.getIs_favorite());
            setResult(RESULT_OK,intent);
            mUnFavoriteBtn.setEnabled(true);
            mUnFavoriteBtn.setVisibility(View.GONE);
            mFavoriteBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void netFinishError(RequestInfo info, BaseRes result, int tag) {
        super.netFinishError(info, result, tag);
        if (info == RequestInfo.DOFAVORITE) {
            mFavoriteBtn.setEnabled(true);
        }else if (info == RequestInfo.UNFAVORITE) {
            mUnFavoriteBtn.setEnabled(true);
        }
    }


}
