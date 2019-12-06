package com.example.company.appstore.Owner;

public class CicilanEntity {

    private String tanggal;

    private String nominal;

    public CicilanEntity(String tanggal, String nominal) {
        this.tanggal = tanggal;
        this.nominal = nominal;
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
}
