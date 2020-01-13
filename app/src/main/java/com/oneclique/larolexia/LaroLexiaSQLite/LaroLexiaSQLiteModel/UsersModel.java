package com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel;

import java.io.Serializable;

public class UsersModel implements Serializable {
    private String a_id;
    private String a_username;
    private String a_character;
    private String a_last_used;
    private String a_stars;
    private String a_new_user;

    public String getA_new_user() {
        return a_new_user;
    }

    public void setA_new_user(String a_new_user) {
        this.a_new_user = a_new_user;
    }

    public String getA_stars() {
        return a_stars;
    }

    public void setA_stars(String a_stars) {
        this.a_stars = a_stars;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getA_username() {
        return a_username;
    }

    public void setA_username(String a_username) {
        this.a_username = a_username;
    }

    public String getA_character() {
        return a_character;
    }

    public void setA_character(String a_character) {
        this.a_character = a_character;
    }

    public String getA_last_used() {
        return a_last_used;
    }

    public void setA_last_used(String a_last_used) {
        this.a_last_used = a_last_used;
    }
}
