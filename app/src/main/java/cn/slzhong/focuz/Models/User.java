package cn.slzhong.focuz.Models;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.slzhong.focuz.Utils.HttpUtil;

/**
 * Created by SherlockZhong on 10/12/15.
 */
public class User {

    // singleton
    public static volatile User user;

    public static User getInstance() {
        if (user == null) {
            synchronized (User.class) {
                if (user == null) {
                    user = new User();
                }
            }
        }
        return user;
    }

    public JSONObject signInAndOut(String account, String password, String url) {
        NameValuePair accountPair = new BasicNameValuePair("account", account);
        NameValuePair passwordPair = new BasicNameValuePair("password", password);
        List<NameValuePair> params = new ArrayList<>();
        params.add(accountPair);
        params.add(passwordPair);

        JSONObject jsonObject = HttpUtil.post(url, params);
        return jsonObject;
    }

}
