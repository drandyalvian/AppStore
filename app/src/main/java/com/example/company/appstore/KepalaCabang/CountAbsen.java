package com.example.company.appstore.KepalaCabang;

public class CountAbsen {
    private String nama, key;

    public CountAbsen(String nama, String key) {
        this.nama = nama;
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
