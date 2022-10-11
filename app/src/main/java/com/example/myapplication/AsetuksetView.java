package com.example.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Asetusview jossä käyttäjä voi lisätä pituuden, painon ja sukupuolensa.
 * @author Pääkirjoittaja Niilo Urpola, sivukirjoittaja Jani Pudas
 */
public class AsetuksetView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner dropdown;
    EditText editTextPaino;
    EditText editTextPituus;
    String painoData;
    String pituusData;
    String sukupuoliData;
    String valinta;
    int spinnerValittu;

    String[] items = new String[]{"Sukupuoli", "Mies", "Nainen"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asetukset_view);
        editTextPaino = findViewById(R.id.editTextPaino);
        editTextPituus = findViewById(R.id.editTextPituus);
        /**
         * Dropdown vaihtoehtojen luonti
         */
        dropdown = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        valinta = "mies";
        spinnerValittu = adapter.getPosition(valinta);
        dropdown.setSelection(spinnerValittu);

        /**
         *  Hakee tiedot SharedPreferences asetuksiin kirjoitetut tiedot
         */

        SharedPreferences prefGet = getSharedPreferences("Arvot", Activity.MODE_PRIVATE);
        pituusData = prefGet.getString("pituus", "0");
        painoData = prefGet.getString("paino", "0");
        sukupuoliData = prefGet.getString("Sukupuoli", "Sukupuoli");
        editTextPituus.setText(pituusData);
        editTextPaino.setText(painoData);
        /**
         * Asettaa dropdown vaihtoehdon tallennetun datan mukaan.
         */
        if(sukupuoliData.equals("Mies")) {
            dropdown.setSelection(1);
        }else if(sukupuoliData.equals("Nainen")) {
            dropdown.setSelection(2);
        }


    }
    /**
     * Buttonin tallentaa käyttäjän asettamat tiedot
     */
    public void onClick(View view) {

        if (view.getId() == R.id.button_Save_Settings) {
            Log.d("MY_APP", "onClick voisitko kiltisti tallentaa");
            SharedPreferences.Editor prefEditor = getSharedPreferences("Arvot", Activity.MODE_PRIVATE).edit();
            painoData = editTextPaino.getText().toString();
            pituusData = editTextPituus.getText().toString();
            prefEditor.putString("paino", painoData);
            prefEditor.putString("pituus", pituusData);
            prefEditor.commit();
            Toast.makeText(this, "Tiedot tallennettu", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sharedpreferences tallentaa DropDown käyttäjä valinnan
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!items[position].equals("Sukupuoli")) {
            SharedPreferences.Editor prefEditor = getSharedPreferences("Arvot", Activity.MODE_PRIVATE).edit();
            sukupuoliData = items[position];
            prefEditor.putString("Sukupuoli", sukupuoliData);
            prefEditor.commit();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}