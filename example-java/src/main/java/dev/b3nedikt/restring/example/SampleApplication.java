package dev.b3nedikt.restring.example;

import android.app.Application;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import dev.b3nedikt.app_locale.AppLocale;
import dev.b3nedikt.restring.Restring;
import dev.b3nedikt.reword.RewordInterceptor;
import dev.b3nedikt.viewpump.ViewPump;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppLocale.setSupportedLocales(APP_LOCALES);

        Restring.init(this);
        ViewPump.init(RewordInterceptor.INSTANCE);
    }

    private static final List<Locale> APP_LOCALES =
            Arrays.asList(Locale.ENGLISH, Locale.US, Locales.LOCALE_AUSTRIAN_GERMAN);
}