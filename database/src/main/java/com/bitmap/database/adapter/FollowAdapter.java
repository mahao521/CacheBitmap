package com.bitmap.database.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.bitmap.database.bean.CategoryInfo;
import com.bitmap.database.view.ListPlayFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class FollowAdapter extends FragmentStatePagerAdapter {

    private List<CategoryInfo> mInfoList;
    public FollowAdapter(FragmentManager fm,List<CategoryInfo> infos) {
        super(fm);
        this.mInfoList = infos;
    }

    @Override
    public Fragment getItem(int position) {
        CategoryInfo categoryInfo = mInfoList.get(position);
        String id = categoryInfo.getId();
        String name = categoryInfo.getName();
        ListPlayFragment listPlayFragment = ListPlayFragment.create(id, name);
        return (Fragment)listPlayFragment;
    }

    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CategoryInfo categoryInfo = mInfoList.get(position);
        return categoryInfo.getName();
    }
}
