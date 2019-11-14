package com.b3nedikt.restring.repository

import com.b3nedikt.restring.StringRepository
import java.util.*

class CachedStringRepository(private val cacheRepository: StringRepository,
                              val persistentRepository: StringRepository
) : StringRepository {

    override var supportedLocales: Set<Locale> = setOf()

    init {
        supportedLocales.forEach {
            cacheRepository.setStrings(it, persistentRepository.getStrings(it))
        }
    }

    override fun setStrings(locale: Locale, strings: Map<String, CharSequence>) {
        cacheRepository.setStrings(locale, strings)
        persistentRepository.setStrings(locale, strings)
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        cacheRepository.setString(locale, key, value)
        persistentRepository.setString(locale, key, value)
    }

    override fun getString(locale: Locale, key: String): CharSequence? =
            cacheRepository.getString(locale, key)

    override fun getStrings(locale: Locale): Map<String, CharSequence> =
            cacheRepository.getStrings(locale)
}
