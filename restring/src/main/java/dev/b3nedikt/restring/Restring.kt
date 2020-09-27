package dev.b3nedikt.restring

import android.content.Context
import android.content.ContextWrapper
import dev.b3nedikt.restring.internal.DefaultLocaleProvider
import dev.b3nedikt.restring.internal.RestringContextWrapper
import dev.b3nedikt.restring.internal.RestringResources
import dev.b3nedikt.restring.repository.CachedStringRepository
import dev.b3nedikt.restring.repository.SharedPrefsStringRepository
import java.util.*


/**
 * A Android library to replace string resources dynamically.
 * Restring supports all 3 types of string resources: strings, quantity strings, & string arrays.
 */
object Restring {

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
     * Initialize Restring with the default repository implementation, this needs to be called
     * before any other interaction with restring as long as you don´t initialize the repository
     * yourself. Should ideally be called in your application class.
     *
     * @param context the application context.
     */
    @JvmStatic
    fun init(context: Context) {
        stringRepository = CachedStringRepository(
                SharedPrefsStringRepository { sharedPreferencesName ->
                    context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                }
        )
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
     * of the default resources. If the provided [base] context is already wrapped, it will be
     * returned unchanged by this method. If this method is called either before a call to
     * [Restring.init] or before you set your custom [Restring.stringRepository], the provided
     * [base] context will also get returned unchanged.
     */
    @JvmStatic
    fun wrapContext(base: Context): Context {
        if (this::stringRepository.isInitialized.not()) {
            return base
        }

        if (base.resources is RestringResources) return base
        return RestringContextWrapper.wrap(base, stringRepository)
    }
}