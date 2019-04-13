package ch.mobpro.eventapp.repository;

import ch.mobpro.eventapp.dto.JwtTokenResponse;
import ch.mobpro.eventapp.dto.UserCredentials;
import ch.mobpro.eventapp.dto.UserRegistrationForm;
import ch.mobpro.eventapp.service.AuthService;
import io.reactivex.Observable;
import io.reactivex.Single;

import javax.inject.Inject;

public class SessionTokenRepository {

    private AuthService authService;

    private JwtTokenResponse token;

    @Inject
    public SessionTokenRepository(AuthService authService) {
        this.authService = authService;
    }

    public Observable<Boolean> login(UserCredentials userCredentials) {
        return authService.login(userCredentials)
                .map(jwtToken -> token = jwtToken)
                .flatMap(jwtToken -> {
                    if (token != null) {
                        return Single.just(true);
                    }
                    return Single.just(true);
                })
                .toObservable();
//                .toFlowable();
    }

    public Single<Void> register(UserRegistrationForm userRegistrationForm) {
        return authService.register(userRegistrationForm);
    }

    public String getToken() {
        return token.getToken();
    }
}
