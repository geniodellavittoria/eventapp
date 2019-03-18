package ch.mobpro.eventapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import ch.mobpro.eventapp.repository.EventRepository;

import javax.inject.Inject;

public class EditEventViewModel extends ViewModel {

    private final EventRepository eventRepository;

    @Inject
    public EditEventViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}
