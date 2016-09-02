package com.letcome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.letcome.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by rjt on 16/9/2.
 */
public class ImageGridAdapter extends BaseAdapter{
    private LayoutInflater mLayoutInflater;
    private int mWidth;
    public ImageGridAdapter(Context context) {

//        mLoader = new ImageLoader(context);
//        mLoader.setIsUseMediaStoreThumbnails(false);
//        mImageList = list;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth()/3;//屏幕宽度
//        mLoader.setRequiredSize(width / 3); //3表示列数
        mLayoutInflater = LayoutInflater.from(context);


    }
    public int getCount() {
        return 15;
    }
    public Object getItem(int arg0) {
        return null;
    }
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            convertView = mLayoutInflater.inflate(R.layout.item_image,null);
//            holder = new ViewHolder();
//            holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView);
//            convertView.setTag(holder);
//        }
//        holder = (ViewHolder) convertView.getTag();
//        mLoader.DisplayImage(mImageList.get(position), holder.imageView);
        convertView = mLayoutInflater.inflate(R.layout.item_image,null);
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(mWidth,Double.valueOf(Math.random() * 600).intValue());
        imageView.setLayoutParams(mParams);
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
        //  which implements ImageAware interface)
        //
        // Load image, decode it to Bitmap and return Bitmap to callback
        String imageUri = "http://115.159.194.244:8080/LetCome/image/getimg?id="+(145+position);
        imageLoader.displayImage(imageUri,imageView);
        //
        return convertView;
    }
}
