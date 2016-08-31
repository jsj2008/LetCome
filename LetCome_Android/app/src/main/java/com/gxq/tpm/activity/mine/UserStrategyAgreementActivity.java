package com.gxq.tpm.activity.mine;

import java.util.ArrayList;
import java.util.List;

import com.gxq.tpm.GlobalConstant;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.StrategyAgreementAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.mine.AgreementArticleList;
import com.gxq.tpm.mode.mine.AgreementArticleList.AgreementArticle;
import com.gxq.tpm.network.RequestInfo;

import android.os.Bundle;
import android.widget.ListView;

public class UserStrategyAgreementActivity extends SuperActivity {

	private StrategyAgreementAdapter mAdapter;
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle(R.string.user_investor_agreement);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_user_strategy_agreement);
		
		ListView lv = (ListView) findViewById(R.id.listview);
		mAdapter = new StrategyAgreementAdapter(this);
		lv.setAdapter(mAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		showWaitDialog(RequestInfo.AGREEMENT_ARTICLE_SAMPLE_LIST);
		
		AgreementArticleList.Params params = new AgreementArticleList.Params();
		params.user_type = GlobalConstant.USER_TYPE_STRATEGY;
		params.p_type = GlobalConstant.PRODUCT_ALL;
		
		AgreementArticleList.doRequest(params, this);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		
		if (RequestInfo.AGREEMENT_ARTICLE_SAMPLE_LIST == info) {
			AgreementArticleList articleList = (AgreementArticleList) res;
			if (articleList.list != null) {
				List<AgreementArticle> articles = new ArrayList<AgreementArticle>();
				for (AgreementArticle article : articleList.list) {
					if (article.id > 0) {
						articles.add(article);
					}
				}
				mAdapter.setList(articles);
			}
		}
	}
	
}
