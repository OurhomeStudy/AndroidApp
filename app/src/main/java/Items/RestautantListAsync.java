package Items;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Dong_Gyo on 2015. 9. 10..
 */
public class RestautantListAsync extends AsyncTask <Void, Void, String> {

    Context mContext;
    JSONObject _jobj;
    String result = "";
    JSONObject receivedJSON;
    JSONArray receivedJSONArray;

    public RestautantListAsync(Context context, JSONObject jobj) {

        mContext = context;

        _jobj = jobj;

        super.execute();
    }

    @Override
    public void onPreExecute() {
        // Log.i("Test", "onPreExecute Called on global");
    }

    @Override
    protected String doInBackground(Void... urls) {

        Task();
        return  null;
    }

    @Override
    protected void onPostExecute(String responseData) {

        //editText.setText(result);

        try {
            receivedJSONArray = new JSONArray(result);

            receivedJSON = receivedJSONArray.getJSONObject(0);

            //editText.setText(receivedJSON.getString("shop_address_lotnum"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void Task() {

        HttpClient httpClient = new DefaultHttpClient();

        try {
            URI _url = null;

            _url = new URI("http://183.96.25.221:1338");

            HttpPost httpPost = new HttpPost(_url);
            String json = "";
            json = _jobj.toString();
            StringEntity se = new StringEntity(json, "UTF-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpPost);


            BufferedReader bufReader =  new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(),"utf-8" )
            );
            String line = null;

            while ((line = bufReader.readLine())!=null){
                result +=line;
            }

        }
        catch(URISyntaxException e) {
            System.out.println("1");
            e.printStackTrace();
        }
        catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.out.println("2");
            e.printStackTrace();
        }

        catch (IOException e) {
            System.out.println("3");
            e.printStackTrace();
        }
        return;
    }
}