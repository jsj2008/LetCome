package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetArticleContent extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2283399015454015336L;
	
	public String content;
	
	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3817555229116942272L;
		
		public int id;//协议id
		public String nickname_t;// 投资人昵称 String TRUE 
		public String uid_t;// 投资人UID String TRUE 
		public int scheme_id; //	方案ID	int	TRUE
//		public String fund;// 申购金额(股票时需要，使用展示文字 参数例：“30万元”) String TRUE
//		public String apportionment; //80:20   盈利分配
//		public String stockName;// 交易品种(参数例：使用展示文字 “招商银行”) String TRUE 
//		public String stockCode;// 股票代码(股票时需要，使用展示文字 参数例：“600036”) String TRUE 
//		public String amount;// 交易数量（使用展示文字 参数例：“2手”） String TRUE 
//		public String buyDirection;// 点买方向（使用展示文字 股票时不需要 参数例：“看空”） String TRUE 
//		public String holdType;// 持仓时间（使用展示文字 股票时不需要 参数例：“截至本交易时段02:27:59”） String TRUE 
//		public String zy;// 止盈（使用展示文字 参数例：“240指数点/手”） String TRUE 
//		public String zs;// 止损（使用展示文字 参数例：“180指数点/手”） String TRUE 

	}
	
	public static void doRequest(Params params,ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
//		
//		proxy.postRequest(RequestInfo.P_ARTICLE_CONTENT.getOperationType(), ServiceConfig.getServiceUser(), 
//					gson.toJson(params), GetArticleContent.class, null, false, false);
		proxy.getRequest(RequestInfo.P_ARTICLE_CONTENT, params, GetArticleContent.class, RETURN_TYPE, false);
	}

}
