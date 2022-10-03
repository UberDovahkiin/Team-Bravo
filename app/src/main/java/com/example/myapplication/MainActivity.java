package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

/**
 * Luokka sisältää laskennassa tarvittavia apurutiineja <- esimerkki
 * @author
 * @version
 */

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ToggleButton kaynnistaButton;
    Intent intentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabit);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    intentMain = new Intent(MainActivity.this, MainActivity.class);

                }
                if (tab.getPosition() == 1) {
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
        kaynnistaButton = findViewById(R.id.kaynnistaButton);
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
                intentMain = new Intent(MainActivity.this ,
                        AsetuksetView.class);
                MainActivity.this.startActivity(intentMain);
        }
        return false;
    }
    private void update() {

    }
    public void buttonPressed(View view) {
    switch  (view.getId()) {
        case R.id.kaynnistaButton:

    }


    }


}