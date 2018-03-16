package com.ice.restring;

import android.content.res.Resources;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

/**
 * A transformer which transforms Toolbar(from support library): it transforms the text set as title.
 */
class SupportToolbarTransformer implements ViewTransformerManager.Transformer {

    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_APP_TITLE = "app:title";

    @Override
    public Class<? extends View> getViewType() {
        return Toolbar.class;
    }

    @Override
    public View transform(View view, AttributeSet attrs) {
        if (view == null || !getViewType().isInstance(view)) {
            return view;
        }
        Resources resources = view.getContext().getResources();

        for (int index = 0; index < attrs.getAttributeCount(); index++) {
            String attributeName = attrs.getAttributeName(index);
            switch (attributeName) {
                case ATTRIBUTE_APP_TITLE:
                case ATTRIBUTE_TITLE: {
                    String value = attrs.getAttributeValue(index);
                    if (value != null && value.startsWith("@")) {
                        setTitleForView(view, resources.getString(attrs.getAttributeResourceValue(index, 0)));
                    }
                    break;
                }
            }
        }
        return view;
    }

    private void setTitleForView(View view, String text) {
        ((Toolbar) view).setTitle(text);
    }
}
