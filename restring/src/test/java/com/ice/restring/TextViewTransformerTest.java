package com.ice.restring;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TextViewTransformerTest {
    private static final int TEXT_ATTR_INDEX = 3;
    private static final int TEXT_RES_ID = 0x7f0f0123;
    private static final String TEXT_ATTR_KEY = "text";
    private static final String TEXT_ATTR_VALUE = "TEXT_ATTR_VALUE";

    private static final int HINT_ATTR_INDEX = 2;
    private static final int HINT_RES_ID = 0x7f0f0124;
    private static final String HINT_ATTR_KEY = "hint";
    private static final String HINT_ATTR_VALUE = "HINT_ATTR_VALUE";

    private TextViewTransformer transformer;

    @Before
    public void setUp() {
        transformer = new TextViewTransformer();
    }

    @Test
    public void shouldTransformTextView() {
        Context context = getContext();

        View view = transformer.transform(new TextView(context), getAttributeSet(false));

        assertTrue(view instanceof TextView);
        assertEquals(((TextView) view).getText(), TEXT_ATTR_VALUE);
        assertEquals(((TextView) view).getHint(), HINT_ATTR_VALUE);

        view = transformer.transform(new TextView(context), getAttributeSet(true));

        assertTrue(view instanceof TextView);
        assertEquals(((TextView) view).getText(), TEXT_ATTR_VALUE);
        assertEquals(((TextView) view).getHint(), HINT_ATTR_VALUE);
    }

    @Test
    public void shouldTransformExtendedViews() {
        Context context = getContext();

        View view = transformer.transform(new EditText(context), getAttributeSet(false));

        assertTrue(view instanceof EditText);
        assertEquals(((EditText) view).getText().toString(), TEXT_ATTR_VALUE);
        assertEquals(((EditText) view).getHint(), HINT_ATTR_VALUE);

        view = transformer.transform(new EditText(context), getAttributeSet(true));

        assertTrue(view instanceof EditText);
        assertEquals(((EditText) view).getText().toString(), TEXT_ATTR_VALUE);
        assertEquals(((EditText) view).getHint(), HINT_ATTR_VALUE);
    }

    @Test
    public void shouldRejectOtherViewTypes() {
        Context context = getContext();
        AttributeSet attributeSet = getAttributeSet(false);
        RecyclerView recyclerView = new RecyclerView(context);

        View view = transformer.transform(recyclerView, attributeSet);

        assertSame(view, recyclerView);
        verifyZeroInteractions(attributeSet);
    }

    private Context getContext() {
        Context context = Mockito.spy(RuntimeEnvironment.application);
        Resources resources = Mockito.spy(context.getResources());

        doReturn(resources).when(context).getResources();
        doReturn(TEXT_ATTR_VALUE).when(resources).getString(TEXT_RES_ID);
        doReturn(HINT_ATTR_VALUE).when(resources).getString(HINT_RES_ID);

        return context;
    }

    private AttributeSet getAttributeSet(boolean withAndroidPrefix) {
        AttributeSet attributeSet = Mockito.mock(AttributeSet.class);
        when(attributeSet.getAttributeCount()).thenReturn(TEXT_ATTR_INDEX + 2);

        when(attributeSet.getAttributeName(anyInt())).thenReturn("other_attribute");
        when(attributeSet.getAttributeName(TEXT_ATTR_INDEX)).thenReturn((withAndroidPrefix ? "android:" : "") + TEXT_ATTR_KEY);
        when(attributeSet.getAttributeValue(TEXT_ATTR_INDEX)).thenReturn("@" + TEXT_RES_ID);
        when(attributeSet.getAttributeResourceValue(eq(TEXT_ATTR_INDEX), anyInt())).thenReturn(TEXT_RES_ID);
        when(attributeSet.getAttributeName(HINT_ATTR_INDEX)).thenReturn((withAndroidPrefix ? "android:" : "") + HINT_ATTR_KEY);
        when(attributeSet.getAttributeValue(HINT_ATTR_INDEX)).thenReturn("@" + HINT_RES_ID);
        when(attributeSet.getAttributeResourceValue(eq(HINT_ATTR_INDEX), anyInt())).thenReturn(HINT_RES_ID);

        return attributeSet;
    }
}