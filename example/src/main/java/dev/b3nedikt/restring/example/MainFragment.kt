package dev.b3nedikt.restring.example

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.ContentFrameLayout
import androidx.fragment.app.Fragment
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.restring.Restring
import dev.b3nedikt.reword.Reword

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private lateinit var spinner: Spinner

    private lateinit var stringArrayTextView: TextView
    private lateinit var quantityStringTextView: TextView
    private lateinit var changeThemeButton: Button

    private lateinit var stringNotInStringsXmlTextView: TextView
    private lateinit var stringFromApplicationContextTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        spinner = view.findViewById(R.id.spinner)

        stringArrayTextView = view.findViewById(R.id.stringArrayTextView)
        quantityStringTextView = view.findViewById(R.id.quantityStringTextView)
        changeThemeButton = view.findViewById(R.id.changeThemeButton)

        changeThemeButton.setOnClickListener {
            activity?.toggleDarkMode()
        }

        stringNotInStringsXmlTextView = view.findViewById(R.id.stringNotInStringsXmlTextView)
        stringFromApplicationContextTextView =
            view.findViewById(R.id.stringFromApplicationContextTextView)

        AppLocale.supportedLocales.forEach { locale ->
            Restring.putStrings(locale, ExampleStringsGenerator.getStrings(locale))
            Restring.putQuantityStrings(locale, ExampleStringsGenerator.getQuantityStrings(locale))
            Restring.putStringArrays(locale, ExampleStringsGenerator.getStringArrays(locale))
        }

        val localeStrings = AppLocale.supportedLocales.map { it.language + " " + it.country }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            localeStrings
        )

        spinner.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                AppLocale.desiredLocale = AppLocale.supportedLocales[position]

                val rootView =
                    requireActivity()
                        .window
                        .decorView
                        .findViewById<ContentFrameLayout>(android.R.id.content)

                Reword.reword(rootView)

                stringArrayTextView.text = resources.getStringArray(R.array.string_array)
                    .joinToString("\n")

                quantityStringTextView.text = (0 until 3)
                    .joinToString("\n") { resources.getQuantityString(R.plurals.quantity_string, it, it) }

                stringNotInStringsXmlTextView.text = requireContext()
                    .getStringResourceByName("a_string_not_in_strings_xml")

                stringFromApplicationContextTextView.text = requireActivity().applicationContext
                    .getString(R.string.string_from_application_context)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }
}

fun Context.getStringResourceByName(id: String): String? {
    val resId = resources.getIdentifier(id, "string", packageName)
    return if (resId != 0) {
        getString(resId)
    } else {
        null
    }
}

private fun Context.toggleDarkMode() {
    val newMode = when (AppCompatDelegate.getDefaultNightMode()) {
        MODE_NIGHT_NO -> MODE_NIGHT_YES
        MODE_NIGHT_YES -> MODE_NIGHT_NO
        MODE_NIGHT_FOLLOW_SYSTEM -> {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> MODE_NIGHT_YES
                Configuration.UI_MODE_NIGHT_YES -> MODE_NIGHT_NO
                else -> {
                    MODE_NIGHT_YES
                }
            }
        }
        else -> {
            MODE_NIGHT_YES
        }
    }
    AppCompatDelegate.setDefaultNightMode(newMode)
}
