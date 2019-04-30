package ch.mobpro.eventapp.service;

import ch.mobpro.eventapp.dto.JwtTokenResponse;
import ch.mobpro.eventapp.dto.UserCredentials;
import ch.mobpro.eventapp.dto.UserRegistrationForm;
import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static ch.mobpro.eventapp.service.RestClientConstants.AUTH;
import static ch.mobpro.eventapp.service.RestClientConstants.AUTH_LOGIN;
import static ch.mobpro.eventapp.service.RestClientConstants.REGISTER;

public interface AuthService {

    @POST(AUTH + AUTH_LOGIN)
    Single<JwtTokenResponse> login(@Body UserCredentials userCredentials);

    @POST(AUTH + REGISTER)
    Maybe<Void> register(@Body UserRegistrationForm userRegistrationForm);
}
