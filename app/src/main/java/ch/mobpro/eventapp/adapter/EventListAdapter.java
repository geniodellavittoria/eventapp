package ch.mobpro.eventapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.viewmodel.EventListViewModel;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private List<Event> events;

    public EventListAdapter(List<Event> eventDataset) {
        events = eventDataset;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView v = (TextView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_event, viewGroup, false);

        EventViewHolder vh = new EventViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        eventViewHolder.textView.setText(events.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return events.size();

    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public EventViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

}
