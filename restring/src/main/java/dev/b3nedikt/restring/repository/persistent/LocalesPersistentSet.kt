package dev.b3nedikt.restring.repository.persistent

import android.content.SharedPreferences
import dev.b3nedikt.restring.LocaleUtil
import java.util.*

internal class LocalesPersistentSet(
        private val sharedPreferences: SharedPreferences
) : PersistentSet<Locale>() {

    override fun find(key: Locale): Locale? {
        val localeString = sharedPreferences
                .getStringSet("Locales", null)
                ?.find { LocaleUtil.fromLanguageTag(it) == key }
                ?: return null

        return LocaleUtil.fromLanguageTag(localeString)
    }

    override fun findAll(): Collection<Locale> {
        return sharedPreferences
                .getStringSet("Locales", null)
                ?.map { LocaleUtil.fromLanguageTag(it) }
                ?: emptySet()
    }

    override fun save(element: Locale) {
        val oldSet = findAll()
        val newSet = (oldSet + element)
                .map { LocaleUtil.toLanguageTag(it) }
                .toSet()

        sharedPreferences.edit()
                .putStringSet("Locales", newSet)
                .apply()
    }

    override fun saveAll(elements: Collection<Locale>) {
        val oldSet = findAll()
        val newSet = (oldSet + elements)
                .map { LocaleUtil.toLanguageTag(it) }
                .toSet()

        sharedPreferences.edit()
                .putStringSet("Locales", newSet)
                .apply()
    }

    override fun delete(element: Locale) {
        val oldSet = findAll()
        val newSet = (oldSet - element)
                .map { LocaleUtil.toLanguageTag(it) }
                .toSet()

        sharedPreferences.edit()
                .putStringSet("Locales", newSet)
                .apply()
    }

    override fun deleteAll() {
        sharedPreferences.edit().clear().apply()
    }
}