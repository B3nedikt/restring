package dev.b3nedikt.restring.transformer

import android.os.Build
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import dev.b3nedikt.restring.ViewTransformer

/**
 * A transformer which transforms Toolbar: it transforms the text set as title.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal object ToolbarViewTransformer : ViewTransformer<Toolbar> {

    private const val ATTRIBUTE_TITLE = "title"
    private const val ATTRIBUTE_SUBTITLE = "subtitle"
    private const val ATTRIBUTE_ANDROID_TITLE = "android:title"
    private const val ATTRIBUTE_ANDROID_SUBTITLE = "android:subtitle"

    override val viewType = Toolbar::class.java

    override val supportedAttributes = setOf(ATTRIBUTE_TITLE, ATTRIBUTE_SUBTITLE,
            ATTRIBUTE_ANDROID_TITLE, ATTRIBUTE_ANDROID_SUBTITLE)

    override fun Toolbar.transform(attrs: Map<String, Int>) {
        attrs.forEach { entry ->
            when (entry.key) {
                ATTRIBUTE_TITLE, ATTRIBUTE_ANDROID_TITLE -> reword(entry.value, this::setTitle)
                ATTRIBUTE_SUBTITLE, ATTRIBUTE_ANDROID_SUBTITLE -> reword(entry.value, this::setSubtitle)
            }
        }
    }
}