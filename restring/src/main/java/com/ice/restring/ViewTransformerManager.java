package com.ice.restring;

import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all view transformers as a central point for layout inflater.
 * Layout inflater will ask this manager to transform the inflating views.
 */
class ViewTransformerManager {

    private List<Pair<Class<? extends View>, Transformer>> transformers = new ArrayList<>();

    /**
     * Register a new view transformer to be applied on newly inflating views.
     *
     * @param transformer to be added to transformers list.
     */
    void registerTransformer(Transformer transformer) {
        transformers.add(new Pair<>(transformer.getViewType(), transformer));
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
    View transform(View view, AttributeSet attrs) {
        if (view == null) {
            return null;
        }

        View newView = view;
        for (Pair<Class<? extends View>, Transformer> pair : transformers) {
            Class<? extends View> type = pair.first;
            if (!type.isInstance(view)) {
                continue;
            }

            Transformer transformer = pair.second;
            newView = transformer.transform(newView, attrs);
        }

        return newView;
    }

    /**
     * A view transformer skeleton.
     */
    interface Transformer {
        /**
         * The type of view this transformer is for.
         *
         * @return the type of view.
         */
        Class<? extends View> getViewType();

        /**
         * Apply transformation to a view.
         *
         * @param view  to be transformed.
         * @param attrs attributes of the view.
         * @return the transformed view.
         */
        View transform(View view, AttributeSet attrs);
    }
}
