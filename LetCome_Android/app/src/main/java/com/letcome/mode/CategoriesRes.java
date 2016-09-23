package com.letcome.mode;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rjt on 16/9/8.
 */

public class CategoriesRes extends BaseRes {
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
        String category_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }

    public static class Params implements Serializable {

         /**
         *
         */
        private static final long serialVersionUID = -7118811436882564397L;


    }

    public static void doRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
        proxy.getRequest(RequestInfo.CATEGORIES, params, CategoriesRes.class, RETURN_TYPE, false);
    }


}
