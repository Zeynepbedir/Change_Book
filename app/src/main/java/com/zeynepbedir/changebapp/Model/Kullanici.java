package com.zeynepbedir.changebapp.Model;

public class Kullanici {

    private String id;
    private String kullanniciadi;
    private String ad;
    private String resimurl;
    private String bio;

    public Kullanici() {
    }

    public Kullanici(String id, String kullanniciadi, String ad, String resimurl, String bio) {
        this.id = id;
        this.kullanniciadi = kullanniciadi;
        this.ad = ad;
        this.resimurl = resimurl;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullanniciadi() {
        return kullanniciadi;
    }

    public void setKullanniciadi(String kullanniciadi) {
        this.kullanniciadi = kullanniciadi;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
