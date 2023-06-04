package com.zeynepbedir.changebapp.Model;

public class Gonderi {

    //burada gönderi verilerini çekmek için kod bloklarımı yazacağım

    private String gonderen;
    private String gonderiAdi;
    private String gonderiFiyati;
    private String gonderiHakkinda;
    private String gonderiId;
    private String gonderiResmi;

    public Gonderi() {
    }

    public Gonderi(String gonderen, String gonderiAdi, String gonderiFiyati,
                   String gonderiHakkinda, String gonderiId, String gonderiResmi) {
        this.gonderen = gonderen;
        this.gonderiAdi = gonderiAdi;
        this.gonderiFiyati = gonderiFiyati;
        this.gonderiHakkinda = gonderiHakkinda;
        this.gonderiId = gonderiId;
        this.gonderiResmi = gonderiResmi;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getGonderiAdi() {
        return gonderiAdi;
    }

    public void setGonderiAdi(String gonderiAdi) {
        this.gonderiAdi = gonderiAdi;
    }

    public String getGonderiFiyati() {
        return gonderiFiyati;
    }

    public void setGonderiFiyati(String gonderiFiyati) {
        this.gonderiFiyati = gonderiFiyati;
    }

    public String getGonderiHakkinda() {
        return gonderiHakkinda;
    }

    public void setGonderiHakkinda(String gonderiHakkinda) {
        this.gonderiHakkinda = gonderiHakkinda;
    }

    public String getGonderiId() {
        return gonderiId;
    }

    public void setGonderiId(String gonderiId) {
        this.gonderiId = gonderiId;
    }

    public String getGonderiResmi() {
        return gonderiResmi;
    }

    public void setGonderiResmi(String gonderiResmi) {
        this.gonderiResmi = gonderiResmi;
    }
}
