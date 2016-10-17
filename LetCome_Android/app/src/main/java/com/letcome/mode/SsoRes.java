package com.letcome.mode;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;

import java.io.Serializable;

/**
 * Created by rjt on 16/9/8.
 */

public class SsoRes extends BaseRes {
    /**
     *
     */
    private static final long serialVersionUID = 2021297357168322275L;

    String uid;
    String sessionid;
    String fullname;
    String qq;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public static class Params implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -7118811436882564397L;

        String openid;
        String accesstoken;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getAccesstoken() {
            return accesstoken;
        }

        public void setAccesstoken(String accesstoken) {
            this.accesstoken = accesstoken;
        }
    }

    public static void doRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//
//   		proxy.postRequest(RequestInfo.GET_AD.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params), InspAdInfo.class, null, false, false);
        proxy.getRequest(RequestInfo.SSO, params, SsoRes.class, RETURN_TYPE, false);
    }

}
