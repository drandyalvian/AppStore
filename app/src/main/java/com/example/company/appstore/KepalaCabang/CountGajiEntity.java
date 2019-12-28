package com.example.company.appstore.KepalaCabang;


public class CountGajiEntity {

    private String tanggal, key;

    public CountGajiEntity(String tanggal, String key) {
        this.tanggal = tanggal;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
