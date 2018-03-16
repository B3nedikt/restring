package com.ice.restring;

import java.util.Locale;

class RestringUtil {

    static String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }
}
