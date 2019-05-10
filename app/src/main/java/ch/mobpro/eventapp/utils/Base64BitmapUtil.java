package ch.mobpro.eventapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.google.android.gms.common.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Base64BitmapUtil {

    public static Bitmap getBitmapFromString(String imageContent) {
        if (imageContent != null) {
            byte[] decodedString = Base64.decode(imageContent, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null;
    }

    public static String getBase64FromStream(InputStream dataStream) throws IOException {
        byte[] bytes = IOUtils.toByteArray(dataStream);
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }
}
