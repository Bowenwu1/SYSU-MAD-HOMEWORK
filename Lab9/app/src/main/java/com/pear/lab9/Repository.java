package com.pear.lab9;

/**
 * Created by bowenwu on 2017/12/14.
 */

public class Repository {
    private static final int MAX_LENGTH_FOR_SHORT_INTRODUCTION = 15;
    private static final String LEAVE_OUT = "...";
    public String name;
    public String programmingLanguage;

    // short introduction
    private String shortIntroduction;
    // origin introduction
    private String introduction;

    public Repository(String name, String programmingLanguage, String introduction) {
        this.name = name;
        this.programmingLanguage = programmingLanguage;
        this.setIntroduction(introduction);
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
        if (introduction.length() > MAX_LENGTH_FOR_SHORT_INTRODUCTION) {
            shortIntroduction = introduction.substring(0, MAX_LENGTH_FOR_SHORT_INTRODUCTION - 1) + LEAVE_OUT;
        } else {
            shortIntroduction = introduction;
        }
    }

    public String getShortIntroduction() {
        return shortIntroduction;
    }

    public String getOriginIntroduction() {
        return introduction;
    }
}
