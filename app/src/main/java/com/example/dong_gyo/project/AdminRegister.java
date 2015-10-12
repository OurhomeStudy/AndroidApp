package com.example.dong_gyo.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Items.RestaurantListAsync;
import Items.StaticVariable;

/**
 * Created by cho on 2015-09-20.
 */
public class AdminRegister  extends Activity {

    Boolean idCheckFlag = false;

    EditText idInputText;
    EditText pwInputText;
    EditText ownerNameInputText;
    EditText ownerTelNoInputText;
    EditText shopNameInputText;
    EditText shopAddressStreetInputText;
    EditText shopAddressLotnumInputText;
    EditText shopTelNoInputText;
    EditText shopFloorInputText;
    EditText shopCategoryInputText;
    EditText shopTypeInputText;
    EditText shopDetailsInputText;
    EditText shopHomepageInputText;
    EditText shopIntroductInputText;

    Button idDuplicateCheck;
    Button shopNameDuplicateCheck;

    Button registerDoneButton;
    Button registerCancelButton;

    ButtonListener listener;

    String tempSavedId = null;

    private int ADMIN_ID_DUPLICATE_CHECK =1;
    private int ADMIN_SHOP_NAME_SEARCH =2;
    private int ADMIN_REGISTER = 3;

    protected Handler mHandler =  new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                //   BreakTimeout();
                //ConnectionError();
                System.out.println("handler error");
            }
            else if (msg.what == ADMIN_ID_DUPLICATE_CHECK) {

                try {

                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("adminregisteridduplicatecheck")) {

                        if(jobj.get("result").equals("ADMIN_ID_DUPLICATE_CHECK_ERROR")) {
                            Toast.makeText(getApplicationContext(), "ADMIN_ID_DUPLICATE_CHECK_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("ADMIN_ID_DUPLICATE_CHECK_FAIL")) {
                            Toast.makeText(getApplicationContext(), "ADMIN_ID_DUPLICATE_CHECK_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("ADMIN_ID_DUPLICATE_CHECK_SUCCESS")) {


                            JSONArray received = (JSONArray) jobj.get("content");
                            JSONObject temp = (JSONObject) received.get(0);

                            int owner_id_count = -1;
                            owner_id_count = Integer.valueOf(temp.get("owner_id_count").toString());

                            if(owner_id_count == 0) {
                                tempSavedId = idInputText.getText().toString();
                                idCheckFlag = true;
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            }
                            else if(owner_id_count > 0){
                                Toast.makeText(getApplicationContext(), "already exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "MESSAGE_TYPE_WRONG", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (msg.what == ADMIN_SHOP_NAME_SEARCH) {

                try {

                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("adminregistersearchshopname")) {

                        if(jobj.get("result").equals("ADMIN_SHOP_SEARCH_CHECK_ERROR")) {
                            Toast.makeText(getApplicationContext(), "ADMIN_SHOP_SEARCH_CHECK_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("ADMIN_SHOP_SEARCH_CHECK_FAIL")) {
                            Toast.makeText(getApplicationContext(), "ADMIN_SHOP_SEARCH_CHECK_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("ADMIN_SHOP_SEARCH_CHECK_SUCCESS")) {

                            if(jobj.get("content").toString().equals("")){

                                Toast.makeText(getApplicationContext(), "search failed", Toast.LENGTH_SHORT).show();

                                //shopNameInputText.setText();
                                shopAddressStreetInputText.setText("");
                                shopAddressLotnumInputText.setText("");
                                shopTelNoInputText.setText("");
                                shopFloorInputText.setText("");
                                shopCategoryInputText.setText("");
                                shopTypeInputText.setText("");
                                shopDetailsInputText.setText("");
                                shopHomepageInputText.setText("");
                                shopIntroductInputText.setText("");

                            }
                            else{
                                JSONArray received = (JSONArray) jobj.get("content");
                                //있을 때 존재하는 모든 정보를 받아옴
                                JSONObject temp = (JSONObject) received.get(0);

                                String receivedShopName = temp.getString("shop_name").toString();
                                String receivedShopAddressLotnum = temp.getString("shop_address_lotnum").toString();
                                String receivedShopAddressStreet = temp.get("shop_address_street").toString();
                                String receivedShopFloor = temp.get("shop_floor").toString();
                                String receivedShopTelNumber = temp.get("shop_tel_number").toString();
                                String receivedShopCategory = temp.get("shop_category").toString();
                                String receivedShopType = temp.get("shop_type").toString();
                                String receivedShopDetail = temp.get("shop_details").toString();
                                String receivedShopHomepage = temp.get("shop_homepage").toString();
                                String receivedShopIndrotuct = temp.get("shop_introduct").toString();

                                shopNameInputText.setText(receivedShopName);
                                shopAddressStreetInputText.setText(receivedShopAddressStreet);
                                shopAddressLotnumInputText.setText(receivedShopAddressLotnum);
                                shopTelNoInputText.setText(receivedShopTelNumber);
                                shopFloorInputText.setText(receivedShopFloor);
                                shopCategoryInputText.setText(receivedShopCategory);
                                shopTypeInputText.setText(receivedShopType);
                                shopDetailsInputText.setText(receivedShopDetail);
                                shopHomepageInputText.setText(receivedShopHomepage);
                                shopIntroductInputText.setText(receivedShopIndrotuct);


                            }

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "MESSAGE_TYPE_WRONG", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (msg.what == ADMIN_REGISTER){
                // register done button 눌렀을 떄.
                try {

                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("adminregisterdone")) {

                        if(jobj.get("result").equals("ADMIN_REGISTER_DONE_ERROR")) {
                            Toast.makeText(getApplicationContext(), "ADMIN_REGISTER_DONE_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("ADMIN_REGISTER_DONE_FAIL")) {
                            Toast.makeText(getApplicationContext(), "ADMIN_REGISTER_DONE_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("ADMIN_REGISTER_DONE_SUCESS")) {

                            //register done button 이 정상적으로 실행됬을 때.

                            if(jobj.get("content").toString().equals("")){

                                Toast.makeText(getApplicationContext(), "register sucess", Toast.LENGTH_SHORT).show();



                            }
                            else{
                                JSONArray received = (JSONArray) jobj.get("content");
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        listener = new ButtonListener();

        idInputText = (EditText)findViewById(R.id.register_id_edittext);
        pwInputText = (EditText)findViewById(R.id.register_pw_edittext);

        idDuplicateCheck = (Button)findViewById(R.id.register_id_duplicatecheck_button);
        idDuplicateCheck.setOnClickListener(listener);

        ownerNameInputText = (EditText)findViewById(R.id.register_name_edittext);
        ownerTelNoInputText = (EditText)findViewById(R.id.register_phonenumber_edittext);

        shopNameInputText = (EditText)findViewById(R.id.register_shop_name_edittext);

        shopNameDuplicateCheck = (Button)findViewById(R.id.register_shop_name_search_button);
        shopNameDuplicateCheck.setOnClickListener(listener);

        shopTelNoInputText  = (EditText)findViewById(R.id.register_shop_telno_edittext);
        shopAddressStreetInputText = (EditText)findViewById(R.id.register_shop_address_street_edittext);
        shopAddressLotnumInputText  = (EditText)findViewById(R.id.register_shop_address_lotnum_edittext);
        shopFloorInputText = (EditText)findViewById(R.id.register_shop_floor_edittext);
        shopCategoryInputText = (EditText)findViewById(R.id.register_shop_category_edittext);
        shopTypeInputText = (EditText)findViewById(R.id.register_shop_type_edittext);
        shopDetailsInputText = (EditText)findViewById(R.id.register_shop_details_edittext);
        shopHomepageInputText = (EditText)findViewById(R.id.register_shop_homepage_edittext);
        shopIntroductInputText = (EditText)findViewById(R.id.register_shop_introduct_edittext);

        registerDoneButton = (Button) findViewById(R.id.register_done_button);
        registerDoneButton.setOnClickListener(listener);
        registerCancelButton = (Button)findViewById(R.id.register_cancel_button);
        registerCancelButton.setOnClickListener(listener);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            switch(v.getId()) {

                case R.id.register_id_duplicatecheck_button :
                    //id duplicate check
                    if(idInputText.getText().toString().equals("")){
                        //when ID input is null
                        Toast.makeText(getApplicationContext(), "enter id", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        JSONObject sending = new JSONObject();

                        try {
                            sending.put("messagetype", "adminregisteridduplicatecheck");
                            sending.put("adminid", idInputText.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new RestaurantListAsync(getApplicationContext(), StaticVariable.getConnectUrl(), mHandler, sending, ADMIN_ID_DUPLICATE_CHECK, 0);
                    }
                    break;
                case R.id.register_shop_name_search_button:

                    if(shopNameInputText.getText().toString().equals("")){
                        //shop name search
                        Toast.makeText(getApplicationContext(), "enter shop name", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        JSONObject sending = new JSONObject();

                        try {
                            sending.put("messagetype", "adminregistersearchshopname");
                            sending.put("searchshopname", shopNameInputText.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new RestaurantListAsync(getApplicationContext(), StaticVariable.getConnectUrl(), mHandler, sending, ADMIN_SHOP_NAME_SEARCH, 0);
                    }

                    break;
                case R.id.register_done_button:
                    // 빈칸이 존재하면 빈칸 있다고 말해줌.

                    if(idCheckFlag == false || !tempSavedId.equals(idInputText.getText().toString())){
                        //idcheckFlag 가 false거나 인증 받은 후에 id를 바꿨으면 다시 인증해야됨.
                        Toast.makeText(getApplicationContext(), "check id duplication", Toast.LENGTH_SHORT).show();
                    }
                    else{


                        if(pwInputText.getText().toString().equals("") || ownerNameInputText.getText().toString().equals("")
                                || ownerTelNoInputText.getText().toString().equals("") || shopNameInputText.getText().toString().equals("")
                                || shopAddressStreetInputText.getText().toString().equals("") || shopAddressLotnumInputText.getText().toString().equals("")
                                || shopTelNoInputText.getText().toString().equals("") || shopFloorInputText.getText().toString().equals("")
                                || shopCategoryInputText.getText().toString().equals("") || shopTypeInputText.getText().toString().equals("")
                                || shopDetailsInputText.getText().toString().equals("")|| shopHomepageInputText.getText().toString().equals("")
                                || shopIntroductInputText.getText().toString().equals("")){

                            Toast.makeText(getApplicationContext(), "blank exists.", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            //모두 빈칸이 아닐때에 서버에 전달




                        }
                    }



                    break;
                case R.id.register_cancel_button:
                    //register cancel
                    Intent it = new Intent(AdminRegister.this, AdminLogin.class);
                    startActivity(it);
                    break;
            }
        }
    }


    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                }
                else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
