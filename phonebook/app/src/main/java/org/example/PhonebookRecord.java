package app.src.main.java.org.example;

import java.util.TreeMap;


public class PhonebookRecord {
    private final int _id;
    private String _name;
    private String _phone;
    private long _lastModified;

    public PhonebookRecord(int id, String name, String phone) {
        _id = id;
        _name = name;
        _phone = phone;
        _lastModified = System.currentTimeMillis();
    }

    public int getID() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getPhone() {
        return _phone;
    }

    public Long getLastModified() {
        return _lastModified;
    }

    public String setPhone(String phone) {
        String old = _phone;
        _phone = phone;
        _lastModified = System.currentTimeMillis();
        return old;
    }

    public String ToString() {
        return _name + "\t" + _phone;
    }


}