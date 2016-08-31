package com.gxq.tpm.network;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetInfo {
	private static NetInfo instance;
	private ConnectivityManager connManager;
	private NetworkInfo networkInfo;

	private NetInfo(Context context) {
		connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = connManager.getActiveNetworkInfo();
	}

	public static NetInfo instance(Context context) {
		if (instance == null) {
			instance = new NetInfo(context);
		}
		return instance;
	}
	/**
	 * type 网络类型
	 * @return
	 */
	public int getType(){
		if (networkInfo != null) {
			return networkInfo.getType();

		}
		return 0;
	}

	/**
	 * state 网络状态
	 * @return
	 */
	public NetworkInfo.State getState(){
		if (networkInfo != null) {
			return networkInfo.getState();
		}
		return null;
	}

	/**
	 * reason 启动网络原因 可能为null
	 * @return
	 */
	public String getReason(){
		if (networkInfo != null) {
			return networkInfo.getReason();
		}
		return "";
	}

	/**
	 * extra 额外信息 可能为null
	 * @return
	 */
	public String getExtraInfo(){
		if (networkInfo != null) {
			return networkInfo.getExtraInfo();
		}
		return "";
	}

	/**
	 * isAvailable 是否可用
	 * @return
	 */
	public boolean isAvailable(){
		if (networkInfo != null) {
			return networkInfo.isAvailable();

		}
		return false;
	}

	/**
	 * roaming 是否漫游
	 * @return
	 */
	public boolean isRoaming(){
		if (networkInfo != null) {
			return networkInfo.isRoaming();
		}
		return false;
	}

	/**
	 * failover 是否失效
	 * @return
	 */
	public boolean isFailover(){
		if (networkInfo != null) {
			return networkInfo.isFailover();
		}

		return false;
	}
}
