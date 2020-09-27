package dev.b3nedikt.restring.repository

import android.content.SharedPreferences
import dev.b3nedikt.restring.MutableStringRepository
import dev.b3nedikt.restring.internal.repository.persistent.SharedPreferencesKeyValueStore
import dev.b3nedikt.restring.internal.repository.persistent.SharedPrefsValueSetStore
import dev.b3nedikt.restring.internal.repository.serializer.LocaleSerializer
import dev.b3nedikt.restring.internal.repository.serializer.QuantityStringsSerializer
import dev.b3nedikt.restring.internal.repository.serializer.StringArraysSerializer
import dev.b3nedikt.restring.internal.repository.serializer.StringResourcesSerializer
import dev.b3nedikt.restring.internal.repository.util.LocaleUtil
import java.util.*

/**
 * A [MutableStringRepository] that stores resources persistently in the [SharedPreferences]
 *
 * @param sharedPreferencesProvider creates a new shared preference instance with the given
 * name. Note that this class constructs multiple instances of the shared preferences which may
 * be accessed in parallel! In short a implementation of this function should return a different
 * shared preference instance for each name, but will only get called once for a given name.
 */
class SharedPrefsStringRepository(
        private val sharedPreferencesProvider: (sharedPreferencesName: String) -> SharedPreferences
) : MutableStringRepository {

    private val delegate = PersistentStringRepository(
            localesValueSetStore = createSharedPrefsValueSetStore(),
            stringsKeyValueStoreFactory = { locale ->
                val sharedPrefs = getPreferencesForLocale(
                        prefsName = STRINGS_SHARED_PREF_NAME,
                        locale = locale
                )

                SharedPreferencesKeyValueStore(sharedPrefs, StringResourcesSerializer)
            },
            quantityStringsKeyValueStoreFactory = { locale ->
                val sharedPrefs = getPreferencesForLocale(
                        prefsName = QUANTITY_STRINGS_SHARED_PREF_NAME,
                        locale = locale
                )

                SharedPreferencesKeyValueStore(sharedPrefs, QuantityStringsSerializer)
            },
            stringArraysKeyValueStoreFactory = { locale ->
                val sharedPrefs = getPreferencesForLocale(
                        prefsName = STRING_ARRAYS_SHARED_PREF_NAME,
                        locale = locale
                )

                SharedPreferencesKeyValueStore(sharedPrefs, StringArraysSerializer)
            }
    )

    override val supportedLocales: Set<Locale> = delegate.supportedLocales

    override val strings = delegate.strings

    override val quantityStrings = delegate.quantityStrings

    override val stringArrays = delegate.stringArrays

    private fun getPreferencesForLocale(prefsName: String, locale: Locale): SharedPreferences {
        val sharedPrefName = prefsName + "_" + LocaleUtil.toLanguageTag(locale)
        return sharedPreferencesProvider.invoke(sharedPrefName)
    }

    private fun createSharedPrefsValueSetStore() =
            SharedPrefsValueSetStore(
                    sharedPreferences = sharedPreferencesProvider.invoke(LOCALES_SHARED_PREF_NAME),
                    serializer = LocaleSerializer,
                    stringKey = "Locales"
            )

    private companion object {
        private const val LOCALES_SHARED_PREF_NAME = "dev.b3nedikt.restring.Restring_Locals"
        private const val STRINGS_SHARED_PREF_NAME = "dev.b3nedikt.restring.Restring_Strings"
        private const val QUANTITY_STRINGS_SHARED_PREF_NAME = "dev.b3nedikt.restring.Restring_Quantity_Strings"
        private const val STRING_ARRAYS_SHARED_PREF_NAME = "dev.b3nedikt.restring.Restring_String_Arrays"
    }
}