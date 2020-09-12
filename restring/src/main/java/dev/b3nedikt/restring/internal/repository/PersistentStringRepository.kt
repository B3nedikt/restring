package dev.b3nedikt.restring.internal.repository

import android.content.Context
import android.content.SharedPreferences
import dev.b3nedikt.restring.internal.repository.util.LocaleUtil
import dev.b3nedikt.restring.MutableStringRepository
import dev.b3nedikt.restring.internal.repository.persistent.*
import java.util.*

internal class PersistentStringRepository(private val context: Context) : MutableStringRepository {

    override val supportedLocales: Set<Locale>
        get() = _supportedLocales

    private val _supportedLocales by
    LocalesPersistentSet(context.getSharedPreferences(LOCALES_SHARED_PREF_NAME, Context.MODE_PRIVATE))

    override val strings by localeToResourcesPersistentMap(
            persistentMapFactory = { locale ->
                val sharedPrefs = getPreferencesForLocale(
                        prefsName = STRINGS_SHARED_PREF_NAME,
                        locale = locale
                )

                StringResourcesPersistentMap(sharedPrefs)
            })

    override val quantityStrings by localeToResourcesPersistentMap(
            persistentMapFactory = { locale ->
                val sharedPrefs = getPreferencesForLocale(
                        prefsName = QUANTITY_STRINGS_SHARED_PREF_NAME,
                        locale = locale
                )

                QuantityStringsResourcesPersistentMap(sharedPrefs)
            })

    override val stringArrays by localeToResourcesPersistentMap(
            persistentMapFactory = { locale ->
                val sharedPrefs = getPreferencesForLocale(
                        prefsName = STRING_ARRAYS_SHARED_PREF_NAME,
                        locale = locale
                )

                StringArraysPersistentMap(sharedPrefs)
            })

    private fun <V> localeToResourcesPersistentMap(
            persistentMapFactory: (locale: Locale) -> ResourcesPersistentMap<V>
    ): LocalePersistentMap<V> {
        return LocalePersistentMap(
                context = context,
                locales = _supportedLocales,
                persistentMapFactory = persistentMapFactory
        )
    }

    private fun getPreferencesForLocale(prefsName: String, locale: Locale): SharedPreferences {
        val sharedPrefName = prefsName + "_" + LocaleUtil.toLanguageTag(locale)
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
    }

    private companion object {
        private const val LOCALES_SHARED_PREF_NAME = "dev.b3nedikt.restring.Restring_Locals"
        private const val STRINGS_SHARED_PREF_NAME = "dev.b3nedikt.restring.Restring_Strings"
        private const val QUANTITY_STRINGS_SHARED_PREF_NAME = "dev.b3nedikt.restring.Restring_Quantity_Strings"
        private const val STRING_ARRAYS_SHARED_PREF_NAME = "dev.b3nedikt.restring.Restring_String_Arrays"
    }
}