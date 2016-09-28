package com.letcome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clj.memoryspinner.MemorySpinner;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.BaiduLocationUtils;
import com.letcome.App;
import com.letcome.R;
import com.letcome.mode.CategoriesRes;
import com.letcome.mode.UpdateProductRes;
import com.letcome.prefs.UserPrefs;

import java.util.ArrayList;

public class SellDetailActivity extends SuperActivity implements View.OnClickListener {
    final static String PRODUCT_ID = "PRODUCT_ID" ;
    Button mCloseBtn,mDoneBtn;
    EditText mPriceEt,mMobileEt,mDescEt;
    MemorySpinner mMmemorySpinner;
    String productid;

    static void create(Activity activity,int resultCode,String proudctid){

        Intent intent = new Intent(activity,SellDetailActivity.class);
        intent.putExtra(PRODUCT_ID, proudctid);
        activity.startActivityForResult(intent,resultCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_detail);
        productid = getIntent().getStringExtra(PRODUCT_ID);
        initView();
        initAction();
    }
    private void initView() {
        mCloseBtn = (Button)findViewById(R.id.close_btn);
        mDoneBtn = (Button)findViewById(R.id.done_btn);
        mPriceEt = (EditText)findViewById(R.id.sell_price);
        mMobileEt = (EditText)findViewById(R.id.sell_mobile);
        mDescEt = (EditText)findViewById(R.id.sell_desc);
        mMmemorySpinner = (MemorySpinner)findViewById(R.id.categories_select);

        UserPrefs userPrefs = App.getUserPrefs();
        CategoriesRes res = userPrefs.getCategories();
        ArrayList<String> list = new ArrayList<String>();
        for (CategoriesRes.Record r:res.getRecords()) {
            list.add(r.getCategory_name());
        }
        mMmemorySpinner.setMemoryCount(2);
        mMmemorySpinner.setData(null, list);
    }

    private void initAction() {
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellDetailActivity.this.setResult(RESULT_CANCELED);

                SellDetailActivity.this.finish();
            }
        });
        mDoneBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        UpdateProductRes.Params p = new UpdateProductRes.Params();
        p.setId(productid);
        String price = mPriceEt.getText().toString();
        if(price.isEmpty()){
            showToastMsg("请输入价格");
            return;
        }
        p.setPrice(price);

        String desc = mDescEt.getText().toString();
        if(desc.isEmpty()){
            showToastMsg("请输入宝贝描述");
            return;
        }
        p.setDescription(desc);
        String mobile = mMobileEt.getText().toString();
        if(mobile.isEmpty()){
            showToastMsg("请输入联系方式");
            return;
        }


        p.setContact_info(mobile);

        p.setCategory_id(findCategoryByName(mMmemorySpinner.getSelectedItem().toString()));
        p.setLatitude(String.valueOf(BaiduLocationUtils.mLocation.getLatitude()));
        p.setLongitude(String.valueOf(BaiduLocationUtils.mLocation.getLongitude()));
        p.setCity(BaiduLocationUtils.mLocation.getCity());

        UpdateProductRes.doRequest(p, this);



//        showWaitDialog(RequestInfo.UPDATEPRODUCT);
    }

    public  String findCategoryByName(String name){
        UserPrefs userPrefs = App.getUserPrefs();
        CategoriesRes res = userPrefs.getCategories();
        for (CategoriesRes.Record r:res.getRecords()) {
            if (r.getCategory_name().equals(name)){
                return r.getId();
            }
        }
        return null;
    }

    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
        super.netFinishOk(info, res, tag);
        if (info==RequestInfo.UPDATEPRODUCT){
            //设置返回数据
            this.setResult(RESULT_OK);
            //关闭Activity
            this.finish();
        }
    }
}
