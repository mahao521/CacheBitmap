package com.bitmap.database.view;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bitmap.database.R;
import com.bitmap.database.adapter.FollowAdapter;
import com.bitmap.database.base.BaseLoadFragment;
import com.bitmap.database.bean.CategoryInfo;
import com.bitmap.database.utils.PagerSlidingTab;
import com.bitmap.database.utils.TextSizePx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FollowFragment extends BaseLoadFragment implements ViewPager.OnPageChangeListener {

   PagerSlidingTab mPagerSlidingTab;
   ViewPager mViewPager;

   public FollowFragment(){}

    @Override
    protected int layoutRes() {
        return R.layout.fragment_follow;
    }


    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this,view);
        mPagerSlidingTab = (PagerSlidingTab) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mPagerSlidingTab.setOnPageChangeListener(this);
        initPagerSlidTab();
    }

    private void initPagerSlidTab() {
        this.mPagerSlidingTab.setUnderlineHeight((int) TypedValue.applyDimension(1, 0.0F, this.getResources().getDisplayMetrics()));
        this.mPagerSlidingTab.setIndicatorHeight((int)TypedValue.applyDimension(1, 3.0F, this.getResources().getDisplayMetrics()));
        this.mPagerSlidingTab.setTextSize(TextSizePx.a(this.getContext(), 14.0F));
        this.mPagerSlidingTab.setSelectedTextColor(getResources().getColor(R.color.black));
        this.mPagerSlidingTab.setTextColor(getResources().getColor(R.color.tab_text_color_default));
        this.mPagerSlidingTab.setLineIndicator(true);
        FollowAdapter adapter = new FollowAdapter(getChildFragmentManager(),makeCateInfo());
        mViewPager.setAdapter(adapter);
        this.mPagerSlidingTab.setViewPager(mViewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public List<CategoryInfo> makeCateInfo(){
        List<CategoryInfo> infos = new ArrayList<>();
        infos.add(new CategoryInfo("1","音乐"));
        infos.add(new CategoryInfo("2","社会"));
        infos.add(new CategoryInfo("3","科技"));
        infos.add(new CategoryInfo("4","影视"));
        infos.add(new CategoryInfo("5","游戏"));
        infos.add(new CategoryInfo("6","体育"));
        infos.add(new CategoryInfo("7","生活"));
        infos.add(new CategoryInfo("8","军事"));
        infos.add(new CategoryInfo("9","汽车"));
        return infos;
    }
}
