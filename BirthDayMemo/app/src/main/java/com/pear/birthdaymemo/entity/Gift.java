package com.pear.birthdaymemo.entity;

/**
 * Created by bowenwu on 2017/12/5.
 */

public class Gift {
    public int id;
    public String name;
    public String birthday;
    public String gift;

    public Gift() {}
    public Gift(String name, String birthday, String gift) {
        this.name = name;
        this.birthday = birthday;
        this.gift = gift;
    }
}
