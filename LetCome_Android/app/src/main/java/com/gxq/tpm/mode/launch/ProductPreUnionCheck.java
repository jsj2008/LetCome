package com.gxq.tpm.mode.launch;

import com.gxq.tpm.mode.ProductPreCheck;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.network.RequestInfo;

public class ProductPreUnionCheck extends ProductPreCheck {

	private static final long serialVersionUID = 4760937404316014923L;
	
	public static void doRequest(ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		
		proxy.getRequest(RequestInfo.PRODUCT_PRE_UNION_CHECK, null, ProductPreUnionCheck.class, RETURN_TYPE, false);
	}
	
}
