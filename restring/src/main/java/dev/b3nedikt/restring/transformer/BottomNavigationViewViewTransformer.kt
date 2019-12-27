package dev.b3nedikt.restring.transformer

import android.content.res.Resources
import android.util.AttributeSet
import android.util.Pair
import android.util.Xml
import dev.b3nedikt.restring.ViewTransformer
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.*

/**
 * A transformer which transforms BottomNavigationView: it transforms the texts coming from the menu.
 */
internal object BottomNavigationViewViewTransformer : ViewTransformer<BottomNavigationView> {

    private const val ATTRIBUTE_MENU = "menu"
    private const val ATTRIBUTE_APP_MENU = "app:menu"
    private const val ATTRIBUTE_ID = "id"
    private const val ATTRIBUTE_ANDROID_ID = "android:id"
    private const val ATTRIBUTE_TITLE = "title"
    private const val ATTRIBUTE_ANDROID_TITLE = "android:title"
    private const val ATTRIBUTE_TITLE_CONDENSED = "titleCondensed"
    private const val ATTRIBUTE_ANDROID_TITLE_CONDENSED = "android:titleCondensed"
    private const val XML_MENU = "menu"
    private const val XML_ITEM = "item"

    override val viewType = BottomNavigationView::class.java

    override val supportedAttributes = setOf(ATTRIBUTE_MENU, ATTRIBUTE_APP_MENU, ATTRIBUTE_ID,
            ATTRIBUTE_ANDROID_ID, ATTRIBUTE_TITLE, ATTRIBUTE_ANDROID_TITLE, ATTRIBUTE_TITLE_CONDENSED,
            ATTRIBUTE_ANDROID_TITLE_CONDENSED, XML_MENU, XML_ITEM)

    override fun BottomNavigationView.transform(attrs: Map<String, Int>) {
        val resources = this.context.resources

        for (attributeName in attrs.keys) {
            if (attributeName == ATTRIBUTE_APP_MENU || attributeName == ATTRIBUTE_MENU) {

                val resId = attrs[attributeName] ?: continue
                val itemStrings = getMenuItemsStrings(resources, resId)

                for ((key, value1) in itemStrings) {

                    if (value1.title != 0) {
                        menu.findItem(key).title = resources.getString(value1.title)
                    }
                    if (value1.titleCondensed != 0) {
                        menu.findItem(key).titleCondensed = resources.getString(value1.titleCondensed)
                    }
                }
            }
        }
    }

    private fun getMenuItemsStrings(resources: Resources, resId: Int): Map<Int, MenuItemStrings> {
        val parser = resources.getLayout(resId)
        val attrs = Xml.asAttributeSet(parser)
        return try {
            parseMenu(parser, attrs)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
            HashMap()
        } catch (e: IOException) {
            e.printStackTrace()
            HashMap()
        }

    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseMenu(parser: XmlPullParser, attrs: AttributeSet): Map<Int, MenuItemStrings> {

        val menuItems = mutableMapOf<Int, MenuItemStrings>()
        var eventType = parser.eventType
        var tagName: String

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.name
                if (tagName == XML_MENU) {
                    eventType = parser.next()
                    break
                }

                throw RuntimeException("Expecting menu, got $tagName")
            }
            eventType = parser.next()
        } while (eventType != XmlPullParser.END_DOCUMENT)

        var reachedEndOfMenu = false
        var menuLevel = 0
        while (!reachedEndOfMenu) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    tagName = parser.name
                    if (tagName == XML_ITEM) {
                        val item = parseMenuItem(attrs)
                        if (item != null) {
                            menuItems[item.first] = item.second
                        }
                    } else if (tagName == XML_MENU) {
                        menuLevel++
                    }
                }

                XmlPullParser.END_TAG -> {
                    tagName = parser.name
                    if (tagName == XML_MENU) {
                        menuLevel--
                        if (menuLevel <= 0) {
                            reachedEndOfMenu = true
                        }
                    }
                }

                XmlPullParser.END_DOCUMENT -> reachedEndOfMenu = true
            }

            eventType = parser.next()
        }
        return menuItems
    }

    private fun parseMenuItem(attrs: AttributeSet): Pair<Int, MenuItemStrings>? {
        var menuId = 0
        var menuItemStrings: MenuItemStrings? = null
        val attributeCount = attrs.attributeCount
        for (index in 0 until attributeCount) {
            if (attrs.getAttributeName(index) == ATTRIBUTE_ANDROID_ID
                    || attrs.getAttributeName(index) == ATTRIBUTE_ID) {
                menuId = attrs.getAttributeResourceValue(index, 0)
            } else if (attrs.getAttributeName(index) == ATTRIBUTE_ANDROID_TITLE
                    || attrs.getAttributeName(index) == ATTRIBUTE_TITLE) {
                val value = attrs.getAttributeValue(index)
                if (value == null || !value.startsWith("@")) break
                if (menuItemStrings == null) {
                    menuItemStrings = MenuItemStrings()
                }
                menuItemStrings.title = attrs.getAttributeResourceValue(index, 0)
            } else if (attrs.getAttributeName(index) == ATTRIBUTE_ANDROID_TITLE_CONDENSED
                    || attrs.getAttributeName(index) == ATTRIBUTE_TITLE_CONDENSED) {
                val value = attrs.getAttributeValue(index)
                if (value == null || !value.startsWith("@")) break
                if (menuItemStrings == null) {
                    menuItemStrings = MenuItemStrings()
                }
                menuItemStrings.titleCondensed = attrs.getAttributeResourceValue(index, 0)
            }
        }
        return if (menuId != 0 && menuItemStrings != null)
            Pair(menuId, menuItemStrings)
        else
            null
    }

    private class MenuItemStrings {
        var title: Int = 0
        var titleCondensed: Int = 0
    }
}