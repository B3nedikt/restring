package com.b3nedikt.restring

import android.util.AttributeSet
import android.view.View

/**
 * A view transformer skeleton.
 */
interface ViewTransformer {
    /**
     * The type of view this transformer is for.
     *
     * @return the type of view.
     */
    val viewType: Class<out View>

    /**
     * Apply transformation to a view.
     *
     * @param view  to be transformed.
     * @param attrs attributes of the view.
     * @return the transformed view.
     */
    fun transform(view: View, attrs: AttributeSet): View

    /**
     * Apply transformation to a view.
     *
     * @param view  to be transformed.
     * @param attrs attributes of the view.
     * @return the transformed view.
     */
    fun reword(view: View, attrs: AttributeSet)

    fun View.reword(attrs: AttributeSet, index: Int, setTextFunction: (CharSequence) -> Unit) {
        val value: String? = attrs.getAttributeValue(index)
        if (isValidResourceId(value)) {
            attrs.getAttributeResourceValue(index, -1)
                    .takeIf { it != -1 }
                    ?.let { setTextFunction(resources.getString(it)) }
        }
    }

    private fun isValidResourceId(value: String?) =
            value?.run { startsWith("@") && equals("@0").not() } ?: false
}