package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.dto.JwtTokenResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private JwtTokenResponse token;

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
    }

    public String getToken() {
        return token.getToken();
    }

    public String getUsername() {
        if (token != null) {
            DecodedJWT jwt = JWT.decode(token.getToken());
            return jwt.getSubject();
        }
        return "";
    }

    public String getSurname() {
        if (token != null) {
            DecodedJWT jwt = JWT.decode(token.getToken());
            return jwt.getClaim("surname").asString();
        }
        return "";
    }

    public String getName() {
        if (token != null) {
            DecodedJWT jwt = JWT.decode(token.getToken());
            return jwt.getClaim("name").asString();
        }
        return "";
    }

    public boolean isLoggedIn() {
        return token != null;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (original.url().encodedPath().contains("/login") && original.method().equals("post")
                || (original.url().encodedPath().contains("/register") && original.method().equals("post"))
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
