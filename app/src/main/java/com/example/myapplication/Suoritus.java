package com.example.myapplication;

public class Suoritus {

    private boolean aika;
    private boolean askelMaara;

    public Suoritus(boolean aika, boolean askelMaara) {
        this.aika = aika;
        this.askelMaara = askelMaara;
    }

    public String getSuoritus() {
        return this.askelMaara + " " + this.aika;
    }
}
