package cn.slzhong.focuz.Models;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.slzhong.focuz.Constants.URLS;
import cn.slzhong.focuz.Utils.HttpUtil;
import cn.slzhong.focuz.Utils.StorageUtil;

/**
 * Created by SherlockZhong on 10/21/15.
 */
public class Recorder {

    public String userId;

    public long startAt;
    public long endAt;

    public List<Integer> attentions;
    public List<Integer> meditations;

    public long attention;
    public long meditation;
    public int rate;

    public Recorder(String id) {
        attentions = new ArrayList<>();
        meditations = new ArrayList<>();
        startAt = new Date().getTime();
        userId = id;
    }

    public Recorder(JSONObject jsonObject) {
        try {
            startAt = jsonObject.getLong("startAt");
            endAt = jsonObject.getLong("endAt");
            rate = jsonObject.getInt("rate");
            attention = jsonObject.getLong("attention");
            meditation = jsonObject.getLong("meditation");
            userId = jsonObject.has("userId") ? jsonObject.getString("userId") : "";

            JSONArray jsonAttentions = jsonObject.getJSONArray("attentions");
            attentions = new ArrayList<>();
            for (int i = 0; i < jsonAttentions.length(); i++) {
                attentions.add(jsonAttentions.getInt(i));
            }

            JSONArray jsonMeditations = jsonObject.getJSONArray("meditations");
            meditations = new ArrayList<>();
            for (int i = 0; i < jsonMeditations.length(); i++) {
                meditations.add(jsonMeditations.getInt(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pushAttention(int num) {
        attentions.add(num);
    }

    public void pushMeditation(int num) {
        meditations.add(num);
    }

    public void setRate(int num) {
        rate = num;
    }

    public void stop() {
        endAt = new Date().getTime();
    }

    public void save() {
        int sumAttention = 0;
        for (int i = 0; i < attentions.size(); i++) {
            sumAttention += attentions.get(i);
        }
        attention = Math.round(1.0 * sumAttention / attentions.size());

        int sumMeditation = 0;
        for (int i = 0; i < meditations.size(); i++) {
            sumMeditation += meditations.get(i);
        }
        meditation = Math.round(1.0 * sumMeditation / meditations.size());

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("startAt", startAt);
            jsonObject.put("endAt", endAt);
            jsonObject.put("rate", rate);
            jsonObject.put("attention", attention);
            jsonObject.put("meditation", meditation);

            JSONArray attentionsArray = new JSONArray();
            for (int i = 0; i < attentions.size(); i++) {
                attentionsArray.put(attentions.get(i));
            }
            jsonObject.put("attentions", attentionsArray);

            JSONArray meditationsArray = new JSONArray();
            for (int i = 0; i < meditations.size(); i++) {
                meditationsArray.put(meditations.get(i));
            }
            jsonObject.put("meditations", meditationsArray);

            String tag = "" + (new Date().getTime());
            StorageUtil.saveStringAsFile(tag, jsonObject.toString());
            saveToServer(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToServer(final String tag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> params = new LinkedList<>();
                params.add(new BasicNameValuePair("tag", tag));
                params.add(new BasicNameValuePair("userId", userId));
                params.add(new BasicNameValuePair("startAt", "" + startAt));
                params.add(new BasicNameValuePair("endAt", "" + startAt));
                params.add(new BasicNameValuePair("rate", "" + startAt));
                params.add(new BasicNameValuePair("attention", "" + startAt));
                params.add(new BasicNameValuePair("meditation", "" + startAt));
                params.add(new BasicNameValuePair("attentions", attentions.toString()));
                params.add(new BasicNameValuePair("meditations", meditations.toString()));
                HttpUtil.post(URLS.SAVE, params);
            }
        }).start();
    }

}
