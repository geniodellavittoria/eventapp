package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.dto.UserCredentials;
import ch.mobpro.eventapp.repository.SessionTokenRepository;
import ch.mobpro.eventapp.ui.LayoutActivityId;
import ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent;
import ch.mobpro.eventapp.viewmodel.validation.PasswordErrorEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.inject.Inject;

import static ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent.EMPTY;
import static ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent.INVALID_FORMAT;
import static ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent.NONE;
import static ch.mobpro.eventapp.viewmodel.validation.ValidationHelper.hasMinLength;
import static ch.mobpro.eventapp.viewmodel.validation.ValidationHelper.isEmpty;

public class LoginViewModel extends ViewModel {

    private final String TAG = this.getClass().getSimpleName();

    private CompositeDisposable disposable;
    private SessionTokenRepository sessionTokenRepository;

    public UserCredentials userCredentials = new UserCredentials();
    public MutableLiveData<EmailErrorEvent> emailErrorEvent = new MutableLiveData<>();
    public MutableLiveData<PasswordErrorEvent> passwordErrorEvent = new MutableLiveData<>();
    public MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public SingleLiveData<LayoutActivityId> layoutActivity = new SingleLiveData<>();

    @Inject
    public LoginViewModel(SessionTokenRepository sessionTokenRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
        disposable = new CompositeDisposable();
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
        isLoading.setValue(true);
        if (validateUsername() && validatePassword()) {
            Log.i(TAG, "Form is valid");
            disposable.add(sessionTokenRepository.login(userCredentials)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    /*.subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onSubscribe(Subscription s) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            loginSuccess.postValue(true);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.e("TAG", "Throwable " + t.getMessage());
                            loginSuccess.postValue(false);
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete");
                        }
                    });*/
                    .subscribe(t -> {
                                Log.i(TAG, "Login succeded!");
                                loginSuccess.postValue(true);
                                isLoading.postValue(false);
                            },
                            throwable -> {
                                Log.e(TAG, "Throwable " + throwable.getMessage());
                                loginSuccess.postValue(false);
                                isLoading.postValue(false);
                            }));
        } else {
            isLoading.setValue(false);
        }
    }

    public void register() {
        layoutActivity.setValue(LayoutActivityId.REGISTER_ACTIVITY);
    }
}
