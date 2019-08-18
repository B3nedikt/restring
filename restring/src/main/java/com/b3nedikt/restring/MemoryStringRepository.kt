package com.b3nedikt.restring

/**
 * A StringRepository which keeps the strings ONLY in memory.
 *
 *
 * it's not ThreadSafe.
 */
internal class MemoryStringRepository : StringRepository {

    private val strings = mutableMapOf<String, MutableMap<String, String>>()

    override fun setStrings(language: String, strings: Map<String, String>) {
        this.strings[language] = strings.toMutableMap()
    }

    override fun setString(language: String, key: String, value: String) {
        if (!strings.containsKey(language)) {
            strings[language] = mutableMapOf()
        }
        strings[language]?.put(key, value)
    }

    override fun getString(language: String, key: String) = strings[language]?.get(key)

    override fun getStrings(language: String) = strings[language]?.toMap() ?: mapOf()
}