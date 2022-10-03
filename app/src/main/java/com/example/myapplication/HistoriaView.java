package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class HistoriaView extends AppCompatActivity {
    Intent intentMain;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia_view);
        tabLayout = findViewById(R.id.tabit);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
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
    }
    // luo settings napin headeriin
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    // vie settings view
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                intentMain = new Intent(HistoriaView.this ,
                        AsetuksetView.class);
                HistoriaView.this.startActivity(intentMain);
        }
        return false;
    }
}