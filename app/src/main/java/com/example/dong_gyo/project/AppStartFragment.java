package com.example.dong_gyo.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Song on 2015-10-04.
 */
public class AppStartFragment extends Fragment {
    //바뀌는 fragment (fragment_main)
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AppStartFragment newInstance(int sectionNumber) {
        AppStartFragment fragment = new AppStartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public AppStartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {   // Fragment 의 layout 을 설정하기 위해서는 onCreateView() 메소드를 사용
        View rootView = inflater.inflate(R.layout.appstart, container, false);

        startActivity(new Intent(getActivity(), LoadingApp.class));

        LinearLayout cuslayout = (LinearLayout)rootView.findViewById(R.id.customer);
        LinearLayout hostlayout = (LinearLayout)rootView.findViewById(R.id.shophost);

        cuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), MapFind.class);
                startActivity(it);
            }
        });

        hostlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), AdminLogin.class);
                startActivity(it);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {       //Fragment 는 onAttach() 이벤트시에 Activity 에 대한 참조를 얻을 수 있다
        super.onAttach(activity);
        ((NavigationMain) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

