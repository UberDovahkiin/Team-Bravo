package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

/**
 * Ohjelman pääluokka sisältää askelmittarin käynnistyksen
 * @author Pääkirjoittaja Jani Pudas, sivukirjoittaja Niko Heilimo, sivukirjoittaja Niilo Urpola, sivukirjoittaja Jeremia Kekkonen
 * @version 2.5
 */

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Intent intentMain;
    private TextView textViewAskeleet;
    private TextView textViewKm;
    private TextView textViewTimer;
    private Integer askeleita = 0;
    private SensorManager sensoriManageri;
    private Sensor askelMittari;
    private float matka;
    private boolean sensoriOn = false;
    private boolean timerOn = false;
    private Timer timer;
    private Timerlogiikka timerlogiikka;
    private DatabaseHelper dbHelper;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(MainActivity.this);
        /**
         * Kysyy käyttäjältä luvan käyttää pedometeriä
         * */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        TabLayout tabLayout = findViewById(R.id.tabit);
        textViewKm = findViewById(R.id.textViewKm);
        textViewAskeleet = findViewById(R.id.textViewAskeleet);
        textViewTimer = findViewById(R.id.textViewTimer);
        sensoriManageri = (SensorManager) getSystemService(SENSOR_SERVICE);
        askelMittari = sensoriManageri.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        timer = new Timer();
        SharedPreferences prefGet = getSharedPreferences("Arvot" , Activity.MODE_PRIVATE);
        if(!prefGet.getString("Aika", "0").equals("0.0")) {
            double aika = Double.parseDouble(prefGet.getString("Aika", "0"));
            timerlogiikka = new Timerlogiikka(aika);
        }else {
            timerlogiikka = new Timerlogiikka();
        }
        /**
         * Luo kuuntelijan tabin painamista varten
         */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            /**
             * Toiminta joka tapahtuu tabia painaessa
             */
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    intentMain = new Intent(MainActivity.this, MainActivity.class);

                }else if (tab.getPosition() == 1) {
                    intentMain = new Intent(MainActivity.this,
                            HistoriaView.class);
                    Log.e("TEST", "Painettiin toista");
                    MainActivity.this.startActivity(intentMain);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Log.e("TEST","Created");
    }

    /**
     * luo settings napin headeriin.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * vie settings view
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            intentMain = new Intent(MainActivity.this,
                    AsetuksetView.class);
            MainActivity.this.startActivity(intentMain);
        }
        return false;
    }
    /**
     * Tarkkailee jos nappia clickataan ja tekee halutut jutut.
     * Aloittaa askelten laskemisen ja kellon.
     */
    public void onClick(View view) {
        if (view.getId() == R.id.buttonReset) {
            String aika = textViewTimer.getText().toString();
            String askeleet = textViewAskeleet.getText().toString();
            String matkaTXT = String.format("%.2f", matka);
            String paiva = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            dbHelper.LisaaSuoritus(aika,askeleet,matkaTXT,paiva);
            askeleita = 0;
            if(timerOn) {
                timerlogiikka.lopetaTimer();
            }
            timerlogiikka = new Timerlogiikka();
            textViewTimer.setText(timerlogiikka.pyoristaLuvut());
            timerlogiikka.aloitaTimer(textViewTimer,timer);
            timerlogiikka.lopetaTimer();
            textViewAskeleet.setText(String.valueOf(askeleita));
            textViewKm.setText(String.format("%.2f", matka));

            timerOn = false;

        }else if(view.getId() == R.id.buttonStart) {
            sensoriManageri.registerListener(this, askelMittari, SensorManager.SENSOR_DELAY_FASTEST);
            if(!timerOn) {
                timerlogiikka.aloitaTimer(textViewTimer,timer);
                textViewTimer.setText(timerlogiikka.pyoristaLuvut());
            }
            timerOn = true;
        }else if(view.getId() == R.id.buttonStop) {
            if (timerOn) {
                timerlogiikka.lopetaTimer();
            }
            sensoriOn = false;
            timerOn = false;
            sensoriManageri.unregisterListener(this, askelMittari);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            Log.d("TEST", "ASKEL");

            if(sensoriOn) {
                askeleita++;
            }

            textViewAskeleet.setText(String.valueOf(askeleita));
            if(askeleita > 0) {
                SharedPreferences prefGet = getSharedPreferences("Arvot" , Activity.MODE_PRIVATE);
                String sukupuoli = prefGet.getString("Sukupuoli","");
                if(sukupuoli.equals("Mies")) {
                    matka = (float)(askeleita*78)/(float)100000;
                    Log.d("TEST","Mies ladattu");
                }else if (sukupuoli.equals("Nainen")) {
                    matka = (float)(askeleita*70)/(float)100000;
                    Log.d("TEST","Nainen ladattu");
                }else {
                    matka = (float)(askeleita*78)/(float)100000;
                }
                textViewKm.setText(String.format("%.2f", matka));
            }
        }
        sensoriOn = true;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void onPause() {

        super.onPause();
        Log.e("TEST","Paused");
        double aika = timerlogiikka.nykyinenAika();
        SharedPreferences.Editor editor = getSharedPreferences("Arvot",Activity.MODE_PRIVATE).edit();
        editor.putString("Aika", String.valueOf(aika));
        editor.putString("Matka", String.valueOf(matka));
        editor.putString("Askeleet", String.valueOf(askeleita));
        editor.putBoolean("kaynnista", timerOn);
        editor.commit();
    }
    public void onResume() {

        super.onResume();
        Log.e("TEST","Resumed");
        SharedPreferences prefGet = getSharedPreferences("Arvot" , Activity.MODE_PRIVATE);
        textViewAskeleet.setText(prefGet.getString("Askeleet", "0"));
        matka = Float.parseFloat(prefGet.getString("Matka", "0"));
        if(!prefGet.getString("Aika", "0.0").equals("0.0") && !timerOn) {
            Log.e("TEST", prefGet.getString("AIKA","0.0"));
            timerlogiikka.aloitaTimer(textViewTimer,timer);
            timerOn = true;
            textViewKm.setText(String.format("%.2f", matka));
        }
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.e("TEST","Killed");
        SharedPreferences.Editor editor = getSharedPreferences("Arvot",Activity.MODE_PRIVATE).edit();
        editor.putString("Aika", "0");
        editor.putString("Matka", "0");
        editor.putString("Askeleet", "0");
        editor.commit();
        timerlogiikka.lopetaTimer();
        dbHelper.close();
    }
}