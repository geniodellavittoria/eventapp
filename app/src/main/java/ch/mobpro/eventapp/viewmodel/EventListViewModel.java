package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.repository.EventRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class EventListViewModel extends ViewModel {

    private static final String TAG = EventListViewModel.class.getSimpleName();

    private EventRepository eventRepository;

    private final MutableLiveData<List<Event>> events = new MutableLiveData<>();
    private final MutableLiveData<Boolean> eventLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final CompositeDisposable disposable;

    @Inject
    EventListViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        disposable = new CompositeDisposable();
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

    public void loadEvents() {
        disposable.add(eventRepository.getEvents().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Event>>() {
                    @Override
                    public void onSuccess(List<Event> value) {
                        eventLoadError.setValue(false);
                        events.setValue(value);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error on loading events occurred:", e);
                        eventLoadError.setValue(true);
                        loading.setValue(false);
                    }
                }));
    }

    public void updateEvent(Event event) {
        List<Event> eventList = events.getValue();
        Optional<Event> eventOptional = events.getValue().stream()
                .filter(e -> e.getId().equals(event.getId()))
                .findFirst();
        if (eventList != null && eventOptional.isPresent()) {
            int index = eventList.indexOf(eventOptional.get());
            eventList.set(index, event);
            events.postValue(eventList);
        }
    }

    public void deleteEvent(String id) {
        List<Event> eventList = events.getValue();
        if (eventList != null) {
            eventList.removeIf(e -> e.getId().equals(id));
            events.postValue(eventList);
        }
    }

    public void addEvent(Event event) {
        List<Event> eventList = events.getValue();
        if (eventList != null) {
            eventList.add(event);
            events.postValue(eventList);
        }
    }
}
