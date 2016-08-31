package com.gxq.tpm.mode;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockList extends BaseRes {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4221736397951862398L;

	public List<Stock> stockList;
	
	public static class StockListParse extends BaseArrayParse<StockList> {

		@Override
		protected StockList parse(JSONArray jsonArray) {
			StockList stockList = new StockList();
			stockList.stockList = new ArrayList<Stock>();
			
			try {
				for (int index = 0; index < jsonArray.length(); index++) {
					JSONObject object = jsonArray.getJSONObject(index);
					Stock stock = new Stock();
					stock.setStockCode(parseString(object, "code"));
					stock.setStockName(parseString(object, "name"));
					stock.setStockAbbr(parseString(object, "abbr"));
					stock.setCreateTime(parseString(object, "create_time"));
					stock.setUpdateTime(parseString(object, "update_time"));
					stock.setPlateType(parseString(object, "platetype"));
					stock.setCodeShsz(parseString(object, "code_shsz"));
					stockList.stockList.add(stock);
				}
				return stockList;
			} catch (JSONException e) {
				return null;
			}
		}
	}
	
}
