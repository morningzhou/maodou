package com.today.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.today.R;

public class PrivacyAgreementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉状态栏
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_soft);
        TextView tv = findViewById(R.id.content);
        TextView title = findViewById(R.id.user_title);
        Intent intent = getIntent();
        if (intent != null) {
            String type = intent.getType();
            if ("agreement".equals(type)) {
                title.setText(R.string.user_agreement_title);
                tv.setText(R.string.user_agreement_content);
            } else if ("policy".equals(type)) {
                title.setText(R.string.privacy_policy_title);
                tv.setText(R.string.privacy_policy_content);
            }
        }
    }
}
