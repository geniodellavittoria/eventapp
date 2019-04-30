package ch.mobpro.eventapp.service;

import android.util.Base64;
import android.util.Log;
import ch.mobpro.eventapp.dto.JwtToken;
import ch.mobpro.eventapp.dto.JwtTokenResponse;
import ch.mobpro.eventapp.utils.JWTUtils;
import ch.mobpro.eventapp.viewmodel.CreateEventViewModel;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private static final String TAG = AuthInterceptor.class.getSimpleName();

    private JwtTokenResponse token;
    private JwtToken jwtTokenData;

    private static final AuthInterceptor INSTANCE;

    private AuthInterceptor() {

    }

    static {
        INSTANCE = new AuthInterceptor();
    }

    public static AuthInterceptor getInstance() {
        return INSTANCE;
    }

    public void setToken(JwtTokenResponse token) {
        this.token = token;
        getJwtTokenData();
    }

    private void getJwtTokenData() {
        if (token == null) {
            return;
        }
        try {
            jwtTokenData = JWTUtils.decoded(token.getToken());
        } catch (Exception e) {
            Log.e(TAG, "Could not get token information from jwt token", e);
            jwtTokenData = null;
        }
    }

    public String getToken() {
        return token.getToken();
    }

    public String getUsername() {
        if (jwtTokenData != null) {
            return jwtTokenData.sub;
        }
        return "";
    }

    public String getSurname() {
        if (jwtTokenData != null) {
            return jwtTokenData.surname;
        }
        return "";
    }

    public String getName() {
        if (jwtTokenData != null) {
            return jwtTokenData.name;
        }
        return "";
    }

    public boolean isLoggedIn() {
        return token != null;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (original.url().encodedPath().contains("/login") && original.method().equals("POST")
                || (original.url().encodedPath().contains("/register") && original.method().equals("POST"))
        ) {
            return chain.proceed(original);
        }

        if (token != null) {

            HttpUrl originalHttpUrl = original.url();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + token.getToken())
                    .url(originalHttpUrl);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
        return chain.proceed(original);
    }
}
