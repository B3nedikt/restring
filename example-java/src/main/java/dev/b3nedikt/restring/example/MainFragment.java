package dev.b3nedikt.restring.example;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.b3nedikt.app_locale.AppLocale;
import dev.b3nedikt.restring.Restring;
import dev.b3nedikt.reword.Reword;

public class MainFragment extends Fragment {

    private Spinner spinner;
    private TextView stringArrayTextView;
    private TextView quantityStringTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SampleStringsGenerator generator = new SampleStringsGenerator();

        for (Locale locale : AppLocale.getSupportedLocales()) {
            Restring.putStrings(locale, generator.getStrings(locale));
            Restring.putQuantityStrings(locale, generator.getQuantityStrings(locale));
            Restring.putStringArrays(locale, generator.getStringArrays(locale));
        }

        spinner = view.findViewById(R.id.spinner);
        stringArrayTextView = view.findViewById(R.id.stringArrayTextView);
        quantityStringTextView = view.findViewById(R.id.quantityStringTextView);

        final List<String> localeStrings = new ArrayList<>();
        for (Locale locale : AppLocale.getSupportedLocales()) {
            localeStrings.add(locale.getLanguage() + " " + locale.getCountry());
        }

        final SpinnerAdapter adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, localeStrings);

        spinner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Restring.setLocale(AppLocale.getSupportedLocales().get(position));

                // The layout containing the views you want to localize
                final View rootView = requireActivity()
                        .getWindow()
                        .getDecorView()
                        .findViewById(android.R.id.content);

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
