package com.letcome.mode;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.crypt.MD5;

import java.io.Serializable;

/**
 * Created by rjt on 16/9/8.
 */

public class SignupRes extends BaseRes {
    /**
     *
     */
    private static final long serialVersionUID = 2021297357168322275L;

    String uid;
    String sessionid;
    String fullname;
    String qq;

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

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public static class Params implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -7118811436882564397L;

        String email;
        String pwd;	//md5加密
        String fullname;
        String qq;

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = MD5.md5(pwd).toUpperCase();
        }
    }

    public static void doRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//
//   		proxy.postRequest(RequestInfo.GET_AD.getOperationType(), ServiceConfig.getServiceUser(), gson.toJson(params), InspAdInfo.class, null, false, false);
        proxy.getRequest(RequestInfo.SIGNUP, params, SignupRes.class, RETURN_TYPE, false);
    }

}
