package com.example.dong_gyo.project;

import android.app.LocalActivityManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

        //tsp = thost.newTabSpec("info").setIndicator("상세정보");

        //thost.addTab(tsp);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
