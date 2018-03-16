package com.ice.restring;

import java.util.Map;

/**
 * Repository of strings.
 */
interface StringRepository {

    /**
     * Set strings(key, value) for a specific language.
     *
     * @param language the strings belongs to.
     * @param strings  new strings for the language.
     */
    void setStrings(String language, Map<String, String> strings);

    /**
     * set a single string(key, value) for a specific language.
     *
     * @param language the string belongs to.
     * @param key      the key of the string which is the string resource id.
     * @param value    the new string.
     */
    void setString(String language, String key, String value);

    /**
     * Get a string for a language & key.
     *
     * @param language the language of the string.
     * @param key      the string resource id.
     * @return the string if exists, otherwise NULL.
     */
    String getString(String language, String key);

    /**
     * Get all strings for a specific language.
     *
     * @param language the lanugage of the strings.
     * @return the map of string key & values. return empty map if there's no.
     */
    Map<String, String> getStrings(String language);
}
