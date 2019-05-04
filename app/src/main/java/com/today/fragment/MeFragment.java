package com.today.fragment;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.today.R;
import com.today.base.BaseFragment;
import com.today.ui.PrivacyAgreementActivity;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private TextView mVersion;
    private RelativeLayout mUserAgreement;
    private RelativeLayout mPolicy;

    @Override
    protected void findView(View view) {
        mVersion = view.findViewById(R.id.soft_version);
        mUserAgreement = view.findViewById(R.id.user_agreement);
        mPolicy = view.findViewById(R.id.privacy_policy);
        String data = getResources().getString(R.string.app_version);
        String format = String.format(data, getLocalVersionName());
        mVersion.setText(format);
        mUserAgreement.setOnClickListener(this);
        mPolicy.setOnClickListener(this);
    }

    @Override
    public View initView() {
        return View.inflate(mContext,R.layout.fragment_me,null);
    }

    private String getLocalVersionName() {
        String localVersion = "";
        try {
            PackageInfo packageInfo = getActivity().getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_agreement:
                Intent agreeIntent = new Intent(getActivity(), PrivacyAgreementActivity.class);
                agreeIntent.setType("agreement");
                startActivity(agreeIntent);
                break;
            case R.id.privacy_policy:
                Intent policyIntent = new Intent(getActivity(), PrivacyAgreementActivity.class);
                policyIntent.setType("policy");
                startActivity(policyIntent);
                break;
            default:
                break;
        }
    }
}
