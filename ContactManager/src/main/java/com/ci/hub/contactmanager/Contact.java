package com.ci.hub.contactmanager;

/**
 * Created by Alex on 2/20/15.
 */
public class Contact {
    private String name;
    private String phone;

    public Contact(String name, String phone) {
        setName(name);
        setPhone(phone);
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
