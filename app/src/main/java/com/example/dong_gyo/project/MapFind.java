package com.example.dong_gyo.project;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Items.Restaurant;
import Items.RestaurantListAsync;
import Items.StaticVariable;

public class MapFind extends ActionBarActivity {

    private int GET_RESTAURANT_LIST = 1;


    private int clickedFlag= -1;

    GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationManager lm;
    String locationProvider;
    Location location;
    Button mapListbut;
    Button mapSearchbut;
    ListView mapList;
    ArrayList listarr;
    ArrayAdapter mapadapter;
    android.support.v7.app.ActionBar actionBar;
    JSONObject sending;

    ArrayList<Restaurant> reslist;
    ArrayList<Marker> mk;

    double center_latitude = 0;
    double center_longitude = 0;

    final double LATDISTANCE = 0.0045050118256;
    final double LONGDISTANCE = 0.005659725956;

    protected Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                //   BreakTimeout();
                //ConnectionError();
                System.out.println("handler error");
            } else if (msg.what == GET_RESTAURANT_LIST) {

                try {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if (jobj.get("messagetype").equals("searchLocalRestaurant")) {

                        if (jobj.get("result").equals("GET_RESTAURANT_INFO_ERROR")) {
                            Toast.makeText(getApplicationContext(), "GET_RESTAURANT_INFO_ERROR", Toast.LENGTH_SHORT).show();
                        } else if (jobj.get("result").equals("GET_RESTAURANT_INFO_FAIL")) {
                            Toast.makeText(getApplicationContext(), "GET_RESTAURANT_INFO_FAIL", Toast.LENGTH_SHORT).show();
                        } else if (jobj.get("result").equals("GET_RESTAURANT_INFO_SUCCESS")) {

                            if (mk != null) {
                                while (mk.size() != 0) {
                                    mk.get(0).setVisible(false);
                                    mk.remove(0);
                                }
                            }
                            reslist = new ArrayList<Restaurant>();
                            mk = new ArrayList<Marker>();
                            listarr = new ArrayList();

                            if(!jobj.get("content").toString().equals("")) {

                                JSONArray received = (JSONArray) jobj.get("content");

                                for (int i = 0; i < received.length(); i++) {
                                    JSONObject temp = (JSONObject) received.get(i);

                                    int shop_id = temp.getInt("shop_id");
                                    String shop = temp.get("shop_name").toString();
                                    String laddr = temp.get("shop_address_lotnum").toString();
                                    String saddr = temp.get("shop_address_street").toString();
                                    String floor = temp.get("shop_floor").toString();
                                    String telno = temp.get("shop_tel_number").toString();
                                    String category = temp.get("shop_category").toString();
                                    String type = temp.get("shop_type").toString();
                                    String detail = temp.get("shop_details").toString();
                                    String homepg = temp.get("shop_homepage").toString();
                                    String introduct = temp.get("shop_introduct").toString();
                                    String lat = temp.get("shop_latitude").toString();
                                    String lng = temp.get("shop_longitude").toString();

                                    Restaurant restmp = new Restaurant(shop_id, shop, laddr, saddr, floor, telno, category, type, detail, homepg, introduct, lat, lng);

                                    LatLng latlngtmp = restmp.getLatlng();

                                    Marker mktmp = mMap.addMarker(new MarkerOptions()
                                            .position(latlngtmp)
                                            .icon(BitmapDescriptorFactory
                                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .title(restmp.getShopname()));


                                    reslist.add(restmp);
                                    mk.add(mktmp);
                                    listarr.add(reslist.get(reslist.size() - 1).getShopname());
                                    mk.get(mk.size() - 1).showInfoWindow();
                                }

                                for (int i = 0; i < reslist.size(); i++) {
                                    Log.i("레스토랑", reslist.get(i).getShopname());
                                    Log.i("리스트뷰", listarr.get(i).toString());
                                }
                                Log.i("레스토랑 갯수", Integer.toString(reslist.size()));
                                Log.i("마커 갯수", Integer.toString(mk.size()));

                                mapadapter = new ArrayAdapter(MapFind.this, android.R.layout.simple_list_item_1, listarr);
                                mapList.setAdapter(mapadapter);


                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "MESSAGE_TYPE_WRONG", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_find);

        actionBar = getSupportActionBar();
        actionBar.setTitle("지도로 검색하기");
        actionBar.show();

        setUpMapIfNeeded();

        ButtonListener btl = new ButtonListener();

        listarr = new ArrayList();

        mapListbut = (Button) findViewById(R.id.showMapList);
        mapSearchbut = (Button) findViewById(R.id.findShop);

        mapListbut.setOnClickListener(btl);
        mapSearchbut.setOnClickListener(btl);

        mapList = (ListView) findViewById(R.id.mapList);
        mapList.setVisibility(View.INVISIBLE);

        mapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listview 클릭리스너
                for (int i = 0; i < reslist.size(); i++) {

                    if (mapList.getItemAtPosition(position).toString().equals(reslist.get(i).getShopname())) {

                        clickedFlag = i;
                        break;
                    }
                }

                JSONObject clickedRestaurant = new JSONObject();
                try {

                    clickedRestaurant.put("shop_id", reslist.get(clickedFlag).getShopid());
                    clickedRestaurant.put("shop_name", reslist.get(clickedFlag).getShopname());
                    clickedRestaurant.put("shop_address_lotnum", reslist.get(clickedFlag).getLAddress());
                    clickedRestaurant.put("shop_address_street", reslist.get(clickedFlag).getSAdress());
                    clickedRestaurant.put("shop_floor", reslist.get(clickedFlag).getFloor());
                    clickedRestaurant.put("shop_tel_number", reslist.get(clickedFlag).getTelno());
                    clickedRestaurant.put("shop_category", reslist.get(clickedFlag).getCategory());
                    clickedRestaurant.put("shop_type", reslist.get(clickedFlag).getType());
                    clickedRestaurant.put("shop_details", reslist.get(clickedFlag).getDetail());
                    clickedRestaurant.put("shop_homepage", reslist.get(clickedFlag).getHomepg());
                    clickedRestaurant.put("shop_introduct", reslist.get(clickedFlag).getIntroduction());

                    Log.i("성공!!", clickedRestaurant.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent it = new Intent(MapFind.this, RestaurantMain.class);

                it.putExtra("content", clickedRestaurant.toString());

                startActivity(it);



            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap(LatLng LOC)} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationProvider = lm.getBestProvider(new Criteria(), true);

            location = lm.getLastKnownLocation(locationProvider);

            double latitude;
            double longitude;

            if (location == null) {


                AlertDialog.Builder agreeLoc = new AlertDialog.Builder(MapFind.this);
                agreeLoc.setMessage("위치정보 제공에 동의하고 있지 않습니다.\n" +
                        "위치정보제공에 동의하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                return;
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = agreeLoc.create();
                alert.show();


                latitude = 37.5665;
                longitude = 126.978;

            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            mMap.setMyLocationEnabled(true);
            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);

            final LatLng LOC = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOC, 16));

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                    /*
                     * double nw_latitude; //븍서쪽 좌표 double nw_longitude; double
                     * se_latitude; //남동쪽 좌표 double se_longitude;
                     */
                    Double[] location = new Double[4];

                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {

                        LatLng location_center = cameraPosition.target;
                        center_latitude = cameraPosition.target.latitude;
                        center_longitude = cameraPosition.target.longitude;

                        location[0] = center_latitude + LATDISTANCE;
                        location[1] = center_longitude - LONGDISTANCE;
                        location[2] = center_latitude - LATDISTANCE;
                        location[3] = center_longitude + LONGDISTANCE;

                    }
                });
            }

            LocationListener locationListener = new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProviderEnabled(String provider) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onLocationChanged(Location location) {
                    // TODO Auto-generated method stub

                    updateMap(location);

                }
            };
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(LatLng LOC) {
        mMap.addMarker(new MarkerOptions().position(LOC));
    }

    public void updateMap(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        final LatLng LOC = new LatLng(latitude, longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LOC, 16));
        /*
        Marker mk = mMap.addMarker(new MarkerOptions()
                .position(LOC)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("나의 위치 ").snippet("김영준"));

        mk.showInfoWindow();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String arg0) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String arg0) {
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.showMapList :
                    if (!mapList.isShown()) {
                        mapList.setVisibility(View.VISIBLE);
                        mapListbut.setSelected(true);
                        mapListbut.setText("목록 닫기");
                    } else if (mapList.isShown()) {
                        mapList.setVisibility(View.INVISIBLE);
                        mapListbut.setSelected(false);
                        mapListbut.setText("목록 보기");
                    } break;
                case R.id.findShop :
                    if (center_latitude != 0) {
                        LatLng now = new LatLng(center_latitude, center_longitude);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(now, 16));

                        sending = new JSONObject();

                        try {
                            sending.put("messagetype", "searchLocalRestaurant");
                            sending.put("sendUp", Double.toString(center_latitude + LATDISTANCE));
                            sending.put("sendDown", Double.toString(center_latitude - LATDISTANCE));
                            sending.put("sendLeft", Double.toString(center_longitude - LONGDISTANCE));
                            sending.put("sendRight", Double.toString(center_longitude + LONGDISTANCE));

                            new RestaurantListAsync(MapFind.this , StaticVariable.getConnectUrl(), mHandler, sending, GET_RESTAURANT_LIST, 0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                    } break;
            }

        }
    }
}