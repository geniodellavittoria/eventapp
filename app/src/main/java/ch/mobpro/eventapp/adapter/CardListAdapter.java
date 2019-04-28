package ch.mobpro.eventapp.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.model.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.EventCardViewHolder> {
    private List<Event> events;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EventCardViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public TextView eventNameText;
        public EventCardViewHolder(CardView cardView) {
            super(cardView);
            imageView = cardView.findViewById(R.id.event_image);
            eventNameText = cardView.findViewById(R.id.event_name_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardListAdapter(List<Event> events) {
        this.events = events;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public EventCardViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new EventCardViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull EventCardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.eventNameText.setText(events.get(position).getName());
        //holder.imageView.setIma

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.size();
    }
}