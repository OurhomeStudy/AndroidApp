package com.example.dong_gyo.project;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Customize.ReviewAdapter;
import Items.Restaurant;
import Items.Review;
import Items.StaticVariable;

/**
 * Created by Dong_Gyo on 15. 8. 27..
 */
public class RestaurantMain extends ActionBarActivity {

    TabHost thost;
    TabHost.TabSpec tsp;
    android.support.v7.app.ActionBar actionBar;
    Resources res;
    LocalActivityManager lam;
    JSONObject receivedRestaurant;
    Intent intent;

    ListView reviewlist;
    Button reviewbut;
    Button imagebut;
    EditText reviewtxt;

    Restaurant restaurant;
    ArrayList<Review> reviews = new ArrayList<Review>();
    ReviewAdapter revadap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info);

        LayoutListener lls = new LayoutListener();
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
            restaurant = new Restaurant(shop, laddr, saddr, floor, telno, category, type, detail, homepg, introduct, "0", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //이제 이 위에것으로 사용하면됨



        actionBar = getSupportActionBar();
        actionBar.setTitle(restaurant.getShopname());
        actionBar.show();

        lam = new LocalActivityManager(this, false);

        res = this.getResources();
        lam.dispatchCreate(savedInstanceState);
        thost = (TabHost)findViewById(R.id.tabhost);
        thost.setup(lam);

        /** --------------------------------- 여기는 상세정보 탭임 --------------------------------- **/

        tsp = thost.newTabSpec("info2").setIndicator("상세정보1").setContent(R.id.tab1);
        thost.addTab(tsp);

        LinearLayout showweb = (LinearLayout)findViewById(R.id.showweb);
        showweb.setClickable(true);

        LinearLayout phonecall = (LinearLayout)findViewById(R.id.phonecall);
        phonecall.setClickable(true);

        LinearLayout showmap = (LinearLayout)findViewById(R.id.showmap);
        showmap.setClickable(true);

        TextView tv = (TextView)findViewById(R.id.introduct);

        tv.setText(restaurant.getIntroduction());

        /** --------------------------------- 여기는 사진보기 탭임 --------------------------------- **/

        tsp = thost.newTabSpec("info2").setIndicator("사진보긔").setContent(R.id.tab2);
        thost.addTab(tsp);

        /** --------------------------------- 여기는 리뷰보기 탭임 --------------------------------- **/

        tsp = thost.newTabSpec("info3").setIndicator("리뷰보기").setContent(R.id.tab3);
        thost.addTab(tsp);

        revadap = new ReviewAdapter(this, R.layout.review_shower, reviews);
        reviewlist = (ListView)findViewById(R.id.reviewlist);
        reviewbut = (Button)findViewById(R.id.reviewbut);
        imagebut = (Button)findViewById(R.id.imagebut);
        reviewtxt = (EditText)findViewById(R.id.reviewtext);

    }



    public String getImageNameToUri(Uri data){
        //filepath to Uri string method

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);

        return imgPath;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class LayoutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.showweb :
                    String auth = "http://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ie=utf8&query=";
                    String query = restaurant.getShopname();
                    Log.i("음식점", query);
                    Uri webpage = Uri.parse(auth + query);
                    Intent it = new Intent(Intent.ACTION_VIEW, webpage);
                    if (it != null) startActivity(it);
                    break;
                case R.id.phonecall :
                    Uri phoneNum = Uri.parse("tel:"+restaurant.getTelno().toString());
                    it = new Intent(Intent.ACTION_DIAL, phoneNum);
                    if (it != null) startActivity(it);
                    break;
                case R.id.showmap :
                    auth = "https://www.google.co.kr/maps/search/";
                    query = restaurant.getShopname();
                    webpage = Uri.parse(auth + query);
                    it = new Intent(Intent.ACTION_VIEW, webpage);
                    if (it != null) startActivity(it);
                    break;
                case R.id.reviewbut :

                    String review_content = reviewtxt.getText().toString();

                    if(review_content != "" || review_content != " ") {

                        JSONObject registerReview = new JSONObject();

                        try {
                            registerReview.put("messagetype", "review_send");
                            registerReview.put("review_writer_id", StaticVariable.getUser_id());
                            registerReview.put("review_writer_name", StaticVariable.getUser_name());
                            registerReview.put("review_shop_name", restaurant.getShopname());
                            registerReview.put("review_content", review_content);

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                            String date = dateFormat.format(Calendar.getInstance().getTime());

                            registerReview.put("review_registered_date", date);
                            registerReview.put("pathStr", "pathStr");

                            Log.i("성공!!", registerReview.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

        }
    }
}
