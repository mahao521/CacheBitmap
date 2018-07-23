package com.bitmap.database.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitmap.database.R;
import com.bitmap.database.base.BaseFragment;
import com.bitmap.database.base.BaseLoadFragment;
import com.bitmap.database.bean.ChannelListData;
import com.bitmap.database.http.ErrorBaseObserver;
import com.bitmap.database.http.MainModel;
import com.bitmap.database.utils.SlidingTabLayout;
import com.bitmap.database.widget.LoadFrameLayout;

import java.util.List;

import butterknife.ButterKnife;

public class RecommendFragment extends BaseFragment {

    private SlidingTabLayout mSlidingTabLayout;
    private ImageView mTabSearch;
    private ViewPager mViewPager;
    private LoadFrameLayout mLoadFrameLayout;
    private TextView btnRetry;
    private String[] titles = {"推荐","搞笑","娱乐"};

    @Override
    protected int layoutRes() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this,view);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.rec_tab_layout);
        mTabSearch = (ImageView) view.findViewById(R.id.tab_search);
        mViewPager = (ViewPager) view.findViewById(R.id.rec_view_pager);
        btnRetry = (TextView) view.findViewById(R.id.tv_retry);
        mLoadFrameLayout = (LoadFrameLayout) view.findViewById(R.id.load_frameLayout);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChannelData();
            }
        });
        getChannelData();
    }

    public void getChannelData(){
        MainModel.getInstance().executeChannelList(new ErrorBaseObserver<ChannelListData>(context,"加载中，请稍后...") {
            @Override
            protected void onBaseNext(ChannelListData data) {
                mLoadFrameLayout.showContentView();
                List<ChannelListData.ChannelListItem> channelList = data.getChannelList();
            }

            @Override
            protected void onBaseError(Throwable throwable) {
                super.onBaseError(throwable);
                mLoadFrameLayout.showErrorView();
            }
        });
    }

    class ItemAdapter extends FragmentStatePagerAdapter{

        SparseArray<BaseLoadFragment> mFragments = new SparseArray<>();
        public ItemAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("position",position);
            bundle.putString("title",titles[position]);
            bundle.putString("id","1");
            mFragments.put(position, RecommandChildFragment.newInstance(bundle));
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
