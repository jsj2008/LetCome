package com.gxq.tpm.mode.strategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

import com.gxq.tpm.mode.BaseParse;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductProfit extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = -320790655715502102L;
	
	public ArrayList<Profit> records;
	
	public static class Profit implements Serializable {

		private static final long serialVersionUID = 9209932780448760318L;
		public int id;
		
		public float cur_price;	//现价（对手 价）	
		public double profit; //	盈亏(正盈负亏)	
		public float profit_rate; //盈亏率
		public int state;	//	状态:状态： >0 <200 待开仓 >=200 <500 待平仓 >= 500 收付款 -1状态查询;0;失败;1;正在申购;201：申购完成等待卖出（股票当日不可卖状态）;203;已开仓，可中止合作; 204：系统接管订单（用户失去操作权）; 205触发价申报中;206;触发锁定卖出中;207;市价锁定卖出中;301;触发锁定卖出;302;市价锁定卖出;303;回调止赢卖出;304;止损中止卖出;305;到时中止卖出;501;结束未结算;502;结束已结算;503：开仓前已中止
		public String state_name;	//状态翻译文字	
		public float y_close;	//昨收价
		public float buy_deal_price_avg; //开仓价
        public float sell_deal_price_avg; //平仓价
        
        public float earnest_loss_rate;
        
        public float price_proift; // 涨跌
        public String is_trade_time; // 交易时间
	}
	
	public static class Params implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7994440189961140576L;
		public String p_id_arr; //产品ID组 多个产品ID以【，】号隔开

	}
	
	public static void doRequest(Params params, ICallBack netBack) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.PRODUCT_PROFIT, params, 
				new ProductProfitParse(), RETURN_TYPE, true);
	}
	
	public static void doRequest(Params params, ICallBack netBack, int tag) {
		NetworkProxy proxy = new NetworkProxy(netBack);

		proxy.getRequest(RequestInfo.PRODUCT_PROFIT, params, 
				new ProductProfitParse(), tag, true);
	}
	
	public static class ProductProfitParse extends BaseParse<ProductProfit> {
		
		@Override
		protected ProductProfit parse(JSONObject jsonObject) {
			ProductProfit state = new ProductProfit();
			state.records = new ArrayList<ProductProfit.Profit>();
			
			Iterator<?> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				if (key.equals("error_code")
						|| key.equals("error_msg")) {
					continue;
				}
				ProductProfit.Profit profit = new ProductProfit.Profit();
				JSONObject object = parseObject(jsonObject, key);
				if (object != null) {
					profit.cur_price = (float) parseDouble(object, "cur_price");
					profit.y_close = (float) parseDouble(object, "y_close");
					profit.profit = parseDouble(object, "profit");
					profit.profit_rate = (float) parseDouble(object, "profit_rate");
					profit.state = parseInteger(object, "state");
					profit.state_name = parseString(object, "state_name");
					profit.id = Integer.valueOf(key);
					
					profit.buy_deal_price_avg = (float) parseDouble(object, "buy_deal_price_avg");
					profit.sell_deal_price_avg = (float) parseDouble(object, "sell_deal_price_avg");
					profit.price_proift = (float) parseDouble(object, "price_proift");
					profit.earnest_loss_rate = (float) parseDouble(object, "earnest_loss_rate");
					profit.is_trade_time = parseString(object, "is_trade_time");
					state.records.add(profit);
				}
			}
			return state;
		}
	}

}
