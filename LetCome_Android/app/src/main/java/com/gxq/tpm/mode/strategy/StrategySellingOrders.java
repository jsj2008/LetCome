package com.gxq.tpm.mode.strategy;

import java.util.List;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class StrategySellingOrders extends BaseRes {

	private static final long serialVersionUID = -1634258260912398040L;
	public List<StrategySelling> records;
	
	public static class StrategySelling extends AbstractStrategy {

		private static final long serialVersionUID = -4157566204402573784L;

//		public int id; // 交易ID	
		public String pno; // 交易单号
		public int operation_direction; // 点买方向 1:多 2:空 3:双向多 4:双向空
		public int start_time; //开始时间
		public int create_time;
		public int end_time; // 结束时间
		public float bid_bond; // 保证金
		public String stocker_user_name; // 开户名	
		public float init_fund; // 实际申购金额
		public float left_fund; // 剩余申购金额
		public float fund; // 投资单元	
		public int state; // 状态 0：禁止平仓 1：允许平仓(APP再根据是否为交易时间判断样式) 2：系统平仓
		public String state_name; // 状态翻译文字	
		public int amount; // 总股数
		public int sell_amount; // 已卖股数
		public int left_amount; // 待卖股数
		public String code; // 产品代码	
		public float buy_way; // 点买方式
		public String stock_name; // 股票名称
		public int invester_id; // 投资人Id
		public String invest_account; // 投资人账户
		public String dealer_replace; // 投资人代号
		public String invester_name; // 投资人名	
		public String invest_dealer; // 交易市场
		public String invest_open_dealer; // 投资人开户营业部
		public String invest_location; // 投资人开户地
		public String invest_icon; // 投资人头像地址
		public float stop_profit_point; // 止赢（盈利目标）	
		public float stop_loss_point; // 止损	
		public float start_price; // 开仓触发价
		public float buy_deal_price_avg; // 买入均价	
		public float use_rate; // 使用率
		public int times; // 已卖次数	
//		public int max_times; // 最大次数
		public int buy_max_times;
		public int sell_max_times; 
	}
	
	public static void doRequest(QueryParams params, ICallBack netback) {
		NetworkProxy proxy = new NetworkProxy(netback);
		
		proxy.getRequest(RequestInfo.PRODUCT_SELLING, params, StrategySellingOrders.class, RETURN_TYPE, false);
	}
	
}
