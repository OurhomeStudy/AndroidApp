package com.example.dong_gyo.project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by Dong_Gyo on 15. 8. 11..
 */
public class LoadingApp extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_app);

        ImageView imv = (ImageView)findViewById(R.id.loading);

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);
    }
}
