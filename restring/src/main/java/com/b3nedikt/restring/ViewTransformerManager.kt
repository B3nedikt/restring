package com.b3nedikt.restring

import android.util.AttributeSet
import android.util.Pair
import android.view.View

import java.util.ArrayList

/**
 * Manages all view transformers as a central point for layout inflater.
 * Layout inflater will ask this manager to transform the inflating views.
 */
internal class ViewTransformerManager {

    private val transformers = ArrayList<Pair<Class<out View>, Transformer>>()

    /**
     * Register a new view transformer to be applied on newly inflating views.
     *
     * @param transformer to be added to transformers list.
     */
    fun registerTransformer(transformer: Transformer) {
        transformers.add(Pair(transformer.viewType, transformer))
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
    fun transform(view: View?, attrs: AttributeSet): View? {
        if (view == null) {
            return null
        }

        var newView: View = view
        for (pair in transformers) {
            val type = pair.first
            if (!type.isInstance(view)) {
                continue
            }

            val transformer = pair.second
            newView = transformer.transform(newView, attrs)
        }

        return newView
    }

    /**
     * A view transformer skeleton.
     */
    internal interface Transformer {
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
    }
}
