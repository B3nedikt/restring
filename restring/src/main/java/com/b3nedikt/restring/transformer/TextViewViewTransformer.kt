package com.b3nedikt.restring.transformer

import android.view.View
import android.widget.TextView
import com.b3nedikt.restring.ViewTransformer

/**
 * A transformer which transforms TextView(or any view extends it like Button, EditText, ...):
 * it transforms "text" & "hint" attributes.
 */
internal object TextViewViewTransformer : ViewTransformer {

    private const val ATTRIBUTE_TEXT = "text"
    private const val ATTRIBUTE_ANDROID_TEXT = "android:text"
    private const val ATTRIBUTE_HINT = "hint"
    private const val ATTRIBUTE_ANDROID_HINT = "android:hint"

    override val viewType = TextView::class.java

    override val supportedAttributes = setOf(ATTRIBUTE_TEXT, ATTRIBUTE_ANDROID_TEXT, ATTRIBUTE_HINT,
            ATTRIBUTE_ANDROID_HINT)

    override fun reword(view: View, attributes: Map<String, Int>) {
        when (view) {
            is TextView -> view.transform(attributes)
        }
    }

    override fun transform(view: View, attributes: Map<String, Int>): View = view.apply {
        when (this) {
            is TextView -> transform(attributes)
        }
    }

    private fun TextView.transform(attrs: Map<String, Int>) {
        attrs.forEach { entry ->
            when (entry.key) {
                ATTRIBUTE_ANDROID_TEXT, ATTRIBUTE_TEXT -> reword(entry.value, this::setText)
                ATTRIBUTE_ANDROID_HINT, ATTRIBUTE_HINT -> reword(entry.value, this::setHint)
            }
        }
    }
}