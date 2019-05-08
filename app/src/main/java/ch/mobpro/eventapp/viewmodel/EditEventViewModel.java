package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import ch.mobpro.eventapp.dto.CreateEventFormEventMapper;
import ch.mobpro.eventapp.dto.EventDetailForm;
import ch.mobpro.eventapp.dto.EventRegistrationForm;
import ch.mobpro.eventapp.model.EventRegistrationCategory;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.repository.EventRepository;
import ch.mobpro.eventapp.repository.SessionTokenRepository;
import ch.mobpro.eventapp.service.EventRegistrationService;
import ch.mobpro.eventapp.service.EventService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class EditEventViewModel extends ViewModel {

    private static final String TAG = EditEventViewModel.class.getSimpleName();
    private final EventService eventService;
    private final CompositeDisposable disposable;
    private final SessionTokenRepository sessionTokenRepository;
    private final EventRegistrationService eventRegistrationService;

    public Event event;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;

    private CreateEventFormEventMapper mapper;

    private MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private MutableLiveData<String> updateEventTitle = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> eventRegistrationSuccess = new MutableLiveData<>();

    @Inject
    public EditEventViewModel(EventService eventService,
                              EventRegistrationService eventRegistrationService,
                              SessionTokenRepository sessionTokenRepository) {
        this.eventService = eventService;
        this.eventRegistrationService = eventRegistrationService;
        this.sessionTokenRepository = sessionTokenRepository;
        disposable = new CompositeDisposable();
    }

    public void editEvent() {
        event.setStartTime(LocalDateTime.of(startDate, startTime).toInstant(ZoneOffset.UTC));
        event.setEndTime(LocalDateTime.of(endDate, endTime).toInstant(ZoneOffset.UTC));
        disposable.add(eventService.updateEvent(event.getId(), event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    updateSuccess.postValue(true);
                }, throwable -> {
                    updateSuccess.postValue(false);
                    Log.e(TAG, "Error occurred", throwable);
                }));

    }

    public void deleteEvent() {
        disposable.add(eventService.deleteEvent(event.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    deleteSuccess.postValue(true);
                }, throwable -> {
                    deleteSuccess.postValue(false);
                    Log.e(TAG, "Error occurred", throwable);
                }));
    }

    public void registerEvent() {
        EventRegistrationForm eventRegistrationForm = new EventRegistrationForm();
        eventRegistrationForm.setUsername(sessionTokenRepository.getUsername());
        eventRegistrationForm.setPaidPrice(event.getPrice());

        disposable.add(eventRegistrationService.createEventRegistration(event.getId(), eventRegistrationForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    eventRegistrationSuccess.postValue(true);
                }, throwable -> {
                    eventRegistrationSuccess.postValue(false);
                    Log.e(TAG, "Error occurred", throwable);
                }));
    }

    public void updateEventName(CharSequence name, int start, int before, int count) {
        updateEventTitle.postValue(name.toString());
    }

    public MutableLiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public LiveData<String> getUpdatedEventName() {
        return updateEventTitle;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public MutableLiveData<Boolean> getDeleteSuccess() {
        return deleteSuccess;
    }

    public MutableLiveData<Boolean> getEventRegistrationSuccess() {
        return eventRegistrationSuccess;
    }
}
