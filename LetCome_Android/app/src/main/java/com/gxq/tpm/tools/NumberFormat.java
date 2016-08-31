package com.gxq.tpm.tools;

import java.text.DecimalFormat;

import com.letcome.R;

public class NumberFormat {
	/**
	 * 格式化数据，默认保留2位小数
	 */
	public static String decimalFormat(double data) {
		return decimalFormat("0.00", data);
	}

	/**
	 * 格式化数据，默认保留2位小数
	 */
	public static String decimalFormatWithSymbol(double data) {
		String symbol = data >= 0 ? "+" : "-";
		return symbol + decimalFormat("0.00", Math.abs(data));
	}

	/**
	 * 格式化数据，默认保留2位小数
	 */
	public static String decimalFormatWithNegativeSymbol(double data) {
		String symbol = data >= 0 ? "" : "-";
		return symbol + decimalFormat("0.00", Math.abs(data));
	}
	/**
	 * 格式化数据，不保留小数
	 */
	public static String decimalFormat0(double data) {
		return decimalFormat("0", data);
	}

	/**
	 * 格式化数据，保留1位小数
	 */
	public static String decimalFormat1(double data) {
		return decimalFormat("0.0", data);
	}
	
	/**
	 * 格式化数据，保留3位小数
	 */
	public static String decimalFormat3(double data) {
		return decimalFormat("0.000", data);
	}
	
	/**
	 * 格式化数据
	 */
	public static String decimalFormat(String pattern, double data) {
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(data);
	}
	
	/**
	 * @return 转换数据，0.022->2.20%，保留2位小数
	 */
	public static String percent(double perent) {
		return decimalFormat(perent * 100) + Util.transformString(R.string.percent_unit);
	}
	
	/**
	 * @return 转换数据，保留符号，0.022->+2.20%，保留2位小数
	 */
	public static String percentWithSymbol(double perent) {
		String symbol = perent >= 0 ? "+" : "-";
		return symbol + decimalFormat(Math.abs(perent * 100)) + Util.transformString(R.string.percent_unit);
	}
	
	/**
	 * @return 转换数据，保留符号，0.02->+2%，不保留小数
	 */
	public static String percentWithSymbol0(double perent) {
		String symbol = perent >= 0 ? "+" : "-";
		return symbol + decimalFormat0(Math.abs(perent * 100)) + Util.transformString(R.string.percent_unit);
	}

	/**
	 * @return 转换数据，保留符号，0.022->+2.2%，保留1位小数
	 */
	public static String percentWithSymbol1(double perent) {
		String symbol = perent >= 0 ? "+" : "-";
		return symbol + decimalFormat1(Math.abs(perent * 100)) + Util.transformString(R.string.percent_unit);
	}
	
	/**
	 * @return 转换数据，0.02->2%，不保留小数
	 */
	public static String percentWithInteger(double perent) {
		return decimalFormat0(perent * 100) + Util.transformString(R.string.percent_unit);
	}
	
	/**
	 * @param data
	 * @return xx,xxx.xx
	 */
	public static String bigDecimalFormat(double data) {
		return decimalFormat("###,##0.00", data);
	}
	
	/**
	 * @param amount //点买数量
	 * @return amount + 股
	 */
	public static String stockAmountToString(int amount) {
		String amountText = decimalFormat("0", amount);
		return amountText + Util.transformString(R.string.stock_amount_unit);
	}
	/**
	 * @param amount //点买数量
	 * @return amount + 股
	 */
	public static String stockAmountToString(String value) {
		return value + Util.transformString(R.string.stock_amount_unit);
	}
	
	/**
	 * @param value
	 * @return ￥ + value
	 */
	public static String moneySymbol(String value) {
		return Util.transformString(R.string.money_symbol) + value;
	}
	
	/**
	 * @param value
	 * @return value + 万
	 */
	public static String tenThousand(String value) {
		return Util.transformString(R.string.ten_thousand, value);
	}
	
	/**
	 * @param value
	 * @return value + 万元
	 */
	public static String tenThousandMoney(String value) {
		return Util.transformString(R.string.ten_thousand_money, value);
	}
	
	/**
	 * @param value
	 * @return value + 元
	 */
	public static String moneyUnit(String value) {
		return value + Util.transformString(R.string.money_unit);
	}
	
	/**
	 * @param value
	 * @return value + 元
	 */
	public static String moneyUnit(double value) {
		return decimalFormat(value) + Util.transformString(R.string.money_unit);
	}
	
	/**
	 * 数量为0，返回默认值"--"
	 */
	public static String amountDefaultValue(int value) {
		String amountStr = value == 0 ? Util.transformString(R.string.default_value)
				: NumberFormat.stockAmountToString(value);
		return amountStr;
	}
	/**
	 * 价格为0，返回默认值"--"
	 */
	public static String priceDefaultValue(float value) {
		String priceStr = value == 0.0 ? Util.transformString(R.string.default_value_1)
				: NumberFormat.decimalFormat(value);
		return priceStr;
	}

	/**
	 * 策略次数
	 */
	public static String getStrategyTimes(int value) {
		return Util.transformString(R.string.settlement_detail_strategy_time)
				+value+
			   Util.transformString(R.string.strategy_time);
	}
	/**
	 * 时间为0，返回默认值"--"
	 */
	public static String timeMilliToMonthMinuteDefault(long value) {
		String timetStr = value == 0 ? Util.transformString(R.string.default_value)
				: TimeFormat.milliToMonthMinute(value);
		return timetStr;
	}
	
	public static String timeMilliToMonthDefault(long value) {
		String timetStr = value == 0 ? Util.transformString(R.string.default_value)
				: TimeFormat.milliToMonthMinute(value);
		return timetStr;
	}
}
