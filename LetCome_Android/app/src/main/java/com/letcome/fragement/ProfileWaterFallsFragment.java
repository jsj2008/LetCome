package com.letcome.fragement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gxq.tpm.fragment.FragmentBase;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.letcome.R;
import com.letcome.activity.MyProductsActivity;
import com.letcome.adapter.ProductsGridAdapter;
import com.letcome.ui.WaterFallsView;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileWaterFallsFragment extends FragmentBase implements WaterFallsView.OnRefreshListener, WaterFallsView.OnMoreListener{
    protected WaterFallsView mAdapterView = null;
    protected ProductsGridAdapter adapter;

    protected ArrayList<Integer> colorList;

    protected Integer mPage = 1;
//    private MainActivity parent;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        parent = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile_waterfalls, container, false);
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
        mAdapterView = (WaterFallsView) view.findViewById(R.id.list);
        mAdapterView.setOnRefreshListener(this);
        mAdapterView.setMoreListener(this);
        adapter = new ProductsGridAdapter(this.mContext);
        mAdapterView.setAdapter(adapter);
    }

    private void initAction(View view) {
        onRefresh();
        mAdapterView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView) view.findViewById(R.id.imageView);

                if (iv.getDrawable() instanceof BitmapDrawable) {
                    Intent intent = new Intent(view.getContext(), MyProductsActivity.class);
                    intent.putExtra("records", (Serializable) adapter.getRecords());
                    intent.putExtra("id", id);
                    intent.putExtra("position", position - 1);
                    Bitmap image = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                    intent.putExtra("bitmap", image);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onMore() {
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
        super.netFinishOk(info,res,tag);
    }

    @Override
    public int netFinishError(RequestInfo info, int what, String msg, int tag) {
        return super.netFinishError(info, what, msg, tag);
    }

}
