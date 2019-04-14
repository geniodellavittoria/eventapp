package ch.mobpro.eventapp.repository;

import ch.mobpro.eventapp.dto.JwtTokenResponse;
import ch.mobpro.eventapp.dto.UserCredentials;
import ch.mobpro.eventapp.dto.UserRegistrationForm;
import ch.mobpro.eventapp.model.User;
import ch.mobpro.eventapp.service.AuthService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.reactivex.Observable;
import io.reactivex.Single;

import javax.inject.Inject;

public class SessionTokenRepository {

    private AuthService authService;

    private JwtTokenResponse token;

    private User user;

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
    }

    public Single<Void> register(UserRegistrationForm userRegistrationForm) {
        return authService.register(userRegistrationForm);
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
}
