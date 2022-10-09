package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SuoritusView extends AppCompatActivity {
    TabLayout tabLayout;
    Intent intentMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suoritus_view);
        tabLayout = findViewById(R.id.tabit);
        Bundle b = getIntent().getExtras();
        int i = b.getInt(HistoriaView.EXTRA, 0);
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
                    intentMain = new Intent(SuoritusView.this, MainActivity.class);
                    SuoritusView.this.startActivity(intentMain);
                }
                if (tab.getPosition() == 1) {
                    intentMain = new Intent(SuoritusView.this,
                            HistoriaView.class);
                    Log.e("TEST", "Painettiin toista");
                    SuoritusView.this.startActivity(intentMain);
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
}