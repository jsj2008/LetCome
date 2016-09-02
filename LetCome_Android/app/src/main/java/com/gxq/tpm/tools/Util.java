package com.gxq.tpm.tools;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore.Audio;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.letcome.App;
import com.letcome.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	protected static final String Tag = "Util";
	private static float sPixelDensity = 1;

	private Util() {
	}

	public static void initialize(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		sPixelDensity = metrics.density;
	}

	public static int dpToPixel(float dp) {
		return Math.round(sPixelDensity * dp);
	}
	
	public static int getScreenWidth(Activity activity){
		DisplayMetrics metric = new DisplayMetrics();  
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);  
		int width = metric.widthPixels;     // 屏幕宽度（像素）  
//		int height = metric.heightPixels;   // 屏幕高度（像素）  
//		float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）  
//		int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）  
		return width;
	}
	
	public static int getScreenHeight(Activity activity){
		DisplayMetrics metric = new DisplayMetrics();  
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);  
		int height = metric.heightPixels;   // 屏幕高度（像素）  
		return height;
	}

	/**
	 * 检查是否空字符串，如果为null,返回""
	 * @param string
	 * @return
	 */
	public static String checkS(String string) {
		if (string == null)
			return "";
		return string;
	}

	/**
	 * 直接获取资源文件，无需在类中使用Resources
	 */
	public static Drawable getDrawable(int id) {
		return App.instance().getResources().getDrawable(id);
	}

	public static int transformColor(int colorId) {
		return App.instance().getResources().getColor(colorId);
	}

	public static String transformString(int strId) {
		return App.instance().getResources().getString(strId);
	}
	
	public static String transformString(int strId, Object... formatArgs) {
		return App.instance().getResources().getString(strId, formatArgs);
	}

	public static int transformDimen(int dimen) {
		return App.instance().getResources().getDimensionPixelSize(dimen);
	}

	public static boolean transformBoolean(int id) {
		return App.instance().getResources().getBoolean(id);
	}

	public static String[] getStringArray(int strId) {
		return App.instance().getResources().getStringArray(strId);
	}

	/**
	 * 字符串设置颜色
	 * @param src 字符串
	 * @param color 颜色
	 * @return
	 */
	public static SpannableString strChangeColor(String src, int color) {
		return strChangeColor(src, 0, color);
	}

	public static SpannableString strChangeColor(String src, int start, int color) {
		return strChangeColor(src, start, src.length(), color);
	}

	public static SpannableString strChangeColor(String src, int start,int end, int color) {
		SpannableString srcSpan = new SpannableString(src);
		return strChangeColor(srcSpan, start, end, color);
	}

	public static SpannableString strChangeColor(SpannableString span,
			int start, int end, int color) {
		if ((start > -1) && (end > start)) {
			span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return span;
	}

	/**
	 * 设置字符串中部分文字字体大小
	 * @param src
	 * @param start	 
	 * @param end
	 * @param size
	 * @return
	 */
	public static SpannableString strChangeSize(CharSequence src, int start,
			int end, int size) {
		SpannableString span = new SpannableString(src);
		if ((start > -1) && (end > start)) {
			span.setSpan(new AbsoluteSizeSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return span;
	}
	
	/**
	 * 拼接两个颜色不同的字符串
	 * @param textLeft
	 * @param leftColor
	 * @param textRight
	 * @param rightColor
	 * @return
	 */
	public static CharSequence addCharSequence(String textLeft, int leftColor, String textRight, int rightColor) {
		SpannableStringBuilder builder = new SpannableStringBuilder(textLeft + textRight);
		builder.setSpan(new ForegroundColorSpan(leftColor), 0, textLeft.length(), 0);
		builder.setSpan(new ForegroundColorSpan(rightColor), textLeft.length(), textLeft.length() + textRight.length(), 0);
		return builder;
	}

	public static CharSequence addCharSequence(CharSequence textLeft, String textRight, int rightColor) {
		SpannableStringBuilder builder = new SpannableStringBuilder(textLeft);
		builder.append(textRight);
		builder.setSpan(new ForegroundColorSpan(rightColor), textLeft.length(), textLeft.length() + textRight.length(), 0);
		return builder;
	}

	/**
	 * 将数组内容依次拼接到字符串后面
	 * @param separator 原字符串
	 * @param array 数组
	 * @return
	 */
	public static String join(String separator, int[] array) {
		StringBuffer sb = new StringBuffer();
		int length = array.length;
		for (int i = 0; i < length; i++) {
			if (i == (length - 1)) {
				sb.append(array[i]);
			} else {
				sb.append(array[i]).append(separator);
			}
		}
		
		return new String(sb);
	}
	
	public static String join(String separator, String[] array) {
		StringBuffer sb = new StringBuffer();
		int length = array.length;
		for (int i = 0; i < length; i++) {
			if (i == (length - 1)) {
				sb.append(array[i]);
			} else {
				sb.append(array[i]).append(separator);
			}
		}
		
		return new String(sb);
	}

	/**
	 * 将列表内容依次拼接到字符串后面
	 * @param separator 原字符串
	 * @param array 列表
	 * @return
	 */
	public static String join(String separator, List<?> array) {
		StringBuffer sb = new StringBuffer();
		int length = array.size();
		for (int i = 0; i < length; i++) {
			if (i == (length - 1)) {
				sb.append(array.get(i));
			} else {
				sb.append(array.get(i)).append(separator);
			}
		}

		return new String(sb);
	}

	/**
	 * 为数字字符串添加"+"号
	 * @param numb
	 * @return
	 */
	public static String addMarkByPlusMinus(String numb) {
		if (!isNumericSign(numb)) {
			return numb;
		}

		// delete sign ","
		String r = numb.replace(",", "");

		if (Double.parseDouble(r) >= 0) {
			numb = "+" + numb;
		}

		return numb;
	}

	/**
	 * 获取字符串需要的颜色
	 * @param price
	 * @return
	 */
	public static int getColorByPlusMinus1(float price) {
		int colorId;

//		if (!isNumericSign(price)) {
//			price = "0";
//		}
//
//		// delete sign ","
//		price = price.replace(",", "");

		if (price > 0) {
			colorId = R.color.gain_color;
		} else if (price < 0) {
			colorId = R.color.loss_color;
		} else {
			colorId = R.color.text_color_sub;
		}

		return transformColor(colorId);
	}
	
	public static int getColorByPlusMinus(double price) {
		return transformColor(price >= 0 ? R.color.gain_color : R.color.loss_color);
	}

	/**
	 * @param numb
	 * @param type 1只判定是否为红色
	 * @return
	 */
	public static int getColorByPlusMinus(double numb, int type) {
		int colorId;

		if (numb > 0) {
			colorId = R.color.gain_color;
		} else if (numb < 0) {
			if (type != 1)
				colorId = R.color.loss_color;
			else
				colorId = R.color.text_color_title;
		} else {
			colorId = R.color.color_999999;
		}

		return transformColor(colorId);
	}

	/**
	 * 大于昨收为红，小于昨收为绿
	 */
	public static int getStockColor(float value, float yClose) {
		return getStockColor(value, yClose, R.color.text_color_title);
	}

	/**
	 * 大于昨收为红，小于昨收为绿
	 */
	public static int getStockColor(float value, float yClose, int defColor) {
		if (value > yClose) {
			return transformColor(R.color.gain_color);
		} else if (value < yClose) {
			return transformColor(R.color.loss_color);
		}
		return transformColor(defColor);
	}

	// 看多为红，看空为绿
	public static int getStockColor(boolean lookBullish) {
		if (lookBullish) {
			return transformColor(R.color.gain_color);
		} else {
			return transformColor(R.color.loss_color);
		}
	}

	public static boolean isChinese(String str) {
		if (str == null) {
			return false;
		}
		Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
		return pattern.matcher(str.trim()).find();
	}
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean isNumericSign(String str) {
		Pattern pattern = Pattern.compile("^[+-]?\\d+(,\\d+)?(.\\d+)?");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	// 校验手机号码, 1开头11位数字
	public static boolean checkMoblie(String s) {
		if (null == s)
			return false;
		return s.matches("^1[0-9]{10}");
	}

	// 校验密码, 6到18位字符
	public static boolean checkPwd(String s) {
		if (null == s)
			return false;
		return s.matches("[a-zA-Z0-9]{6,18}");
	}

	// 校验验证码, 4位字符
	public static boolean checkImgVerify(String s) {
		if (null == s)
			return false;
		return s.matches("[a-zA-Z0-9]{4}");
	}

	//校验6位验证码
	public static boolean checkNumVerify(String s) {
		if (null == s)
			return false;
		return s.matches("[a-zA-Z0-9]{6}");
	}

	//校验昵称
	public static boolean checkNickName(String s) {
		if (null == s)
			return false;
		Pattern pattern = Pattern.compile("^[\u4E00-\u9fa5]{2,6}$");
		return pattern.matcher(s).find();
	}

	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0 || " ".equals(str))
			return true;
		else
			return false;
	}


	public static boolean equals(CharSequence a, CharSequence b) {
		if (a == b)
			return true;
		int length;
		if (a != null && b != null && (length = a.length()) == b.length()) {
			if (a instanceof String && b instanceof String) {
				return a.equals(b);
			} else {
				for (int i = 0; i < length; i++) {
					if (a.charAt(i) != b.charAt(i))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	
	public static int parseInteger(String value) {
		if (value == null || value.length() == 0) {
			return 0;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
 	}
	
	public static float parseFloat(String value) {
		if (value == null || value.length() == 0) {
			return 0;
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static int getOperType(boolean mLookBullish) {
		return mLookBullish ? 1 : 2;
	}
	
	public static String getSymbol(boolean lookBullish) {
		if (lookBullish) {
			return "+";
		} else {
			return "-";
		}
	}
	
	public static String captureFirst(String str) {
		char[] cs = str.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	public static void initWebView(WebView webView) {
		webView.setBackgroundColor(0); // 设置背景色
		
		if (webView.getBackground() != null) {
			webView.getBackground().setAlpha(0);
		}
		
		webView.requestFocusFromTouch();
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);// JS
		webView.getSettings().setDefaultTextEncodingName("UTF-8");
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setLoadsImagesAutomatically(true);
		
		if (Build.VERSION.SDK_INT >= 19) {
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	    }
		
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient());
	}
	
	public static void bindPushService(Context context){
		try {
			Resources resource = context.getResources();
			String pkgName = context.getPackageName();
			PushManager.startWork(context.getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,resource.getString(R.string.api_key));
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
			// PushManager.enableLbs(getApplicationContext());
			
			// Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
			// 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
			// 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
			CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
	                resource.getIdentifier(
	                        "notification_custom_builder", "layout", pkgName),
	                resource.getIdentifier("notification_icon", "id", pkgName),
	                resource.getIdentifier("notification_title", "id", pkgName),
	                resource.getIdentifier("notification_text", "id", pkgName));
	        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
	        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
	        cBuilder.setStatusbarIcon(context.getApplicationInfo().icon);
	        cBuilder.setLayoutDrawable(resource.getIdentifier(
	                "simple_notification_icon", "drawable", pkgName));
	        cBuilder.setNotificationSound(Uri.withAppendedPath(
	                Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
	        // 推送高级设置，通知栏样式设置为下面的ID
	        PushManager.setNotificationBuilder(context, 1, cBuilder);
		} catch (Exception e) {
			
		}
		
	}
	public static boolean needUpdate(String version, String currentVersion) {
		String[] oldVersions = currentVersion.split("\\.");
		String[] newVersions = version.split("\\.");
		
		int length = Math.min(oldVersions.length, newVersions.length);
		
		for (int i = 0; i < length; i++) {
			int siVersion = Integer.parseInt(oldVersions[i]);
			int ciVersion = Integer.parseInt(newVersions[i]);
			if (siVersion > ciVersion) return false;
			if (siVersion < ciVersion) return true;
		}
		
		return oldVersions.length < newVersions.length;
	}
	


	
}
