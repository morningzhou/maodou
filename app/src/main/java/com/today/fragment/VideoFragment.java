package com.today.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.today.adapter.AdapterRecyclerViewVideo;
import com.today.base.BaseFragment;
import com.today.R;
import com.today.base.MyApplication;
import com.today.bean.PageConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private View viewById;
    private ImageView mImageView;
    private SpringView mSpringView;
    private AdapterRecyclerViewVideo mAdapterRecyclerViewVideo;
    private ArrayList<PageConstant.PageBean.ListBean> mList;
    private int mIndex;
    private final int CREATE_VIEW = 0;
    private final int REFRESH_VIEW = 1;
    private final int ADD_VIEW = 2;
    private int mCurrentState = CREATE_VIEW;
    private RefreshLayout mSmartRefresh;
    private PagerSnapHelper mSnapHelper;
    private HttpProxyCacheServer mProxy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProxy = MyApplication.getProxy(MyApplication.getContext());
    }

    @Override
    public View initView() {
        return View.inflate(mContext, R.layout.fragment_video,null);
    }

    @Override
    public void init() {
        super.init();
        SharedPreferences pageIndex = mContext.getSharedPreferences("PageIndex", 0);
        mIndex = pageIndex.getInt("Index", 1);
        mCurrentState = CREATE_VIEW;
    }

    @Override
    protected void findView(View view) {
        mList = new ArrayList<PageConstant.PageBean.ListBean>();
        mRecyclerView = view.findViewById(R.id.video_home_recyclerview);
        mImageView = view.findViewById(R.id.error_load_image0);

        mSmartRefresh = view.findViewById(R.id.refreshLayout);
/*        在25.1.0版本中，官方又提供了一个PagerSnapHelper的子类，可以使RecyclerView
          像ViewPager一样的效果，一次只能滑一页，而且居中显示*/
        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(mRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapterRecyclerViewVideo = new AdapterRecyclerViewVideo(mContext, mList);
        mRecyclerView.setAdapter(mAdapterRecyclerViewVideo);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        View view = mSnapHelper.findSnapView(layoutManager);
                        JzvdStd.releaseAllVideos();
                        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                        if (viewHolder != null && viewHolder instanceof AdapterRecyclerViewVideo.MyViewHolder) {
                            ((AdapterRecyclerViewVideo.MyViewHolder) viewHolder).jzvdStd.startVideo();
                        }

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    public void initListener() {
        super.initListener();
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurrentState = REFRESH_VIEW;
                initData();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        mSmartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                mCurrentState = ADD_VIEW;
                initData();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    @Override
    public void initData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .header("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNTQ3NzcyNTc5LCJleHAiOjE1NzkzMDg1Nzl9.gWsdfhZZOZ3_q600egIAZqGMeVSlEdFaaFTpWocAcFOi0-827LTxsCtH2FhwU2H4qMjBKThL76D6q8HdkaO5cA")
                .url("  http://47.107.75.100:8080/kite/app/video/list?page="+mIndex+"&limit=10")
                .method("GET", null)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String string = response.body().string();
                    Gson gson = new Gson();
                    PageConstant pageConstant= gson.fromJson(string, PageConstant.class);
                    List<PageConstant.PageBean.ListBean> list = pageConstant.getPage().getList();
                    for (int i =0;i<list.size();i++){
                        String videoUrl = list.get(i).getVideoUrl();
                        videoUrl = mProxy.getProxyUrl(videoUrl);
                        list.get(i).setVideoUrl(videoUrl);
                    }
                    if (mCurrentState == REFRESH_VIEW){
                        mList.clear();
                        mList.addAll(list);
                    }else {
                        mList.addAll(list);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "请求数据成功", Toast.LENGTH_SHORT).show();
                        mAdapterRecyclerViewVideo.notifyDataSetChanged();
                    }
                });
            }
        });
        mIndex++;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences pageIndex = mContext.getSharedPreferences("PageIndex", 0);
        SharedPreferences.Editor edit = pageIndex.edit();
        edit.putInt("Index",mIndex);
        edit.commit();
    }
}
