package com.letcome.mode;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;

import java.io.Serializable;

/**
 * Created by rjt on 16/9/8.
 */

public class FavoritesRes extends ProductsRes {

    public static class Params implements Serializable {

        public static final String STATUS_PUBLISH = "0";
        public static final String STATUS_SELLING = "1";
        public static final String STATUS_SOLD = "2";

        private static final long serialVersionUID = -7118811436882564397L;

        String longitude;//	经度	可选
        String latitude;//	纬度	可选
        String distance;//	距离,单位米	可选
        String status;	//状态:0(发布)，1（正在售出），2（已售）,默认为全部
        String limit;   //默认20条	可选
        String pno;     // 第几页，默认为1	可选

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        public String getPno() {
            return pno;
        }

        public void setPno(String pno) {
            this.pno = pno;
        }
    }

    public static void doRefreshRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//
//   		proxy.postRequest(RequestInfo.GET_AD.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params), InspAdInfo.class, null, false, false);
        proxy.getRequest(RequestInfo.FAVORITES_REFRESH, params, FavoritesRes.class, RETURN_TYPE, true);
    }

    public static void doMoreRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//
//   		proxy.postRequest(RequestInfo.GET_AD.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params), InspAdInfo.class, null, false, false);
        proxy.getRequest(RequestInfo.FAVORITES_MORE, params, FavoritesRes.class, RETURN_TYPE, true);
    }

}
