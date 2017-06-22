package com.app.proxy.Beans;

/**
 * Created by dhruv.suri on 22/06/17.
 */
public class Profile {
    private String name;
    private String number;

    public Profile() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
