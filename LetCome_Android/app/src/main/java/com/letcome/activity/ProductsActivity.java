package com.letcome.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.gxq.tpm.activity.SuperActivity;
import com.letcome.R;
import com.letcome.mode.WaterFallsRes;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ProductsActivity extends SuperActivity {

    ImageView mImageView;
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

    }

    private void initAction() {


        mImageView.setImageBitmap(mBitmap);

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
//        // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
//        //  which implements ImageAware interface)
//        //
//        // Load image, decode it to Bitmap and return Bitmap to callback
//
//
//

        String imageUri = mRecords.get(mPosition).getImagepath();
        imageLoader.displayImage(imageUri, mImageView);
    }
}
