package com.letcome.fragement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gxq.tpm.fragment.ViewPagerFragment;
import com.letcome.R;

/**
 * Created by rjt on 16/9/14.
 */
public class SignupFragment extends ViewPagerFragment {
    public SignupFragment(Context context, View container) {
        super(context, container);
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);


    }
}
