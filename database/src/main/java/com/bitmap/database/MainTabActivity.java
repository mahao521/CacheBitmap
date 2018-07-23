package com.bitmap.database;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;

import com.bitmap.database.base.BaseActivity;
import com.bitmap.database.constant.BuildConfigs;
import com.bitmap.database.http.HttpServletAddress;
import com.bitmap.database.view.FollowFragment;
import com.bitmap.database.view.MainFragment;
import com.bitmap.database.view.MineFragment;
import com.bitmap.database.view.RecommendFragment;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

public class MainTabActivity extends BaseActivity {

    private TabHost mTab;
    private TabManager mTabManager;
    private String TAB1 = "MAIN";
    private String TAB2 = "VIDEO";
    private String TAB3 = "FOLLOWING";
    private String TAB4 = "MINE";
    private LayoutInflater mLayoutInflater;

    @Override
    public int layoutRes() {
        return R.layout.activity_main_tab;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        HttpServletAddress.getInstance().setOnlineAddress(BuildConfigs.BASE_URL);
    }

    @Override
    protected void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        mTab = (TabHost) findViewById(android.R.id.tabhost);
        mTab.setup();
        //去除分割线
        mTab.getTabWidget().setDividerDrawable(null);
        mTabManager = new TabManager(this,mTab,R.id.realtabcontent);
        mTabManager.addTab(mTab.newTabSpec(TAB1).setIndicator(createView(R.layout.tab_main)),MainFragment.class,null);
        mTabManager.addTab(mTab.newTabSpec(TAB2).setIndicator(createView(R.layout.tab_following)), FollowFragment.class,null);
        mTabManager.addTab(mTab.newTabSpec(TAB3).setIndicator(createView(R.layout.tab_video)), RecommendFragment.class,null);
        mTabManager.addTab(mTab.newTabSpec(TAB4).setIndicator(createView(R.layout.tab_mine)), MineFragment.class,null);
    }

    public View createView(int layoutId){
        return mLayoutInflater.inflate(layoutId,null);
    }

    @Override
    public boolean addDisposable(Disposable dispoable) {
        return false;
    }

    public static class TabManager implements TabHost.OnTabChangeListener{
        private FragmentActivity mActivity;
        private  TabHost mTabHost;
        private int mContainId;
        private HashMap<String,TabInfo> mTabs = new HashMap<>();
        private TabInfo mLastTab;

        @Override
        public void onTabChanged(String tabId) {

            int position = mTabHost.getCurrentTab();
            if(position == 0){

            }
            TabInfo newTab = mTabs.get(tabId);
            if(mLastTab != newTab){
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if(mLastTab != null){
                    if (mLastTab.mFragment != null) {
                        ft.hide(mLastTab.mFragment);
                    }
                }
                if(newTab != null){
                    if(newTab.mFragment == null){
                        newTab.mFragment = Fragment.instantiate(mActivity,newTab.mClass.getName(),newTab.args);
                        ft.add(mContainId,newTab.mFragment,newTab.tag);
                    }else {
                      ft.show(newTab.mFragment);
                    }
                }
                mLastTab = newTab;
                ft.commitAllowingStateLoss();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }

        static final class TabInfo{
            private String tag;
            private Class<?> mClass;
            private  Bundle args;
            private Fragment mFragment;

            TabInfo(String _tag,Class<?> _class,Bundle _args){
                tag = _tag;
                mClass  = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory{
            private  Context mContext;
            DummyTabFactory(Context context) {
                this.mContext = context;
            }
            @Override
            public View createTabContent(String tag) {
                View view = new View(mContext);
                view.setMinimumHeight(0);
                view.setMinimumWidth(0);
                return view;
            }
        }

        public TabManager(FragmentActivity activity,TabHost tabHost,int containId){
            this.mActivity = activity;
            this.mTabHost = tabHost;
            this.mContainId = containId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec,Class<?> clzz,Bundle bundle){
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();
            TabInfo info = new TabInfo(tag,clzz,bundle);
            info.mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if(info.mFragment != null && !info.mFragment.isDetached()){
                FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
                transaction.hide(info.mFragment);
                transaction.commitAllowingStateLoss();
            }
            mTabs.put(tag,info);
            mTabHost.addTab(tabSpec);
        }
    }
}
