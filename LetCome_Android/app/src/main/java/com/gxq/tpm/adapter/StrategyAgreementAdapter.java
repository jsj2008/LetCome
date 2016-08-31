package com.gxq.tpm.adapter;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.mode.mine.AgreementArticleList.AgreementArticle;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.WebHelper;
import com.gxq.tpm.tools.WebHelper.WebParams;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StrategyAgreementAdapter extends ArrayListAdapter<AgreementArticle> {

	public StrategyAgreementAdapter(Context context) {
		super(context);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.agreement_article_list_item, parent, false);
			holder = new ViewHolder();
			
			holder.container = convertView.findViewById(R.id.container_item_name);
			holder.item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
		} else {
			holder = (ViewHolder) convertView.getTag();		
		}
		
		final AgreementArticle article = getList().get(position);
		holder.item_name.setText(article.name);
		holder.container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, WebActivity.class); 
				intent.putExtra(ProductIntent.EXTRA_TITLE,
						Util.transformString(R.string.user_strategy_agreement_content));
				intent.putExtra(ProductIntent.EXTRA_URL, 
						WebHelper.addParam(RequestInfo.PRODUCT_STRATEGY_AGREEMENT.getUrl())
						.addParam(WebParams.ID, article.id).getUrl());
				intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
				mContext.startActivity(intent);
			}
		});
		convertView.setTag(holder);
		return convertView;
	}
	
	private static class ViewHolder {
		public View container;
		public TextView item_name;
	}
	
}
