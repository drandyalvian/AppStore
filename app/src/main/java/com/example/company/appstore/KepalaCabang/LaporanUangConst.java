package com.example.company.appstore.KepalaCabang;

import java.io.Serializable;

public class LaporanUangConst implements Serializable {

    String tanggal, nominal, key;
    public LaporanUangConst(){

    }

    public LaporanUangConst(String tanggal, String nominal, String key) {
        this.tanggal = tanggal;
        this.nominal = nominal;
        this.key = key;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
