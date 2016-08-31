package com.gxq.tpm.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.launch.LockLoginActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.launch.Login;
import com.gxq.tpm.mode.launch.LoginCpb;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CPopupWindow;

public class WebActivity extends SuperActivity implements View.OnClickListener {
	protected static final String Tag = "WebActivity";
	
	private final static String GO_HOME				= "goHome";
	private final static String CPB					= "cpb";
	private final static String FINISH	 			= "finish";
	private final static String BACK_HOME			= "android//yhfk_backhome";
	private final static String GO_TO_STRATEGY		= "gotoStrategy";
	private final static String APP_LOGIN	        = "applogin";

	private final static String SESSION_ID			= "session_id";
	private final static String UDID				= "did";
	private final static String RESULT_SUCCESS		= "Y";
	private final static String JS_NAME				= "jsListener";
	
	private final static int REQUEST_CODE_PHOTO		= 1001;

	private final static int REQUEST_CODE_PICTURE	= 1002;
	private WebView mWebView;
//	private String title;
	private String mInitUrl;
	private String mCurrentUrl;
	
	private boolean mNeedSession; // 是否需要session
	private boolean mFinshDirect; // 按back键直接返回app
	private int mHttpRequest; // http请求方式
	private boolean mClearCache; // 清缓存
	
	private ValueCallback<Uri> mValueCallback;
	private CPopupWindow mPopupWindow;
	private String mImagePath;
	private String mImageFile = "auth.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		mInitUrl = getIntent().getStringExtra(ProductIntent.EXTRA_URL);
		
		// 是否需要添加session_id
		mNeedSession = getIntent().getBooleanExtra(ProductIntent.EXTRA_NEED_SESSION, false);
		// 是否直接退出
		mFinshDirect = getIntent().getBooleanExtra(ProductIntent.EXTRA_FINISH_DIRECT, false);
		// 请求方式
		mHttpRequest = getIntent().getIntExtra(ProductIntent.EXTRA_HTTP_REQUEST, ProductIntent.REQUEST_POST);
		
		mClearCache = getIntent().getBooleanExtra(ProductIntent.EXTRA_CLEAR_CACHE, false);
		
		if (mInitUrl != null) {
			mCurrentUrl = addSession(mInitUrl, mNeedSession);
		} else {
			mInitUrl = ""; 
			mCurrentUrl = "";
		}
		
		mWebView = (WebView) findViewById(R.id.webView);

