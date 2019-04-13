package com.today.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.today.R;
import com.today.bean.PageConstant;
import com.today.bean.VideoConstant;

import java.util.ArrayList;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class AdapterRecyclerViewVideo extends RecyclerView.Adapter<AdapterRecyclerViewVideo.MyViewHolder> {

    public static final String TAG = "AdapterRecyclerViewVideo";
    private Context context;
    private ArrayList<PageConstant.PageBean.ListBean> mVideoList;

    public AdapterRecyclerViewVideo(Context context, ArrayList<PageConstant.PageBean.ListBean> mList) {
        this.context = context;
        mVideoList =mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_videoview, parent,
                false));
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder [" + holder.jzvdStd.hashCode() + "] position=" + position);

        holder.jzvdStd.setUp(
                mVideoList.get(position).getVideoUrl(),
                mVideoList.get(position).getVideoDesc(), Jzvd.SCREEN_WINDOW_LIST);
        Glide.with(holder.jzvdStd.getContext()).load(mVideoList.get(position).getCoverUrl()).into(holder.jzvdStd.thumbImageView);
    }

    @Override
    public int getItemCount() {
        if (mVideoList.size() > 0){
            return mVideoList.size();
        }
        return 0;
    }

   public static class MyViewHolder extends RecyclerView.ViewHolder {
        public JzvdStd jzvdStd;

        public MyViewHolder(View itemView) {
            super(itemView);
            jzvdStd = itemView.findViewById(R.id.videoplayer);
//            jzvdStd.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);//让预览图片全屏展示
//            jzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);//让视频全屏展示，但是有些图标会变形
        }
    }

}
