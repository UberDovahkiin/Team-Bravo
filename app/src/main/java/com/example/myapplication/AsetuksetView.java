package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AsetuksetView extends AppCompatActivity {


    private EditText editTextPituus;
    private EditText editTextPaino;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asetukset_view);
        SharedPreferences prefGet = this.getSharedPreferences("com.example.myapplication", Context.MODE_PRIVATE);


        int editTextPituus = prefGet.getInt("pituus", 1);
        int editTextPaino = prefGet.getInt("paino", 1);

        // Dropdown vaihtoehtojen luonti
        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Sukupuoli", "Mies", "Nainen", "Muu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.button_Save_Settings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefPut = getSharedPreferences("com.example.myapplication", Activity.MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = prefPut.edit();

                prefEditor.commit();
            }
        });

    }
}