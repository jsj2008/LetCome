package com.gxq.tpm.mode.mine;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class UserCompensateOrders extends BaseRes {
	
	 /**
	 * 
	 */
    private static final long serialVersionUID = -297179658680283838L;
    public ArrayList<CompensateDetail> records;


	public static class Params implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 487726519736234972L;
		public String  p_type;//	产品类型
		public long from_time;//	点买时间>=该时间	Int  可选默认今天
		public long to_time;//	点买时间<=该时间	Int  可选
		public long limit;//			取出记录数	Int 可选  0时取全部
		public long offset;//				从第几条记录开始取	Int 可选此参数0表示从第1条记录开始  limit=0时，此参数无效
		public long start_id;//				增量开始id	Int 可选 0时不使用该参数，默认为0.正数时，获取id小于该参数绝对值的记录.负数时，获取id大于该参数绝对值的记录
	}
	
	public static class CompensateDetail implements Serializable{

		/**
		 * 
		 */
        private static final long serialVersionUID = -5267841434450538923L;
        public long id;//      	修正ID		Int
        public long p_id;// 	产品Id	Int
        public String usefor;//      	用途	String
        public long start_time;//   	开始时间			Int
        public String pno;//           	单号	String
        public double fund;//        	交易金额	float
        public double profit;//       	修正盈利	float
        public double last_profit;//  	原盈利	float
        public double delta_profit;// 	差额	Int float
        public long create_time;//  	创建时间		Int
        public int state; //      	状态	Int        0 未支付 1 已支付 2 未补偿 3 已补偿 4支付中
        public String state_name;
        public String Code;//  	产品代码	String
        public long amount; //     	数量	Int
        public long payer_uid; //	支付人id	Int
        public String payer_nickname;//	支付人昵称	String
        public String payer_type;//	支付人类型	string
        public long pay_time;//  	支付时间		Int
        public long payee_uid; //	接收人id	Int
        public String payee_nickname;//	接收人昵称	string
        public String payee_type;//	收款人类型	string
        public String fix_way; // 修正方式
        public String type_name; // 补偿类型
		public String p_type;
		public String fix_fail_time_str;
	}

	public static void doRequest(Params params,ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.S_USER_COMPENSATE, params,
				UserCompensateOrders.class, RETURN_TYPE, false);
	}
	
}
