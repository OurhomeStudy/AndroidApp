package com.example.dong_gyo.project;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

/**
 * Created by Dong_Gyo on 15. 8. 27..
 */
public class ResMain2 extends ActionBarActivity {

    TabHost thost;
    TabHost.TabSpec tsp;
    android.support.v7.app.ActionBar actionBar;
    Resources res;
    LocalActivityManager lam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info2);

        actionBar = getSupportActionBar();
        actionBar.setTitle("경발원");
        actionBar.show();

        lam = new LocalActivityManager(this, false);

        res = this.getResources();
        lam.dispatchCreate(savedInstanceState);
        thost = (TabHost)findViewById(R.id.tabhost);
        thost.setup(lam);

        tsp = thost.newTabSpec("info2").setIndicator("상세정보1").setContent(R.id.tab1);
        thost.addTab(tsp);

        LinearLayout showweb = (LinearLayout)findViewById(R.id.ShowWeb1);
        showweb.setClickable(true);
        showweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auth = "http://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ie=utf8&query=";
                String query = "경발원";
                Uri webpage = Uri.parse(auth + query);
                Intent it = new Intent(Intent.ACTION_VIEW, webpage);
                if (it != null) startActivity(it);
            }
        });

        tsp = thost.newTabSpec("info2").setIndicator("상세정보2").setContent(R.id.tab2);
        thost.addTab(tsp);

        tsp = thost.newTabSpec("info3").setIndicator("상세정보3").setContent(R.id.tab3);
        thost.addTab(tsp);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
