package dev.b3nedikt.restring

import android.content.res.Resources
import android.os.Build
import java.util.*


/**
 * This is the wrapped resources which will be provided by Restring.
 * For getting strings and texts, it checks the strings repository first and if there's a new string
 * that will be returned, otherwise it will fallback to the original resource strings.
 */
@Suppress("DEPRECATION")
internal class RestringResources(val res: Resources,
                                 private val stringRepository: StringRepository)
    : Resources(res.assets, res.displayMetrics, res.configuration) {

    @Throws(NotFoundException::class)
    override fun getString(id: Int): String {
        setLocale()

        val value = getStringFromRepository(id)
        return value?.toString() ?: super.getString(id)
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String {
        setLocale()

        val value = getStringFromRepository(id)
        if (value != null) return String.format(value.toString(), *formatArgs)
        return super.getString(id, *formatArgs)
    }

    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence {
        setLocale()

        val value = getStringFromRepository(id)
        return value ?: super.getText(id)
    }

    override fun getText(id: Int, def: CharSequence): CharSequence {
        setLocale()

        val value = getStringFromRepository(id)
        return value ?: super.getText(id, def)
    }

    override fun getQuantityText(id: Int, quantity: Int): CharSequence {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)
        return value ?: super.getQuantityText(id, quantity)
    }

    override fun getQuantityString(id: Int, quantity: Int): String {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)?.toString()
        return value ?: super.getQuantityString(id, quantity)
    }

    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)?.toString()
                ?.let { String.format(it, *formatArgs) }
        return value ?: super.getQuantityString(id, quantity, *formatArgs)
    }

    override fun getStringArray(id: Int): Array<String> {
        setLocale()

        val value = getStringArrayFromRepository(id)
        return value?.map { it.toString() }?.toTypedArray() ?: super.getStringArray(id)
    }

    override fun getTextArray(id: Int): Array<CharSequence> {
        setLocale()

        val value = getStringArrayFromRepository(id)
        return value ?: super.getTextArray(id)
    }

    private fun getQuantityStringFromRepository(id: Int, quantity: Int): CharSequence? {

        val resultLocale = getLocale() ?: return null

        return try {
            val stringKey = getResourceEntryName(id)
            val quantityString = stringRepository.getQuantityString(resultLocale, stringKey)
            return quantityString?.get(quantity.toPluralKeyword(resultLocale))
        } catch (e: NotFoundException) {
            null
        }
    }

    private fun getStringArrayFromRepository(id: Int): Array<CharSequence>? {
        val resultLocale = getLocale() ?: return null

        return try {
            val stringKey = getResourceEntryName(id)
            return stringRepository.getStringArray(resultLocale, stringKey)
        } catch (e: NotFoundException) {
            null
        }
    }

    private fun getStringFromRepository(id: Int): CharSequence? {

        val resultLocale = getLocale() ?: return null

        return try {
            val stringKey = getResourceEntryName(id)
            return stringRepository.getString(resultLocale, stringKey)
        } catch (e: NotFoundException) {
            null
        }
    }

    private fun getLocale(): Locale? {
        val currentLocale = Restring.locale
        val supportedLocales = stringRepository.supportedLocales

        return if (supportedLocales.contains(currentLocale)) {
            currentLocale
        } else {
            supportedLocales.find { it.language == currentLocale.language }
        }
    }

    private fun setLocale() {
        if (Restring.localeProvider.isInitial) return

        val conf = res.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(Restring.locale)
        } else {
            conf.locale = Restring.locale
        }

        res.updateConfiguration(conf, res.displayMetrics)
    }

    private fun Int.toPluralKeyword(locale: Locale): PluralKeyword =
            PluralKeyword.fromQuantity(res, locale, this)
}

