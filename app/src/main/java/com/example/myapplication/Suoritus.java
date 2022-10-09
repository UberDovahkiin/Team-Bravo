package com.example.myapplication;

public class Suoritus {

    private String  aika;
    private String  askelMaara;
    private String matka;

    public Suoritus(String aika, String askelMaara, String matka) {
        this.aika = aika;
        this.askelMaara = askelMaara;
        this.matka = matka;
    }

    public String getAika() {
        return aika;
    }

    public String getAskelMaara() {
        return askelMaara;
    }

    public String getMatka() {
        return matka;
    }

    @Override
    public String toString() {
        return "Aika: " + aika +" Askelmäärä: " + askelMaara + " Matka: " + matka + "Km";
    }
}
