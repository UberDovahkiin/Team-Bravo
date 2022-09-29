package com.example.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Suoritukset extends MainActivity{
    private static final Suoritukset ourInstance = new Suoritukset();
    private ArrayList<Suoritus> suoritukset;
    public static Suoritukset getInstance() {

        return ourInstance;
    }
    private Suoritukset() {
        suoritukset = new ArrayList<>();


    }
}
