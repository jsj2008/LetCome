package com.gxq.tpm.push;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.letcome.App;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.UserDevice;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.Print;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调

 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many

 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class PushReceiver extends PushMessageReceiver {
	/** TAG to Log */
	public static final String TAG = PushReceiver.class.getSimpleName();
	
	private int count = 0;

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode非0时为null
	 * @param userId
	 *            应用user id。errorCode非0时为null
	 * @param channelId
	 *            应用channel id。errorCode非0时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */
//	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		Print.d(TAG, responseString);

		if (errorCode == 0) {
			setUserDevice(channelId);
		}
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
//	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		String messageString = "透传消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		Print.d(TAG, messageString);

		// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (!customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 接收通知点击的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
//	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Print.d(TAG, notifyString);

		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> task_info = manager.getRunningTasks(20);

		String className = "";
		Intent intent = new Intent();
		boolean isLaunch = false;
		for (int i = 0; i < task_info.size(); i++) {
			if (context.getPackageName().equals(task_info.get(i).topActivity.getPackageName())) {
				className = task_info.get(i).topActivity.getClassName();
				// 这里是指从后台返回到前台 前两个的是关键
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				try {
					intent.setComponent(new ComponentName(context, Class
							.forName(className)));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}//
					// intent.setClass(context,
					// Class.forName(className));
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
						| Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				context.startActivity(intent);
				isLaunch = true;
			}
		}
		if(!isLaunch){
			String packageName = context.getPackageName();
			intent = context.getPackageManager().getLaunchIntentForPackage(packageName);        
			context.startActivity(intent);
		}

		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (!customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 接收通知到达的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */

//	@Override
	public void onNotificationArrived(Context context, String title,
			String description, String customContentString) {

		String notifyString = "onNotificationArrived  title=\"" + title
				+ "\" description=\"" + description + "\" customContent="
				+ customContentString;
		Print.d(TAG, notifyString);
		
		UserPrefs prefs = UserPrefs.get(context);
		
		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
		if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				if (!customJson.isNull("unread_count")) {
					count = customJson.getInt("unread_count");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
//			count = prefs.getUnreadCount()+1;
		}
//		BadgeUtils.setBadge(context, count);
//		prefs.setUnreadCount(count);
//		prefs.save();
		// 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
	}

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param successTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
//	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Print.d(TAG, responseString);
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param successTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
//	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode
				+ " sucessTags=" + sucessTags + " failTags=" + failTags
				+ " requestId=" + requestId;
		Print.d(TAG, responseString);
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示列举tag成功；非0表示失败。
	 * @param tags
	 *            当前应用设置的所有tag。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
//	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags="
				+ tags;
		Print.d(TAG, responseString);
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
//	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Print.d(TAG, responseString);

		if (errorCode == 0) {
			// 解绑定成功
		}
	}

	public void setUserDevice(String channelId){
		if (App.getUserPrefs().getUid() > 0) {
			UserDevice.Params params = new UserDevice.Params();
			params.dev_id = channelId;
			params.dev_type = 0; //设备类型（0:android 1:ios）
			UserDevice.doRequest(params, new NetworkProxy.ICallBack() {

				@Override
				public void callBack(RequestInfo info, BaseRes result, int tag) {
					//UserDevice res = (UserDevice) object;
					//Print.e("", "operationType = "+operationType+" res = " +res+" "+res.error_code+" "+res.result);
				}
			});
		}
	}
}
