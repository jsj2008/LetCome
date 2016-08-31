package com.gxq.tpm.db;

import java.util.List;

import com.gxq.tpm.mode.Stock;
import com.gxq.tpm.mode.StockList;

import android.content.Context;
//import android.util.Log;

public class DBManager {

	private static DBManager instance;
	
	private StockSQLiteOpenHelper mStockOpenHelper;
	
	private DBManager(Context context) {
		mStockOpenHelper = new StockSQLiteOpenHelper(context);
	}
	
	public static DBManager getInstance(Context context) {
		if (instance == null) {
			instance = new DBManager(context);
		}
		return instance;
	}
	
	public void init() {
		mStockOpenHelper.getReadableDatabase();
	}
	
//	public StockSQLiteOpenHelper getStockOpenHelper() {
//		return mStockOpenHelper;
//	}
	
	public List<Stock> stockQuery(String name) {
		return mStockOpenHelper.stockQuery(name);
	}
	
	public Stock stockQueryByCode(String code) {
		return mStockOpenHelper.stockQueryByCode(code);
	}
	
	public void updateStockList(StockList stockList) {
		mStockOpenHelper.updateDbStock(stockList);
	}
	
}
