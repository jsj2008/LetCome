package com.letcome.fragement;

import android.util.Log;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.letcome.mode.WaterFallsRes;

import java.util.ArrayList;

/**
 * Created by rjt on 16/9/26.
 */
public class SellingWaterFallsFragment extends ProfileWaterFallsFragment {
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
}
