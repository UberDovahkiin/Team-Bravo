package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class SuoritusView extends AppCompatActivity {
    TabLayout tabLayout;
    Intent intentMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suoritus_view);
        tabLayout = findViewById(R.id.tabit);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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