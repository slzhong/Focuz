package cn.slzhong.focuz.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.slzhong.focuz.Utils.StorageUtil;

/**
 * Created by SherlockZhong on 10/21/15.
 */
public class Recorder {

    public long startAt;
    public long endAt;

    public List<Integer> attentions;
    public List<Integer> meditations;

    public long attention;
    public long meditation;
    public int rate;

    public Recorder() {
        attentions = new ArrayList<>();
        meditations = new ArrayList<>();
        startAt = new Date().getTime();
    }

    public Recorder(JSONObject jsonObject) {
        try {
            startAt = jsonObject.getLong("startAt");
            endAt = jsonObject.getLong("endAt");
            rate = jsonObject.getInt("rate");
            attention = jsonObject.getLong("attention");
            meditation = jsonObject.getLong("meditation");

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

            StorageUtil.saveStringAsFile(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
