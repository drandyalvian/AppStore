package com.example.company.appstore.KepalaCabang;


public class CountGajiEntity {

    private String tanggal, key, keterangan;

    public CountGajiEntity(String tanggal, String key, String keterangan) {
        this.tanggal = tanggal;
        this.key = key;
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
