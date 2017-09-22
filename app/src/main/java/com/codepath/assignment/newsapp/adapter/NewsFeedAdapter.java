package com.codepath.assignment.newsapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.models.NewsStory;
import com.codepath.assignment.newsapp.utils.NewsFeedDiffCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saip92 on 9/21/2017.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewsStory> mNewsStories;
    private static final int WITH_IMAGE = 1;
    private static final int WITHOUT_IMAGE = 2;

    public NewsFeedAdapter(Context context, List<NewsStory> newsStories){
        mContext = context;
        mNewsStories = newsStories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder;
        switch(viewType){
            case WITH_IMAGE:
                View v1 = inflater.inflate(R.layout.news_item_layout,parent,false);
                viewHolder = new NewsFeedWithImageViewHolder(v1);
                break;
            case WITHOUT_IMAGE:
            default:
                View v2 = inflater.inflate(R.layout.news_item_text_layout, parent, false);
                viewHolder = new NewsFeedWithoutImageViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        NewsStory newsStory = mNewsStories.get(position);
        switch(viewType){
            case WITH_IMAGE:
                NewsFeedWithImageViewHolder withImageViewHolder = (NewsFeedWithImageViewHolder) holder;
                withImageViewHolder.bindNewsStory(newsStory);
                break;
            case WITHOUT_IMAGE:

            default:
                NewsFeedWithoutImageViewHolder withoutImageViewHolder = (NewsFeedWithoutImageViewHolder) holder;
                withoutImageViewHolder.bindNewsStory(newsStory);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mNewsStories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (mNewsStories.get(position).getThumbnailUrl() != null )? WITH_IMAGE : WITHOUT_IMAGE;
    }

    public NewsStory getNewsStory(int position){
        return mNewsStories.get(position);
    }

    public void swapItems(List<NewsStory> newsStories){
        final NewsFeedDiffCallback diffCallback = new NewsFeedDiffCallback(this.mNewsStories,newsStories);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mNewsStories.clear();
        this.mNewsStories.addAll(newsStories);

        diffResult.dispatchUpdatesTo(this);

    }

    public void addMoreData(List<NewsStory> newsStories){
        if(mNewsStories.size() >= 0){
            int oldSize = mNewsStories.size();
            mNewsStories.addAll(newsStories);
            notifyItemRangeChanged(oldSize, mNewsStories.size());
        }

    }

    private class NewsFeedWithImageViewHolder extends RecyclerView.ViewHolder{

        ImageView mIvThumbnail;

        TextView mTvHeadline;

        AppCompatButton mBtnCategory;

        TextView mTvSummary;

        NewsFeedWithImageViewHolder(View itemView) {
            super(itemView);

            mIvThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
            mTvHeadline = (TextView) itemView.findViewById(R.id.tvHeadline);
            mBtnCategory = (AppCompatButton) itemView.findViewById(R.id.btnCategory);
            mTvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
        }

        void bindNewsStory(NewsStory newsStory){
            if(newsStory != null){
                Glide.with(mContext)
                        .load(newsStory.getThumbnailUrl())
                        .into(mIvThumbnail);
                mTvHeadline.setText(newsStory.getHeadline());
                if(newsStory.getNewsDesk() != null) {
                    mBtnCategory.setVisibility(View.VISIBLE);
                    mBtnCategory.setText(newsStory.getNewsDesk());
                }
                else
                    mBtnCategory.setVisibility(View.GONE);

                mTvSummary.setText(newsStory.getBriefStory());
            }
        }
    }

    private class NewsFeedWithoutImageViewHolder extends RecyclerView.ViewHolder{

        TextView mTvHeadline;

        AppCompatButton mBtnCategory;

        TextView mTvSummary;

        NewsFeedWithoutImageViewHolder(View itemView) {
            super(itemView);
            mTvHeadline = (TextView) itemView.findViewById(R.id.tvHeadline);
            mBtnCategory = (AppCompatButton) itemView.findViewById(R.id.btnCategory);
            mTvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
        }

        void bindNewsStory(NewsStory newsStory){
            mTvHeadline.setText(newsStory.getHeadline());
            if(newsStory.getNewsDesk() != null){
                mBtnCategory.setVisibility(View.VISIBLE);
                mBtnCategory.setText(newsStory.getNewsDesk());
            }
            else{
                mBtnCategory.setVisibility(View.GONE);
            }

            mTvSummary.setText(newsStory.getBriefStory());
        }
    }
}
