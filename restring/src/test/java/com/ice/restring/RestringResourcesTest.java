package com.ice.restring;

import android.content.res.Resources;
import android.text.Html;
import android.text.TextUtils;

import com.ice.restring.shadow.MyShadowAssetManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {MyShadowAssetManager.class})
public class RestringResourcesTest {
    private static final int STR_RES_ID = 0x7f0f0123;
    private static final String STR_KEY = "STR_KEY";
    private static final String STR_VALUE = "STR_VALUE";
    private static final String STR_VALUE_WITH_PARAM = "STR_VALUE %s";
    private static final String STR_VALUE_HTML = "STR_<b>value</b>";

    @Mock private StringRepository repository;
    private Resources resources;
    private RestringResources restringResources;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resources = RuntimeEnvironment.application.getResources();

        restringResources = Mockito.spy(new RestringResources(resources, repository));
        doReturn(STR_KEY).when(restringResources).getResourceEntryName(STR_RES_ID);
    }

    @Test
    public void shouldGetStringFromRepositoryIfExists() {
        doReturn(STR_VALUE).when(repository).getString(getLanguage(), STR_KEY);

        String stringValue = restringResources.getString(STR_RES_ID);

        assertEquals(STR_VALUE, stringValue);
    }

    @Test
    public void shouldGetStringFromResourceIfNotExists() {
        doReturn(null).when(repository).getString(getLanguage(), STR_KEY);

        String stringValue = restringResources.getString(STR_RES_ID);

        String expected = new MyShadowAssetManager().getResourceText(STR_RES_ID).toString();
        assertEquals(expected, stringValue);
    }

    @Test
    public void shouldGetStringWithParamsFromRepositoryIfExists() {
        final String param = "PARAM";
        doReturn(STR_VALUE_WITH_PARAM).when(repository).getString(getLanguage(), STR_KEY);

        String stringValue = restringResources.getString(STR_RES_ID, param);

        assertEquals(String.format(STR_VALUE_WITH_PARAM, param), stringValue);
    }

    @Test
    public void shouldGetStringWithParamsFromResourceIfNotExists() {
        final String param = "PARAM";
        doReturn(null).when(repository).getString(getLanguage(), STR_KEY);

        String stringValue = restringResources.getString(STR_RES_ID, param);

        String expected = new MyShadowAssetManager().getResourceText(STR_RES_ID).toString();
        assertEquals(expected, stringValue);
    }

    @Test
    public void shouldGetHtmlTextFromRepositoryIfExists() {
        doReturn(STR_VALUE_HTML).when(repository).getString(getLanguage(), STR_KEY);

        CharSequence realValue = restringResources.getText(STR_RES_ID);

        CharSequence expected = Html.fromHtml(STR_VALUE_HTML, Html.FROM_HTML_MODE_COMPACT);
        assertTrue(TextUtils.equals(expected, realValue));
    }

    @Test
    public void shouldGetHtmlTextFromResourceIfNotExists() {
        doReturn(null).when(repository).getString(getLanguage(), STR_KEY);

        CharSequence realValue = restringResources.getText(STR_RES_ID);

        CharSequence expected = new MyShadowAssetManager().getResourceText(STR_RES_ID);
        assertTrue(TextUtils.equals(expected, realValue));
    }

    @Test
    public void shouldReturnDefaultHtmlTextFromRepositoryIfResourceIdIsInvalid() {
        final CharSequence def = Html.fromHtml("<b>def</b>", Html.FROM_HTML_MODE_COMPACT);
        doReturn(null).when(repository).getString(getLanguage(), STR_KEY);

        CharSequence realValue = restringResources.getText(0, def);

        assertTrue(TextUtils.equals(def, realValue));
    }

    private String getLanguage() {
        return Locale.getDefault().getLanguage();
    }
}