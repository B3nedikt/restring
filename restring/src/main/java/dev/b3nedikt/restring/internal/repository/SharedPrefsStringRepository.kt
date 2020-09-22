package dev.b3nedikt.restring.internal.repository

import android.content.Context
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

class SharedPrefsStringRepository(private val context: Context) : MutableStringRepository {

    private val delegate = PersistentStringRepository(
            context = context,
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
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
    }

    private fun createSharedPrefsValueSetStore() =
            SharedPrefsValueSetStore(
                    sharedPreferences = context.getSharedPreferences(
                            LOCALES_SHARED_PREF_NAME,
                            Context.MODE_PRIVATE
                    ),
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