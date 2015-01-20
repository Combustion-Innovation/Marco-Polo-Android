package com.ci.hub.myapplication;

/**
 * Created by Alex on 1/19/15.
 */
public class Validator {
    public static final int OK = 0;
    public static final int NULL = 1;
    public static final int EMPTY = 2;
    public static final int BAD_NUMBER = 3;

    public static int validateUsername(String username) {
        if (username == null) return NULL;
        if (username.length() == 0) return EMPTY;
        return OK;
    }

    public static int validatePhoneNumber(String number) {
        if (number == null) return NULL;
        if (number.length() == 0) return EMPTY;
        for (int i = 0; i < number.length(); i++) {
            Character c = number.charAt(i);
            if (!Character.isDigit(c)
                    && c.compareTo('(') != 0
                    && c.compareTo(')') != 0
                    && c.compareTo(' ') != 0
                    && c.compareTo('-') != 0) {
                return BAD_NUMBER;
            }
        }
        return OK;
    }

    public static int validatePassword(String password) {
        if (password == null) return NULL;
        if (password.length() == 0) return EMPTY;
        return OK;
    }
}
