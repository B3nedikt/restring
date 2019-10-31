package com.b3nedikt.restring

import android.content.Context
import android.content.ContextWrapper
import com.b3nedikt.restring.repository.CachedStringRepository
import com.b3nedikt.restring.repository.MemoryStringRepository
import com.b3nedikt.restring.repository.SharedPrefStringRepository
import com.b3nedikt.restring.transformer.BottomNavigationViewViewTransformer
import com.b3nedikt.restring.transformer.SupportToolbarViewTransformer
import com.b3nedikt.restring.transformer.TextViewViewTransformer
import com.b3nedikt.restring.transformer.ToolbarViewTransformer
import java.util.*

/**
 * Entry point for Restring. it will be used for initializing Restring components, setting new strings,
 * wrapping activity context.
 */
object Restring {

    private var isInitialized = false
    private lateinit var stringRepository: StringRepository

    internal val viewTransformerManager: ViewTransformerManager by lazy {
        ViewTransformerManager().apply {
            registerTransformer(TextViewViewTransformer)
            registerTransformer(ToolbarViewTransformer)
            registerTransformer(SupportToolbarViewTransformer)
            registerTransformer(BottomNavigationViewViewTransformer)
        }
    }

    /**
     * Initialize Restring with the specified configuration.
     *
     * @param context of the application.
     * @param config  of the Restring.
     */
    @JvmOverloads
    fun init(context: Context, config: RestringConfig = RestringConfig.default) {
        if (isInitialized) {
            return
        }

        isInitialized = true
        initStringRepository(context, config)

        config.viewTransformers.forEach { viewTransformerManager.registerTransformer(it) }
    }

    /**
     * Wraps context of an activity to provide Restring features.
     *
     * @param base context of an activity.
     * @return the Restring wrapped context.
     */
    fun wrapContext(base: Context): ContextWrapper {
        return RestringContextWrapper.wrap(base, stringRepository)
    }

    /**
     * Set strings of a language.
     *
     * @param locale the strings are for.
     * @param newStrings the strings of the language.
     */
    fun setStrings(locale: Locale, newStrings: Map<String, String>) {
        stringRepository.setStrings(locale, newStrings)
    }

    /**
     * Set a single string for a language.
     *
     * @param locale the string is for.
     * @param key      the string key.
     * @param value    the string value.
     */
    fun setString(locale: Locale, key: String, value: String) {
        stringRepository.setString(locale, key, value)
    }

    private fun initStringRepository(context: Context, config: RestringConfig) {
        stringRepository = config.stringRepository ?: defaultRepository(context)

        if (config.stringsLoader != null) {
            StringsLoaderTask(config.stringsLoader, stringRepository).run()
        }
    }

    private fun defaultRepository(context: Context) = CachedStringRepository(
            cacheRepository = MemoryStringRepository(),
            persistentRepository = SharedPrefStringRepository(context)
    )

    /**
     * Loader of strings skeleton. Clients can implement this interface if they want to load
     * strings on initialization. First the list of languages will be asked, then strings of each
     * language.
     */
    interface StringsLoader {

        /**
         * List of supported languages
         */
        val locales: List<Locale>

        /**
         * Get strings of a language as keys &amp; values.
         *
         * @param locale of the strings.
         * @return the strings as (key, value).
         */
        fun getStrings(locale: Locale): Map<String, CharSequence>
    }
}