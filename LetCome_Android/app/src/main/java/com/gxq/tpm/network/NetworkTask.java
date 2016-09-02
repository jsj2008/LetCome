package com.gxq.tpm.network;

import android.os.Handler;
import android.os.Message;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.tools.Print;

public abstract class NetworkTask {
	private static final String TAG = "NetworkTask";
	
	private RequestInfo mInfo;
	private boolean mRepeatable;
	
	private Handler mHandler;
	private NetThreadPool mThreadPool;
	
	public NetworkTask(RequestInfo info, NetworkCallback callback) {
		this(info, callback, BaseRes.RETURN_TYPE);
	}
	
	public NetworkTask(RequestInfo info, NetworkCallback callback, int tag) {
		this(info, callback, tag, false);
	}

	public NetworkTask(RequestInfo info, NetworkCallback callback, int tag,
			boolean repeatable) {
		this.mInfo = info;
		this.mRepeatable = repeatable;
		
		mThreadPool = NetThreadPool.instance();
		
		initHandler(info, callback, tag);
	}
	
	public void initHandler(final RequestInfo info,
			final NetworkCallback callback, final int tag) {
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				mThreadPool.remove(info, mRepeatable);

				if (msg.obj != null && msg.obj instanceof BaseRes) {
//					UserPrefs prefs = App.getUserPrefs();
					String session = null;
					try {
						session = ((BaseRes) msg.obj).session_id;
					} catch (Exception e) {
						e.printStackTrace();
					}
//					if (null != session)
//						prefs.setSession(session);
				}

				if (callback != null)
					callback.networkFinishedProxy(info, (BaseRes) msg.obj, tag);
			}
		};
	}
	
	protected void doInBackground() {
		Print.i(TAG, mThreadPool.getRequest(false).toString());
		Print.i(TAG, mThreadPool.getRequest(true).toString());
		
		if (!mThreadPool.checkRequest(mInfo, mRepeatable)) {
			if (mRepeatable) {
				Print.i(TAG, mInfo.getOperationType() + "重复请求超过" + NetThreadPool.THREADPOOL_MAX_REPEAT_SIZE + "次");
			} else {
				Print.i(TAG, mInfo.getOperationType() + "重复请求");
			}
			return;
		}
		
		mThreadPool.add(mInfo, mRepeatable);
		mThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				BaseRes result = null;
				try {
					result = callService();
				} catch (Exception e) {
					Print.e(TAG, "" + e);
				} finally {
					Message msg = mHandler.obtainMessage();
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}
		}, mRepeatable);
	}	

	protected abstract BaseRes callService() throws Exception;
	
	public static interface NetworkCallback {
		public void networkFinishedProxy(RequestInfo info, BaseRes result, int tag);
	}
	
}