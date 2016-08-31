package com.gxq.tpm.mode.strategy;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductDetails extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4588407453525765983L;
	
	public int id; //	产品id	Int
	public int uid; //	点买人id	int
	public int invester_id;//	投资人Id	Int
	public String invester_name;	//投资人名	string
	public String invest_dealer;	//交易市场	string
	public String invest_open_dealer; //	开户营业部	string
	public String invest_location; //	开户地	string
	public String invest_account; //	资金账号	string
	public String holder_account; //	股东账号
	public float bid_bond; //	保证金	Float
	public long create_time; //	发布时间	Int
	public long start_time; //	合作时间	Int
	public long end_time; //	结束时间	Int
	public int operation_direction; //	操作方向 1：多 2：空	Int
	public int state; //	状态	Int
	public String state_name; //	状态翻译	Int
	public String pno; //	交易单号	String
	public String code; //	产品代码	String
	public int amount; //	买数量	Int
	public float fund; //	金额	float
	public float buy_deal_price_avg;	//买入均价（开仓价）	float
	public float sell_deal_price_avg;//	卖出均价（平仓价）	float
	public float buy_start_price; //	开仓触发价	float
	public float sell_start_price; //	平仓触发价	float
	public long buy_execute_time;//	开仓策略执行时间	string
	public long sell_execute_time; //	平仓策略执行时间
	public long buy_deal_time	; //开仓成交时间	string
	public long sell_deal_time; //	平仓成交时间	string
	public int buy_way; //	点买方式 0:市价点买 1：触发价点买	Int
	public int sell_way; //	点卖方式 0:市价点卖 1：触发价点卖	Int
	public float profit; //	盈利	float
	public float user_profit; //	分配利润 >0时填盈利分配 <0时填亏损赔付	Float
	public int settlemnt_flag;//	清算标志 1 已结算	Int
	public float bid_bond_minus; //	扣减保证金	Int
	public long settlement_deadline;//	预计讨讫（结算）时间	int
	public String nickname; //	点买人昵称	String
	public String stock_name; //	产品名称	String
	public int clear_type; //	结算类型 1市价卖出 2止盈中止 3止损中止 4尾盘到时中止 5到时中止 6触发价挂单超时（买入时）	Int
	public float credit_repayment; //	信用还款	float
	public String buy_type; //	买入类型翻译文字	String
	public String sell_type; //	卖出类型翻译文字	String
	public float fee_total;	//点买手续费	Float
	public String scheme_name; //	方案名	string
	public Distribution distribution; //	赢利分配方案
	public float user_profit_rate;//	用户分配比例	float
	public float stop_profit_point;//	盈利目标	float
	public float stop_loss_point;//	最大亏损赔付	float
	public int end_flag;	//协商中止:1:允许 0：不允许	string
	public String settlement_predict_str; //预测结算时间
	public int dissent_flag; //	异议标志:0:不能提交异议 1:可以提交异议 2：有异议处理中
	public float start_price;
	public int compensate_flag; //违约标志:0:非违约 1:违约
	public String dealer_replace; // 投资人标志代码
	public int stop_profit_point_flag; // 盈利目标标识 1：百分比 2：元/股
	
	
	public String buy_date;	//开仓日期	String
	public int buy_times;	//策略买次数 int
	public int buy_amount;	//买入股数	int
	public float buy_deal_avg_price;//	买入价	float
	public String apply_sell_date; //	通知平仓日	String
	public String apply_sell_days; //	通知平仓日所在日	String
	public String y_close;//	通知平仓日收盘价	String
	public String apply_sell_profit_rate; //	通知平仓日收益浮盈   String
	public String sell_date	; //平仓日期	String
	public int sell_times; //	策略卖次数	int
	public float sell_deal_avg_price; //	卖出价	float
	public int sell_amount;//	卖出股数	Int

	
	public static class Distribution implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8549909294941340631L;
		public float user_profit; //用户分配比例
		public float t_profit;
		public float f_profit;
		public float p_profit;
	}
	
	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -825578161210740937L;
		public int p_id;	//产品ID	
		public String p_type;	//产品类型	
	}

	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);
		proxy.getRequest(RequestInfo.PRODUCT_DETAILS, params, ProductDetails.class, RETURN_TYPE, false);
	}

}
