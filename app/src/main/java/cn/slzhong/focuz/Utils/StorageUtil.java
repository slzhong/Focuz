package cn.slzhong.focuz.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Date;

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

    public static void saveStringAsFile(String content) {
        String path = Environment.getExternalStorageDirectory() + "/FOCUZ/";
        File dir = new File(path);
        dir.mkdir();
        try {
            File file = new File(path + (new Date().getTime()));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readStringFromFile(String path) {
        try {
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            return bufferedReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File[] listFiles() {
        String path = Environment.getExternalStorageDirectory() + "/FOCUZ/";
        File dir = new File(path);
        if (dir.exists()) {
            return dir.listFiles();
        } else {
            return null;
        }
    }

}
