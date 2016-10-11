package com.letcome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.channguyen.rsv.RangeSliderView;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.tools.Print;
import com.letcome.App;
import com.letcome.R;
import com.letcome.mode.CategoriesRes;
import com.letcome.mode.WaterFallsRes;
import com.letcome.prefs.UserPrefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterActivity extends SuperActivity {

    RelativeLayout mPriceAsc,mPriceDesc,mTime24h,mTime7d,mTime30d,mTimeAll;
    LinearLayout mCategoryArea;
    TextView mDistanceText;
    RangeSliderView rsView;
    int mCurrentDistance;
    Button mSaveBtn;
    WaterFallsRes.Params mParams;
    List<RelativeLayout> mTimeList,mSortList;
    List<LinearLayout> mCategoryList;
    public List<Integer> imageList;

    static public void create(Activity activity,WaterFallsRes.Params params){
        Intent intent = new Intent(activity,FilterActivity.class);
        intent.putExtra("params", params);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParams = (WaterFallsRes.Params)getIntent().getSerializableExtra("params");

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

        mTimeList = new ArrayList<RelativeLayout>();
        mSortList = new ArrayList<RelativeLayout>();
        mCategoryList = new ArrayList<LinearLayout>();

        setContentView(R.layout.activity_filter);
        getTitleBar().setTitle(R.string.filter_title);
        getTitleBar().setLeftImage(R.drawable.ic_prev_dialog_active);
        initView();
        initAction();
        initParams();
    }

    public void initView(){

        mCategoryArea = (LinearLayout)findViewById(R.id.category_area);

        UserPrefs prefs = App.getUserPrefs();
        CategoriesRes res = prefs.getCategories();
        LinearLayout rl = null;
        for(int i = 0;i<res.getRecords().size();++i) {
            CategoriesRes.Record record = res.getRecords().get(i);
            rl = (LinearLayout) getLayoutInflater().inflate(R.layout.item_filter, null);
            TextView tv = (TextView) rl.findViewById(R.id.filter_text);
            tv.setText(record.getCategory_name());
            ImageView iv = (ImageView) rl.findViewById(R.id.filter_icon);
            iv.setImageResource(imageList.get(i));
            mCategoryArea.addView(rl);
            mCategoryList.add(rl);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilterActivity.this.categoryClicked(v);
                }
            });
            tv = (TextView) rl.findViewById(R.id.filter_id);
            tv.setText(record.getId());
        }


        mPriceAsc = (RelativeLayout)findViewById(R.id.sort_price_asc);
        mPriceDesc = (RelativeLayout)findViewById(R.id.sort_price_desc);
        mSortList.add(mPriceAsc);
        mSortList.add(mPriceDesc);
        mTime24h = (RelativeLayout)findViewById(R.id.time_24h);
        mTime7d = (RelativeLayout)findViewById(R.id.time_7d);
        mTime30d = (RelativeLayout)findViewById(R.id.time_30d);
        mTimeAll = (RelativeLayout)findViewById(R.id.time_all);
        mTimeList.add(mTime24h);
        mTimeList.add(mTime7d);
        mTimeList.add(mTime30d);
        mTimeList.add(mTimeAll);

        mDistanceText = (TextView)findViewById(R.id.distance_text);
        rsView = (RangeSliderView)findViewById(R.id.rsv_small);

        mSaveBtn = (Button)findViewById(R.id.save_btn);
    }
    public void initAction(){
        mPriceAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterActivity.this.priceSortClicked(v);
            }
        });
        mPriceDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterActivity.this.priceSortClicked(v);
            }
        });

        mTime24h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterActivity.this.timeClicked(v);
            }
        });
        mTime7d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterActivity.this.timeClicked(v);
            }
        });
        mTime30d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterActivity.this.timeClicked(v);
            }
        });
        mTimeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterActivity.this.timeClicked(v);
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterActivity.this.saveClicked();
            }
        });

        rsView.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                changeDistance(index);
            }
        });
    }

    public void initParams(){
        if(mParams!=null ) {
            if (mParams.getPricerank()!=null && mParams.getPricerank().equals("asc")) {
                choseLine(mPriceAsc);
            }else if (mParams.getPricerank()!=null && mParams.getPricerank().equals("desc")) {
                choseLine(mPriceDesc);
            }

            if(mParams.getStarttime()!=null && mParams.getEndtime()!=null){
                long inteval = Long.valueOf(mParams.getEndtime()) - Long.valueOf(mParams.getStarttime());
                long daytime = 24 * 60 * 60;
                long day = inteval/daytime;
                if (day==1){
                    choseLine(mTime24h);
                }else if (day==7){
                    choseLine(mTime7d);
                }else if (day==30){
                    choseLine(mTime30d);
                }

            }else{
                choseLine(mTimeAll);
            }
            if(mParams.getDistance()!=null && !mParams.getDistance().isEmpty()){
                int l = Integer.valueOf(mParams.getDistance())/2000;
                mDistanceText.setText(Integer.valueOf(mParams.getDistance()) / 1000 + "公里");
                rsView.setInitialIndex(l);
            }
            if(mParams.getCid()!=null && !mParams.getCid().isEmpty()){
                String cids = mParams.getCid();
                String[] str = cids.split(",");
                for(String s : str){
                    for(LinearLayout l:mCategoryList){
                        if (((TextView)l.findViewById(R.id.filter_id)).getText().equals(s)){
                            choseLine(l);
                            break;
                        }

                    }
                }
            }
        }
    }

    public void priceSortClicked(View v){
        for (RelativeLayout r:mSortList) {
            if (r == v){
                choseLine(r);
            }else{
                unChoseLine(r);
            }
        }
    }

    void changeDistance(int index){
        switch (index) {
            case 0:
                mDistanceText.setText("不限");
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                mDistanceText.setText(index*2+"公里");
                mCurrentDistance = index;
                break;
            default:
                mDistanceText.setText("不限");
                break;
        }
    }

    void choseLine(View line){
        TextView tv = (TextView)line.findViewById(R.id.filter_text);
        tv.setTextColor(ContextCompat.getColor(this, R.color.nav_red));
        ImageView iv = (ImageView)line.findViewById(R.id.filter_img);
        iv.setVisibility(View.VISIBLE);
    }

    void unChoseLine(View line){
        TextView tv = (TextView)line.findViewById(R.id.filter_text);
        tv.setTextColor(ContextCompat.getColor(this, R.color.black_color));
        ImageView iv = (ImageView)line.findViewById(R.id.filter_img);
        iv.setVisibility(View.GONE);
    }

    public void timeClicked(View v){
        for (RelativeLayout r:mTimeList) {
            if (r == v){
                choseLine(r);
            }else{
                unChoseLine(r);
            }
        }
    }

    public void categoryClicked(View v){

        ImageView iv = (ImageView)v.findViewById(R.id.filter_img);
        if (iv.getVisibility() == View.VISIBLE){
            unChoseLine(v);
        }else{
            choseLine(v);
        }
    }

    public void saveClicked(){
        WaterFallsRes.Params params = new WaterFallsRes.Params();
        if(mPriceAsc.findViewById(R.id.filter_img).getVisibility() == View.VISIBLE){
            params.setPricerank("asc");
        }else if(mPriceDesc.findViewById(R.id.filter_img).getVisibility() == View.VISIBLE){
            params.setPricerank("desc");
        }
        Date date = new Date();
        long time = date.getTime()/1000;
        long daytime = 24 * 60 * 60;
        Print.i("FilterActivity", String.valueOf(daytime));
        if(mTime24h.findViewById(R.id.filter_img).getVisibility() == View.VISIBLE){
            params.setStarttime(String.valueOf(time - daytime));
            params.setEndtime(String.valueOf(time));
        }else if(mTime7d.findViewById(R.id.filter_img).getVisibility() == View.VISIBLE){
            params.setStarttime(String.valueOf(time - 7*daytime));
            params.setEndtime(String.valueOf(time));
        }else if(mTime30d.findViewById(R.id.filter_img).getVisibility() == View.VISIBLE){
            params.setStarttime(String.valueOf((time - 30 * daytime)));
            params.setEndtime(String.valueOf(time));
        }else if(mTimeAll.findViewById(R.id.filter_img).getVisibility() == View.VISIBLE){
            params.setEndtime(null);
            params.setStarttime(null);
        }

        if(mCurrentDistance>0){
            params.setDistance(String.valueOf(mCurrentDistance*2*1000));
        }

        String cids = "";

        for(int i=0;i<mCategoryList.size();++i){
            LinearLayout line = mCategoryList.get(i);
            ImageView iv = (ImageView)line.findViewById(R.id.filter_img);
            if (iv.getVisibility() == View.VISIBLE){
                if(!cids.isEmpty()){
                    cids += ",";
                }
                cids += ((TextView)line.findViewById(R.id.filter_id)).getText();
            }
        }
        params.setCid(cids);

        ResultActivity.create(this,params);
        finish();
    }
}
