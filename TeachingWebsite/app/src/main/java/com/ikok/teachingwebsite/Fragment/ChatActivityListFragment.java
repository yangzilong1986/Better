package com.ikok.teachingwebsite.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikok.teachingwebsite.R;

/**
 * Created by Anonymous on 2016/4/16.
 */
public class ChatActivityListFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_no_data,container,false);
        return mView;
    }
}
