package dev.b3nedikt.restring

import java.util.*

/**
 * Immutable repository of strings resources. Restring supports all 3 types of string resources:
 * [strings], [quantityStrings] & [stringArrays].
 */
interface StringRepository {

    /**
     * The [Locale]s supported by the repository, Restring will only replace strings of [Locale]s
     * in this set. Meaning if [strings] contains a string resource, but this string resource
     * does belong to a [Locale] not in the [supportedLocales], it will get used by Restring.
     */
    val supportedLocales: Set<Locale>

    /**
     * The string resources with their [Locale] as key and maps of string resource names to
     * their resource value as value.
     */
    val strings: Map<Locale, Map<String, CharSequence>>

    /**
     * The quantity string resources with their [Locale] as key and maps of string resource names to
     * their resource value as value.
     */
    val quantityStrings: Map<Locale, Map<String, Map<PluralKeyword, CharSequence>>>

    /**
     * The string array resources with their [Locale] as key and maps of string resource names to
     * their resource value as value.
     */
    val stringArrays: Map<Locale, Map<String, Array<CharSequence>>>
}
