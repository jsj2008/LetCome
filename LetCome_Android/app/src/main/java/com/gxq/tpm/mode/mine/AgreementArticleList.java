package com.gxq.tpm.mode.mine;

import java.io.Serializable;
import java.util.List;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class AgreementArticleList extends BaseRes {

	private static final long serialVersionUID = -3292953939044630815L;
	
	public List<AgreementArticle> list;
	
	public static class AgreementArticle implements Serializable {
		private static final long serialVersionUID = -2233960776029258635L;

		public int id;
		public String name;
	}

	public static class Params implements Serializable {
		private static final long serialVersionUID = -503684233935825340L;
		
		public int user_type;
		public String p_type;
	}
	
	public static void doRequest(Params params, ICallBack netback) {
		NetworkProxy proxy = new NetworkProxy(netback);
		
		proxy.getRequest(RequestInfo.AGREEMENT_ARTICLE_SAMPLE_LIST, params, AgreementArticleList.class, RETURN_TYPE, false);
	}
	
}
