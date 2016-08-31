package com.gxq.tpm.mode;

public class Stock extends BaseRes {
	
	private static final long serialVersionUID = 6854736073857926306L;

	private String stockName;
	private String stockCode;
	private String stockAbbr;
	private String createTime;
	private String updateTime;
	private String plateType;
	private String codeShsz;
	
	public Stock() {
	}
	
	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	
	public String getStockAbbr() {
		return stockAbbr;
	}
	
	public void setStockAbbr(String stockAbbr) {
		this.stockAbbr = stockAbbr;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPlateType() {
		return plateType;
	}

	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}

	public String getCodeShsz() {
		return codeShsz;
	}

	public void setCodeShsz(String codeShsz) {
		this.codeShsz = codeShsz;
	}
	
}
