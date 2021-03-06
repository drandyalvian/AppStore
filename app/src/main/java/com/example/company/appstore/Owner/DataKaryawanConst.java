package com.example.company.appstore.Owner;


import java.io.Serializable;

public class DataKaryawanConst implements Serializable {

    String nama;
    String posisi;
    String alamat;
    String gender;
    String nama_cabang;
    String umur;
    String telepon;
    String key;
    String key_name;
    String cabang;

    public DataKaryawanConst () {

    }

    public DataKaryawanConst(String nama, String posisi, String alamat, String gender,
                             String nama_cabang, String umur, String telepon, String key, String cabang, String key_name) {
        this.nama = nama;
        this.posisi = posisi;
        this.alamat = alamat;
        this.gender = gender;
        this.nama_cabang = nama_cabang;
        this.umur = umur;
        this.telepon = telepon;
        this.key = key;
        this.cabang = cabang;
        this.key_name = key_name;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNama_cabang() {
        return nama_cabang;
    }

    public void setNama_cabang(String nama_cabang) {
        this.nama_cabang = nama_cabang;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }
}
