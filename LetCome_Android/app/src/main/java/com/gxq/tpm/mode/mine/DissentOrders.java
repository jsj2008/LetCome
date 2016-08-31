package com.gxq.tpm.mode.mine;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class DissentOrders extends BaseRes {	
    /**
	 * 
	 */
    private static final long serialVersionUID = -3975096623456657190L;
	public ArrayList<DissentDetail> records;


	public static class Params implements Serializable {
		/**
		 * 
		 */
        private static final long serialVersionUID = 7896808053704003806L;
		public String  p_type;//	产品类型 stock,au,ag,spif
		public long from_time;//	点买时间>=该时间	Int  可选默认今天
		public long to_time;//	点买时间<=该时间	Int  可选
		public long limit;//			取出记录数	Int 可选  0时取全部
		public long offset;//				从第几条记录开始取	Int 可选此参数0表示从第1条记录开始  limit=0时，此参数无效
		public long start_id;//				增量开始id	Int 可选 0时不使用该参数，默认为0.正数时，获取id小于该参数绝对值的记录.负数时，获取id大于该参数绝对值的记录
	}
	
	public static class DissentDetail implements Serializable{
        /**
		 * 
		 */
        private static final long serialVersionUID = -8122384550690909844L;
		public long id;//                 	异议ID		Int
        public long p_id;// 	交易id	Int
        public String p_type;//             	交易类型	Int
        public int point;//   	                	异议点	Int       1买入价 2卖出价 3盈亏分配 4交易数量 5亏损赔付 6履约金解冻  >=4其他
        public String point_name;
        public double price ;//             	价格	float
        public String reason;//             	异议原因内容	String
        public String comment;//            	回复内容	String
        public long create_time;//  	创建时间		Int
        public String update_time;//        更新时间	float
        public long comment_time;//         	回复时间	Int 
        public int state; //      	0等待处理 1处理中 2处理完成,无修正 3处理完成，有修正
        public long start_time;//            	开始时间	Int
        public String pno;//           	交易单号	Int
        public int operation_direction;//	操做方向	Int
        public String Code;//  	产品代码	String 
        public String stock_name;//     产品名称	string
        public long amount;//    	交易数量	Int
        public double fund;//        	交易金额	float  
	}
	
	
	public static void doRequest(Params params,ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
//   		Gson gson=new Gson();
//
//   		HashMap<String, String> postParams = postParams();
//   		postParams=getHashMapParams(params, postParams, gson);
//		proxy.getRequest(RequestInfo.S_DISSENT_ORDERS.getOperationType(),
//				ServiceConfig.getServicePlatform()/*.replace("https", "http")*/, postParams, DissentCorrection.class,null,false);
   
//		proxy.postRequest(RequestInfo.S_DISSENT_ORDERS.getOperationType(),
//				ServiceConfig.getServicePlatform()/*.replace("https", "http")*/, gson.toJson(params), DissentOrders.class,null,false,false);
		proxy.getRequest(RequestInfo.S_DISSENT_ORDERS, params, DissentOrders.class, RETURN_TYPE, false);
	}
}
