package com.bitmap.database.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitmap.database.R;
import com.bitmap.database.base.BaseLoadFragment;

import butterknife.ButterKnife;
import okio.Buffer;

public class RecommandChildFragment extends BaseLoadFragment {

    @Override
    protected int layoutRes() {
        return R.layout.fragment_recommand_child;
    }

    public static RecommandChildFragment newInstance(Bundle args){
        RecommandChildFragment fragment = new RecommandChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onViewReallyCreated(View view) {
         mUnbinder = ButterKnife.bind(this,view);
         Bundle bundle = getArguments();
    }


}
