package ch.mobpro.eventapp.viewmodel.validation;

import android.text.TextUtils;

public class ValidationHelper {

    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    public static boolean hasMinLength(String text, int length) {
        if (!isEmpty(text)) {
            return text.length() > length;
        }
        return false;
    }
}
