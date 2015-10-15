package cn.slzhong.focuz.Models;

import android.app.Activity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.slzhong.focuz.Constants.CODES;
import cn.slzhong.focuz.Utils.HttpUtil;
import cn.slzhong.focuz.Utils.StorageUtil;

/**
 * Created by SherlockZhong on 10/12/15.
 */
public class User {

    // singleton
    public static volatile User user;
    public boolean hasSignedIn;

    // real object body
    private Activity activity;

    public User(Activity a) {
        activity = a;

        hasSignedIn = StorageUtil.getSharedPreference(activity, "userId") != null;
    }

    public static User getInstance(Activity activity) {
        if (user == null) {
            synchronized (User.class) {
                if (user == null) {
                    user = new User(activity);
                }
            }
        }
        return user;
    }

    public JSONObject signInAndUp(String account, String password, String url) {
        NameValuePair accountPair = new BasicNameValuePair("account", account);
        NameValuePair passwordPair = new BasicNameValuePair("password", password);
        List<NameValuePair> params = new ArrayList<>();
        params.add(accountPair);
        params.add(passwordPair);

        JSONObject jsonObject = HttpUtil.post(url, params);

        try {
            if (jsonObject.getInt("code") == CODES.SUCCESS) {
                JSONObject user = (JSONObject) jsonObject.get("data");
                String userId = user.getString("_id");
                String userAccount = user.getString("account");
                String userPassword = user.getString("password");
                StorageUtil.saveSharedPreference(activity, "userId", userId);
                StorageUtil.saveSharedPreference(activity, "userAccount", userAccount);
                StorageUtil.saveSharedPreference(activity, "userPassword", userPassword);
                hasSignedIn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
