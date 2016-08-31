package com.gxq.tpm.mode.mine;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class DissentCreate extends BaseRes {
	/**
	 * 
	 */
    private static final long serialVersionUID = -949557133701245286L;
    public String result;//结果	string‘Y’成功/‘N’失败
    
    public static class Params implements Serializable {
		/**
		 * 
		 */
        private static final long serialVersionUID = -2061527431266696677L;
        public String p_type;//	产品类型	Stock，au，ag，if
        public long p_id;//	产品ID	int
        public int point;//	异议点	int 1买入价 2卖出价 3盈亏分配 4交易数量 5亏损赔付 6履约金解冻7其他
        public double price;//	异议价格	Float
        public String reason;//	异议理由	String

    }
    
    public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		proxy.getRequest(RequestInfo.S_DISSENT_COMMIT, params, DissentCreate.class, RETURN_TYPE, false);
   	}
}
