package com.gxq.tpm.ui;


import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class ContractsDetailDialog extends Dialog implements View.OnClickListener {
	private TextView mTvTitle;
	private WebView mWebView;

	public ContractsDetailDialog(Context context) {
		super(context, R.style.DialogStyle);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contract_detail_dialog);
		
		View close = findViewById(R.id.iv_agreement_close);
		close.setOnClickListener(this);
		
		mTvTitle = (TextView) findViewById(R.id.agreement_title);
		mWebView = (WebView) findViewById(R.id.agreement_content);

		initWebView();
	}
	
	// webView
	private void initWebView() {
		Util.initWebView(mWebView);

		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient());
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_agreement_close) {
			dismiss();
		}
	}
	
	public void setContent(String title, String content) {
		mTvTitle.setText(title);
		mWebView.loadDataWithBaseURL(null, content, "text/html", "UTF-8",null);
	}
	
}
