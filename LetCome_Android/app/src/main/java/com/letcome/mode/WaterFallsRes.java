package com.letcome.mode;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rjt on 16/9/8.
 */

public class WaterFallsRes extends BaseRes {
    /**
     *
     */
    private static final long serialVersionUID = 2021297357168322275L;
    ArrayList<Record> records;

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public static class Record implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 5699267005828976840L;
        String id;
        String description;
        String created_at;
        String updated_at;
        String longitude;
        String latitude;
        String city;
        String status;
        String price;
        String category_id;
        String uid;
        String imagename;
        String imagepath;
        String thumbpath;
        String thumbheight;
        String thumbwidth;
        String imageheight;
        String imagewidth;
        String image_id;
        String contact_info;
        String is_favorite;

        int hold_color;

        public String getThumbheight() {
            return thumbheight;
        }

        public void setThumbheight(String thumbheight) {
            this.thumbheight = thumbheight;
        }

        public String getThumbwidth() {
            return thumbwidth;
        }

        public void setThumbwidth(String thumbwidth) {
            this.thumbwidth = thumbwidth;
        }

        public String getImageheight() {
            return imageheight;
        }

        public void setImageheight(String imageheight) {
            this.imageheight = imageheight;
        }

        public String getImagewidth() {
            return imagewidth;
        }

        public void setImagewidth(String imagewidth) {
            this.imagewidth = imagewidth;
        }

        public int getHold_color() {
            return hold_color;
        }

        public void setHold_color(int hold_color) {
            this.hold_color = hold_color;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getImagename() {
            return imagename;
        }

        public void setImagename(String imagename) {
            this.imagename = imagename;
        }

        public String getImagepath() {
            return imagepath;
        }

        public void setImagepath(String imagepath) {
            this.imagepath = imagepath;
        }

        public String getThumbpath() {
            return thumbpath;
        }

        public void setThumbpath(String thumbpath) {
            this.thumbpath = thumbpath;
        }

        public String getImage_id() {
            return image_id;
        }

        public void setImage_id(String image_id) {
            this.image_id = image_id;
        }

        public String getContact_info() {
            return contact_info;
        }

        public void setContact_info(String contact_info) {
            this.contact_info = contact_info;
        }

        public String getIs_favorite() {
            return is_favorite;
        }

        public void setIs_favorite(String is_favorite) {
            this.is_favorite = is_favorite;
        }
    }

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
