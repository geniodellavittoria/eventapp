package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import ch.mobpro.eventapp.dto.EventDetailForm;
import ch.mobpro.eventapp.dto.CreateEventFormEventMapper;
import ch.mobpro.eventapp.model.User;
import ch.mobpro.eventapp.repository.EventRepository;
import ch.mobpro.eventapp.service.AuthInterceptor;
import ch.mobpro.eventapp.service.EventService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;

import static ch.mobpro.eventapp.utils.Base64BitmapUtil.getBase64FromStream;

public class CreateEventViewModel extends ViewModel {

    private static final String TAG = CreateEventViewModel.class.getSimpleName();
    private final EventService eventService;

    private MutableLiveData<Boolean> creationSuccess = new MutableLiveData<>();
    private MutableLiveData<String> updateEventTitle = new MutableLiveData<>();
    private MutableLiveData<Boolean> onEventImageSelected = new MutableLiveData<>();

    private CompositeDisposable disposable;
    public EventDetailForm event = new EventDetailForm();

    @Inject
    CreateEventViewModel(EventService eventService) {
        this.eventService = eventService;
        disposable = new CompositeDisposable();
    }

    public void createEvent() {
        CreateEventFormEventMapper eventMapper = new CreateEventFormEventMapper(event);
        eventMapper.event.setUsername(AuthInterceptor.getInstance().getUsername());
        disposable.add(eventService.createEvent(eventMapper.event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    creationSuccess.postValue(true);
                }, throwable -> {
                    creationSuccess.postValue(false);
                    Log.e(TAG, "Error occurred", throwable);
                }));
    }

    public void chooseEventImage() {
        onEventImageSelected.postValue(true);
    }

    public MutableLiveData<Boolean> getCreationSuccess() {
        return creationSuccess;
    }

    public void updateEventName(CharSequence name, int start, int before, int count) {
        updateEventTitle.postValue(name.toString());
    }

    public MutableLiveData<Boolean> getOnEventImageSelected() {
        return onEventImageSelected;
    }

    public LiveData<String> getUpdatedEventName() {
        return updateEventTitle;
    }

    public void storeEventImage(InputStream dataStream) throws IOException {
        event.setEventImage(getBase64FromStream(dataStream));
    }
}
