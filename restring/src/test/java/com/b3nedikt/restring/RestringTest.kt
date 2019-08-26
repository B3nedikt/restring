package com.b3nedikt.restring

import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import com.b3nedikt.restring.activity.TestActivity
import com.b3nedikt.restring.shadow.MyShadowAsyncTask
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.hamcrest.core.StringStartsWith.startsWith
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*
import kotlin.collections.LinkedHashMap

@RunWith(RobolectricTestRunner::class)
@Config(shadows = [MyShadowAsyncTask::class])
class RestringTest {

    @Before
    fun setUp() {
        Restring.init(
                ApplicationProvider.getApplicationContext(),
                RestringConfig.Builder()
                        .persist(false)
                        .stringsLoader(MyStringLoader())
                        .build()
        )
        Robolectric.flushBackgroundThreadScheduler()
    }

    @Test
    fun shouldInflateAndTransformViewsOnActivityCreation() {
        val languages = listOf("en", "fa", "de")
        for (lang in languages) {
            Locale.setDefault(Locale(lang))
            val activityController = Robolectric.buildActivity(TestActivity::class.java)
            val activity = activityController.create().start().resume().visible().get()
            val viewGroup = activity.findViewById<ViewGroup>(R.id.root_container)

            val childCount = viewGroup.childCount
            for (i in 0 until childCount) {
                when (val view = viewGroup.getChildAt(i)) {
                    is TextView -> {
                        assertThat("TextView[text]", view.text.toString(), startsWith(lang))
                        assertThat("TextView[hint]", view.hint.toString(), startsWith(lang))
                    }
                    is Toolbar -> assertThat("Toolbar[title]", view.title.toString(), startsWith(lang))
                    is BottomNavigationView -> {
                        val itemCount = view.menu.size()
                        for (item in 0 until itemCount) {
                            assertThat("BottomNavigationView#$item[title]",
                                    view.menu.getItem(item).title.toString(), startsWith(lang))
                            assertThat("BottomNavigationView#$item[titleCondensed]",
                                    view.menu.getItem(item).titleCondensed.toString(), startsWith(lang))
                        }
                    }
                }
            }

            activityController.pause().stop().destroy()
        }
    }

    private inner class MyStringLoader : Restring.StringsLoader {

        override val languages: List<String>
            get() = listOf("en", "fa", "de")

        override fun getStrings(language: String): Map<String, String> {
            val strings = LinkedHashMap<String, String>()
            strings["header"] = language + "_" + "header"
            strings["header_hint"] = language + "_" + "hint"
            strings["menu1title"] = language + "_" + "Menu 1"
            strings["menu1titleCondensed"] = language + "_" + "Menu1"
            strings["menu2title"] = language + "_" + "Menu 2"
            strings["menu2titleCondensed"] = language + "_" + "Menu2"
            strings["menu3title"] = language + "_" + "Menu 3"
            strings["menu3titleCondensed"] = language + "_" + "Menu3"
            return strings
        }
    }
}