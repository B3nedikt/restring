package com.b3nedikt.restring.transformer

import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.b3nedikt.restring.ViewTransformer

/**
 * A transformer which transforms Toolbar(from support library): it transforms the text set as title.
 */
internal object SupportToolbarViewTransformer : ViewTransformer {

    override val viewType = Toolbar::class.java

    override fun transform(view: View, attrs: AttributeSet) = view.apply {
        when (this) {
            is Toolbar -> transform(attrs)
        }
    }

    private fun Toolbar.transform(attributeSet: AttributeSet) {
        attributeSet.forEach {
            when (attributeSet.getAttributeName(it)) {
                ATTRIBUTE_TITLE, ATTRIBUTE_APP_TITLE -> reword(attributeSet, it, this::setTitle)
                ATTRIBUTE_SUBTITLE, ATTRIBUTE_APP_SUBTITLE -> reword(attributeSet, it, this::setSubtitle)
            }
        }
    }

    private const val ATTRIBUTE_TITLE = "title"
    private const val ATTRIBUTE_SUBTITLE = "subtitle"
    private const val ATTRIBUTE_APP_TITLE = "app:title"
    private const val ATTRIBUTE_APP_SUBTITLE = "app:subtitle"
}