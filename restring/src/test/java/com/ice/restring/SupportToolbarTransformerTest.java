package com.ice.restring;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SupportToolbarTransformerTest {
    private static final int TITLE_ATTR_INDEX = 3;
    private static final int TITLE_RES_ID = 0x7f0f0123;
    private static final String TITLE_ATTR_KEY = "title";
    private static final String TITLE_ATTR_VALUE = "TITLE_ATTR_VALUE";

    private SupportToolbarTransformer transformer;

    @Before
    public void setUp() {
        transformer = new SupportToolbarTransformer();
    }

    @Test
    public void shouldTransformToolbar() {
        Context context = getContext();

        View view = transformer.transform(new Toolbar(context), getAttributeSet(false));

        assertTrue(view instanceof Toolbar);
        assertEquals(((Toolbar) view).getTitle(), TITLE_ATTR_VALUE);

        view = transformer.transform(new Toolbar(context), getAttributeSet(true));

        assertTrue(view instanceof Toolbar);
        assertEquals(((Toolbar) view).getTitle(), TITLE_ATTR_VALUE);
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
        doReturn(TITLE_ATTR_VALUE).when(resources).getString(TITLE_RES_ID);

        return context;
    }

    private AttributeSet getAttributeSet(boolean withAppPrefix) {
        AttributeSet attributeSet = Mockito.mock(AttributeSet.class);
        when(attributeSet.getAttributeCount()).thenReturn(TITLE_ATTR_INDEX + 2);

        when(attributeSet.getAttributeName(anyInt())).thenReturn("other_attribute");
        when(attributeSet.getAttributeName(TITLE_ATTR_INDEX)).thenReturn((withAppPrefix ? "app:" : "") + TITLE_ATTR_KEY);
        when(attributeSet.getAttributeValue(TITLE_ATTR_INDEX)).thenReturn("@" + TITLE_RES_ID);
        when(attributeSet.getAttributeResourceValue(eq(TITLE_ATTR_INDEX), anyInt())).thenReturn(TITLE_RES_ID);

        return attributeSet;
    }
}
