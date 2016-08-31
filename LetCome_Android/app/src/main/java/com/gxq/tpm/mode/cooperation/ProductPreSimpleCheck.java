package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductPreSimpleCheck extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6645391645506679758L;
	public String bullish; //	是否可以看多（股票点买） “Y” 可以 “N”不可以	STRING
	public String bearish; //	是否可以看空 “Y” 可以 “N”不可以	STRING
	public String error_reason_msg; //	提示文字 空时不展示	STRING
	public String error_reason; //	错误代码	STRING

	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1482487929532836933L;
		public String stock_code; //	产品代码	String	TRUE
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.PRODUCT_PRE_SIMPLE_CHECK, params, ProductPreSimpleCheck.class, RETURN_TYPE, false);
   	}

}

