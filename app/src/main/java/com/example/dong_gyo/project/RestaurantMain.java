package com.example.dong_gyo.project;

import android.app.LocalActivityManager;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Customize.ReviewAdapter;
import Items.Restaurant;
import Items.RestaurantListAsync;
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

    File file;

    ListView reviewlist;
    Button reviewbut;
    Button imagebut;
    EditText reviewtxt;

    Restaurant restaurant;

    ButtonClickListener buttonClickListener;
    LayoutListener layoutListener;

    ArrayList<Review> reviews;
    ReviewAdapter revadap;

    private int REVIEW_REGISTER =1;
    private int GET_RESTAURANT_REVIEW = 2;

    private int SELECT_IMAGE = 11;

    ArrayList pathStrArr  =  new ArrayList<String>();

    JSONArray received_review_arr = new JSONArray();
    JSONArray received_picture_str_iter = new JSONArray();
    JSONArray received_picture_base64_string = new JSONArray();

    protected Handler mHandler =  new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                System.out.println("handler error");
            } else if (msg.what == REVIEW_REGISTER) {

                try {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("review_send")) {

                        if(jobj.get("result").equals("REVIEW_REGISTER_ERROR")) {
                            Toast.makeText(getApplicationContext(), "REVIEW_REGISTER_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("REVIEW_REGISTER_FAIL")) {
                            Toast.makeText(getApplicationContext(), "REVIEW_REGISTER_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("REVIEW_REGISTER_SUCESS")) {
                            Toast.makeText(getApplicationContext(), "REVIEW_REGISTER_SUCESS", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "MESSAGE_TYPE_WRONG", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            else if( msg.what  == GET_RESTAURANT_REVIEW){
                //review를 받아올 때
                try {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("searchshopreview")) {

                        if(jobj.get("result").equals("SEARCH_SHOP_REVIEW_ERROR")) {
                            Toast.makeText(getApplicationContext(), "SEARCH_SHOP_REVIEW_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("SEARCH_SHOP_REVIEW_FAIL")) {
                            Toast.makeText(getApplicationContext(), "SEARCH_SHOP_REVIEW_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("SEARCH_SHOP_REVIEW_SUCCESS")) {

                            if( jobj.get("content").toString().equals("review_exist")){

                                reviews = new ArrayList<Review>();

                                received_review_arr = (JSONArray) jobj.get("review_arr");
                                //리뷰들 받아오기
                                received_picture_str_iter = (JSONArray) jobj.get("picture_str_iter");
                                //리뷰의 그림들 받아오기
                                received_picture_base64_string = (JSONArray) jobj.get("picture_base64_string");



                                for(int i = 0 ; i < received_review_arr.length(); i++){

                                    //리뷰 정보 가져오기
                                    JSONObject temp = new JSONObject(received_review_arr.get(i).toString());

                                    int reviewnum = temp.getInt("review_num");
                                    String user_id = temp.get("user_id").toString();
                                    String content = temp.get("content").toString();
                                    String date = temp.get("registered_date").toString();

                                    int count = 0;
                                    int tempnow = -1;
                                    for(int j = 0; j < received_picture_str_iter.length(); j++){

                                        int picture_iter = Integer.parseInt(received_picture_str_iter.get(j).toString());

                                        if(reviewnum == picture_iter && count > 0){
                                            if(tempnow != reviewnum){
                                                count = 0;
                                            }
                                            else{
                                                count++;
                                            }
                                        }
                                        if(reviewnum == picture_iter && count == 0){

                                            String picture_base64_str = received_picture_base64_string.get(j).toString();
                                            reviews.add(new Review(user_id, content, picture_base64_str));
                                            count++;
                                            tempnow= reviewnum;
                                        }

                                    }
                                }

                                Toast.makeText(getApplicationContext(), "REVIEW_ONE_MORE", Toast.LENGTH_SHORT).show();

                                Log.i("size", Integer.toString(reviews.size()) + "개의 리뷰");

                                for(int i=0; i<reviews.size(); i++) {
                                    Log.i("size", reviews.get(i).getName() + " " + reviews.get(i).getContent());
                                }

                                revadap = new ReviewAdapter(RestaurantMain.this, R.layout.review_shower, reviews);
                                reviewlist.setAdapter(revadap);

                            }
                            else if ( jobj.get("content").toString().equals("review_no_exist")){
                                Toast.makeText(getApplicationContext(), "REVIEW_NO_RECEIVE", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "MESSAGE_TYPE_WRONG", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info);

        layoutListener = new LayoutListener();
        buttonClickListener = new ButtonClickListener();
        intent = getIntent();

        try {
            receivedRestaurant = new JSONObject(intent.getExtras().getString("content"));

            int shop_id = receivedRestaurant.getInt("shop_id");
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



            restaurant = new Restaurant(shop_id, shop, laddr, saddr, floor, telno, category, type, detail, homepg, introduct, "0", "0");




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
        showweb.setOnClickListener(layoutListener);

        LinearLayout phonecall = (LinearLayout)findViewById(R.id.phonecall);
        phonecall.setClickable(true);
        phonecall.setOnClickListener(layoutListener);

        LinearLayout showmap = (LinearLayout)findViewById(R.id.showmap);
        showmap.setClickable(true);
        showmap.setOnClickListener(layoutListener);


        TextView tv = (TextView)findViewById(R.id.introduct);

        tv.setText(restaurant.getIntroduction());

        /** --------------------------------- 여기는 사진보기 탭임 --------------------------------- **/

        tsp = thost.newTabSpec("info2").setIndicator("사진보긔").setContent(R.id.tab2);
        thost.addTab(tsp);

        /** --------------------------------- 여기는 리뷰보기 탭임 --------------------------------- **/

        tsp = thost.newTabSpec("info3").setIndicator("리뷰보기").setContent(R.id.tab3);
        thost.addTab(tsp);


        thost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                int i = thost.getCurrentTab();
                if (i == 2) {

                    JSONObject get_review_sending = new JSONObject();

                    try {
                        get_review_sending.put("messagetype", "searchshopreview");
                        get_review_sending.put("content", restaurant.getShopname());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new RestaurantListAsync(RestaurantMain.this, StaticVariable.getConnectUrl(), mHandler, get_review_sending, GET_RESTAURANT_REVIEW, 0);
                }
            }
        });

        LinearLayout tmp = (LinearLayout)findViewById(R.id.reviewshower);

        revadap = new ReviewAdapter(this, R.layout.review_shower, reviews);
        reviewlist = (ListView)findViewById(R.id.reviewlist);
        reviewbut = (Button)findViewById(R.id.reviewbut);
        reviewbut.setOnClickListener(buttonClickListener);
        reviewtxt = (EditText)findViewById(R.id.reviewtext);
        imagebut = (Button)findViewById(R.id.imagebut);
        imagebut.setOnClickListener(buttonClickListener);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){


        Bitmap bitmap = null;

        //imageView = (ImageView)findViewById(R.id.imageView);

        if(resultCode == RESULT_OK && requestCode == SELECT_IMAGE){

            pathStrArr.clear();

            ClipData clipData = intent.getClipData();

            if(clipData != null) {
                //하나이상의 사진을 선택했을 떄

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri image = clipData.getItemAt(i).getUri();


                    pathStrArr.add(getImageNameToUri(image));

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                Uri image = intent.getData();
                pathStrArr.add(getImageNameToUri(image));
            }
        }
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
                    Uri phoneNum = Uri.parse("tel:" + restaurant.getTelno().toString());
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
            }
        }
    }

    class ButtonClickListener implements View.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()) {

                case R.id.reviewbut:

                    String review_content = reviewtxt.getText().toString();

                    if (review_content != "" || review_content != " ") {

                        JSONObject registerReview = new JSONObject();

                        JSONObject pathStr;
                        JSONArray pathArr = new JSONArray();

                        try {

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                            String date = dateFormat.format(Calendar.getInstance().getTime());

                            registerReview.put("messagetype", "review_send");
                            registerReview.put("review_writer_id", StaticVariable.getUser_id());
                            registerReview.put("review_writer_name", StaticVariable.getUser_name());
                            registerReview.put("review_shop_id", restaurant.getShopid());
                            registerReview.put("review_shop_name", restaurant.getShopname());
                            registerReview.put("review_content", review_content);
                            registerReview.put("review_registered_date", date);

                            if(pathStrArr.size() == 0){
                                //사진이 하나도 선택되지 않았을 때
                                pathStr = new JSONObject();
                                pathStr.put("pathStr", "");
                                pathArr.put(pathStr);
                            }
                            else {
                                //사진이 하나 이상 선택되었을 때
                                for(int i = 0 ; i < pathStrArr.size(); i++) {

                                    pathStr = new JSONObject();
                                    String pictureName = restaurant.getShopname() + "_" + StaticVariable.getUser_id() + "_" + date + "_" + i;
                                    pathStr.put("pathStr", pathStrArr.get(i).toString());
                                    pathStr.put("pictureName", pictureName);

                                    pathArr.put(pathStr);
                                }
                            }
                            registerReview.put("pathArr", pathArr);
                            Log.i("성공!!", registerReview.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new RestaurantListAsync(RestaurantMain.this, StaticVariable.getConnectUrl(), mHandler, registerReview, REVIEW_REGISTER, 0);
                    }

                    break;

                case R.id.imagebut:

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "사진 선택"), SELECT_IMAGE);

                    break;
            }
        }
    }
}
