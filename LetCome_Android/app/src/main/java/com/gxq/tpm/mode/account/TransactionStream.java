package com.gxq.tpm.mode.account;

import java.io.Serializable;
import java.util.ArrayList;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.network.RequestInfo;
public class TransactionStream extends BaseRes{
	/**
	 * 
	 */
	public ArrayList<TransactionStreamItem> records;
	private static final long serialVersionUID = 1466546780954468510L;

	public static class Params implements Serializable {

		private static final long serialVersionUID = 3283117850072068450L;
		public int p_id;//订单id
		public String type;//交易类型
	}
	public static class TransactionStreamItem implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5960856169992835798L;
		public float deal_price;//成交价格
		public int deal_amount;//成交数量
		public float deal_fund;//成交金额
		public int sell_buy;//买卖标志
		public int deal_time;//交易时间
		public int type;//交易类型
		public String type_name;//交易类型翻译
		public String state_name;//状态翻译
		public int state;
		
	}
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.PRODUCT_DEAL_HISTORY, params,
				TransactionStream.class, RETURN_TYPE, false);
	}
}
