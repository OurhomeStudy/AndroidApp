package com.example.dong_gyo.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Dong_Gyo on 15. 8. 12..
 */
public class ResMain extends ActionBarActivity {

    LinearLayout showWeb;
    LinearLayout showMap;
    LinearLayout phoneCall;
    ImageView im;
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info);

        actionBar = getSupportActionBar();
        actionBar.setTitle("경발원");
        actionBar.show();

        im = (ImageView)findViewById(R.id.food);

        showWeb = (LinearLayout)findViewById(R.id.ShowWeb);
        showWeb.setClickable(true);
        showWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auth = "http://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ie=utf8&query=";
                String query = "경발원";
                Uri webpage = Uri.parse(auth + query);
                Intent it = new Intent(Intent.ACTION_VIEW, webpage);
                if (it != null) startActivity(it);
            }
        });

        showMap = (LinearLayout)findViewById(R.id.ShowMap);
        showMap.setClickable(true);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auth = "https://www.google.co.kr/maps/search/";
                String query = "경발원";
                Uri webpage = Uri.parse(auth + query);
                Intent it = new Intent(Intent.ACTION_VIEW, webpage);
                if (it != null) startActivity(it);
            }
        });

        phoneCall = (LinearLayout)findViewById(R.id.Phone);
        phoneCall.setClickable(true);
        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri phoneNum = Uri.parse("tel:01033702231");
                Intent it = new Intent(Intent.ACTION_DIAL, phoneNum);
                if (it != null) startActivity(it);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
