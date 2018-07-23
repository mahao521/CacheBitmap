package com.bitmap.database.view;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitmap.database.R;
import com.bitmap.database.base.BaseLoadFragment;

public class ListPlayFragment extends BaseLoadFragment {

    public ListPlayFragment(){}

    @Override
    protected int layoutRes() {
        return R.layout.fragment_list_play;
    }

    public static ListPlayFragment create(String id, String name){
        ListPlayFragment listPlayFragment = new ListPlayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tab_bundle_category", id);
        bundle.putString("tab_bundle_columns", name);
        listPlayFragment.setArguments(bundle);
        return listPlayFragment;
    }

    @Override
    protected void onViewReallyCreated(View view) {

    }
}
