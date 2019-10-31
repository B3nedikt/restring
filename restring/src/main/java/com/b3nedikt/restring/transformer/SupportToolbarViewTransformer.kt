package com.b3nedikt.restring.transformer

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.b3nedikt.restring.ViewTransformer

/**
 * A transformer which transforms Toolbar(from support library): it transforms the text set as title.
 */
internal object SupportToolbarViewTransformer : ViewTransformer {

    private const val ATTRIBUTE_TITLE = "title"
    private const val ATTRIBUTE_SUBTITLE = "subtitle"
    private const val ATTRIBUTE_APP_TITLE = "app:title"
    private const val ATTRIBUTE_APP_SUBTITLE = "app:subtitle"

    override val viewType = Toolbar::class.java

    override val supportedAttributes = setOf(ATTRIBUTE_TITLE, ATTRIBUTE_SUBTITLE,
            ATTRIBUTE_APP_TITLE, ATTRIBUTE_APP_SUBTITLE)

    override fun reword(view: View, attributes: Map<String, Int>) {
        when (view) {
            is Toolbar -> view.transform(attributes)
        }
    }

    override fun transform(view: View, attributes: Map<String, Int>): View = view.apply {
        when (this) {
            is Toolbar -> transform(attributes)
        }
    }

    private fun Toolbar.transform(attrs: Map<String, Int>) {
        attrs.forEach { entry ->
            when (entry.key) {
                ATTRIBUTE_TITLE, ATTRIBUTE_APP_TITLE -> reword(entry.value, this::setTitle)
                ATTRIBUTE_SUBTITLE, ATTRIBUTE_APP_SUBTITLE -> reword(entry.value, this::setSubtitle)
            }
        }
    }
}