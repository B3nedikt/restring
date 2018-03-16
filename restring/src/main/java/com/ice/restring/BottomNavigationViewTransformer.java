package com.ice.restring;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Xml;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A transformer which transforms BottomNavigationView: it transforms the texts coming from the menu.
 */
class BottomNavigationViewTransformer implements ViewTransformerManager.Transformer {

    private static final String ATTRIBUTE_MENU = "menu";
    private static final String ATTRIBUTE_APP_MENU = "app:menu";
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_ANDROID_ID = "android:id";
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_ANDROID_TITLE = "android:title";
    private static final String ATTRIBUTE_TITLE_CONDENSED = "titleCondensed";
    private static final String ATTRIBUTE_ANDROID_TITLE_CONDENSED = "android:titleCondensed";
    private static final String XML_MENU = "menu";
    private static final String XML_ITEM = "item";

    @Override
    public Class<? extends View> getViewType() {
        return BottomNavigationView.class;
    }

    @Override
    public View transform(View view, AttributeSet attrs) {
        if (view == null || !getViewType().isInstance(view)) {
            return view;
        }
        Resources resources = view.getContext().getResources();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view;

        for (int index = 0; index < attrs.getAttributeCount(); index++) {
            String attributeName = attrs.getAttributeName(index);
            switch (attributeName) {
                case ATTRIBUTE_APP_MENU:
                case ATTRIBUTE_MENU: {
                    String value = attrs.getAttributeValue(index);
                    if (value == null || !value.startsWith("@")) break;

                    int resId = attrs.getAttributeResourceValue(index, 0);
                    Map<Integer, MenuItemStrings> itemStrings = getMenuItemsStrings(resources, resId);

                    for (Map.Entry<Integer, MenuItemStrings> entry : itemStrings.entrySet()) {

                        if (entry.getValue().title != 0) {
                            bottomNavigationView.getMenu().findItem(entry.getKey()).setTitle(
                                    resources.getString(entry.getValue().title)
                            );
                        }
                        if (entry.getValue().titleCondensed != 0) {
                            bottomNavigationView.getMenu().findItem(entry.getKey()).setTitleCondensed(
                                    resources.getString(entry.getValue().titleCondensed)
                            );
                        }
                    }

                    break;
                }
            }
        }

        return view;
    }

    private Map<Integer, MenuItemStrings> getMenuItemsStrings(Resources resources, int resId) {
        XmlResourceParser parser = resources.getLayout(resId);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        try {
            return parseMenu(parser, attrs);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private Map<Integer, MenuItemStrings> parseMenu(XmlPullParser parser, AttributeSet attrs)
            throws XmlPullParserException, IOException {

        Map<Integer, MenuItemStrings> menuItems = new HashMap<>();
        int eventType = parser.getEventType();
        String tagName;

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.getName();
                if (tagName.equals(XML_MENU)) {
                    eventType = parser.next();
                    break;
                }

                throw new RuntimeException("Expecting menu, got " + tagName);
            }
            eventType = parser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);

        boolean reachedEndOfMenu = false;
        int menuLevel = 0;
        while (!reachedEndOfMenu) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tagName = parser.getName();
                    if (tagName.equals(XML_ITEM)) {
                        Pair<Integer, MenuItemStrings> item = parseMenuItem(attrs);
                        if (item != null) {
                            menuItems.put(item.first, item.second);
                        }
                    } else if (tagName.equals(XML_MENU)) {
                        menuLevel++;
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if (tagName.equals(XML_MENU)) {
                        menuLevel--;
                        if (menuLevel <= 0) {
                            reachedEndOfMenu = true;
                        }
                    }
                    break;

                case XmlPullParser.END_DOCUMENT:
                    reachedEndOfMenu = true;
            }

            eventType = parser.next();
        }
        return menuItems;
    }

    private Pair<Integer, MenuItemStrings> parseMenuItem(AttributeSet attrs) {
        int menuId = 0;
        MenuItemStrings menuItemStrings = null;
        int attributeCount = attrs.getAttributeCount();
        for (int index = 0; index < attributeCount; index++) {
            switch (attrs.getAttributeName(index)) {
                case ATTRIBUTE_ANDROID_ID:
                case ATTRIBUTE_ID: {
                    menuId = attrs.getAttributeResourceValue(index, 0);
                    break;
                }
                case ATTRIBUTE_ANDROID_TITLE:
                case ATTRIBUTE_TITLE: {
                    String value = attrs.getAttributeValue(index);
                    if (value == null || !value.startsWith("@")) break;
                    if (menuItemStrings == null) {
                        menuItemStrings = new MenuItemStrings();
                    }
                    menuItemStrings.title = attrs.getAttributeResourceValue(index, 0);
                    break;
                }
                case ATTRIBUTE_ANDROID_TITLE_CONDENSED:
                case ATTRIBUTE_TITLE_CONDENSED: {
                    String value = attrs.getAttributeValue(index);
                    if (value == null || !value.startsWith("@")) break;
                    if (menuItemStrings == null) {
                        menuItemStrings = new MenuItemStrings();
                    }
                    menuItemStrings.titleCondensed = attrs.getAttributeResourceValue(index, 0);
                    break;
                }
            }
        }
        return (menuId != 0 && menuItemStrings != null)
                ? new Pair<>(menuId, menuItemStrings)
                : null;
    }

    private static class MenuItemStrings {
        public int title;
        public int titleCondensed;
    }
}
