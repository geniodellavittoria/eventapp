package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;
import ch.mobpro.eventapp.dto.JwtTokenResponse;
import ch.mobpro.eventapp.dto.UserCredentials;
import ch.mobpro.eventapp.service.AuthService;
import ch.mobpro.eventapp.viewmodel.data.SessionTokenRepository;
import ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent;
import ch.mobpro.eventapp.viewmodel.validation.PasswordErrorEvent;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

import static ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent.EMPTY;
import static ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent.INVALID_FORMAT;
import static ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent.NONE;
import static ch.mobpro.eventapp.viewmodel.validation.ValidationHelper.hasMinLength;
import static ch.mobpro.eventapp.viewmodel.validation.ValidationHelper.isEmpty;

public class LoginViewModel extends ViewModel {

    private AuthService authService;
    private SessionTokenRepository sessionTokenRepository;

    public UserCredentials userCredentials = new UserCredentials();
    public MutableLiveData<EmailErrorEvent> emailErrorEvent = new MutableLiveData<>();
    public MutableLiveData<PasswordErrorEvent> passwordErrorEvent = new MutableLiveData<>();
    public MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();

    @Inject
    public LoginViewModel(AuthService authService, SessionTokenRepository sessionTokenRepository) {
        this.authService = authService;
        this.sessionTokenRepository = sessionTokenRepository;
    }

    public boolean isPasswordValid() {
        //TODO: Replace this with your own logic
        if (TextUtils.isEmpty(userCredentials.getPassword())) {
            return false;
        }
        return userCredentials.getPassword().length() > 4;
    }

    public boolean validateUsername() {
        emailErrorEvent.setValue(NONE);
        if (isEmpty(userCredentials.getUsername())) {
            emailErrorEvent.setValue(EMPTY);
            return false;
        } else if (!hasMinLength(userCredentials.getUsername(), 4)) {
            emailErrorEvent.setValue(INVALID_FORMAT);
            return false;
        }
        return true;
    }

    public boolean validatePassword() {
        passwordErrorEvent.setValue(PasswordErrorEvent.NONE);
        if (isEmpty(userCredentials.getPassword())) {
            passwordErrorEvent.setValue(PasswordErrorEvent.EMPTY);
            return false;
        } else if (!hasMinLength(userCredentials.getPassword(), 3)) {
            passwordErrorEvent.setValue(PasswordErrorEvent.INVALID);
            return false;
        }
        return true;
    }

    public void login() {
        if (validateUsername() && validatePassword()) {
            sessionTokenRepository.login(userCredentials)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(t -> loginSuccess.setValue(true),
                            throwable -> {
                                Log.e("TAG", "Throwable " + throwable.getMessage());
                                loginSuccess.setValue(false);
                            });
        }
    }
}
