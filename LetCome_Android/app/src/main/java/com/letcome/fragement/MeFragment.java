package com.letcome.fragement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gxq.tpm.fragment.FragmentBase;
import com.huewu.pla.lib.MultiColumnListView;
import com.letcome.R;
import com.letcome.adapter.ImageGridAdapter;

import java.util.ArrayList;

/**
 * Created by rjt on 16/9/2.
 */
public class MeFragment extends FragmentBase{
    private MultiColumnListView mAdapterView = null;
    private ArrayList<String> imageUrls;
    private ImageGridAdapter adapter;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initAction(view);
    }

    private void initView(View view) {
        //mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);
        mAdapterView = (MultiColumnListView)view.findViewById(R.id.list);
        imageUrls = new ArrayList<String>();
        adapter = new ImageGridAdapter(this.mContext);
        mAdapterView.setAdapter(adapter);
//        queryMediaImages();
    }

    private void initAction(View view) {

    }



}
