package com.tv.uscreen.utils;

import android.text.Editable;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public final class ObjectHelper {
    private ObjectHelper() {
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map) object).isEmpty();
        } else if (object instanceof String) {
            return ((String) object).trim().isEmpty();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else if (object instanceof Editable) {
            return object.toString().isEmpty();
        } else if (object instanceof TextView) {
            return isEmpty(((TextView) object).getText());
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static boolean isExactlySame(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        } else if (str1 != null) {
            return str1.equals(str2);
        } else {
            return false;
        }
    }

    public static boolean isNotExactlySame(String str1, String str2) {
        return !isExactlySame(str1, str2);
    }

    public static boolean isSame(String str1, String str2) {
        return str1 != null && str2 != null && isExactlySame(str1.toLowerCase(),
                str2.toLowerCase());
    }

    public static boolean isNotSame(String str1, String str2) {
        return !isSame(str1, str2);
    }

    public static String getText(TextView textView) {
        if (textView == null || textView.getText() == null) {
            return null;
        } else {
            return textView.getText().toString().trim();
        }
    }

    public static int getSize(Collection<?> collection) {
        return isEmpty(collection) ? 0 : collection.size();
    }

    public static boolean startWith(String str, String startsWith) {
        if (isEmpty(str) || isEmpty(startsWith)) return false;
        return str.toLowerCase(Locale.ROOT).startsWith(startsWith.toLowerCase(Locale.ROOT));
    }
}
