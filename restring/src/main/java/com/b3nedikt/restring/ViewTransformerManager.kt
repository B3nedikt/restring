package com.b3nedikt.restring

import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Manages all view transformers as a central point for layout inflater.
 * Layout inflater will ask this manager to transform the inflating views.
 */
internal class ViewTransformerManager {

    private val transformers = mutableListOf<ViewTransformer>()
    private val attributes = mutableMapOf<Int, AttributeSet>()

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
                    ?.also { attributes[view.id] = attrs }
                    ?: view

    fun transformChildren(parentView: View) {
        val visited = mutableListOf<View>()
        val unvisited = mutableListOf<View>()
        unvisited.add(parentView)

        while (unvisited.isNotEmpty()) {
            val child = unvisited.removeAt(0)

            attributes[child.id]?.let { attrs ->
                transformers.find { it.viewType.isInstance(child) }
                        ?.reword(child, attrs)

                if(child is TextView){
                    child.text = child.id.toString()
                }
            }

            visited.add(child)
            if (child !is ViewGroup) continue
            val childCount = child.childCount
            for (i in 0 until childCount) unvisited.add(child.getChildAt(i))
        }
    }


}
