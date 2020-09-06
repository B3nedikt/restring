package dev.b3nedikt.restring

import android.content.Context
import dev.b3nedikt.restring.repository.CachedStringRepository
import dev.b3nedikt.restring.repository.PersistentStringRepository
import java.util.*


/**
 * A Android library to replace string resources dynamically
 */
object Restring {

    private var isInitialized = false

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
     * Wraps context of an activity to provide Restring features.
     * Will return the context as it was passed in, if it is already wrapped.
     *
     * @param base context of an activity.
     * @return the wrapped context.
     */
    @JvmStatic
    fun wrapContext(base: Context): Context {
        if (base.resources is RestringResources) return base
        return RestringContextWrapper.wrap(base, stringRepository)
    }
}