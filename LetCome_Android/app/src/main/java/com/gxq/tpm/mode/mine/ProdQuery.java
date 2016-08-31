package com.gxq.tpm.mode.mine;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProdQuery extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = -1643282327272773351L;
	public String result;//Y/N
	public double available;//可用金额
    public double freeze; //冻结金额
    //error_code:    79012 用户未绑定操盘宝    79013 查询失败
	
    public static class Params implements Serializable{

		/**
		 * 
		 */
        private static final long serialVersionUID = -956714007349879825L;    	
    }
    
    public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.PROD_QUERY, params, ProdQuery.class, RETURN_TYPE, true);
   	}
    
}
