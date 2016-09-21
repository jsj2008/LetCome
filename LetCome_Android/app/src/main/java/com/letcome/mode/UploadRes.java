package com.letcome.mode;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;

import java.io.Serializable;

/**
 * Created by rjt on 16/9/8.
 */

public class UploadRes extends BaseRes {
    /**
     *
     */
    private static final long serialVersionUID = 2021297357168322275L;



    public static class Params implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -7118811436882564397L;

        byte[] myfile;


        public byte[] getMyfile() {
            return myfile;
        }

        public void setMyfile(byte[] myfile) {
            this.myfile = myfile;
        }
    }

    public static void doRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//
//   		proxy.postRequest(RequestInfo.GET_AD.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params), InspAdInfo.class, null, false, false);
        proxy.getUploadRequest(RequestInfo.UPLOAD, params.getMyfile(), UploadRes.class, RETURN_TYPE, false);
    }

}
