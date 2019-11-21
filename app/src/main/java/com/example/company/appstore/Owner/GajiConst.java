package com.example.company.appstore.Owner;

import java.io.Serializable;

public class GajiConst implements Serializable {

    String nama,posisi,alamat,gender,nama_cabang, cabang, umur, telepon, key, gaji_lembur,
            gaji_pokok, kompensasi, pinjaman, uang_makan, tgl_gaji;

    public GajiConst() {

    }

    public GajiConst(String nama, String posisi, String alamat, String gender, String nama_cabang, String cabang, String umur, String telepon, String key, String gaji_lembur, String gaji_pokok, String kompensasi, String pinjaman, String uang_makan, String tgl_gaji) {
        this.nama = nama;
        this.posisi = posisi;
        this.alamat = alamat;
        this.gender = gender;
        this.nama_cabang = nama_cabang;
        this.cabang = cabang;
        this.umur = umur;
        this.telepon = telepon;
        this.key = key;
        this.gaji_lembur = gaji_lembur;
        this.gaji_pokok = gaji_pokok;
        this.kompensasi = kompensasi;
        this.pinjaman = pinjaman;
        this.uang_makan = uang_makan;
        this.tgl_gaji = tgl_gaji;
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

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
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

    public String getGaji_lembur() {
        return gaji_lembur;
    }

    public void setGaji_lembur(String gaji_lembur) {
        this.gaji_lembur = gaji_lembur;
    }

    public String getGaji_pokok() {
        return gaji_pokok;
    }

    public void setGaji_pokok(String gaji_pokok) {
        this.gaji_pokok = gaji_pokok;
    }

    public String getKompensasi() {
        return kompensasi;
    }

    public void setKompensasi(String kompensasi) {
        this.kompensasi = kompensasi;
    }

    public String getPinjaman() {
        return pinjaman;
    }

    public void setPinjaman(String pinjaman) {
        this.pinjaman = pinjaman;
    }

    public String getUang_makan() {
        return uang_makan;
    }

    public void setUang_makan(String uang_makan) {
        this.uang_makan = uang_makan;
    }

    public String getTgl_gaji() {
        return tgl_gaji;
    }

    public void setTgl_gaji(String tgl_gaji) {
        this.tgl_gaji = tgl_gaji;
    }
}

