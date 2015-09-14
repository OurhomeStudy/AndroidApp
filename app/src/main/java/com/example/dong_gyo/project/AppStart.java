package com.example.dong_gyo.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Dong_Gyo on 2015. 9. 13..
 */
public class AppStart extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appstart);

        startActivity(new Intent(this, LoadingApp.class));

        LinearLayout cuslayout = (LinearLayout)findViewById(R.id.customer);
        LinearLayout hostlayout = (LinearLayout)findViewById(R.id.shophost);

        cuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(AppStart.this, MapFind.class);
                startActivity(it);
            }
        });
    }
}
