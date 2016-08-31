package com.gxq.tpm.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchStock implements Serializable {

	private static final long serialVersionUID = -1469197535781449163L;
	private static final int STOCK_LIMIT = 10;
	
	private List<Stock> stocks;
	
	public SearchStock() {
		stocks = new ArrayList<Stock>();
	}
	
	public List<Stock> getStocks() {
		return stocks;
	}
	
	public boolean addStock(Stock stock) {
		int index = -1;
		int size = stocks.size();
		for(int i = 0; i < size; i++) {
			Stock ss = stocks.get(i);
			if (ss.getStockCode() != null && ss.getStockCode().equals(stock.getStockCode())) {
				index = i;
				break;
			}
		}
		
		if (index >= 0) {
			if (stocks.remove(index) != null) {
				stocks.add(0, stock);
				return size == stocks.size();
			} else {
				return false;
			}
		} else {
			stocks.add(0, stock);
			if (size >= STOCK_LIMIT) {
				stocks.remove(size);
				return size == stocks.size();
			} else {
				return (size + 1) == stocks.size();
			}
			
		}
	}
	
	public Stock removeStock(int index) {
		return stocks.remove(index);
	}
	
	public void clearAll() {
		stocks.clear();
	}
	
}
