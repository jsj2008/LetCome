package com.gxq.tpm.tools;

import android.widget.ImageView;

import com.letcome.App;
import com.letcome.R;
import com.gxq.tpm.prefs.UserPrefs;

public class GlobalInfo {
	private static final String Tag = "GlobalInfo";
	public static final String isloadingPageShow = "isloadingOrderPageShow";//加载页是否显示
	public static final int isShow = 1;//1为显示

	public static boolean thread;

	/**
	 *
	 *  @Description    : 方法描述   获取头像微博
	 *  @return         : void
	 *  @Creation Date  : 2014-10-30 下午3:20:15
	 *  @Author         : morong
	 *  @Update Date    :
	 *  @Update Author  : morong
	 */
	public static void setHeadPortrait(ImageView imageView,String src){
		Print.i(Tag, "url="+src);
		if (null ==  src || "".equals(src)){
			imageView.setBackgroundResource(R.drawable.default_head_logout);
//			Bitmap bitmap = BitmapFactory.decodeResource(App.instance().getResources(), R.drawable.default_head2);
//			imageView.setImageBitmap(bitmap);
		} else {
			LoaderWeiBookImage ddBookImage = new LoaderWeiBookImage();
			ddBookImage.setLoaderWeiBookImage(imageView, src, R.drawable.default_head_logout);
		}
	}
	
	public static void setNetworkImage(ImageView imageView, String src, int defaultResId){
		if (null ==  src || "".equals(src)){
			imageView.setBackgroundResource(defaultResId);
		} else {
			LoaderWeiBookImage ddBookImage = new LoaderWeiBookImage();
			ddBookImage.setLoaderWeiBookImage(imageView, src, defaultResId);
		}
	}

	public static String checkS(String string){
		if (string==null)
			return "";
		return string;
	}

	public static final  void cleanLogin(){
		UserPrefs prefs = UserPrefs.get(App.instance());
		if(checkS(prefs.getFlag()).equals("ok")){
			prefs.setSession("");
			prefs.setUid(0);
			prefs.setLoginID("");
			prefs.setUserInfo(null);
			prefs.setIsNeedVerify(false);
			prefs.save();
			
//			WebView webView = new WebView(App.instance());
//			webView.clearCache(true);
//			webView.clearHistory();
		}
	}
	public static final void cleanInstallation(){
		UserPrefs prefs = UserPrefs.get(App.instance());
		prefs.setFlag("");
		prefs.save();
	}

}
