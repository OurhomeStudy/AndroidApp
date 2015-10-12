package Items;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyStore;

/**
 * Created by Dong_Gyo on 2015. 9. 10..
 */
public class RestaurantListAsync extends AsyncTask <Void, Void, String> {

    private Handler mhandler;
    int DataContent;
    int handlernum = 1;

    String responseString;
    String _url;
    Context mContext;
    JSONObject _jobj;


    public RestaurantListAsync(Context context, String urls, Handler handler,
                               JSONObject jobj, int hnum, int Data) {

        mhandler = handler;
        mContext = context;
        _url = urls;

        handlernum = hnum;
        DataContent = Data;
        _jobj = jobj;

        super.execute();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... urls) {

        return Task(_url,_jobj);
    }

    @Override
    protected void onPostExecute(String responseData) {

        Message msg = mhandler.obtainMessage();
        msg.what = handlernum;
        msg.obj = responseString;
        msg.arg1 = DataContent;
        mhandler.sendMessage(msg);

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

    public String Task(String urlString, JSONObject jobj) {

        HttpClient httpClient = getHttpClient();


        try {

            if(jobj.get("messagetype").toString().equals("review_send")){


                String messagetype = jobj.get("messagetype").toString();
                String review_writer_id = jobj.get("review_writer_id").toString();
                String review_writer_name = jobj.get("review_writer_name").toString();
                int review_shop_id = jobj.getInt("review_shop_id");
                String review_shop_name = jobj.get("review_shop_name").toString();
                String review_content = jobj.get("review_content").toString();
                String review_registered_date = jobj.get("review_registered_date").toString();

                URI _url = new URI(urlString);
                HttpPost httpPost = new HttpPost(_url+"reviewsend");

                MultipartEntity entity = new MultipartEntity();
                entity.addPart("messagetype", new StringBody(messagetype, Charset.forName("UTF-8")));
                entity.addPart("review_writer_id", new StringBody(review_writer_id, Charset.forName("UTF-8")));
                entity.addPart("review_writer_name", new StringBody(review_writer_name, Charset.forName("UTF-8")));
                entity.addPart("review_shop_id", new StringBody( String.valueOf(review_shop_id), Charset.forName("UTF-8")));
                entity.addPart("review_shop_name", new StringBody(review_shop_name, Charset.forName("UTF-8")));
                entity.addPart("review_content", new StringBody(review_content, Charset.forName("UTF-8")));
                entity.addPart("review_registered_date", new StringBody(review_registered_date, Charset.forName("UTF-8")));

                JSONArray pathStrArr = (JSONArray) jobj.get("pathArr");
                JSONObject temp = (JSONObject)pathStrArr.get(0);


                if( pathStrArr.length() == 1  && temp.get("pathStr").toString().equals("")){
                    // if picture is not selected

                }
                else{


                    JSONArray tempPictureNameArr = new JSONArray();
                    for(int i = 0 ; i < pathStrArr.length(); i++){
                        JSONObject tempJSONObject = (JSONObject) pathStrArr.get(i);
                        JSONObject tempJSONNAMEvalue = new JSONObject();

                        String tempvalue = tempJSONObject.get("pictureName").toString();
                        tempJSONNAMEvalue.put("pictureName", tempvalue);
                        tempPictureNameArr.put(tempJSONNAMEvalue);


                    }
                    entity.addPart("file_name", new StringBody(tempPictureNameArr.toString(), Charset.forName("UTF-8")));
                    //filename to JONSArr


                    for(int i = 0 ; i< pathStrArr.length(); i++){

                        JSONObject tempJSONObject = (JSONObject) pathStrArr.get(i);
                        JSONObject tempJSONNAMEvalue = new JSONObject();

                        String tempvalue = tempJSONObject.get("pictureName").toString();
                        tempJSONNAMEvalue.put("pictureName", tempvalue);

                        String pathStr = tempJSONObject.getString("pathStr").toString();
                        File file = new File(pathStr);
                        entity.addPart(tempvalue, new FileBody(file));

                    }


                }


                httpPost.setEntity(entity);

                httpPost.setHeader("enctype", "multipart/form-data");
                HttpResponse response = httpClient.execute(httpPost);

                responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                //System.out.println(responseString);
            }
            else{

                try {

                    URI _url = new URI(urlString);

                    HttpPost httpPost = new HttpPost(_url);

                    String json = "";
                    json = jobj.toString();

                    String encodedJSON = Base64.encodeToString(jobj.toString().getBytes(), 0);
                    // json to base64 encoding


                    StringEntity se = new StringEntity(encodedJSON, "UTF-8");
                    httpPost.setEntity(se);

                    System.out.println("send : " + jobj.toString());
                    //System.out.println("encoded : " + encodedJSON);

                    se.setContentType("application/json");

                    HttpResponse response = httpClient.execute(httpPost);
                    responseString =EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                    //System.out.println(responseString);
                }
                catch(URISyntaxException e) {
                    System.out.println("1");
                    e.printStackTrace();
                }
                catch (ClientProtocolException e) {
                    System.out.println("2");
                    e.printStackTrace();
                }
                catch (IOException e) {
                    System.out.println("3");
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}