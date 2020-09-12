package dev.b3nedikt.restring.example

import android.app.Application
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.restring.Restring
import dev.b3nedikt.restring.example.Locales.LOCALE_AUSTRIAN_GERMAN
import dev.b3nedikt.reword.RewordInterceptor
import dev.b3nedikt.viewpump.ViewPump
import java.util.*


class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppLocale.supportedLocales = listOf(Locale.ENGLISH, Locale.US, LOCALE_AUSTRIAN_GERMAN)

        Restring.init(this)
        Restring.localeProvider = AppLocaleLocaleProvider

        ViewPump.init(ViewPump.builder()
                .addInterceptor(RewordInterceptor)
                .build()
        )
    }
}