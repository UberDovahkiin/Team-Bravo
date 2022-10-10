package com.example.myapplication;

import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class Timerlogiikka extends MainActivity{
    /**
     * Luokka sisältää kellon toiminnot.
     * @author Niko Heilimo
     * @version
     */
    Double time = 0.0;
    Timer timer;
    TimerTask timerTask;

    public Timerlogiikka(double time) {
        this.time = time;
        this.timer = timer;
        this.timerTask = timerTask;
    }
    public Timerlogiikka() {
        this.time = time;
        this.timer = timer;
        this.timerTask = timerTask;
    }

    /**
     *
     * @param textView annettu textview jossa aika juoksee.
     * @param timer annettu timer instance.
     * @return palauttaa annetun kentän johon timer lisää numerot.
     */
    public TextView aloitaTimer(TextView textView, Timer timer) {

        timerTask = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        time++;
                        textView.setText(pyoristaLuvut());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
        return textView;
    }

    /**
     * Pysäyttää timerin
     */
    public void lopetaTimer() {
        timerTask.cancel();
    }

    /**
     * Muuntaa ajan oikein
     */
    public String pyoristaLuvut() {
        int rounded = (int) Math.round(time);
        int tunnit = ((rounded % 86400) / 3600);
        int minuutit = ((rounded % 86400) % 3600)/ 60;
        int sekunnit = ((rounded % 86400) % 3600) % 60;
        return formatTime(sekunnit,minuutit,tunnit);
    }
    public String formatTime(int sekunnit, int minuutit, int tunnit) {
        return String.format("%02d",tunnit) + ":" + String.format("%02d",minuutit) + ":" + String.format("%02d",sekunnit);
    }

    /**
     *
     * @return palauttaa nykyisen ajan.
     */
    public double nykyinenAika() {

        return time;
    }

}
