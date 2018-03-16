package com.ice.restring;

import com.ice.restring.shadow.MyShadowAsyncTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(shadows = {MyShadowAsyncTask.class})
public class StringsLoaderTaskTest {

    @Before
    public void setUp() {
    }

    @Test
    public void shouldLoadStringsAndSaveInRepository() {
        List<String> langs = Arrays.asList("en", "fa");
        Map<String, String> enStrings = new HashMap<>();
        enStrings.put("string1", "value1");
        enStrings.put("string2", "value2");
        Map<String, String> deStrings = new HashMap<>();
        deStrings.put("string3", "value3");
        deStrings.put("string4", "value4");

        Restring.StringsLoader loader = Mockito.mock(Restring.StringsLoader.class);
        when(loader.getLanguages()).thenReturn(langs);
        when(loader.getStrings("en")).thenReturn(enStrings);
        when(loader.getStrings("fa")).thenReturn(deStrings);

        StringRepository repository = Mockito.mock(StringRepository.class);

        StringsLoaderTask task = new StringsLoaderTask(loader, repository);
        task.run();

        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();

        ArgumentCaptor<Map<String, String>> enCaptor = ArgumentCaptor.forClass(Map.class);
        verify(repository).setStrings(eq("en"), enCaptor.capture());
        assertEquals(enStrings, enCaptor.getValue());

        ArgumentCaptor<Map<String, String>> deCaptor = ArgumentCaptor.forClass(Map.class);
        verify(repository).setStrings(eq("fa"), deCaptor.capture());
        assertEquals(deStrings, deCaptor.getValue());
    }
}