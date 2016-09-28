package com.letcome.mode;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;

import java.io.Serializable;

/**
 * Created by rjt on 16/9/8.
 */

public class DoFavoriteRes extends BaseRes {
    /**
     *
     */
    private static final long serialVersionUID = 2021297357168322275L;

    public static class Params implements Serializable {

        private static final long serialVersionUID = -7118811436882564397L;
        String pid;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }
    }

    public static void doRequest(Params params, NetworkProxy.ICallBack netBack) {
        NetworkProxy proxy = new NetworkProxy(netBack);
        proxy.getRequest(RequestInfo.DOFAVORITE, params, DoFavoriteRes.class, RETURN_TYPE, false);
    }

}
