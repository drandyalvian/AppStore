package com.example.company.appstore.KepalaCabang;

public class CountAbsen {
    private String nama, key, keterangan;

    public CountAbsen(String nama, String key, String keterangan) {
        this.nama = nama;
        this.key = key;
        this.keterangan = keterangan;
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

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
