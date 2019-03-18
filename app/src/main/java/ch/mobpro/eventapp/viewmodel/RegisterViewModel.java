package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import ch.mobpro.eventapp.dto.UserRegistrationForm;
import ch.mobpro.eventapp.repository.SessionTokenRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class RegisterViewModel extends ViewModel {
    private static final String TAG = RegisterViewModel.class.getSimpleName();

    private SessionTokenRepository sessionTokenRepository;

    public UserRegistrationForm userRegistrationForm = new UserRegistrationForm();
    public String confirmPassword = null;

    private MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();

    @Inject
    public RegisterViewModel(SessionTokenRepository sessionTokenRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
    }

    public void register() {
        sessionTokenRepository.register(userRegistrationForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((t) -> registrationSuccess.setValue(true), t -> {
                    Log.e(TAG, "Throwable " + t.getMessage());
                    registrationSuccess.setValue(false);
                })
                .dispose();
    }
}
