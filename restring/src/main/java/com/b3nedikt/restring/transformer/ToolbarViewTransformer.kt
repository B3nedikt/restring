package com.b3nedikt.restring.transformer

import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import com.b3nedikt.restring.ViewTransformer

/**
 * A transformer which transforms Toolbar: it transforms the text set as title.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal object ToolbarViewTransformer : ViewTransformer {

    override val viewType = Toolbar::class.java

    override fun transform(view: View, attrs: AttributeSet): View = view.apply {
        when (this) {
            is Toolbar -> transform(attrs)
        }
    }

    private fun Toolbar.transform(attributeSet: AttributeSet) {
        attributeSet.forEach {
            when (attributeSet.getAttributeName(it)) {
                ATTRIBUTE_TITLE, ATTRIBUTE_ANDROID_TITLE -> reword(attributeSet, it, this::setTitle)
                ATTRIBUTE_SUBTITLE, ATTRIBUTE_ANDROID_SUBTITLE -> reword(attributeSet, it, this::setSubtitle)
            }
        }
    }

    private const val ATTRIBUTE_TITLE = "title"
    private const val ATTRIBUTE_SUBTITLE = "subtitle"
    private const val ATTRIBUTE_ANDROID_TITLE = "android:title"
    private const val ATTRIBUTE_ANDROID_SUBTITLE = "android:subtitle"
}