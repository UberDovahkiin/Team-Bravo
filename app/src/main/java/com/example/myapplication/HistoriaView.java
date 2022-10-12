package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * View jossa näkyy suoritushistoria listviewn avulla
 * @author Pääkirjoittaja Janipudas, Sivukirjoittaja Niilo Urpola
 * @version 1.5
 */
public class HistoriaView extends AppCompatActivity {
    private Intent intentMain;
    private TabLayout tabLayout;
    private ListView lv;
    private boolean timerOn;
    public static final String EXTRA = "com.example.myapplication.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia_view);
        SharedPreferences prefGet = getSharedPreferences("Arvot" , Activity.MODE_PRIVATE);
        timerOn = prefGet.getBoolean("Timer",true);
        tabLayout = findViewById(R.id.tabit);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
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
                    intentMain = new Intent(HistoriaView.this, MainActivity.class);
                    HistoriaView.this.startActivity(intentMain);
                }
                if (tab.getPosition() == 1) {
                    intentMain = new Intent(HistoriaView.this,
                            HistoriaView.class);
                    Log.e("TEST", "Painettiin toista");
                    HistoriaView.this.startActivity(intentMain);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /**
         * Luodaan arraylist SQL-databasesta sekä luodaan sitä hyväksikäyttäen listview itemit.
         */
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Suoritus> suoritukset = dbHelper.haeSuoritukset();
        lv = findViewById(R.id.lvHistoria);
        ListView lv = findViewById(R.id.lvHistoria);

        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.suoritus_list_item, suoritukset);
        lv.setAdapter(adapter);
    }

    /**
     * luo settings nappulan.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * Kuuntelee jos settings nappulaa painetaan ja vie AsetuksetView.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            if(!timerOn) {
                intentMain = new Intent(HistoriaView.this,
                        AsetuksetView.class);
                HistoriaView.this.startActivity(intentMain);
            }else {
                Toast.makeText(this, "Tallenna tai resettaa askelmittaus", Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }
}