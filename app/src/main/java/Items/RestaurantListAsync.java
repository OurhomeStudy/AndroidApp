package Items;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
            URI _url = new URI(urlString);

            HttpPost httpPost = new HttpPost(_url);
            /*
            String encodedJSON = Base64.encodeToString(jobj.toString().getBytes(), 0);
            StringEntity entity = new StringEntity(encodedJSON, "UTF-8");
            */
            String json = "";
            json = jobj.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            System.out.println("send : " + jobj.toString());
            //System.out.println("encoded : " + encodedJSON);

            se.setContentType("application/json");

            //httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            System.out.println(responseString);

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

        return null;
    }

}
