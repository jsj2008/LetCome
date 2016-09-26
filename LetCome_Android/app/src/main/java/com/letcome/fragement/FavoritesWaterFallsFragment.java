package com.letcome.fragement;

import android.annotation.SuppressLint;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.letcome.mode.FavoritesRes;
import com.letcome.mode.ProductsRes;

import java.util.ArrayList;

/**
 * Created by rjt on 16/9/26.
 */
@SuppressLint("ValidFragment")
public class FavoritesWaterFallsFragment extends ProfileWaterFallsFragment {


    @Override
    public void onMore() {
        FavoritesRes.Params params = new FavoritesRes.Params();
        params.setLongitude("0");
        params.setLatitude("0");
        params.setDistance("0");
        params.setPno(String.valueOf(++mPage));
        FavoritesRes.doMoreRequest(params, this);
    }

    @Override
    public void onRefresh() {
        this.showWaitDialog(RequestInfo.FAVORITES_REFRESH);

        FavoritesRes.Params params = new FavoritesRes.Params();
        params.setLongitude("0");
        params.setLatitude("0");
        params.setDistance("0");
        mPage = 1;
        params.setPno(String.valueOf(mPage));
        FavoritesRes.doRefreshRequest(params, this);
    }

    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
        if (info == RequestInfo.FAVORITES_REFRESH) {
            ProductsRes r = (ProductsRes)res;
            adapter.clearRecords();
            ArrayList<ProductsRes.Record> rs = r.getRecords();
            for (ProductsRes.Record record:rs) {
                record.setHold_color(this.colorList.get(Double.valueOf(Math.random() * 1000 % 4).intValue()));
            }
            adapter.addRecords(rs);
            mAdapterView.onRefreshComplete();
            adapter.notifyDataSetChanged();

        }else if(info == RequestInfo.FAVORITES_MORE) {
            mAdapterView.onMoreComplete();
            ProductsRes r = (ProductsRes)res;
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
        if (info == RequestInfo.FAVORITES_REFRESH) {
            mAdapterView.onRefreshComplete();
        }else if(info == RequestInfo.FAVORITES_MORE) {
            mAdapterView.onMoreComplete();
        }
        return super.netFinishError(info, what, msg, tag);
    }
}
