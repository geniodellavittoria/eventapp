package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import ch.mobpro.eventapp.dto.CreateEventForm;
import ch.mobpro.eventapp.dto.CreateEventFormEventMapper;
import ch.mobpro.eventapp.repository.EventRepository;
import ch.mobpro.eventapp.service.EventService;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class CreateEventViewModel extends ViewModel {

    private static final String TAG = CreateEventViewModel.class.getSimpleName();
    private final EventService eventService;

    private MutableLiveData<Boolean> creationSuccess = new MutableLiveData<>();

    private final EventRepository eventRepository;

    private CompositeDisposable disposable;
    public CreateEventForm event = new CreateEventForm();

    @Inject
    public CreateEventViewModel(EventRepository eventRepository, EventService eventService) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        disposable = new CompositeDisposable();
    }

    public void createEvent() {
        CreateEventFormEventMapper mapper = new CreateEventFormEventMapper(event);
        disposable.add(eventService.createEvent(mapper.event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    creationSuccess.postValue(true);
                }, throwable -> {
                    creationSuccess.postValue(false);
                }));
    }


    public MutableLiveData<Boolean> getCreationSuccess() {
        return creationSuccess;
    }


}
