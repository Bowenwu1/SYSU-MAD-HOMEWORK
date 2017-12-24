package com.pear.lab9;

/**
 * Created by bowenwu on 2017/12/14.
 */

public class Repository {
    public String name;
    public String language;
    public String description;

    public Repository(String name, String programmingLanguage, String introduction) {
        this.name = name;
        this.language = programmingLanguage;
        this.description = introduction;
    }
}
