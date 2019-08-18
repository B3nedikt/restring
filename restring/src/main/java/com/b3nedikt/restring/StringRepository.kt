package com.b3nedikt.restring

/**
 * Repository of strings.
 */
internal interface StringRepository {

    /**
     * Set strings(key, value) for a specific language.
     *
     * @param language the strings belongs to.
     * @param strings  new strings for the language.
     */
    fun setStrings(language: String, strings: Map<String, String>)

    /**
     * set a single string(key, value) for a specific language.
     *
     * @param language the string belongs to.
     * @param key      the key of the string which is the string resource id.
     * @param value    the new string.
     */
    fun setString(language: String, key: String, value: String)

    /**
     * Get a string for a language & key.
     *
     * @param language the language of the string.
     * @param key      the string resource id.
     * @return the string if exists, otherwise NULL.
     */
    fun getString(language: String, key: String): String?

    /**
     * Get all strings for a specific language.
     *
     * @param language the lanugage of the strings.
     * @return the map of string key & values. return empty map if there's no.
     */
    fun getStrings(language: String): Map<String, String>
}
