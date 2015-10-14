package com.example.dong_gyo.project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import Items.sslManager;

public class CustomerLogin extends Activity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessToken accesstoken;
    AccessTokenTracker accessTokenTracker;

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
                                Log.d("user 정보 : ", response.getJSONObject().toString());
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

    class LoginAsync extends AsyncTask<Void, Void, Void> {

        JSONObject logininfo;

        public LoginAsync(JSONObject rInfo){
            logininfo = rInfo;
        }

        @Override
        public void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HttpClient httpClient = getHttpClient();

            return null;
        }

        protected void onPostExecute(Void result) {
            /*
            sock.closeSocket();

            Intent LocalSearchActivity = new Intent(MainActivity.this,
                    LocalSearchActivity.class);

            LocalSearchActivity.putExtra("localGu", localGu);
            LocalSearchActivity.putExtra("localDong", localDong);

            startActivity(LocalSearchActivity);*/

            super.onPostExecute(result);
        }

        private HttpClient getHttpClient() {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new sslManager(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 15443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                return new DefaultHttpClient();
            }
        }
    }
}