package com.letcome.fragement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gxq.tpm.fragment.FragmentBase;
import com.gxq.tpm.tools.BaiduLocationUtils;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.SimpleViewPagerIndicator;
import com.letcome.App;
import com.letcome.R;
import com.letcome.activity.MainActivity;
import com.letcome.activity.SettingActivity;
import com.letcome.mode.LoginRes;
import com.letcome.mode.MyProductsRes;
import com.letcome.prefs.UserPrefs;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends FragmentBase{
    TextView mNameFirst,mName,mCity;
    Button mSettingBtn;

    SimpleViewPagerIndicator mIndicator;
    private String[] mTitles = new String[] {
            Util.transformString(R.string.profile_selling),
            Util.transformString(R.string.profile_sold),
            Util.transformString(R.string.profile_favorite)};
    private ViewPager mViewPager;

    private MainActivity parent;

    private List<ProfileWaterFallsFragment> mFragments;

    private ProfileWaterFallsFragment mSellingFragment,mSoldFragment,mFavoriteFragment;
    private FragmentPagerAdapter mAdapter;

    public ProfileFragment() {
        this(R.id.tab_me);
    }

    @SuppressLint("ValidFragment")
    public ProfileFragment(int markId) {
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

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initAction(view);
    }

    private void initView(View view) {
        //mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);
//        mAdapterView = (MultiColumnListView) view.findViewById(R.id.list);
//        View v = new View(parent);
//        v.setLayoutParams(new com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
//        v.setBackgroundColor(ContextCompat.getColor(parent, R.color.category_bg));
//        mAdapterView.addFooterView(v);
//        adapter = new CategoryAdapter(this.mContext);
//        mAdapterView.setAdapter(adapter);
//
//        mSelleBtn = (Button) view.findViewById(R.id.sell_product);
//        mSelleBtn.setOnClickListener(parent);
//        queryMediaImages();

        UserPrefs prefs = App.getUserPrefs();
        LoginRes res = prefs.getUserInfo();

        mCity = (TextView)view.findViewById(R.id.profile_city);
        mName = (TextView)view.findViewById(R.id.profile_name);
        mNameFirst = (TextView)view.findViewById(R.id.name_first);
        mSettingBtn = (Button)view.findViewById(R.id.setting_btn);

        mSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        mName.setText(res.getFullname());
        mNameFirst.setText(res.getFullname().substring(0, 1).toUpperCase());
        mCity.setText(BaiduLocationUtils.mLocation.getCity());

        mIndicator = (SimpleViewPagerIndicator) view.findViewById(R.id.id_stickynavlayout_indicator);
        mIndicator.setTitles(mTitles);

        mViewPager = (ViewPager) view.findViewById(R.id.id_stickynavlayout_viewpager);

        mSellingFragment = new MyWaterFallsFragment(MyProductsRes.STATUS_SELLING);
        mSoldFragment = new MyWaterFallsFragment(MyProductsRes.STATUS_SOLD);
        mFavoriteFragment = new FavoritesWaterFallsFragment();


        mFragments = new ArrayList<ProfileWaterFallsFragment>();

        mFragments.add(mSellingFragment);
        mFragments.add(mSoldFragment);
        mFragments.add(mFavoriteFragment);

        mAdapter = new FragmentPagerAdapter(getActivity()
                .getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void initAction(View view) {
        mIndicator.setTitleClickListener(new SimpleViewPagerIndicator.OnTitleClickListener() {

            @Override
            public void onTitleClick(int position) {
                mViewPager.setCurrentItem(position);
            }

        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mIndicator.generateTitleView(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mAdapterView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
//                ImageView iv = (ImageView) view.findViewById(R.id.imageView);
//
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//
////                transaction.add(R.id.my_framelayout, my_fragment);
//
//            }
//        });
    }

}
