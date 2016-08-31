package com.gxq.tpm.mode.launch;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class PrepareMsg extends BaseRes {
    private static final long serialVersionUID = -7716582766074480049L;

    public ArrayList<ResData> res_data;

	public static class ResData implements Serializable {
        private static final long serialVersionUID = -8596295082806011730L;

        public String result;//Y:成功     N:失败
	}
    
	public static class Params implements Serializable{
        private static final long serialVersionUID = -2856373506494728940L;

        public int user_type;//	用户类型 1:策略人 2：投资人 3：风控人
    }
    
    public static void doRequest(ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		Params params = new Params();
   		params.user_type = GlobalConstant.USER_TYPE_STRATEGY;
   		
   		proxy.getRequest(RequestInfo.MSG_PREPARE, params, PrepareMsg.class, RETURN_TYPE, false);
   	}
    
}
