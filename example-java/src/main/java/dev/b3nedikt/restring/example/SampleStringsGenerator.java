package dev.b3nedikt.restring.example;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dev.b3nedikt.restring.PluralKeyword;

/**
 * Generates sample data
 */
public class SampleStringsGenerator {

    @NotNull
    public Map<String, CharSequence> getStrings(@NotNull Locale locale) {
        final Map<String, CharSequence> map = new HashMap<>();
        if (locale == Locale.ENGLISH) {
            map.put("title", "Title (from restring).");
            map.put("subtitle", "Subtitle (from restring).");
        } else if (locale == Locale.US) {
            map.put("title", "Title US (from restring).");
            map.put("subtitle", "Subtitle US (from restring).");
        } else if (locale == Locales.LOCALE_AUSTRIAN_GERMAN) {
            map.put("title", "Titel (aus restring).");
            map.put("subtitle", "Untertitel (aus restring).");
        }
        return map;
    }

    @NotNull
    public Map<String, Map<PluralKeyword, CharSequence>> getQuantityStrings(@NotNull Locale locale) {
        final Map<String, Map<PluralKeyword, CharSequence>> map = new HashMap<>();

        final Map<PluralKeyword, CharSequence> quantityStrings = new HashMap<>();
        quantityStrings.put(PluralKeyword.ONE, "%d quantity string " + locale + " (from restring)");
        quantityStrings.put(PluralKeyword.OTHER, "%d quantity strings " + locale + " (from restring)");

        map.put("quantity_string", quantityStrings);
        return map;
    }

    @NotNull
    public Map<String, CharSequence[]> getStringArrays(@NotNull Locale locale) {
        final Map<String, CharSequence[]> map = new HashMap<>();

        final CharSequence[] stringArray = new CharSequence[2];
        stringArray[0] = "String Array 1 " + locale + " (from restring)";
        stringArray[1] = "String Array 2 " + locale + " (from restring)";

        map.put("string_array", stringArray);
        return map;
    }
}