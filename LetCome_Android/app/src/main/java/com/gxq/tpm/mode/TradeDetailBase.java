package com.gxq.tpm.mode;


public class TradeDetailBase extends BaseRes {
    private static final long serialVersionUID = -7777608495053497818L;
    public long p_id;//	产品id	int
	public double  buy_deal_price_avg;//   	买均价	float
    public double  sell_deal_price_avg;//  	卖均价	
    public double user_profit ;//       	分配利润	float        >0时填盈利分配        <0时填亏损赔付
    
    public static String BuyType(int state)
	{
		switch (state)
		{
		case 0:
			return "即时买入";
		case 1:
			return "触发买入";
		default:
			return "即时买入";
		}
	}

	public static String SellType(int state)
	{
		switch (state)
		{
		case 0:
			return "触发卖出";
		case 1:
			return "即时卖出";
		case 2:
			return "触发止盈";
		case 3:
			return "触发止损";
		case 4:
			return "到时中止";
		case 5:
			return "到时中止";//约定中止
		case 6:
			return "触发买入超时";
		}
		return "";
	}
	
}
