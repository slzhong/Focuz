package cn.slzhong.focuz.Models;

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


}
