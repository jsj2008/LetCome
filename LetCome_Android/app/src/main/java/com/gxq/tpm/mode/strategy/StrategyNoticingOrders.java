package com.gxq.tpm.mode.strategy;

import java.util.List;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class StrategyNoticingOrders extends BaseRes {
	private static final long serialVersionUID = -3426954127176666940L;

	public List<StrategyNoticing> records;
	
	public static class StrategyNoticing extends AbstractStrategy {

		private static final long serialVersionUID = 1116467395865002393L;

//		public int id; // 交易ID
		public String pno; // 交易单号	
		public int operation_direction; // 点买方向 1:多 2:空 3:双向多 4:双向空	
		public int start_time; // 开始时间
		public int create_time; // 开始时间
		public int end_time; // 结束时间
		public float bid_bond; // 保证金
		public String stocker_user_name; // 开户名
		public float init_fund; // 实际申购金额
		public float left_fund; // 剩余申购金额
		public float fund; // 投资单元
		public int scheme_id; // 方案号
		public int state; // 状态 默认为-1 正在查询 具体状态请使用产品动态信息接口获取
		public String state_name; // 状态翻译文字
		public int amount; // 申购数量
		public String code; // 产品代码;
		public float buy_way; // 点买方式
		public String stock_name; // 股票名称
		public int invester_id; // 投资人Id;
		public String invest_account; // 投资人账户
		public String dealer_replace; // 投资人代号
		public String invester_name; // 投资人名	
		public String invest_dealer; // 交易市场
		public String invest_open_dealer; // 投资人开户营业部
		public String invest_location; // 投资人开户地
		public String invest_icon; // 投资人头像地址
		public float stop_profit_point; //	止赢（盈利目标）
		public float stop_loss_point; // 止损	
		public float mid_safe_point; // 中间安全点
		public float max_safe_point; // 最大安全点
		public float start_price; // 开仓触发价
		public float buy_deal_price_avg; // 买入均价
		public float use_rate; // 使用率	
		public int trade_day; // 已交易日天数	
		public int max_trade_day; // 最大交易日天数
		public int apply_start_time; // 申请通知开始时间
		public int apply_end_time; // 申请通知结束时间	
	}
	
	public static void doRequest(QueryParams params, ICallBack netback) {
		NetworkProxy proxy = new NetworkProxy(netback);
		
		proxy.getRequest(RequestInfo.PRODUCT_TODO, params, StrategyNoticingOrders.class, RETURN_TYPE, false);
	}
	
}
