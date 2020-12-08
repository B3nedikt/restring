package dev.b3nedikt.restring

import android.os.Build
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.b3nedikt.restring.activity.TestActivity
import dev.b3nedikt.restring.internal.DefaultLocaleProvider
import dev.b3nedikt.reword.RewordInterceptor
import dev.b3nedikt.viewpump.ViewPump
import org.amshove.kluent.shouldStartWith
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class RestringTest {

    @Before
    fun setUp() {
        Restring.init(ApplicationProvider.getApplicationContext())

        ViewPump.init(RewordInterceptor)

        val locales = listOf(Locale.ENGLISH, Locale.GERMAN)

        locales.forEach { locale ->
            Restring.putStrings(locale, getStrings(locale))
        }
    }

    @Test
    fun shouldInflateAndTransformViewsOnActivityCreation() {
        Restring.localeProvider = DefaultLocaleProvider
        DefaultLocaleProvider.isInitial = true

        val locales = listOf(Locale.ENGLISH, Locale.GERMAN)

        for (locale in locales) {
            Locale.setDefault(locale)
            shouldInflateAndTransformViewsOnActivityCreation(locale.language)
        }
    }

    @Test
    fun shouldInflateAndTransformViewsOnActivityCreationFromRestringLocale() {
        val locales = listOf(Locale.ENGLISH, Locale.GERMAN)

        for (locale in locales) {
            DefaultLocaleProvider.currentLocale = locale
            shouldInflateAndTransformViewsOnActivityCreation(locale.language)
        }
    }

    private fun shouldInflateAndTransformViewsOnActivityCreation(lang: String) {
        val activityController = Robolectric.buildActivity(TestActivity::class.java)
        val activity = activityController.create().start().resume().visible().get()
        val viewGroup = activity.findViewById<ViewGroup>(R.id.root_container)

        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            when (val view = viewGroup.getChildAt(i)) {
                is TextView -> {
                    view.text shouldStartWith lang
                    view.hint shouldStartWith lang
                }
                is Toolbar -> view.title shouldStartWith lang
                is BottomNavigationView -> {
                    val itemCount = view.menu.size()
                    for (item in 0 until itemCount) {
                        view.menu.getItem(item).title shouldStartWith lang
                        view.menu.getItem(item).titleCondensed shouldStartWith lang
                    }
                }
            }
        }

        activityController.pause().stop().destroy()
    }

    private fun getStrings(locale: Locale) = mapOf(
            "header" to locale.language + "_" + "header",
            "header_hint" to locale.language + "_" + "hint",
            "menu1title" to locale.language + "_" + "Menu 1",
            "menu1titleCondensed" to locale.language + "_" + "Menu1",
            "menu2title" to locale.language + "_" + "Menu 2",
            "menu2titleCondensed" to locale.language + "_" + "Menu2",
            "menu3title" to locale.language + "_" + "Menu 3",
            "menu3titleCondensed" to locale.language + "_" + "Menu3"
    )
}