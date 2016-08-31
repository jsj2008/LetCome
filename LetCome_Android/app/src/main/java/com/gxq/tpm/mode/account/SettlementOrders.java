package com.gxq.tpm.mode.account;

import java.io.Serializable;
import java.util.ArrayList;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class SettlementOrders extends BaseRes {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4316198977825129670L;
	public ArrayList<Settlement> records;
	public long total;

	public static class Settlement implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7303717287468627638L;

		public int id; //	清算id	
		public String p_type; //	产品类型	
		public int p_id; //	产品id	int
		public int operation_direction;//	点买方向 1：多， 2：空,3:双向做多 4:双向做空	
		public int sub_type;//	产品子类型 0:非递延 n:T+Dn(9就是T+D9)
		public String stocker_user_name; //	开户名（无需使用）	
		public int state; //	状态 0：待清算 1：待收款 2：待付款 3：待结算 4：已结算	
		public String state_name; //	状态翻译	
		public String pno; //	交易单号	
		public long create_time; //	发布时间	
		public long start_time; //	合作时间	
		public String code; //	产品代码	
		public String stock_name; //	产品名称	
		public int amount; //	点买数量	
		public float fund;	//点买金额	
		public float buy_deal_price_avg;//	点买触发价	
		public int buy_way; //	点买方式 0：市价点买 1：触发价点买	
		public float sell_deal_price_avg; //	点卖触发价	
		public int sell_way; //	点买方式 0：市价点卖 1：触发价点卖
		public float profit; //	盈利（执行结果）	
		public float user_profit; //	分配利润	
		public float break_cost; //	成本补偿	
		public float share_profit; //分享利润	
		public float stop_profit_point; //	止盈（盈利目标）	
		public String scheme_name; //	方案名	
		public int invester_id; //	投资人Id	
		public String invest_account; //	投资人账户	
		public String invester_name; //	投资人名	
		public String invest_dealer; //	交易市场	
		public String invest_open_dealer; //	投资人开户营业部	
		public String invest_location; //	投资人开户地	
		public String invest_icon; //	投资人头像地址
		public float stop_loss_point;
		public String dealer_replace;//投资人代号
		public String pay_end_time;//付款结束时间
		public int dissent_flag;//异议标志:0:不能提交异议 1:可以提交异议 2：有异议处理中
		public float fee;//付款结束时间
		public long close_time;//增量平仓时间
	}

	public static class Params implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2849067919291412299L;
		public long from_time;// 点买时间>=该时间 Int 可选默认今天
		public long to_time;// 点买时间<=该时间 Int 可选
		public long limit;// 取出记录数 Int 可选 0时取全部
		public long offset;// 从第几条记录开始取 Int 可选此参数0表示从第1条记录开始 limit=0时，此参数无效
		public long start_id;// 增量开始id Int 可选
								// 0时不使用该参数，默认为0.正数时，获取id小于该参数绝对值的记录.负数时，获取id大于该参数绝对值的记录
		public long close_time;//增量平仓时间
	}

	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.PRODUCT_SETTLEMENT_ORDERS, params,
				SettlementOrders.class, RETURN_TYPE, false);
	}
}
