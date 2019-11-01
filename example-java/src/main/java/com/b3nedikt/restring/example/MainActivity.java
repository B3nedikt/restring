package com.b3nedikt.restring.example;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.Nullable;

import com.b3nedikt.restring.Restring;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);

        final List<String> localeStrings = new ArrayList<>();
        for (Locale locale : Locales.APP_LOCALES) {
            localeStrings.add(locale.getLanguage() + " " + locale.getCountry());
        }

        final SpinnerAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, localeStrings);

        spinner.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Restring.setLocale(Locales.APP_LOCALES.get(position));

                // The layout containing the views you want to localize
                final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                Restring.reword(rootView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
