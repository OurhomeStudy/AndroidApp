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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import Items.Restaurant;

/**
 * Created by Dong_Gyo on 15. 8. 27..
 */
public class ResMain2 extends ActionBarActivity {

    TabHost thost;
    TabHost.TabSpec tsp;
    android.support.v7.app.ActionBar actionBar;
    Resources res;
    LocalActivityManager lam;
    JSONObject receivedRestaurant;
    Intent intent;

    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info2);


        intent = getIntent();

        try {
            receivedRestaurant = new JSONObject(intent.getExtras().getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String shop = receivedRestaurant.get("shop_name").toString();
            String laddr = receivedRestaurant.get("shop_address_lotnum").toString();
            String saddr = receivedRestaurant.get("shop_address_street").toString();
            String floor = receivedRestaurant.get("shop_floor").toString();
            String telno = receivedRestaurant.get("shop_tel_number").toString();
            String category = receivedRestaurant.get("shop_category").toString();
            String type = receivedRestaurant.get("shop_type").toString();
            String detail = receivedRestaurant.get("shop_details").toString();
            String homepg = receivedRestaurant.get("shop_homepage").toString();
            String introduct = receivedRestaurant.get("shop_introduct").toString();
            String lat = receivedRestaurant.get("shop_latitude").toString();
            String lng = receivedRestaurant.get("shop_longitude").toString();
            restaurant = new Restaurant(shop, laddr, saddr, floor, telno, category, type, detail, homepg, introduct, lat, lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //이제 이 위에것으로 사용하면됨



        actionBar = getSupportActionBar();
        CharSequence tmp = restaurant.getShopname();
        actionBar.setTitle(tmp);
        actionBar.show();

        lam = new LocalActivityManager(this, false);

        res = this.getResources();
        lam.dispatchCreate(savedInstanceState);
        thost = (TabHost)findViewById(R.id.tabhost);
        thost.setup(lam);

        /** --------------------------------- 여기는 상세정보 탭임 --------------------------------- **/

        tsp = thost.newTabSpec("info2").setIndicator("상세정보1").setContent(R.id.tab1);
        thost.addTab(tsp);

        LinearLayout showweb = (LinearLayout)findViewById(R.id.ShowWeb1);
        showweb.setClickable(true);
        showweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auth = "http://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ie=utf8&query=";
                String query = restaurant.getShopname();
                Uri webpage = Uri.parse(auth + query);
                Intent it = new Intent(Intent.ACTION_VIEW, webpage);
                if (it != null) startActivity(it);
            }
        });

        TextView tv = (TextView)findViewById(R.id.introduct);

        tmp = restaurant.getIntroduction();
        tv.setText(tmp);

        /** --------------------------------- 여기는 사진보기 탭임 --------------------------------- **/

        tsp = thost.newTabSpec("info2").setIndicator("사진보긔").setContent(R.id.tab2);
        thost.addTab(tsp);

        /** --------------------------------- 여기는 리뷰보기 탭임 --------------------------------- **/

        tsp = thost.newTabSpec("info3").setIndicator("리뷰보기").setContent(R.id.tab3);
        thost.addTab(tsp);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
