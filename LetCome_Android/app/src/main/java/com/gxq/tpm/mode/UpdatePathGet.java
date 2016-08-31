package com.gxq.tpm.mode;

import java.io.Serializable;

import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class UpdatePathGet extends BaseRes  {
    private static final long serialVersionUID = 6911846990218446886L;

    public Data res_data;	
	
    public static class Params implements Serializable{
        private static final long serialVersionUID = -3867211876388609152L;

        public String name;//version_ios，version_android，stock_list，trader_list，
    }
    
    public static class Data implements Serializable{
        private static final long serialVersionUID = -2875848654959641071L;

        public String name;//version_ios，version_android，stock_list，trader_list，
    	public String version;//版本号
    	public long create_time;//更新时间
    	public int flag;//是否强制更新  1强制升级- 2-建议升级
    	public String url;//升级地址
    }
    
    public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack, 5);

   		proxy.getRequest(RequestInfo.UPDATE_PATH_GET, params, UpdatePathGet.class, RETURN_TYPE, true);
   	}
    
}
