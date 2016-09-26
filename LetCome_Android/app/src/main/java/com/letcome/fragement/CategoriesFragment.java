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
import android.widget.ImageView;

import com.gxq.tpm.fragment.FragmentBase;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.letcome.R;
import com.letcome.activity.MainActivity;
import com.letcome.adapter.CategoryAdapter;

import java.util.ArrayList;

public class CategoriesFragment extends FragmentBase{
    private MultiColumnListView mAdapterView = null;
    private CategoryAdapter adapter;
    private ArrayList<Integer> colorList;
    private Button mSelleBtn;
    private MainActivity parent;

    public CategoriesFragment() {
        this(R.id.tab_me);
    }

    @SuppressLint("ValidFragment")
    public CategoriesFragment(int markId) {
        super(markId);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (MainActivity)activity;
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
        View v = new View(parent);
        v.setLayoutParams(new com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        v.setBackgroundColor(ContextCompat.getColor(parent, R.color.category_bg));
        mAdapterView.addFooterView(v);
        adapter = new CategoryAdapter(this.mContext);
        mAdapterView.setAdapter(adapter);

        mSelleBtn = (Button) view.findViewById(R.id.sell_product);
        mSelleBtn.setOnClickListener(parent);
//        queryMediaImages();
    }

    private void initAction(View view) {
        mAdapterView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView) view.findViewById(R.id.imageView);

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

//                transaction.add(R.id.my_framelayout, my_fragment);

            }
        });
    }

}
