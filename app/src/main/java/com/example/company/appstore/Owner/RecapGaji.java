package com.example.company.appstore.Owner;

public class RecapGaji {

    String key,gaji_pokok;

    public RecapGaji(String key, String gaji_pokok) {
        this.key = key;
        this.gaji_pokok = gaji_pokok;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGaji_pokok() {
        return gaji_pokok;
    }

    public void setGaji_pokok(String gaji_pokok) {
        this.gaji_pokok = gaji_pokok;
    }
}
