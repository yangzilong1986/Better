package com.ikok.teachingwebsite.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikok.teachingwebsite.R;

/**
 * Created by Anonymous on 2016/5/1.
 */
public class GuideTwo extends Fragment {

    private View mView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorguidetwo));
        mView = inflater.inflate(R.layout.activity_guide_pagetwo,container,false);
        return mView;
    }
}
