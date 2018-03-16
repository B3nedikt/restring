package com.ice.restring;

import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * A transformer which transforms TextView(or any view extends it like Button, EditText, ...):
 * it transforms "text" & "hint" attributes.
 */
class TextViewTransformer implements ViewTransformerManager.Transformer {

    private static final String ATTRIBUTE_TEXT = "text";
    private static final String ATTRIBUTE_ANDROID_TEXT = "android:text";
    private static final String ATTRIBUTE_HINT = "hint";
    private static final String ATTRIBUTE_ANDROID_HINT = "android:hint";

    @Override
    public Class<? extends View> getViewType() {
        return TextView.class;
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
                case ATTRIBUTE_ANDROID_TEXT:
                case ATTRIBUTE_TEXT: {
                    String value = attrs.getAttributeValue(index);
                    if (value != null && value.startsWith("@")) {
                        setTextForView(view, resources.getString(attrs.getAttributeResourceValue(index, 0)));
                    }
                    break;
                }
                case ATTRIBUTE_ANDROID_HINT:
                case ATTRIBUTE_HINT: {
                    String value = attrs.getAttributeValue(index);
                    if (value != null && value.startsWith("@")) {
                        setHintForView(view, resources.getString(attrs.getAttributeResourceValue(index, 0)));
                    }
                    break;
                }
            }
        }
        return view;
    }

    private void setTextForView(View view, String text) {
        ((TextView) view).setText(text);
    }

    private void setHintForView(View view, String text) {
        ((TextView) view).setHint(text);
    }
}