		initWebView();
	}
	
	@Override
	protected void initBar() {
		super.initBar();

		String title = getIntent().getStringExtra(ProductIntent.EXTRA_TITLE);
		
		if (title == null) {
			title = "";
		}
		getTitleBar().setTitle(title);
		getTitleBar().showBackImage();
	}
	
	private String addSession(String url, boolean needSession) {
		if (needSession) {
			if (url.indexOf("?") >= 0) {
				url += "&" + SESSION_ID + "=" + mUserPrefs.getSession();	
			} else {
				url += "?" + SESSION_ID + "=" + mUserPrefs.getSession();
			}
		}
		
		if (url.indexOf("?") >= 0) {
			url += "&" + UDID + "=" + mUserPrefs.getOpenUdid();
		} else {
			url += "?" + UDID + "=" + mUserPrefs.getOpenUdid();
		}
		
		return url;
	}
	
	@Override
	public void onLeftClick(View v) {
		if (mCurrentUrl != null && mCurrentUrl.contains("live800.com")) {
			super.onLeftClick(v);
		} else {
			onBackPressed();
		}
	}
	
	private void back() {
		finish();
    }

	public static void clearCookies(Context context) {
        // Edge case: an illegal state exception is thrown if an instance of
        // CookieSyncManager has not be created.  CookieSyncManager is normally
        // created by a WebKit view, but this might happen if you start the
        // app, restore saved state, and click logout before running a UI
        // dialog in a WebView -- in which case the app crashes
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr =
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

	// 初始化webView控件
	private void initWebView() {
		Util.initWebView(mWebView);
//
//		if (mInitUrl.contains(RequestInfo.CUSTOM_SERVICE.getUrl()) /*&& !App.isLogin()*/){
//			//webView.clearCache(true);
//			//webView.clearHistory();
//			Print.e("ccc", "ccc clearCookies");
//			clearCookies(this);
//		}
//		if (mClearCache) {
//			mWebView.clearCache(true);
//			mWebView.clearHistory();
//			clearCookies(this);
//		}
	
		mWebView.addJavascriptInterface(new JsInterface(), JS_NAME);

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Print.d(Tag,url);
				mCurrentUrl = url;
//				view.loadDataWithBaseURL(null, url,"text/html", "utf-8",null);
				view.loadUrl(url);
				return true;
			}
		});				
		
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {// 
				try {
					Print.d(Tag, "progress-->" + progress);
	                if (progress == 100) {
	                	hideWaitDialog(null);

	                	// listeningRefresh = getListeningRefresh();
	                	if (mWebView != null && mWebView.getUrl() != null)
	                		Print.d(Tag, mWebView.getUrl());
	                }
                } catch (Exception e) {
	                e.printStackTrace();
                }
				super.onProgressChanged(view, progress);
			}
			
			public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
                openFileChooser(uploadFile);
            }
 
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                openFileChooser(uploadFile);
            }

            public void openFileChooser(ValueCallback<Uri> uploadFile) {
            	mValueCallback = uploadFile;
            	showImageFile();
            }
            
		});

		if (mHttpRequest == ProductIntent.REQUEST_POST) {
			loadUrl(mCurrentUrl);
		} else if (mHttpRequest == ProductIntent.REQUEST_GET) {
			mWebView.loadUrl(mCurrentUrl);
		}
	}
	
	private void loadUrl(String url) {
		Print.d(Tag, "loadUrl " + url);

		int index = url.indexOf("?");
		if (index < 0) {
			mWebView.postUrl(url, new byte[0]);
		} else {
			String postData = url.substring(index+1);
			Print.i(Tag, "postData = " + postData);
			mWebView.postUrl(url.substring(0, index), EncodingUtils.getBytes(postData, "utf-8"));
		}
	}
	
	private void showImageFile() {
		mPopupWindow = new CPopupWindow(this);
		mPopupWindow.setContentView(R.layout.popup_window_picture);
		mPopupWindow.findViewById(R.id.tv_take_photo).setOnClickListener(this);
		mPopupWindow.findViewById(R.id.tv_take_picture).setOnClickListener(this);
		mPopupWindow.findViewById(R.id.tv_cancel).setOnClickListener(this);
		mPopupWindow.showAtLocation(mWebView, Gravity.BOTTOM, 0, 0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_take_photo:
			takePhoto();
			break;
		case R.id.tv_take_picture:
			takePicture();
			break;
		case R.id.tv_cancel:
			if (mValueCallback != null)
				mValueCallback.onReceiveValue(null);
			break;
		}
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}
	
	private void takePhoto() {
		if (mImagePath == null) {
			mImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + 
        			getString(R.string.cache_path);
			
			File dir = new File(mImagePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			File file = new File(mImagePath + mImageFile);
			if (file.exists()) {
				file.delete();
			}
		}
		
		Uri uri = Uri.fromFile(new File(mImagePath + mImageFile));
		
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, REQUEST_CODE_PHOTO);
	}
	
	private void takePicture() {
    	Intent intent = new Intent();  
        intent.setType("image/*");  
        intent.setAction(Intent.ACTION_GET_CONTENT);   
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				final File file = new File(mImagePath + mImageFile);
				final Uri uri = Uri.fromFile(file);
				if (file.exists()) {
					if (mValueCallback != null)
	            		mValueCallback.onReceiveValue(uri);
				} else {
					Bundle bundle = data.getExtras();  
		            final Bitmap bitmap = (Bitmap) bundle.get("data");
		            
		            new Thread() {
		            	public void run() {
		            		if (savePhoto(mImagePath + mImageFile, bitmap)) {
		    	            	if (mValueCallback != null)
		    	            		mValueCallback.onReceiveValue(uri);
		    	            } else {
		    	            	if (mValueCallback != null)
		    						mValueCallback.onReceiveValue(null);
		    	            }
		            	}
		            }.start();
				}
			} else {
				if (mValueCallback != null)
					mValueCallback.onReceiveValue(null);
			}
		} else if (requestCode == REQUEST_CODE_PICTURE) {
			if (resultCode == Activity.RESULT_OK) {
				if (mValueCallback != null)
					mValueCallback.onReceiveValue(data.getData());
			} else {
				if (mValueCallback != null)
					mValueCallback.onReceiveValue(null);
			}
		}
	}
	
	private boolean savePhoto(String imagePath, Bitmap bitmap) {
		FileOutputStream output = null;  

        try {  
        	output = new FileOutputStream(imagePath);  
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            return true;
        } catch (IOException e) {
        	Print.i("WebActivity", e.getMessage());
        } finally {
        	if (output != null) {
				try {
					output.flush();
					output.close();
				} catch (IOException e) {
					Print.i("WebActivity", e.getMessage());
				}  
        	}
        }  
        return false;
	}
	
	@Override
	public void onBackPressed() {
		if (mFinshDirect) {
			finish();
		} else if (mCurrentUrl != null && mCurrentUrl.contains("live800.com")) {
			// do nothing
		} else if (mWebView.canGoBack() ) {
			 mWebView.goBack();
		} else{
			back();
		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if (RequestInfo.LOGIN == info) {
			Login login = (Login) res;
			mUserPrefs.setKEY(login.encryptedKey);
			mUserPrefs.setUid(login.uid);
			mUserPrefs.setIsNeedVerify(false);
			mUserPrefs.setLoginID(null);
			mUserPrefs.save();
			
			App.LockStartTime();
			
			Util.bindPushService(WebActivity.this);
			
			setResult(RESULT_OK);
			finish();
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (RequestInfo.LOGIN == info) {
			setResult(RESULT_CANCELED);
			finish();
			return ERROR_TOAST;
		}
		return super.netFinishError(info, what, msg, tag);
	}
	
	private void loginBySso(String token){
		Login.Params params = new Login.Params();
		params.login_id = token;
		params.password = "";
		params.login_type = 7;
		Login.doRequest(params, WebActivity.this);
	}
	
	public class JsInterface {
		@JavascriptInterface
		public void onFinish(String type, String result, String json) {
			if (GO_HOME.equals(type) && RESULT_SUCCESS.equals(result)) {
				Intent intent = new Intent(WebActivity.this, LockLoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
					finish();
			} else if (GO_TO_STRATEGY.equals(type)) {
				Intent intent = new Intent(WebActivity.this, ProductActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(ProductIntent.EXTRA_WHICH_FRAGMENT, R.id.tab_cooperation);
				startActivity(intent);
				finish();
			} else if (FINISH.equalsIgnoreCase(type) && RESULT_SUCCESS.equals(result)) {
				finish();
			} else if (BACK_HOME.equals(type)) {
				Print.i(Tag, "finish");
				finish();
			} else if (mInitUrl.contains(RequestInfo.PROD_URL.getUrl())) {
				if (CPB.equals(type) && BaseRes.RESULT_OK.equals(result)) {
					finish();
				}
			} else if (APP_LOGIN.equals(type) && RESULT_SUCCESS.equals(result)){
				Gson gson = new Gson();
				LoginCpb res = gson.fromJson(json, LoginCpb.class);
				loginBySso(res.token);
			}
				
			Print.i(Tag, "type="+type);
			Print.i(Tag, "result="+result);
			Print.i(Tag, "json="+json);
		}
	}
	
}
