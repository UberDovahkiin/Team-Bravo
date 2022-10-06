package com.example.myapplication;

import static com.example.myapplication.Counter.getDateFromMillis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;

/**
 * Luokka sisältää laskennassa tarvittavia apurutiineja <- esimerkki
 * @author
 * @version
 */

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TabLayout tabLayout;
    private Intent intentMain;
    private TextView textViewSteps;
    private TextView textViewKm;
    private Integer askeleita;
    private SensorManager sensoriManageri;
    private Sensor askelMittari;
    private float matka;

    TextView textViewTimer;
    long startTime, timeInMilliseconds = 0;
    Handler customHandler = new Handler();


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Kysyy käyttäjältä luvan käyttää pedometeriä
         * */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
            }
        }
        tabLayout = findViewById(R.id.tabit);
        textViewKm = findViewById(R.id.textViewKm);
        textViewSteps = findViewById(R.id.textViewSteps);
        textViewTimer = findViewById(R.id.textViewTimer);
        sensoriManageri = (SensorManager) getSystemService(SENSOR_SERVICE);
        askelMittari = sensoriManageri.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


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
            askeleita = 900;
            if(askeleita > 0) {
                matka = (float)(askeleita*78)/(float)100000;
                textViewKm.setText(String.format("%.2f", matka));
            }
            matka = 0;
            textViewSteps.setText(askeleita.toString());
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);

        }else if(view.getId() == R.id.buttonStart) {
            sensoriManageri.registerListener(this, askelMittari, SensorManager.SENSOR_DELAY_FASTEST);
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
        }else if(view.getId() == R.id.buttonStop) {
            customHandler.removeCallbacks(updateTimerThread);
            sensoriManageri.unregisterListener(this, askelMittari);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            Log.d("TEST", "ASKEL");
            askeleita++;
            textViewSteps.setText(askeleita.toString());
            if(askeleita > 0) {
                matka = (float)(askeleita*78)/(float)100000;
                textViewKm.setText(String.valueOf(matka));
            }

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private final Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            textViewTimer.setText(getDateFromMillis(timeInMilliseconds));
            customHandler.postDelayed(this, 1000);
        }
    };
}