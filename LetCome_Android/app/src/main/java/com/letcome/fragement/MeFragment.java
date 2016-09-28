package com.letcome.fragement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gxq.tpm.fragment.FragmentBase;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.Print;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.letcome.R;
import com.letcome.activity.MainActivity;
import com.letcome.activity.ProductsActivity;
import com.letcome.adapter.ImageGridAdapter;
import com.letcome.mode.ProductsRes;
import com.letcome.mode.WaterFallsRes;
import com.letcome.ui.WaterFallsView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rjt on 16/9/2.
 */
public class MeFragment extends FragmentBase implements WaterFallsView.OnRefreshListener, WaterFallsView.OnMoreListener {
    public final static int PRODUCT_DETAIL					= 4;
    private WaterFallsView mAdapterView;
    private ImageGridAdapter adapter;
    private ArrayList<Integer> colorList;
    private Integer mPage = 1;
    private Button mSelleBtn;
    private MainActivity parent;

    public MeFragment() {
        this(R.id.tab_me);
    }

    @SuppressLint("ValidFragment")
    public MeFragment(int markId) {
        super(markId);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.colorList = new ArrayList<Integer>();
        this.colorList.add(Color.rgb(199, 200, 182));
        this.colorList.add(Color.rgb(189, 189, 189));
        this.colorList.add(Color.rgb(186, 162, 153));
        this.colorList.add(Color.rgb(243, 241, 236));
        initView(view);
        initAction(view);
    }

    private void initView(View view) {
        //mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);
        mAdapterView = (WaterFallsView) view.findViewById(R.id.list);
        mAdapterView.setOnRefreshListener(this);
        mAdapterView.setMoreListener(this);
        adapter = new ImageGridAdapter(this.mContext);
        mAdapterView.setAdapter(adapter);

        mSelleBtn = (Button) view.findViewById(R.id.sell_product);
        mSelleBtn.setOnClickListener(parent);
//        queryMediaImages();
    }

    private void initAction(View view) {
        onRefresh();
        mAdapterView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView) view.findViewById(R.id.imageView);

                if (iv.getDrawable() instanceof BitmapDrawable) {
                    Intent intent = new Intent(view.getContext(), ProductsActivity.class);
                    intent.putExtra("records", (Serializable) adapter.getRecords());
                    intent.putExtra("id", id);
                    intent.putExtra("position", position - 1);
                    Bitmap image = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                    intent.putExtra("bitmap", image);
                    startActivityForResult(intent, PRODUCT_DETAIL);
//                    Intent intent = new Intent(view.getContext(), LocationDemo.class);
//                    startActivity(intent);

                }

            }
        });
    }


    @Override
    public void onMore() {
        Log.i("MeFragment", "onMore");
        WaterFallsRes.Params params = new WaterFallsRes.Params();
        params.setLongitude("0");
        params.setLatitude("0");
        params.setDistance("0");
        params.setPno(String.valueOf(++mPage));
        WaterFallsRes.doMoreRequest(params, this);
    }

    @Override
    public void onRefresh() {
        this.showWaitDialog(RequestInfo.WATER_FALLS_REFRESH);
        Log.i("MeFragment", "onRefresh");
        WaterFallsRes.Params params = new WaterFallsRes.Params();
        params.setLongitude("0");
        params.setLatitude("0");
        params.setDistance("0");
        mPage = 1;
        params.setPno(String.valueOf(mPage));
        WaterFallsRes.doRefreshRequest(params, this);
    }

    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
        if (info == RequestInfo.WATER_FALLS_REFRESH) {
            WaterFallsRes r = (WaterFallsRes)res;
            adapter.clearRecords();
            ArrayList<WaterFallsRes.Record> rs = r.getRecords();
            for (WaterFallsRes.Record record:rs) {
                record.setHold_color(this.colorList.get(Double.valueOf(Math.random() * 1000 % 4).intValue()));
            }
            adapter.addRecords(rs);
            mAdapterView.onRefreshComplete();
            adapter.notifyDataSetChanged();
            this.hideWaitDialog(RequestInfo.WATER_FALLS_REFRESH);

        }else if(info == RequestInfo.WATER_FALLS_MORE) {
            mAdapterView.onMoreComplete();
            WaterFallsRes r = (WaterFallsRes)res;
            ArrayList<WaterFallsRes.Record> rs = r.getRecords();
            for (WaterFallsRes.Record record:rs) {
                record.setHold_color(this.colorList.get(Double.valueOf(Math.random() * 1000 % 4).intValue()));
            }
            adapter.addRecords(rs);
            mAdapterView.onRefreshComplete();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int netFinishError(RequestInfo info, int what, String msg, int tag) {
        if (info == RequestInfo.WATER_FALLS_REFRESH) {
            mAdapterView.onRefreshComplete();
            this.hideWaitDialog(RequestInfo.WATER_FALLS_REFRESH);
        }else if(info == RequestInfo.WATER_FALLS_MORE) {
            mAdapterView.onMoreComplete();
        }
        return super.netFinishError(info, what, msg, tag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PRODUCT_DETAIL && resultCode == Activity.RESULT_OK && data != null){
            String id = data.getStringExtra("id");
            String isFavorite = data.getStringExtra("is_favorite");
            for (ProductsRes.Record record:adapter.getRecords()) {
                if (record.getId().equals(id)){
                    record.setIs_favorite(isFavorite);
                    break;
                }
            }

            Print.i("ProductsActivity", resultCode + ";" + data);
        }
    }
}
