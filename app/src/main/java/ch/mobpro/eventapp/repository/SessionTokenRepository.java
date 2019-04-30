package ch.mobpro.eventapp.repository;

import ch.mobpro.eventapp.dto.UserCredentials;
import ch.mobpro.eventapp.dto.UserRegistrationForm;
import ch.mobpro.eventapp.service.AuthInterceptor;
import ch.mobpro.eventapp.service.AuthService;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    public Maybe<Void> register(UserRegistrationForm userRegistrationForm) {
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
