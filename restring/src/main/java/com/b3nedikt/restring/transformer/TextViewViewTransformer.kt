package com.b3nedikt.restring.transformer

import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.b3nedikt.restring.ViewTransformer

/**
 * A transformer which transforms TextView(or any view extends it like Button, EditText, ...):
 * it transforms "text" & "hint" attributes.
 */
internal object TextViewViewTransformer : ViewTransformer {

    override val viewType = TextView::class.java

    override fun transform(view: View, attrs: AttributeSet): View = view.apply {
        when (this) {
            is TextView -> transform(attrs)
        }
    }

    private fun TextView.transform(attrs: AttributeSet) {
        attrs.forEach { index ->
            when (attrs.getAttributeName(index)) {
                ATTRIBUTE_ANDROID_TEXT, ATTRIBUTE_TEXT -> reword(attrs, index, this::setText)
                ATTRIBUTE_ANDROID_HINT, ATTRIBUTE_HINT -> reword(attrs, index, this::setHint)
            }
        }
    }

    private const val ATTRIBUTE_TEXT = "text"
    private const val ATTRIBUTE_ANDROID_TEXT = "android:text"
    private const val ATTRIBUTE_HINT = "hint"
    private const val ATTRIBUTE_ANDROID_HINT = "android:hint"
}