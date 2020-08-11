package dev.b3nedikt.restring

import android.content.Context
import android.content.res.Resources
import android.os.Build
import java.util.*


/**
 * This is the wrapped resources which will be provided by Restring.
 * For getting strings and texts, it checks the strings repository first and if there's a new string
 * that will be returned, otherwise it will fallback to the original resource strings.
 */
@Suppress("DEPRECATION")
internal class RestringResources(
        private var res: Resources,
        private val stringRepository: StringRepository,
        private val context: Context
) : Resources(res.assets, res.displayMetrics, res.configuration) {

    override fun getIdentifier(name: String, defType: String?, defPackage: String?): Int {

        val identifier = super.getIdentifier(name, defType, defPackage)

        if (defType == "string" && identifier == 0) {
            stringRepository.getString(Restring.locale, name) ?: return 0
            val stringId = UUID.randomUUID().hashCode()

            val managedString = Restring.managedStrings
                    .toList()
                    .firstOrNull { it.second == name }

            if (managedString != null) {
                return managedString.first
            }

            Restring.managedStrings[stringId] = name
            return stringId
        }

        return identifier
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int): String {
        setLocale()

        val value = getStringFromRepository(id)
        return value?.toString() ?: res.getString(id)
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String {
        setLocale()

        val value = getStringFromRepository(id)
        if (value != null) return String.format(value.toString(), *formatArgs)
        return res.getString(id, *formatArgs)
    }

    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence {
        setLocale()

        val value = getStringFromRepository(id)
        return value ?: res.getText(id)
    }

    override fun getText(id: Int, def: CharSequence): CharSequence {
        setLocale()

        val value = runCatching {
            getStringFromRepository(id)
        }.getOrNull()

        return value ?: res.getText(id, def)
    }

    override fun getQuantityText(id: Int, quantity: Int): CharSequence {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)
        return value ?: res.getQuantityText(id, quantity)
    }

    override fun getQuantityString(id: Int, quantity: Int): String {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)?.toString()
        return value ?: res.getQuantityString(id, quantity)
    }

    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)?.toString()
                ?.let { String.format(it, *formatArgs) }
        return value ?: res.getQuantityString(id, quantity, *formatArgs)
    }

    override fun getStringArray(id: Int): Array<String> {
        setLocale()

        val value = getStringArrayFromRepository(id)
        return value?.map { it.toString() }?.toTypedArray() ?: res.getStringArray(id)
    }

    override fun getTextArray(id: Int): Array<CharSequence> {
        setLocale()

        val value = getStringArrayFromRepository(id)
        return value ?: res.getTextArray(id)
    }

    private fun getQuantityStringFromRepository(id: Int, quantity: Int): CharSequence? {

        val resultLocale = getLocale() ?: return null

        val stringKey = getResourceEntryName(id)
        val quantityString = stringRepository.getQuantityString(resultLocale, stringKey)
        return quantityString?.get(quantity.toPluralKeyword(resultLocale))
    }

    private fun getStringArrayFromRepository(id: Int): Array<CharSequence>? {
        val resultLocale = getLocale() ?: return null

        val stringKey = getResourceEntryName(id)
        return stringRepository.getStringArray(resultLocale, stringKey)
    }

    private fun getStringFromRepository(id: Int): CharSequence? {

        val resultLocale = getLocale() ?: return null

        try {
            val stringKey = getResourceEntryName(id)
            return stringRepository.getString(resultLocale, stringKey)
        } catch (e: NotFoundException) {

            val stringKey = Restring.managedStrings[id]
            if (stringKey != null) {
                return stringRepository.getString(resultLocale, stringKey)
            }

            throw e
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
            res = context.createConfigurationContext(conf).resources
        } else {
            conf.locale = Restring.locale
            res.updateConfiguration(conf, res.displayMetrics)
        }
    }

    private fun Int.toPluralKeyword(locale: Locale): PluralKeyword =
            PluralKeyword.fromQuantity(res, locale, this)
}

