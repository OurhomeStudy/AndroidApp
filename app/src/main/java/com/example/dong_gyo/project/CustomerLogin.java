package com.example.dong_gyo.project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import Items.RestaurantListAsync;
import Items.StaticVariable;

public class CustomerLogin extends Activity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessToken accesstoken;
    AccessTokenTracker accessTokenTracker;
    JSONObject fb_userinfo;

    private int SEND_FB_USERINFO = 1;

    protected Handler mHandler =  new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                System.out.println("handler error");
            } else if (msg.what == SEND_FB_USERINFO) {
                try {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("send_fb_userinfo")) {

                        if(jobj.get("result").equals("SEND_FB_USERINFO_ERROR")) {
                            Toast.makeText(getApplicationContext(), "SEND_FB_USERINFO_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("SEND_FB_USERINFO_FAIL")) {
                            Toast.makeText(getApplicationContext(), "SEND_FB_USERINFO_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("SEND_FB_USERINFO_SUCCESS")) {

                            Toast.makeText(getApplicationContext(), "SEND_FB_USERINFO_SUCCESS", Toast.LENGTH_SHORT).show();

                            //니가 하고 싶은거

                            //intent

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

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.customer_login);

        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.song.mappal",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //login버튼 클릭시 action을 위한 콜백함수 등록
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Log.d("페북아이디", loginResult.getAccessToken().getUserId());   //UserID 출력

                GraphRequest request =  new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "me",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {

                                JSONObject sending = new JSONObject();
                                fb_userinfo = response.getJSONObject();

                                try {
                                    sending.put("messagetype", "send_fb_userinfo");
                                    sending.put("content", fb_userinfo.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                new RestaurantListAsync(CustomerLogin.this, StaticVariable.getConnectUrl(), mHandler, sending, SEND_FB_USERINFO, 0);
                                /*if (response != null) {
                                    try {
                                        JSONObject data = response.getJSONObject();
                                        if (data.has("picture")) {
                                            String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                            //Log.d("profileUrl : ",profilePicUrl);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }*/
                            }
                        }
                );
                /*
                        GraphRequest.newMeRequest(       //User info 가져오기
                        loginResult.getAccessToken(),

                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                //Log.d("CustomerLogin", response.toString());
                                Log.d("JsonObject형", response.getJSONObject().toString());
                            }
                        });
                */
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

                //Log.d("Token",loginResult.getAccessToken().getToken().toString());
                //Log.d("Token",loginResult.getAccessToken().getSource().toString());

                /*
                Intent intent_testActivity;
                intent_testActivity = new Intent(CustomerLogin.this,
                        testActivity.class);
                intent_testActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);  //액티비티 스택제거
                intent_testActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_testActivity);*/
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        //Token 관련

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                //Log.d("페북아이디",accesstoken.getUserId());
            }
        };
        // If the access token is available already assign it.

        accesstoken = AccessToken.getCurrentAccessToken();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cutomer_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}