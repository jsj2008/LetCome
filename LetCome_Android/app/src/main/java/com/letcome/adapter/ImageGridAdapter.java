package com.letcome.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gxq.tpm.tools.Util;
import com.letcome.R;
import com.letcome.mode.WaterFallsRes;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjt on 16/9/2.
 */
public class ImageGridAdapter extends BaseAdapter{
    private List<WaterFallsRes.Record> records;
    private LayoutInflater mLayoutInflater;
    private int mWidth;
    public ImageGridAdapter(Context context) {

//        mLoader = new ImageLoader(context);
//        mLoader.setIsUseMediaStoreThumbnails(false);
//        mImageList = list;
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        mWidth = (metric.widthPixels- Util.dpToPixel(16))/3;     // 屏幕宽度（像素）

//        mWidth = (Util.getScreenWidth()- Util.dpToPixel(16))/3;//屏幕宽度

//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Log.i("adasdasd", "----------------" + mWidth+"---"+Util.dpToPixel(8));
//        mWidth = (wm.getDefaultDisplay().getWidth())/3;//屏幕宽度
//        mLoader.setRequiredSize(width / 3); //3表示列数
        mLayoutInflater = LayoutInflater.from(context);

    }
    public int getCount() {
        return getRecords().size();
    }
    public Object getItem(int arg0) {
        return getRecords().get(arg0);
    }
    public long getItemId(int arg0) {
        return Long.valueOf(getRecords().get(arg0).getId());
    }

    public List<WaterFallsRes.Record> getRecords() {
        if (records==null){
            records = new ArrayList<WaterFallsRes.Record>();
        }
        return records;
    }

    public void addRecords(List<WaterFallsRes.Record> records) {
        List<WaterFallsRes.Record> rs = getRecords();
        rs.addAll(records);
    }

    public void clearRecords() {
        List<WaterFallsRes.Record> rs = getRecords();
        rs.clear();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_image,null);
//            holder = new ViewHolder();
//            holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView);
//            convertView.setTag(holder);
        }
//        holder = (ViewHolder) convertView.getTag();
//        mLoader.DisplayImage(mImageList.get(position), holder.imageView);
        WaterFallsRes.Record record = records.get(position);

//        convertView = mLayoutInflater.inflate(R.layout.item_image,null);
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
//        imageView
        int height = Integer.valueOf(record.getThumbheight())==0?Util.dpToPixel(326):Integer.valueOf(record.getThumbheight());
        int width = Integer.valueOf(record.getThumbwidth())==0?Util.dpToPixel(245):Integer.valueOf(record.getThumbwidth());

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(mWidth,Double.valueOf(height*(Double.valueOf(mWidth)/Double.valueOf(width))).intValue());
        imageView.setLayoutParams(mParams);

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
        //  which implements ImageAware interface)
        //
        // Load image, decode it to Bitmap and return Bitmap to callback

//        Drawable drawable = bitmapMap.get(record.getHold_color());
//        if (drawable==null){
//            drawable = new ColorDrawable(record.getHold_color());
//            bitmapMap.put(record.getHold_color(),drawable);
//        }
//        drawab



        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(record.getHold_color()))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        String imageUri = record.getThumbpath();
//        DisplayImageOptions options = new DisplayImageOptions.Builder().
        imageLoader.displayImage(imageUri, imageView,options);



        return convertView;
    }
}
