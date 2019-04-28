package ch.mobpro.eventapp.utils;

import android.util.Base64;
import android.util.Log;
import ch.mobpro.eventapp.dto.JwtToken;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class JWTUtils {

    public static JwtToken decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
//            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            JwtToken jwtToken = getJson(split[1]);
            Log.d("JWT_DECODED", "Body: " + jwtToken);
            return jwtToken;
        } catch (UnsupportedEncodingException e) {
            Log.e("JWT_DECODED", "Could not decode JWT", e);
            return null;
        }
    }

    private static JwtToken getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        String jwt = new String(decodedBytes, StandardCharsets.UTF_8);
        return new Gson().fromJson(jwt, JwtToken.class);
    }
}