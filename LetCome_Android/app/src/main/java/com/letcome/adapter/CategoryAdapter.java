package com.letcome.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxq.tpm.tools.Util;
import com.letcome.App;
import com.letcome.R;
import com.letcome.mode.CategoriesRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjt on 16/9/2.
 */
public class CategoryAdapter extends BaseAdapter{
    private List<CategoriesRes.Record> records;
    private LayoutInflater mLayoutInflater;
    private int mWidth;
    private List<Integer> imageList;

    public CategoryAdapter(Context context) {
        records = App.getUserPrefs().getCategories().getRecords();
        mLayoutInflater = LayoutInflater.from(context);
        mWidth = Util.getScreenWidth((Activity)context)/2;
        imageList = new ArrayList<Integer>(){
            {
                add(R.drawable.electronics);
                add(R.drawable.cars_and_motors);
                add(R.drawable.sports_leisure_and_games);
                add(R.drawable.home_and_garden);
                add(R.drawable.movies_books_and_music);
                add(R.drawable.fashion_and_accesories);
                add(R.drawable.baby_and_child);
                add(R.drawable.other);
            }
        };
    }

    public int getCount() {
        return records.size();
    }
    public Object getItem(int arg0) {
        return records.get(arg0);
    }
    public long getItemId(int arg0) {
        return Long.valueOf(records.get(arg0).getId());
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_category,null);

            convertView.setLayoutParams(new com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams(mWidth,mWidth));
            CategoriesRes.Record r = records.get(position);
            ((TextView)convertView.findViewById(R.id.category_name)).setText(r.getCategory_name());
            ((ImageView)convertView.findViewById(R.id.imageView)).setImageResource(imageList.get(position));
        }
        return convertView;
    }
}
