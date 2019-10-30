package com.b3nedikt.restring

import android.util.AttributeSet
import android.view.View

/**
 * Manages all view transformers as a central point for layout inflater.
 * Layout inflater will ask this manager to transform the inflating views.
 */
internal class ViewTransformerManager {

    private val transformers = mutableListOf<ViewTransformer>()

    /**
     * Register a new view viewTransformer to be applied on newly inflating views.
     *
     * @param viewTransformer to be added to transformers list.
     */
    fun registerTransformer(viewTransformer: ViewTransformer) {
        transformers.add(viewTransformer)
    }

    /**
     * Transforms a view.
     * it tries to find proper transformers for view, and if exists, it will apply them on view,
     * and return the final result as a new view.
     *
     * @param view  to be transformed.
     * @param attrs attributes of the view.
     * @return the transformed view.
     */
    fun transform(view: View, attrs: AttributeSet) =
            transformers.find { it.viewType.isInstance(view) }
                    ?.run { transform(view, attrs) }
                    ?: view

}
