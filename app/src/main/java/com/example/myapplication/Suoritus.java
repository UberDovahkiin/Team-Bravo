package com.example.myapplication;

public class Suoritus {

    private String  aika;
    private String  askelMaara;
    private String matka;
    private String paiva;

    public Suoritus(String aika, String askelMaara, String matka, String paiva) {
        this.aika = aika;
        this.askelMaara = askelMaara;
        this.matka = matka;
        this.paiva = paiva;
    }

    @Override
    public String toString() {
        return paiva + "\n" + "Aika: " + aika +" Askelmäärä: " + askelMaara + " Matka: " + matka + "Km";
    }
}
