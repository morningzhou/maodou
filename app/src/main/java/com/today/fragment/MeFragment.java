package com.today.fragment;


import android.view.View;
import android.widget.TextView;

import com.today.R;
import com.today.base.BaseFragment;

public class MeFragment extends BaseFragment {


    @Override
    protected void findView(View view) {

    }

    @Override
    public View initView() {
        return View.inflate(mContext,R.layout.fragment_me,null);
    }
}
