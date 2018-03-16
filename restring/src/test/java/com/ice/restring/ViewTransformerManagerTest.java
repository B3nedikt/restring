package com.ice.restring;

import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ViewTransformerManagerTest {

    private ViewTransformerManager transformerManager;

    @Before
    public void setUp() {
        transformerManager = new ViewTransformerManager();
    }

    @Test
    public void shouldTransformView() {
        TextView textView = new TextView(RuntimeEnvironment.application);

        ViewTransformerManager.Transformer transformer = Mockito.mock(ViewTransformerManager.Transformer.class);
        doReturn(TextView.class).when(transformer).getViewType();
        when(transformer.transform(any(), any())).thenReturn(textView);
        transformerManager.registerTransformer(transformer);

        View transformedView = transformerManager.transform(
                new TextView(RuntimeEnvironment.application),
                Mockito.mock(AttributeSet.class)
        );

        assertSame(textView, transformedView);
    }
}