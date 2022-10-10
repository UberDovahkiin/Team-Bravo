package com.example.myapplication;

/**
 * Suoritus class
 * @author Jani Pudas
 */
public class Suoritus {

    private final String  aika;
    private final String  askelMaara;
    private final String matka;
    private final String paiva;

    public Suoritus(String aika, String askelMaara, String matka, String paiva) {
        this.aika = aika;
        this.askelMaara = askelMaara;
        this.matka = matka;
        this.paiva = paiva;
    }

    @Override
    public String toString() {
        return paiva + "\n" +"Aika: " + aika +" Askelmäärä: " + askelMaara + " Matka: " + matka + "Km";
    }
}
