package com.gxq.tpm.activity;

import org.apache.http.util.EncodingUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.mode.AgreementBase;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.WebHelper;

public class SignAgreementActivity extends SuperActivity {

	private static final String Tag = SignAgreementActivity.class.getSimpleName();
	private final static String SESSION_ID			= "session_id";
	private final static String UDID				= "did";
	
	private final static String JS_NAME				= "jsListener";
	private final static String FINISH	 			= "finish";
	private final static String RESULT_SUCCESS		= "Y";
	
	private TextView mTvAgreementTitle;
	private WebView mAgreementContent;

	private AgreementBase mAgreement;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				setResult(RESULT_OK);
			} else {
				setResult(RESULT_CANCELED);
			}
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sign_agreement);
		
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//需要添加的语句

		mAgreement = (AgreementBase) getIntent().getSerializableExtra(ProductIntent.EXTRA_UNSIGN_AGREEMENT);
		
		initView();
	}

	@Override
	public void onLeftClick(View v) {
	}

	@Override
	public void onBackPressed() {
	}

	private void initView() {
		mTvAgreementTitle = (TextView) findViewById(R.id.agreement_title);
		mAgreementContent = (WebView) findViewById(R.id.agreement_content);

		mTvAgreementTitle.setText(mAgreement.name);
		
		initWebView();
	}

	// webView
	private void initWebView() {
		Util.initWebView(mAgreementContent);

		mAgreementContent.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Print.d(Tag, url);
				// view.loadDataWithBaseURL(null, url,"text/html",
				// "utf-8",null);
				view.loadUrl(url);
				return true;
			}
		});

		mAgreementContent.setWebChromeClient(new WebChromeClient() {

			public void onProgressChanged(WebView view, int progress) {//
				System.out.println("progress-->" + progress);
				if (progress == 100) {
					hideWaitDialog(null);

					// listeningRefresh = getListeningRefresh();
					Print.d(Tag, mAgreementContent.getUrl());
				}
				super.onProgressChanged(view, progress);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
				AlertDialog.Builder b2 = new AlertDialog.Builder(
						SignAgreementActivity.this)
						.setTitle(R.string.dialog_title_text)
						.setMessage(message)
						.setPositiveButton("ok",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										result.confirm();
										// MyWebView.this.finish();
									}
								});

				b2.setCancelable(false);
				b2.create();
				b2.show();
				return true;
			}
		});

		mAgreementContent.setWebViewClient(new WebViewClient() {

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}
		});
		
		mAgreementContent.addJavascriptInterface(new JsInterface(), JS_NAME);
	
		UserPrefs userPrefs = App.getUserPrefs();
		String url = WebHelper.addParam(RequestInfo.GET_AGREEMENT_INFO)
				.addParam(SESSION_ID, userPrefs.getSession())
				.addParam(UDID, userPrefs.getOpenUdid()).getUrl();
		loadUrl(url);
	}
	
	private void loadUrl(String url) {
		Print.d(Tag, "loadUrl " + url);

		int index = url.indexOf("?");
		if (index < 0) {
			mAgreementContent.postUrl(url, new byte[0]);
		} else {
			String postData = url.substring(index+1);
			Print.i(Tag, "postData = " + postData);
			mAgreementContent.postUrl(url.substring(0, index), EncodingUtils.getBytes(postData, "utf-8"));
		}
	}
	
	public class JsInterface {
		@JavascriptInterface
		public void onFinish(String type, String result, String json) {
			if (FINISH.equalsIgnoreCase(type)) {
				if (RESULT_SUCCESS.equals(result)) {
					mHandler.sendEmptyMessageDelayed(0, 3000);
				} else {
					mHandler.sendEmptyMessageDelayed(1, 3000);
				}
			} 
			
			Print.i(Tag, "type="+type);
			Print.i(Tag, "result="+result);
			Print.i(Tag, "json="+json);
		}
	}
	
}
