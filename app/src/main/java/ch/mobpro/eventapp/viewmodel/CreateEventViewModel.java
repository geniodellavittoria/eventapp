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
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateEventViewModel extends ViewModel {

    private static final String TAG = CreateEventViewModel.class.getSimpleName();
    private final EventService eventService;

    private MutableLiveData<Boolean> creationSuccess = new MutableLiveData<>();
    private MutableLiveData<String> updateEventTitle = new MutableLiveData<>();

    private final EventRepository eventRepository;

    private CompositeDisposable disposable;
    public EventDetailForm event = new EventDetailForm();

    @Inject
    CreateEventViewModel(EventRepository eventRepository, EventService eventService) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
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


    public MutableLiveData<Boolean> getCreationSuccess() {
        return creationSuccess;
    }

    public void updateEventName(CharSequence name, int start, int before, int count) {
        updateEventTitle.postValue(name.toString());
    }

    public LiveData<String> getUpdatedEventName() {
        return updateEventTitle;
    }
}
