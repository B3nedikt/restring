package com.b3nedikt.restring

import android.content.res.Resources
import androidx.core.text.HtmlCompat
import java.util.*


/**
 * This is the wrapped resources which will be provided by Restring.
 * For getting strings and texts, it checks the strings repository first and if there's a new string
 * that will be returned, otherwise it will fallback to the original resource strings.
 */
internal class RestringResources(res: Resources,
                                 private val stringRepository: StringRepository)
    : Resources(res.assets, res.displayMetrics, res.configuration) {

    @Throws(NotFoundException::class)
    override fun getString(id: Int): String {
        val value = getStringFromRepository(id)
        return value ?: super.getString(id)
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String {
        val value = getStringFromRepository(id)
        if (value != null) return String.format(value, *formatArgs)
        return super.getString(id, *formatArgs)
    }

    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence {
        val value = getStringFromRepository(id)
        return value?.let { fromHtml(it) } ?: super.getText(id)
    }

    override fun getText(id: Int, def: CharSequence): CharSequence {
        val value = getStringFromRepository(id)
        return value?.let { fromHtml(it) } ?: super.getText(id, def)
    }

    private fun getStringFromRepository(id: Int): String? {

        val currentLocale = RestringUtil.currentLocale
        val supportedLocales = stringRepository.supportedLocales

        val resultLocale = if (supportedLocales.contains(currentLocale)) {
            currentLocale
        } else {
            supportedLocales.find { it.language == currentLocale.language }
        }

        resultLocale ?: return null

        return try {
            val stringKey = getResourceEntryName(id)
            return stringRepository.getString(resultLocale, stringKey)
        } catch (e: NotFoundException) {
            null
        }
    }

    private fun fromHtml(source: String) =
            HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_COMPACT)
}
