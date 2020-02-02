package dev.b3nedikt.restring.example;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.b3nedikt.app_locale.AppLocale;
import dev.b3nedikt.restring.Restring;
import dev.b3nedikt.reword.Reword;

public class MainActivity extends BaseActivity {

    private Spinner spinner;
    private TextView stringArrayTextView;
    private TextView quantityStringTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        stringArrayTextView = findViewById(R.id.stringArrayTextView);
        quantityStringTextView = findViewById(R.id.quantityStringTextView);

        final List<String> localeStrings = new ArrayList<>();
        for (Locale locale : AppLocale.getSupportedLocales()) {
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
                Restring.setLocale(AppLocale.getSupportedLocales().get(position));

                // The layout containing the views you want to localize
                final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                Reword.reword(rootView);

                displayStringArray();

                displayQuantityString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void displayStringArray() {
        final String[] stringArray = getResources().getStringArray(R.array.string_array);
        final String stringArrayString = TextUtils.join("\n", stringArray);
        stringArrayTextView.setText(stringArrayString);
    }

    private void displayQuantityString() {
        final String[] quantityStrings = new String[3];
        for (int i = 0; i < 3; i++) {
            quantityStrings[i] = getResources().getQuantityString(R.plurals.quantity_string, i, i);
        }
        final String combinedQuantityStrings = TextUtils.join("\n", quantityStrings);
        quantityStringTextView.setText(combinedQuantityStrings);
    }
}
