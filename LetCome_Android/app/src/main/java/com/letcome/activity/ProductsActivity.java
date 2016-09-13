package com.letcome.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxq.tpm.activity.BaseActivity;
import com.letcome.R;
import com.letcome.mode.WaterFallsRes;
import com.letcome.ui.ProductDetailDlg;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ProductsActivity extends BaseActivity {

    ImageView mImageView;
    Button mCloseBtn,mFavoriteBtn,mChatBtn;
    TextView mProductName,mProductPrice,mSellerFirst,mSellerName;
    LinearLayout mProductLayout;
    List<WaterFallsRes.Record> mRecords;
    Long mCurrentID;
    Integer mPosition;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mRecords = (List<WaterFallsRes.Record>)getIntent().getSerializableExtra("records");
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

        WaterFallsRes.Record record = mRecords.get(mPosition);
        mImageView.setImageBitmap(mBitmap);

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
//        // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
//        //  which implements ImageAware interface)
//        //
//        // Load image, decode it to Bitmap and return Bitmap to callback
//
//
//

        mProductName.setText(record.getDescription());
        mProductPrice.setText("¥"+record.getPrice());
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
                ProductDetailDlg dlg = new ProductDetailDlg(ProductsActivity.this);
                dlg.show();
            }
        });

        String imageUri = record.getImagepath();
        imageLoader.displayImage(imageUri, mImageView);
    }
}
