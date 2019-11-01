package com.b3nedikt.restring

import android.util.AttributeSet
import android.view.View
import com.b3nedikt.restring.transformer.associate

/**
 * A view transformer skeleton.
 */
interface ViewTransformer<T : View> {

    val viewType: Class<T>

    val supportedAttributes: Set<String>

    fun T.transform(attrs: Map<String, Int>)

    /**
     * Apply transformation to a view.
     *
     * @param view  to be transformed.
     * @param attrs attributes of the view.
     * @return the transformed view.
     */
    fun extractAttributes(view: View, attrs: AttributeSet): Map<String, Int> {
        return attrs.associate { index ->
            val name = attrs.getAttributeName(index)
            if (name in supportedAttributes) name to view.getResourceId(attrs, index)
            else name to -1
        }.filterValues { it != -1 }
    }

    fun T.reword(resId: Int, setTextFunction: (CharSequence) -> Unit) {
        setTextFunction(resources.getString(resId))
    }

    fun View.getResourceId(attrs: AttributeSet, index: Int): Int {
        val value: String? = attrs.getAttributeValue(index)
        if (isValidResourceId(value)) {
            return attrs.getAttributeResourceValue(index, -1)
        }
        return -1;
    }

    private fun isValidResourceId(value: String?) =
            value?.run { startsWith("@") && equals("@0").not() } ?: false
}