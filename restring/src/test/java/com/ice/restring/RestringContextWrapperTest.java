package com.ice.restring;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ice.restring.shadow.MyShadowAssetManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {MyShadowAssetManager.class})
public class RestringContextWrapperTest {
    private static final int STR_RES_ID = 0x7f0f0123;
    private static final String STR_KEY = "STR_KEY";
    private static final String STR_VALUE = "STR_VALUE";

    private RestringContextWrapper restringContextWrapper;
    private Context context;
    private Resources originalResources;
    @Mock private StringRepository stringRepository;
    @Mock private ViewTransformerManager transformerManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = RuntimeEnvironment.application;
        originalResources = context.getResources();

        when(transformerManager.transform(any(), any())).thenAnswer(i -> i.getArgument(0));
        restringContextWrapper = RestringContextWrapper.wrap(
                context,
                stringRepository,
                transformerManager
        );
    }

    @Test
    public void shouldWrapResourcesAndGetStringsFromRepository() {
        ((MyShadowAssetManager) Shadow.extract(originalResources.getAssets()))
                .addResourceEntryNameForTesting(STR_RES_ID, STR_KEY);

        doReturn(STR_VALUE).when(stringRepository).getString(getLanguage(), STR_KEY);

        String real = restringContextWrapper.getResources().getString(STR_RES_ID);

        assertEquals(STR_VALUE, real);
    }

    @Test
    public void shouldProvideCustomLayoutInflaterToApplyViewTransformation() {
        LayoutInflater layoutInflater = (LayoutInflater) restringContextWrapper.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assertTrue(layoutInflater instanceof RestringLayoutInflater);

        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.test_layout, null, false);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        verify(transformerManager, atLeastOnce()).transform(captor.capture(), any());
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            captor.getAllValues().contains(child);
        }
    }

    private String getLanguage() {
        return Locale.getDefault().getLanguage();
    }
}