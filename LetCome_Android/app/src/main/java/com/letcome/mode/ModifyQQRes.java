package com.letcome.mode;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;

import java.io.Serializable;

/**
 * Created by rjt on 16/9/8.
 */

public class ModifyQQRes extends BaseRes {
    /**
     *
     */
    private static final long serialVersionUID = 2021297357168322275L;



    public static class Params implements Serializable {

        private static final long serialVersionUID = -7118811436882564397L;
        String qq;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }

    public static void doRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
        proxy.getRequest(RequestInfo.MODIFY_QQ, params, ModifyQQRes.class, RETURN_TYPE, false);
    }

}
