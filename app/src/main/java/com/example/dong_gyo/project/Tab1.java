package com.example.dong_gyo.project;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dong_Gyo on 2015. 9. 14..
 */
public class Tab1 extends Fragment {

    Context mContext;

    public Tab1(Context context) {
        mContext = context;
    }

    public Tab1() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, null);

        return view;
    }

}
