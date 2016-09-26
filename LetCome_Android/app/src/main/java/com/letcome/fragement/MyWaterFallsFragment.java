package com.letcome.fragement;

import android.annotation.SuppressLint;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.letcome.mode.MyProductsRes;
import com.letcome.mode.ProductsRes;

import java.util.ArrayList;

/**
 * Created by rjt on 16/9/26.
 */
@SuppressLint("ValidFragment")
public class MyWaterFallsFragment extends ProfileWaterFallsFragment {

    private String mStatus ;

    public MyWaterFallsFragment(String status) {
        mStatus = status;
    }

    @Override
    public void onMore() {
        MyProductsRes.Params params = new MyProductsRes.Params();
        params.setStatus(mStatus);
        params.setPno(String.valueOf(++mPage));
        MyProductsRes.doMoreRequest(params, this);
    }

    @Override
    public void onRefresh() {
        this.showWaitDialog(RequestInfo.MY_PRODUCTS_REFRESH);
        MyProductsRes.Params params = new MyProductsRes.Params();
        params.setStatus(mStatus);
        mPage = 1;
        params.setPno(String.valueOf(mPage));
        MyProductsRes.doRefreshRequest(params, this);
    }

    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
        if (info == RequestInfo.MY_PRODUCTS_REFRESH) {
            ProductsRes r = (ProductsRes)res;
            adapter.clearRecords();
            ArrayList<ProductsRes.Record> rs = r.getRecords();
            for (ProductsRes.Record record:rs) {
                record.setHold_color(this.colorList.get(Double.valueOf(Math.random() * 1000 % 4).intValue()));
            }
            adapter.addRecords(rs);
            mAdapterView.onRefreshComplete();
            adapter.notifyDataSetChanged();

        }else if(info == RequestInfo.MY_PRODUCTS_MORE) {
            mAdapterView.onMoreComplete();
            ProductsRes r = (MyProductsRes)res;
            ArrayList<ProductsRes.Record> rs = r.getRecords();
            for (ProductsRes.Record record:rs) {
                record.setHold_color(this.colorList.get(Double.valueOf(Math.random() * 1000 % 4).intValue()));
            }
            adapter.addRecords(rs);
            mAdapterView.onRefreshComplete();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int netFinishError(RequestInfo info, int what, String msg, int tag) {
        if (info == RequestInfo.MY_PRODUCTS_REFRESH) {
            mAdapterView.onRefreshComplete();
        }else if(info == RequestInfo.MY_PRODUCTS_MORE) {
            mAdapterView.onMoreComplete();
        }
        return super.netFinishError(info, what, msg, tag);
    }
}
