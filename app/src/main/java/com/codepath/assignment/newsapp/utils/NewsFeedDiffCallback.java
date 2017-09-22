package com.codepath.assignment.newsapp.utils;

import android.support.v7.util.DiffUtil;

import com.codepath.assignment.newsapp.models.NewsStory;

import java.util.List;

/**
 * Created by saip92 on 9/21/2017.
 */

public class NewsFeedDiffCallback extends DiffUtil.Callback {
    private List<NewsStory> mOldList;
    private List<NewsStory> mNewList;


    public NewsFeedDiffCallback(List<NewsStory> oldList, List<NewsStory> newList){
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).getId().equals(mOldList.get(oldItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        NewsStory oldNewsStory = mOldList.get(oldItemPosition);
        NewsStory newNewsStory = mNewList.get(newItemPosition);
        return oldNewsStory.equals(newNewsStory);
    }
}
