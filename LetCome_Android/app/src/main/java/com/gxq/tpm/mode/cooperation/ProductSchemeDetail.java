package com.gxq.tpm.mode.cooperation;

import java.io.Serializable;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;

public class ProductSchemeDetail extends BaseRes{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8943250887919901332L;
	public String fund; 
	public int bid_bond;
	public int break_cost;
	public int fee;
	public String hold_days;
	public String buy_date;
	public String presell_date_start;
	public String presell_date_end;
	public String presell_time_start;
	public String presell_time_end;
	public String sell_date;
	public String buy_way;
	public String sell_way;
	public float stop_loss_point;
	public float stop_profit_point;
	public String account_type;
	public String buy_times;
	public String sell_times;
	public int investor_bond;
	public String id;
	public String name;
	public String name_show;
	public String p_type;
    public String not_break_gain_user;
    public String not_break_loss_user;
    public String not_break_break_base_rate;
    public String not_break_break_dynamic_rate;
    public String break_contract_gain_user;
    public String break_contract_loss_user;
    public String break_contract_break_base_rate;
    public String break_contract_break_dynamic_rate;
    public int temp_buy_times;
    public int temp_sell_times;
    public String hold_time;
    public String buy_time;
    public String sell_time;
    public String apply_sell_time;
    public String apply_sell_date;
    public String success_cost;
    public String success_loss;
    public String success_profit;
    public String fail_cost;
    public String fail_loss;
    public String fail_profit;

	public static class Params implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 8007335553611641571L;
		public int scheme_id; //	方案号	int	true
	}
	
	public static void doRequest(Params params, ICallBack netBack) {
   		NetworkProxy proxy = new NetworkProxy(netBack);
   		
   		proxy.getRequest(RequestInfo.PRODUCT_SCHEME_DETAIL, params, ProductSchemeDetail.class, RETURN_TYPE, false);
   	}

}

