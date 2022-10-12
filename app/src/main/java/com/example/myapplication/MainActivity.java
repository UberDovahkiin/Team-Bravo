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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Timer;

/**
 * Ohjelman pääluokka sisältää askelmittarin käynnistyksen
 * @author Pääkirjoittaja Jani Pudas, sivukirjoittaja Niko Heilimo, sivukirjoittaja Niilo Urpola, sivukirjoittaja Jeremia Kekkonen
 * @version 2.7
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
    private boolean sensoriOn;
    private boolean timerOn;
    private boolean paused = true;
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
        if(!prefGet.getString("Aika", "0.0").equals("0.0")) {
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
                if (tab.getPosition() == 1) {
                    if(!timerOn) {
                        intentMain = new Intent(MainActivity.this,HistoriaView.class);
                        MainActivity.this.startActivity(intentMain);
                    }else {
                        Toast.makeText(MainActivity.this, "Tallenna tai resettaa askelmittaus!", Toast.LENGTH_SHORT).show();
                    }
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
        Log.e("TEST", String.valueOf(paused));
    }


    /**
     * luo settings napin headeriin.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;

    }
public void unregister(Sensor askelMittari) {
    sensoriManageri.unregisterListener(this,askelMittari);
}
    /**
     * vie settings view
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            if(!timerOn) {
                intentMain = new Intent(MainActivity.this,
                        AsetuksetView.class);
                MainActivity.this.startActivity(intentMain);
            }else {
                Toast.makeText(this, "Tallenna tai resettaa askelmittaus!", Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }
    /**
     * Tarkkailee jos nappia clickataan ja tekee halutut jutut.
     * Aloittaa askelten laskemisen ja kellon.
     */
    public void onClick(View view) {
        // Jos painetaan Tallenna
        if (view.getId() == R.id.buttonTallenna) {
            if(!textViewAskeleet.getText().toString().equals("0")) {
                String aika = textViewTimer.getText().toString();
                String askeleet = textViewAskeleet.getText().toString();
                String matkaTXT = String.format("%.2f", matka);
                String paiva = LocalDate.now().format( DateTimeFormatter.ofLocalizedDate( FormatStyle.SHORT ));
                dbHelper.LisaaSuoritus(aika,askeleet,matkaTXT, paiva);
                askeleita = 0;
                matka = 0;
                if(timerOn) {
                    timerlogiikka.lopetaTimer();
                }
                timerlogiikka = new Timerlogiikka();
                textViewTimer.setText(timerlogiikka.pyoristaLuvut());
                timerlogiikka.aloitaTimer(textViewTimer,timer);
                timerlogiikka.lopetaTimer();
                textViewAskeleet.setText(String.valueOf(askeleita));
                textViewKm.setText(String.format("%.2f", matka));
                sensoriManageri.unregisterListener(this, askelMittari);
                timerOn = false;
                sensoriOn = false;
                paused = true;
                Toast.makeText(this,"Suoritus tallennettu!",Toast.LENGTH_SHORT).show();
            }else if (textViewAskeleet.getText().toString().equals("0") && !textViewTimer.getText().toString().equals("0.0")){
                Toast.makeText(this, "0 askelta ei voi tallentaa!", Toast.LENGTH_SHORT).show();
            }
            // Jos painetaan Käynnistä
        }else if(view.getId() == R.id.buttonStart) {
            sensoriManageri.registerListener(this, askelMittari, SensorManager.SENSOR_DELAY_FASTEST);
            if(!timerOn) {
                timerlogiikka.aloitaTimer(textViewTimer,timer);
                textViewTimer.setText(timerlogiikka.pyoristaLuvut());
                timerOn = true;
                paused = false;
            }
            // Jos painetaan Stop
        }else if(view.getId() == R.id.buttonStop) {
            if (timerOn) {
                timerlogiikka.lopetaTimer();
            }
            sensoriManageri.unregisterListener(this, askelMittari);
            timerOn = false;
            sensoriOn = false;
            paused = true;
        }
        // Jos painetaan Reset
        else if (view.getId() == R.id.buttonReset) {
            askeleita = 0;
            matka = 0;
            if(timerOn) {
                timerlogiikka.lopetaTimer();
            }
            timerlogiikka = new Timerlogiikka();
            textViewTimer.setText(timerlogiikka.pyoristaLuvut());
            timerlogiikka.aloitaTimer(textViewTimer,timer);
            timerlogiikka.lopetaTimer();
            textViewAskeleet.setText(String.valueOf(askeleita));
            textViewKm.setText(String.format("%.2f", matka));
            sensoriManageri.unregisterListener(this, askelMittari);
            timerOn = false;
            sensoriOn = false;
            paused = true;
        }
    }

    /**
     * Kuuntelee jos pedometrissä tapahtuu muutos ja lisää askeleen sekä riippuen sukupuoli valinnasta laskee kilometrit eri tavalla.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences.Editor editor = getSharedPreferences("Arvot",Activity.MODE_PRIVATE).edit();
        SharedPreferences prefGet = getSharedPreferences("Arvot" , Activity.MODE_PRIVATE);
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(sensoriOn) {
                askeleita++;
                editor.putString("Askeleet", String.valueOf(askeleita));
                editor.commit();
                Log.e("TEST",prefGet.getString("Askeleet","0"));
            }
            textViewAskeleet.setText(String.valueOf(askeleita));
            if(askeleita > 0) {
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
        Log.e("TEST","PAUSED");
        SharedPreferences.Editor editor = getSharedPreferences("Arvot",Activity.MODE_PRIVATE).edit();
        editor.putString("Aika", String.valueOf(timerlogiikka.nykyinenAika()));
        editor.putBoolean("Timer", timerOn);
        editor.putBoolean("Paused", paused);
        editor.commit();
    }
    public void onResume() {
        super.onResume();
        Log.e("TEST", "RESUMED");
        SharedPreferences prefGet = getSharedPreferences("Arvot" , Activity.MODE_PRIVATE);
        paused = prefGet.getBoolean("Paused",true);
        Log.e("TEST", String.valueOf(timerlogiikka.nykyinenAika()));
        textViewTimer.setText(timerlogiikka.pyoristaLuvut());
        textViewAskeleet.setText(askeleita.toString());
        sensoriManageri.registerListener(this, askelMittari, SensorManager.SENSOR_DELAY_FASTEST);
        if(!timerOn && !paused) {
            Log.e("TEST","Alotettiin");
            askeleita = Integer.valueOf(prefGet.getString("Askeleet","0"));
            timerlogiikka.aloitaTimer(textViewTimer,timer);
            timerOn = true;
            textViewKm.setText(String.format("%.2f", matka));
        }
    }
}