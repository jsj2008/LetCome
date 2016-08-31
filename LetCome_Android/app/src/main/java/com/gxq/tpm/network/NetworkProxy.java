package com.gxq.tpm.network;

import com.gxq.tpm.mode.BaseParse;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkTask.NetworkCallback;
import com.gxq.tpm.tools.Print;

public class NetworkProxy implements NetworkCallback {
//	private static final boolean isRequestable = true; // zxc 前置接口是否开放

	private NetWork mNetwork;
	private ICallBack mCallBack;

	public NetworkProxy(ICallBack callBack) {
		this.mCallBack = callBack;
		mNetwork = NetWork.instance();
	}
	
	/**
	 * @param callBack
	 * @param timeOut  int 秒
	 */
	public NetworkProxy(ICallBack callBack, int timeOut) {
		this.mCallBack = callBack;
		mNetwork = NetWork.instance();
		mNetwork.setTimeOut(timeOut);
	}
	
	public void getRequest(final RequestInfo info, final Object params,
			final Class<? extends BaseRes> cls, int tag, boolean repeatable) {
		
		NetworkTask networkTask = new NetworkTask(info, this, tag, repeatable) {
			@Override
			protected BaseRes callService() throws Exception {
				if (info.isPost()) {
					return mNetwork.postRequest(info, params, cls);
				} else {
					return mNetwork.getRequest(info, params, cls);
				}
			}
		};
		networkTask.doInBackground();
	}
	
	public void getRequest(final RequestInfo info, final Object params,
			final BaseParse<? extends BaseRes> parse, int tag,
			boolean repeatable) {
		NetworkTask networkTask = new NetworkTask(info, this, tag, repeatable) {
			@Override
			protected BaseRes callService() throws Exception {
				if (info.isPost()) {
					return mNetwork.postRequest(info, params, parse);
				} else {
					return mNetwork.getRequest(info, params, parse);
				}
			}
		};
		networkTask.doInBackground();
	}
	
	
	public void getHQRequest(final RequestInfo info, final Object params,
			final Class<? extends BaseRes> cls, final int tag, final boolean repeatable) {
		NetworkTask networkTask = new NetworkTask(info, this, tag, repeatable) {
			@Override
			protected BaseRes callService() throws Exception {
				return mNetwork.getRequest(info, params, cls);
			}
		};
		networkTask.doInBackground();
	}

	@Override
	public void networkFinishedProxy(RequestInfo info, BaseRes result, int tag) {
		if (mCallBack == null) {
			Print.i("NetworkProxy", "handle is null");
			return;
		}

		mCallBack.callBack(info, result, tag);
	}

	public interface ICallBack {
		public void callBack(RequestInfo info, BaseRes result, int tag);
	}
	
}
