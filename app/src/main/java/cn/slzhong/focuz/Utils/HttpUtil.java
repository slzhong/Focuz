package cn.slzhong.focuz.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by SherlockZhong on 10/13/15.
 */
public class HttpUtil {

    public static JSONObject get(String url) {
        return null;
    }

    public static JSONObject post(String url, List<NameValuePair> params) {
        try {
            HttpEntity postEntity = new UrlEncodedFormEntity(params);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(postEntity);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            InputStream inputStream = responseEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String result = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line;

            }
            JSONTokener jsonTokener = new JSONTokener(result);
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
