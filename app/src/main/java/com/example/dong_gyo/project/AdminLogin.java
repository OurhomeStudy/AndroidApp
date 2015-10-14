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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Items.RestaurantListAsync;
import Items.StaticVariable;

/**
 * Created by cho on 2015-09-17.
 */
public class AdminLogin extends Activity {

    EditText idInputText;
    EditText pwInputText;

    Button registerButton;
    Button loginButton;

    ButtonListener listener;

    private int ADMIN_ID_PW_CHECK =1;

    protected Handler mHandler =  new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                //   BreakTimeout();
                //ConnectionError();
                System.out.println("handler error");
            } else if (msg.what == ADMIN_ID_PW_CHECK) {

                try {

                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("adminlogincheck")) {

                        if(jobj.get("result").equals("ADMIN_ID_PW_CHECK_ERROR")) {
                            Toast.makeText(getApplicationContext(), "ADMIN_ID_PW_CHECK_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("ADMIN_ID_PW_CHECK_FAIL")) {
                            Toast.makeText(getApplicationContext(), "ADMIN_ID_PW_CHECK_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("ADMIN_ID_PW_CHECK_SUCCESS")) {

                            /*
                            JSONArray received = (JSONArray) jobj.get("content");
                            JSONObject temp = (JSONObject) received.get(0);

                            String adminid = temp.get("adminid").toString();
                            String adminpw = temp.get("adminpw").toString();
                            */
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.login);

        listener = new ButtonListener();

        idInputText = (EditText)findViewById(R.id.login_id_edittext);
        pwInputText = (EditText)findViewById(R.id.login_pw_edittext);

        registerButton = (Button)findViewById(R.id.login_register_button);
        loginButton = (Button)findViewById(R.id.login_login_button);

        registerButton.setOnClickListener(listener);
        loginButton.setOnClickListener(listener);


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

                case R.id.login_register_button :
                    //register ��ư�� ���

                    Intent it = new Intent(AdminLogin.this, AdminRegister.class);
                    startActivity(it);
                    break;

                case R.id.login_login_button :
                    //login ��ư�� ���

                    if(idInputText.getText().toString().equals("")){
                        //ID �Է��� ����� ��
                        Toast.makeText(getApplicationContext(), "enter id.", Toast.LENGTH_SHORT).show();
                    }
                    else if(pwInputText.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "enter password.", Toast.LENGTH_SHORT).show();
                    }

                    if(!idInputText.getText().toString().equals("") && !pwInputText.getText().toString().equals("")) {
                        //�Ѵ� ���� �ʾ�����.

                        String convertedPW = null;
                        try {
                            convertedPW = SHA1(pwInputText.getText().toString());

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        JSONObject sending = new JSONObject();

                        try {
                            sending.put("messagetype", "adminlogincheck");
                            sending.put("adminid", idInputText.getText().toString());
                            sending.put("adminpw", convertedPW);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        new RestaurantListAsync(AdminLogin.this, StaticVariable.getConnectUrl(), mHandler, sending, ADMIN_ID_PW_CHECK, 0);

                    }
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
