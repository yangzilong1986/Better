package com.ikok.teachingwebsite.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ikok.teachingwebsite.Activity.LoginActivity;
import com.ikok.teachingwebsite.R;

/**
 * Created by Anonymous on 2016/5/1.
 */
public class GuideThree extends Fragment {

    private View mView = null;
    private Button mEnterBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorguidethree));
        mView = inflater.inflate(R.layout.activity_guide_pagethree,container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEnterBtn = (Button) mView.findViewById(R.id.id_guide_enter);
        mEnterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("is_first_in_data", 0x0000);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstIn", false);
                editor.commit();

                getActivity().finish();
            }
        });
    }
}
