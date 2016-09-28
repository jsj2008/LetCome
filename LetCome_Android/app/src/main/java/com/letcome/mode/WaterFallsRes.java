package com.letcome.mode;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;

import java.io.Serializable;

/**
 * Created by rjt on 16/9/8.
 */

public class WaterFallsRes extends ProductsRes {
    /**
     *
     */
    private static final long serialVersionUID = 2021297357168322275L;

    public static class Params implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -7118811436882564397L;

        String longitude;//	经度	可选
        String latitude;//	纬度	可选
        String distance;//	距离,单位米	可选
        String cid;//	目录id,多id用逗号分隔,最后一个数字之后不能有逗号	可选
        String pricerank;//	产品名称，模糊查询	可选
        String productname;//	价格排序，asc/desc	可选
        String starttime;//	开始时间，1970年1月1日的秒数	可选
        String endtime;//	结束时间，1970年1月1日的秒数	可选
        String limit;//	默认20条	可选
        String pno;//	第几页，默认为1	可选

        public String getLongitude() {;//
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getPricerank() {
            return pricerank;
        }

        public void setPricerank(String pricerank) {
            this.pricerank = pricerank;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
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
        proxy.getRequest(RequestInfo.WATER_FALLS_REFRESH, params, WaterFallsRes.class, RETURN_TYPE, false);
    }

    public static void doMoreRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//
//   		proxy.postRequest(RequestInfo.GET_AD.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params), InspAdInfo.class, null, false, false);
        proxy.getRequest(RequestInfo.WATER_FALLS_MORE, params, WaterFallsRes.class, RETURN_TYPE, false);
    }

}
