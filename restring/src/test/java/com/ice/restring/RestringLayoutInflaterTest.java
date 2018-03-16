package com.ice.restring;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {16, 19, 21, 23, 24, 26})
public class RestringLayoutInflaterTest {

    @Mock private ViewTransformerManager transformerManager;
    private RestringLayoutInflater restringLayoutInflater;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(transformerManager.transform(any(), any())).thenAnswer((Answer<View>) invocation ->
                invocation.getArgument(0)
        );
        RuntimeEnvironment.application.setTheme(R.style.Theme_AppCompat);
        restringLayoutInflater = new RestringLayoutInflater(
                LayoutInflater.from(RuntimeEnvironment.application),
                RuntimeEnvironment.application,
                transformerManager,
                false
        );
    }

    @Test
    public void shouldTransformViewsOnInflatingLayouts() {
        ViewGroup viewGroup = (ViewGroup) restringLayoutInflater.inflate(R.layout.test_layout, null, false);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        verify(transformerManager, atLeastOnce()).transform(captor.capture(), any());
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            captor.getAllValues().contains(child);
        }
    }
}