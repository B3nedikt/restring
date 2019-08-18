package com.b3nedikt.restring

import android.util.AttributeSet
import android.view.View
import android.widget.TextView

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
            val attributeName = attrs.getAttributeName(index)
            when (attributeName) {
                ATTRIBUTE_ANDROID_TEXT, ATTRIBUTE_TEXT -> {
                    val value = attrs.getAttributeValue(index)
                    if (value != null && value.startsWith("@")) {
                        setTextForView(view, resources.getString(attrs.getAttributeResourceValue(index, 0)))
                    }
                }
                ATTRIBUTE_ANDROID_HINT, ATTRIBUTE_HINT -> {
                    val value = attrs.getAttributeValue(index)
                    if (value != null && value.startsWith("@")) {
                        setHintForView(view, resources.getString(attrs.getAttributeResourceValue(index, 0)))
                    }
                }
            }
        }
        return view
    }

    private fun setTextForView(view: View, text: String) {
        (view as TextView).text = text
    }

    private fun setHintForView(view: View, text: String) {
        (view as TextView).hint = text
    }

    companion object {

        private const val ATTRIBUTE_TEXT = "text"
        private const val ATTRIBUTE_ANDROID_TEXT = "android:text"
        private const val ATTRIBUTE_HINT = "hint"
        private const val ATTRIBUTE_ANDROID_HINT = "android:hint"
    }
}
