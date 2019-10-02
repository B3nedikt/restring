package com.b3nedikt.restring.transformer

import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.b3nedikt.restring.ViewTransformerManager

/**
 * A transformer which transforms TextView(or any view extends it like Button, EditText, ...):
 * it transforms "text" & "hint" attributes.
 */
internal class TextViewTransformer : ViewTransformerManager.Transformer {

    override val viewType = TextView::class.java

    override fun transform(view: View, attrs: AttributeSet): View {
        if (!viewType.isInstance(view)) {
            return view
        }
        val resources = view.context.resources

        for (index in 0 until attrs.attributeCount) {
            when (attrs.getAttributeName(index)) {
                ATTRIBUTE_ANDROID_TEXT, ATTRIBUTE_TEXT -> {
                    val value = attrs.getAttributeValue(index)
                    if (isValidResourceId(value)) {
                        setTextForView(view, resources.getString(attrs.getAttributeResourceValue(index, 0)))
                    }
                }
                ATTRIBUTE_ANDROID_HINT, ATTRIBUTE_HINT -> {
                    val value: String? = attrs.getAttributeValue(index)
                    if (isValidResourceId(value)) {
                        setHintForView(view, resources.getString(attrs.getAttributeResourceValue(index, 0)))
                    }
                }
            }
        }
        return view
    }

    private fun isValidResourceId(value: String?) =
            value?.run { startsWith("@") && equals("@0").not() } ?: false

    private fun setTextForView(view: View, text: String) {
        (view as TextView).text = text
    }

    private fun setHintForView(view: View, text: String) {
        (view as TextView).hint = text
    }

    private companion object {
        private const val ATTRIBUTE_TEXT = "text"
        private const val ATTRIBUTE_ANDROID_TEXT = "android:text"
        private const val ATTRIBUTE_HINT = "hint"
        private const val ATTRIBUTE_ANDROID_HINT = "android:hint"
    }
}