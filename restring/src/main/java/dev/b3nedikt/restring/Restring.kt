package dev.b3nedikt.restring

import android.content.Context
import android.content.ContextWrapper
import dev.b3nedikt.restring.repository.CachedStringRepository
import dev.b3nedikt.restring.repository.PersistentStringRepository
import dev.b3nedikt.restring.repository.toMutableRepository
import java.util.*


/**
 * A Android library to replace string resources dynamically.
 * Restring supports all 3 types of string resources: [strings], [quantityStrings] & [stringArrays].
 */
object Restring {

    private var isInitialized = false

    /**
     * The [StringRepository] used by Restring
     */
    @JvmStatic
    lateinit var stringRepository: StringRepository

    /**
     * Map of string ids to string names. These strings are only managed by restring,
     * meaning their id is not in R.string, instead their id has been assigned by restring.
     */
    internal val managedStrings = mutableMapOf<Int, String>()

    /**
     * The [Locale] currently used by restring, this defaults to [Locale.getDefault].
     * If needed the [Locale] used by restring can be changed anytime.
     */
    @JvmStatic
    var locale: Locale
        get() = localeProvider.currentLocale
        set(value) {
            localeProvider.currentLocale = value
        }

    /**
     * The [LocaleProvider] defines the way restring determines its [locale], this
     * defaults to the [DefaultLocaleProvider].
     */
    @JvmStatic
    var localeProvider: LocaleProvider = DefaultLocaleProvider

    /**
     * Initialize Restring with the specified configuration.
     *
     * @param context of the application.
     */
    @JvmStatic
    fun init(context: Context) {
        if (isInitialized) {
            return
        }
        isInitialized = true

        stringRepository = CachedStringRepository(PersistentStringRepository(context))
    }

    /**
     * Add string resources to [Restring] which should be returned from calls to [Context.getResources]
     * instead of the ones from the strings.xml files.
     */
    @JvmStatic
    fun putStrings(
            locale: Locale,
            strings: Map<String, CharSequence>
    ) {
        stringRepository.toMutableRepository().strings[locale]?.putAll(strings)
    }

    /**
     * Add string array Resources to [Restring] which should be returned from calls to
     * [Context.getResources] instead of the ones from the strings.xml files.
     */
    @JvmStatic
    fun putStringArrays(
            locale: Locale,
            stringArrays: Map<String, Array<CharSequence>>
    ) {
        stringRepository.toMutableRepository().stringArrays[locale]?.putAll(stringArrays)
    }

    /**
     * Add quantity string Resources to [Restring] which should be returned from calls to
     * [Context.getResources] instead of the ones from the strings.xml files.
     */
    @JvmStatic
    fun putQuantityStrings(
            locale: Locale,
            quantityStrings: Map<String, Map<PluralKeyword, CharSequence>>
    ) {
        stringRepository.toMutableRepository().quantityStrings[locale]?.putAll(quantityStrings)
    }

    /**
     * Wraps the context with a [ContextWrapper] which provides the [RestringResources] instead
     * of the default resources. If the provided context is already wrapped, it will be returned
     * unchanged by this method.
     */
    @JvmStatic
    fun wrapContext(base: Context): Context {
        if (base.resources is RestringResources) return base
        return RestringContextWrapper.wrap(base, stringRepository)
    }
}