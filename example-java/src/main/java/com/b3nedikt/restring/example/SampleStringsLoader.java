package com.b3nedikt.restring.example;

import com.b3nedikt.restring.Restring;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This is just a really simple sample of strings loader.
 * in real applications, you might call an API to get your strings.
 * <p>
 * <p>
 * All overridden methods will be called on background thread.
 */
public class SampleStringsLoader implements Restring.StringsLoader {

    @NotNull
    @Override
    public List<Locale> getLocales() {
        return Locales.APP_LOCALES;
    }

    @NotNull
    @Override
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
}