package com.today.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.today.R;
import com.today.base.BaseFragment;
import com.today.fragment.MeFragment;
import com.today.fragment.NewsFragment;
import com.today.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsHomeActivity extends AppCompatActivity  {

    private FrameLayout mFlcontent;
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉状态栏
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        super.onCreate(savedInstanceState);
        initView();
        initFragmentTabHost();
    }

    private void initView() {
        setContentView(R.layout.activity_news_home);
        mFlcontent = findViewById(R.id.content);
        mTabHost = findViewById(R.id.tabhost);

    }
    private void initFragmentTabHost() {
        //FragmentTabHost初始化
        mTabHost.setup(this, getSupportFragmentManager(), R.id.content);
        /*--------------- FragmentTabHost循环添加选项卡--集合方式 ---------------*/
        List<TabInfo> tabInfos = new ArrayList<>();
        tabInfos.add(new TabInfo("视频", R.drawable.tab_icon_explore, new VideoFragment().getClass()));
        tabInfos.add(new TabInfo("新闻", R.drawable.tab_icon_new, new NewsFragment().getClass()));
//        tabInfos.add(new TabInfo("我的", R.drawable.tab_icon_me, new MeFragment().getClass()));

        for (int i = 0; i < tabInfos.size(); i++) {
            TabInfo tabInfo = tabInfos.get(i);

            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabInfo.title);

            View indicatorView = View.inflate(this, R.layout.inflate_indicatorview, null);
            TextView tabTitle = (TextView) indicatorView.findViewById(R.id.tab_title);

            tabTitle.setText(tabInfo.title);
            tabTitle.setCompoundDrawablesWithIntrinsicBounds(0, tabInfo.topResId, 0, 0);

            tabSpec.setIndicator(indicatorView);

            Bundle args = new Bundle();
            args.putString("args", i + "");

            Fragment fragment = Fragment.instantiate(this, tabInfo.clz.getName(), args);
            Class clz = fragment.getClass();
            mTabHost.addTab(tabSpec, clz, args);
        }

        // 去除分割线
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
    }

    class TabInfo {
        public String title;
        public int    topResId;
        public Class  clz;

        public TabInfo(String title, int topResId, Class clz) {
            this.title = title;
            this.topResId = topResId;
            this.clz = clz;
        }
    }

}
