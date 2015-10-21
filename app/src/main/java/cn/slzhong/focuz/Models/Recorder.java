package cn.slzhong.focuz.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        System.out.println("******a" + attentions);
        System.out.println("******m" + meditations);
        System.out.println("******t" + (endAt - startAt));
        System.out.println("******r" + rate);
        System.out.println("******avgA" + attention);
        System.out.println("******avgM" + meditation);
    }

}
