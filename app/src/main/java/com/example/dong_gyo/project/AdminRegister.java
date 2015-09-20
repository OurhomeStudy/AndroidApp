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

import Items.RestaurantListAsync;

/**
 * Created by cho on 2015-09-20.
 */
public class AdminRegister  extends Activity {

    Boolean idCheckFlag = false;


    EditText idInputText;
    EditText pwInputText;

    Button idDuplicateCheck;

    ButtonListener listener;

    Intent intent;

    private int ADMIN_ID_DUPLICATE_CHECK =1;

    protected Handler mHandler =  new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                //   BreakTimeout();
                //ConnectionError();
                System.out.println("handler error");
            } else if (msg.what == ADMIN_ID_DUPLICATE_CHECK) {

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
                                idCheckFlag = true;
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            }
                            else if(owner_id_count > 0){
                                Toast.makeText(getApplicationContext(), "id가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
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
                    //중복체크 버튼일 경우
                    if(idInputText.getText().toString().equals("")){
                        //ID 입력이 비었을 때
                        Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        JSONObject sending = new JSONObject();

                        try {
                            sending.put("messagetype", "adminregisteridduplicatecheck");
                            sending.put("adminid", idInputText.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new RestaurantListAsync(getApplicationContext(), "https://183.96.25.221:15443/", mHandler, sending, ADMIN_ID_DUPLICATE_CHECK, 0);
                    }
                    break;





            }
        }
    }
}
