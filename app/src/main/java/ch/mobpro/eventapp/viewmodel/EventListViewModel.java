package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.repository.EventRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.List;

public class EventListViewModel extends ViewModel {


    private EventRepository eventRepository;

    private final MutableLiveData<List<Event>> events = new MutableLiveData<>();
    private final MutableLiveData<Boolean> eventLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Inject
    EventListViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        loadEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public LiveData<Boolean> getError() {
        return eventLoadError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    private void loadEvents() {
        eventRepository.getEvents().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<Event>>() {
            @Override
            public void onSuccess(List<Event> value) {
                eventLoadError.setValue(false);
                events.setValue(value);
                loading.setValue(false);
            }

            @Override
            public void onError(Throwable e) {
                eventLoadError.setValue(true);
                loading.setValue(false);
            }
        })
                .dispose();
    }
}
