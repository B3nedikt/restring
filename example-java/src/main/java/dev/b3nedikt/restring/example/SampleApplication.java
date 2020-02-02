package dev.b3nedikt.restring.example;

import android.app.Application;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import dev.b3nedikt.app_locale.AppLocale;
import dev.b3nedikt.restring.Restring;
import dev.b3nedikt.restring.RestringConfig;
import dev.b3nedikt.reword.RewordInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppLocale.setSupportedLocales(APP_LOCALES);

        Restring.init(this,
                new RestringConfig.Builder()
                        .stringsLoader(new SampleStringsLoader())
                        .build()
        );

        ViewPump.init(ViewPump.builder()
                .addInterceptor(RewordInterceptor.INSTANCE)
                .build()
        );
    }

    private static final List<Locale> APP_LOCALES =
            Arrays.asList(Locale.ENGLISH, Locale.US, Locales.LOCALE_AUSTRIAN_GERMAN);
}