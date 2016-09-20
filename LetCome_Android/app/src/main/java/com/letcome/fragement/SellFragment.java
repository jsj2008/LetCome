package com.letcome.fragement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gxq.tpm.fragment.FragmentBase;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.letcome.R;

/**
 * Created by rjt on 16/9/2.
 */
public class SellFragment extends FragmentBase{


    public SellFragment() {
        this(R.id.tab_me);
    }

    @SuppressLint("ValidFragment")
    public SellFragment(int markId) {
        super(markId);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sell, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initAction(view);
    }

    private void initView(View view) {
        //mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);

//        queryMediaImages();
    }

    private void initAction(View view) {

    }



    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {

    }

    @Override
    public int netFinishError(RequestInfo info, int what, String msg, int tag) {

        return super.netFinishError(info, what, msg, tag);
    }
}
