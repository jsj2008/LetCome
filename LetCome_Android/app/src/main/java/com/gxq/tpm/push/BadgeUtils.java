package com.gxq.tpm.push;

import java.lang.reflect.Field;
import java.util.List;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.mine.MsgList;
import com.gxq.tpm.network.NetworkProxy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class BadgeUtils {


    public static void setBadge(Context context, int count) {
//        setBadgeSamsung(context, count);
//        setBadgeSony(context, count);
        /*if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
        	sendToXiaoMi(context,count);
        }*/
    }

    public static void clearBadge(Context context) {
//        setBadgeSamsung(context, 0);
//        setBadgeSony(context,0);
       /* if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
        	sendToXiaoMi(context,0);
        }*/
    }


    private static void setBadgeSamsung(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    private static void setBadgeSony(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", true);
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));
        intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());

        context.sendBroadcast(intent);
    }
    
    private static void sendToXiaoMi(Context context, int count) {
    	String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        boolean isMiUIV6 = true;
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context); 
            builder.setContentTitle("您有"+count+"未读消息");
            builder.setTicker("您有"+count+"未读消息");
            builder.setAutoCancel(true);
            builder.setSmallIcon(context.getApplicationInfo().icon);
            builder.setDefaults(Notification.DEFAULT_LIGHTS);
            notification = builder.build(); 
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            //field.set(miuiNotification, number);// 设置信息数
            field.set(miuiNotification, Integer.valueOf(count));// 设置信息数
            field = notification.getClass().getField("extraNotification"); 
            field.setAccessible(true);
            field.set(notification, miuiNotification);  
            Toast.makeText(context, "Xiaomi=>isSendOk=>1", Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            e.printStackTrace();
            //miui 6之前的版本
            isMiUIV6 = false;
                Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
                localIntent.putExtra("android.intent.extra.update_application_component_name",context.getPackageName() + "/"+ launcherClassName );
                localIntent.putExtra("android.intent.extra.update_application_message_text",count);
                context.sendBroadcast(localIntent);
        }
        finally
        {
          if(notification!=null && isMiUIV6 )
           {
               //miui6以上版本需要使用通知发送
            nm.notify(101010, notification); 
            
           }
        }
    }

    private static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
    
    public static void requestList(Context context) {
    	final UserPrefs prefs = UserPrefs.get(context);
    	final Context mContext = context;
		MsgList.Params params=new MsgList.Params();
		params.limit = Integer.MAX_VALUE;
		/*params.to_time=-1;
		params.from_time=-1;*/
	    MsgList.doRequest(params, new NetworkProxy.ICallBack() {
			
			@Override
			public void callBack(RequestInfo info, BaseRes result, int tag) {
				MsgList msgList = (MsgList) result;
				if (null != msgList) {
//					BadgeUtils.setBadge(mContext, msgList.unread_count);
//					prefs.setUnreadCount(msgList.unread_count);
//					prefs.save();
				}
				
			}
		});
    }
}