package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Suoritukset extends MainActivity {
    private static final Suoritukset ourInstance = new Suoritukset();
    private ArrayList<Suoritus> suoritukset;

    public static Suoritukset getInstance() {

        return ourInstance;
    }

    private Suoritukset() {
        suoritukset = new ArrayList<>();

    }

    public Suoritus getSuoritus(int i) {
        return suoritukset.get(i);
    }

    public List<Suoritus> getSuoritukset() {
        return suoritukset;
    }
}
