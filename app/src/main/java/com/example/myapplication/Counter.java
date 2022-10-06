package com.example.myapplication;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Luokka sisältää kellon toiminnot, osa on mainActivityssä.
 * @author Niko Heilimo
 * @version
 */

public class Counter {

    TextView textViewTimer;
    long startTime, timeInMilliseconds = 0;
    Handler customHandler = new Handler();

    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            textViewTimer.setText(getDateFromMillis(timeInMilliseconds));
            customHandler.postDelayed(this, 1000);
        }
    };
}