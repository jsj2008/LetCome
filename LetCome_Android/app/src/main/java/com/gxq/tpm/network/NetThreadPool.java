package com.gxq.tpm.network;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetThreadPool {
	public static final int THREADPOOL_SIZE = 5;
	public static final int THREADPOOL_MAX_REPEAT_SIZE = 5;
	
	private Stack<RequestInfo> requestUnrepeatable;
	private ExecutorService poolUnrepeatable;
	private Stack<RequestInfo> requestRepeatable;
	private ExecutorService poolRepeatable;
	
	private static NetThreadPool instance;

	private NetThreadPool(){
		poolUnrepeatable = Executors.newFixedThreadPool(THREADPOOL_SIZE);
		requestUnrepeatable = new Stack<RequestInfo>();
		poolRepeatable = Executors.newFixedThreadPool(THREADPOOL_SIZE + THREADPOOL_MAX_REPEAT_SIZE);
		requestRepeatable = new Stack<RequestInfo>();
	}

	public static NetThreadPool instance() {
		if (instance == null) {
			instance = new NetThreadPool();
		}
		return instance;
	}
	
	public boolean add(RequestInfo info, boolean repeatable) {
		return getRequest(repeatable).add(info);
	}
	
	public void remove(RequestInfo info, boolean repeatable) {
		getRequest(repeatable).remove(info);
	}
	
	public void execute(Runnable runnable, boolean repeatable) {
		getPool(repeatable).execute(runnable);
	}
	
	public ExecutorService getPool(boolean isReatable) {
		return isReatable ? poolRepeatable : poolUnrepeatable;
	}
	
	public Stack<RequestInfo> getRequest(boolean isReatable) {
		return isReatable ? requestRepeatable : requestUnrepeatable;
	}
	
	public boolean checkRequest(RequestInfo info, boolean repeatable) {
		if (repeatable) {
			int repeatTimes = 0;
			for (RequestInfo rInfo : requestRepeatable) {
				if (rInfo == info) {
					repeatTimes++;
					
					if (repeatTimes >= NetThreadPool.THREADPOOL_MAX_REPEAT_SIZE)
						return false;
				}
			}
			return true;
		} else {
			return !requestUnrepeatable.contains(info);
		}
	}

}
