package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AsetuksetView extends AppCompatActivity {

    Spinner spinner;
    EditText editTextPaino;
    EditText editTextPituus;

    public static final String MY_App = "com.example.sharedpreferences.Names";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asetukset_view);


        // Dropdown vaihtoehtojen luonti
        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Sukupuoli", "Mies", "Nainen", "Muu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        editTextPituus = (EditText) findViewById(R.id.editTextPituus);
        editTextPaino = (EditText) findViewById(R.id.editTextPaino);

        Button button = (Button) findViewById(R.id.button_Save_Settings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pituus = editTextPituus.getText()
                        .toString()
                        .trim();

                SharedPreferences.Editor editor = getSharedPreferences(MY_App, MODE_PRIVATE).edit();
                editor.putString("", pituus);
                editor.commit();
            }
        });
    }
}

