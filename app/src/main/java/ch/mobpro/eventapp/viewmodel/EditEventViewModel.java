package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import ch.mobpro.eventapp.dto.CreateEventFormEventMapper;
import ch.mobpro.eventapp.dto.EventDetailForm;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.repository.EventRepository;
import ch.mobpro.eventapp.service.EventService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class EditEventViewModel extends ViewModel {

    private final EventRepository eventRepository;
    private static final String TAG = EditEventViewModel.class.getSimpleName();
    private final EventService eventService;
    private final CompositeDisposable disposable;

    public EventDetailForm eventDetails = new EventDetailForm();
    private MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();

    @Inject
    public EditEventViewModel(EventRepository eventRepository, EventService eventService) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        disposable = new CompositeDisposable();
    }

    public void editEvent() {
        CreateEventFormEventMapper eventMapper = new CreateEventFormEventMapper(eventDetails);
        disposable.add(eventService.updateEvent(eventMapper.event.getId(), eventMapper.event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    updateSuccess.postValue(true);
                }, throwable -> {
                    updateSuccess.postValue(false);
                    Log.e(TAG, "Error occurred", throwable);
                }));

    }

    public MutableLiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }
}
