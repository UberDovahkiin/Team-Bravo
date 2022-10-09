package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HistoriaView extends AppCompatActivity {
    Intent intentMain;
    TabLayout tabLayout;
    ListView lv;
    public static final String EXTRA = "com.example.myapplication.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia_view);

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

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Suoritus> suoritukset = dbHelper.haeSuoritukset();
        lv = findViewById(R.id.lvHistoria);
        ListView lv = findViewById(R.id.lvHistoria);

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, suoritukset);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent nextActivity = new Intent(HistoriaView.this, HistoriaView.class);
                nextActivity.putExtra("ITEM", i);
                startActivity(nextActivity);

            }
        });

    }

    /**
     * luo settings napin headeriin
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * vie settings view
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                intentMain = new Intent(HistoriaView.this,
                        AsetuksetView.class);
                HistoriaView.this.startActivity(intentMain);
        }
        return false;
    }
}