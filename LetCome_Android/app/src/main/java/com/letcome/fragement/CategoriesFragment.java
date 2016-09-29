package com.letcome.fragement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gxq.tpm.fragment.FragmentBase;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.letcome.R;
import com.letcome.activity.MainActivity;
import com.letcome.adapter.CategoryAdapter;

public class CategoriesFragment extends FragmentBase{
    private MultiColumnListView mAdapterView = null;
    private CategoryAdapter adapter;
    private Button mSelleBtn;
    private MainActivity mParent;

    public CategoriesFragment() {
        this(R.id.tab_categories);
    }

    @SuppressLint("ValidFragment")
    public CategoriesFragment(int markId) {
        super(markId);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParent = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initAction(view);
    }

    private void initView(View view) {
        //mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);
        mAdapterView = (MultiColumnListView) view.findViewById(R.id.list);
        View v = new View(mParent);
        v.setLayoutParams(new com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        v.setBackgroundColor(ContextCompat.getColor(mParent, R.color.category_bg));
        mAdapterView.addFooterView(v);
        adapter = new CategoryAdapter(this.mContext);
        mAdapterView.setAdapter(adapter);

        mSelleBtn = (Button) view.findViewById(R.id.sell_product);
        mSelleBtn.setOnClickListener(mParent);
//        queryMediaImages();
    }

    private void initAction(View view) {
        mAdapterView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                CategoriesFragment.this.showProductsFragment(position);
            }

        });
    }

    public void showProductsFragment(int position){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MeFragment contentFragment = new MeFragment(R.id.tab_products);
        Bundle bundle = new Bundle();
        bundle.putLong("cid", adapter.getItemId(position));
        bundle.putInt("cimg", adapter.getItemImg(position));
        mParent.changeFragment(R.id.tab_products,bundle);
    }
}
