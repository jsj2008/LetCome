package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class GetArticleList extends BaseRes {

	private static final long serialVersionUID = -6935274838224094783L;

	public ArrayList<Article> records;
	
	public static class Article implements Serializable {

		private static final long serialVersionUID = -8122371559811762755L;
		
		public int id;
		public String name;
	}
	
	public static class Params implements Serializable {

		private static final long serialVersionUID = 4597360915493202572L;
		
//		public int product_type; //1-A股 2-期指 3-沪金 4-沪银 9-基金B
		public int user_type; //产品类型 1-策略人
		public String p_type; // 产品类型 insp:A50
	}
	
	public static void doRequest(Params params,ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//		Gson gson = new Gson();
		
//		proxy.postRequest(RequestInfo.P_ARTICLE_LIST.getOperationType(), ServiceConfig.getServiceUser(), 
//					gson.toJson(params), GetArticleList.class, null, false, false);
		proxy.getRequest(RequestInfo.P_ARTICLE_LIST, params, GetArticleList.class, RETURN_TYPE, false);
	}
}
