package com.ice.restring;

import android.app.Activity;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.ice.restring.activity.TestActivity;
import com.ice.restring.shadow.MyShadowAsyncTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {MyShadowAsyncTask.class})
public class RestringTest {

    @Before
    public void setUp() {
        Restring.init(
                RuntimeEnvironment.application,
                new RestringConfig.Builder()
                        .persist(false)
                        .stringsLoader(new MyStringLoader())
                        .build()
        );
        Robolectric.flushBackgroundThreadScheduler();
    }

    @Test
    public void shouldInflateAndTransformViewsOnActivityCreation() {
        List<String> languages = Arrays.asList("en", "fa", "de");
        for (String lang : languages) {
            Locale.setDefault(new Locale(lang));
            ActivityController<TestActivity> activityController = Robolectric.buildActivity(TestActivity.class);
            Activity activity = activityController.create().start().resume().visible().get();
            ViewGroup viewGroup = activity.findViewById(R.id.root_container);

            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof TextView) {
                    assertThat("TextView[text]", ((TextView) view).getText().toString(), startsWith(getLanguage()));
                    assertThat("TextView[hint]", ((TextView) view).getHint().toString(), startsWith(getLanguage()));
                } else if (view instanceof Toolbar) {
                    assertThat("Toolbar[title]", ((Toolbar) view).getTitle().toString(), startsWith(getLanguage()));
                } else if (view instanceof android.support.v7.widget.Toolbar) {
                    assertThat("Toolbar[title]", ((android.support.v7.widget.Toolbar) view).getTitle().toString(), startsWith(getLanguage()));
                } else if (view instanceof BottomNavigationView) {
                    BottomNavigationView bottomNavigationView = (BottomNavigationView) view;
                    int itemCount = bottomNavigationView.getMenu().size();
                    for (int item = 0; item < itemCount; item++) {
                        assertThat("BottomNavigationView#" + item + "[title]",
                                bottomNavigationView.getMenu().getItem(item).getTitle().toString(), startsWith(getLanguage()));
                        assertThat("BottomNavigationView#" + item + "[titleCondensed]",
                                bottomNavigationView.getMenu().getItem(item).getTitleCondensed().toString(), startsWith(getLanguage()));
                    }
                }
            }

            activityController.pause().stop().destroy();
        }
    }

    private String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    private class MyStringLoader implements Restring.StringsLoader {

        @Override
        public List<String> getLanguages() {
            return Arrays.asList("en", "fa", "de");
        }

        @Override
        public Map<String, String> getStrings(String language) {
            Map<String, String> strings = new LinkedHashMap<>();
            strings.put("header", language + "_" + "header");
            strings.put("header_hint", language + "_" + "hint");
            strings.put("menu1title", language + "_" + "Menu 1");
            strings.put("menu1titleCondensed", language + "_" + "Menu1");
            strings.put("menu2title", language + "_" + "Menu 2");
            strings.put("menu2titleCondensed", language + "_" + "Menu2");
            strings.put("menu3title", language + "_" + "Menu 3");
            strings.put("menu3titleCondensed", language + "_" + "Menu3");
            return strings;
        }
    }
}