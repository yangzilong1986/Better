package com.ikok.teachingwebsite.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikok.teachingwebsite.R;

/**
 * Created by Anonymous on 2016/4/10.
 */
public class DownloadFragment extends Fragment {

    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_no_data,container,false);
        return view;
    }
}
