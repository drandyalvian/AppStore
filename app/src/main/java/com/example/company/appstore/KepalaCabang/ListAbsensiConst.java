package com.example.company.appstore.KepalaCabang;

import java.io.Serializable;

public class ListAbsensiConst implements Serializable {
    String keterangan, tanggal, key, filter;

    public ListAbsensiConst(){

    }

    public ListAbsensiConst(String keterangan, String tanggal, String key, String filter) {
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.key = key;
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
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
}
