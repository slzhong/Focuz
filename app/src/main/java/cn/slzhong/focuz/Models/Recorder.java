package cn.slzhong.focuz.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SherlockZhong on 10/21/15.
 */
public class Recorder {

    private List<Integer> attentions;
    private List<Integer> meditations;

    private long startAt;
    private long endAt;

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

    public void stop() {
        endAt = new Date().getTime();
    }

    public void save() {
        System.out.println("******a" + attentions);
        System.out.println("******m" + meditations);
        System.out.println("******t" + (endAt - startAt));
    }

}
