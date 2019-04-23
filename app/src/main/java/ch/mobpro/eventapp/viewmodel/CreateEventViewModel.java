package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import ch.mobpro.eventapp.dto.CreateEventForm;
import ch.mobpro.eventapp.dto.CreateEventFormEventMapper;
import ch.mobpro.eventapp.repository.EventRepository;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class CreateEventViewModel extends ViewModel {

    private static final String TAG = CreateEventViewModel.class.getSimpleName();

    private MutableLiveData<Boolean> creationSuccess = new MutableLiveData<>();

    private final EventRepository eventRepository;

    public CreateEventForm event = new CreateEventForm();

    @Inject
    public CreateEventViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void createEvent() {
        CreateEventFormEventMapper mapper = new CreateEventFormEventMapper(event);
        eventRepository.createEvent(mapper.event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        creationSuccess.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error occured: ", e);
                        creationSuccess.setValue(false);
                    }
                });
    }


    public MutableLiveData<Boolean> getCreationSuccess() {
        return creationSuccess;
    }


}
