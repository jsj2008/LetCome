package com.gxq.tpm.activity.cooperation;

import android.content.Intent;
import android.os.Bundle;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.fragment.FragmentBase;
import com.gxq.tpm.fragment.StockSearchFragment;

public class StockSearchActivity extends SuperActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent();
		intent.putExtras(getIntent());
		intent.putExtra(ProductIntent.EXTRA_SHOW_CANCEL, true);
		changeFragment(R.id.tab_strategy, intent.getExtras());
	}
	
	@Override
	public FragmentBase createFragmentById(int markId) {
		FragmentBase fragment = null;
		switch (markId) {
		case R.id.tab_strategy:
			fragment = new StockSearchFragment(markId);
			break;
		}
		return fragment;
	}
	
}
