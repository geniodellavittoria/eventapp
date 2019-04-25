package ch.mobpro.eventapp.repository;

import android.app.Application;
import ch.mobpro.eventapp.App;
import ch.mobpro.eventapp.di.component.ApplicationComponent;
import ch.mobpro.eventapp.di.component.DaggerApplicationComponent;
import ch.mobpro.eventapp.dto.JwtTokenResponse;
import ch.mobpro.eventapp.dto.UserCredentials;
import ch.mobpro.eventapp.dto.UserRegistrationForm;
import ch.mobpro.eventapp.model.User;
import ch.mobpro.eventapp.service.AuthInterceptor;
import ch.mobpro.eventapp.service.AuthService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class SessionTokenRepository{

    private AuthService authService;
    private AuthInterceptor authInterceptor;

    @Inject
    public SessionTokenRepository(AuthService authService) {
        this.authService = authService;
        this.authInterceptor = AuthInterceptor.getInstance();
    }

    public Observable<Boolean> login(UserCredentials userCredentials) {
        return authService.login(userCredentials)
                .flatMap(jwtToken -> {
                    if (jwtToken != null) {
                        authInterceptor.setToken(jwtToken);
                        return Single.just(true);
                    }
                    return Single.just(true);
                })
                .toObservable();
    }

    public Single<Void> register(UserRegistrationForm userRegistrationForm) {
        return authService.register(userRegistrationForm);
    }

    public String getUsername() {
        return authInterceptor.getUsername();
    }

    public String getSurname() {
        return authInterceptor.getSurname();
    }

    public String getName() {
        return authInterceptor.getName();
    }

}
