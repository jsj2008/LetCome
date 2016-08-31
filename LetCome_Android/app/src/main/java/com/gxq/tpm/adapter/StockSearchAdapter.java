package com.gxq.tpm.adapter;

import java.util.List;

import com.letcome.App;
import com.letcome.R;
import com.gxq.tpm.db.DBManager;
import com.gxq.tpm.mode.Stock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

public class StockSearchAdapter extends ArrayListAdapter<Stock> implements
		Filter.FilterListener {
	private SearchFilter mSearchFilter;
	private DBManager mDBManager;;
	
	public StockSearchAdapter(Context context) {
		super(context);
		mSearchFilter = new SearchFilter();
		mDBManager = App.getDBManager();
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.stoch_search_item, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
			viewHolder.tvStockCode = (TextView) convertView.findViewById(R.id.tv_stock_code);
//			viewHolder.tvExchange = (TextView) convertView.findViewById(R.id.tv_exchange);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Stock stock = (Stock) getItem(position);
		if (stock != null) {
			viewHolder.tvStockName.setText(stock.getStockName());
			viewHolder.tvStockCode.setText(stock.getStockCode());
//			viewHolder.tvExchange.setText(stock.getPlateType());
		}
		
		convertView.findViewById(R.id.item_bar_line_separate).setVisibility( position == getCount() -1 ? View.GONE: View.VISIBLE);
		
		return convertView;
	}

	public void queryText(String value) {
		mSearchFilter.filter(value, this);
	}
	
	@Override
	public void onFilterComplete(int count) {
	}
	
	private class ViewHolder {
		TextView tvStockCode;
		TextView tvStockName;
//		TextView tvExchange;
	}

	private class SearchFilter extends Filter {
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			List<Stock> list = mDBManager.stockQuery(constraint.toString());

			FilterResults results = new FilterResults();
			if(list != null) {
				results.count = list.size();
				results.values = list;
			} else {
				results.count = 0;
				results.values = null;
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results.values != null && results.values instanceof List<?>) {
				setList((List<Stock>) results.values);
			}
		}
		
	}
	
}
