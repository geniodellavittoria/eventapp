package ch.mobpro.eventapp.viewmodel.data;

import ch.mobpro.eventapp.dto.JwtTokenResponse;
import ch.mobpro.eventapp.dto.UserCredentials;
import ch.mobpro.eventapp.dto.UserRegistrationForm;
import ch.mobpro.eventapp.service.AuthService;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

import javax.inject.Inject;

public class SessionTokenRepository {

    private AuthService authService;

    private JwtTokenResponse token;

    @Inject
    public SessionTokenRepository(AuthService authService) {
        this.authService = authService;
    }

    public void login(UserCredentials userCredentials) {
        authService.login(userCredentials)
                .subscribeWith(new DisposableSingleObserver<JwtTokenResponse>() {
                    @Override
                    public void onSuccess(JwtTokenResponse value) {
                        token = value;

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("e = [" + e + "]");
                        token = null;
                    }
                });
    }

    public Single<Void> register(UserRegistrationForm userRegistrationForm) {
        return authService.register(userRegistrationForm);
    }

    public String getToken() {
        return token.getToken();
    }
}
