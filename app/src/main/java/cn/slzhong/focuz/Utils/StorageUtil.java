package cn.slzhong.focuz.Utils;

import android.app.Activity;
import android.content.SharedPreferences;

import cn.slzhong.focuz.Constants.STRINGS;

/**
 * Created by SherlockZhong on 10/15/15.
 */
public class StorageUtil {

    public static boolean saveSharedPreference(Activity activity, String key, String value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(STRINGS.APP_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getSharedPreference(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(STRINGS.APP_NAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

}
