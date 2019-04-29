package ch.mobpro.eventapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.model.Event;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.EventCardViewHolder> {

    DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                    .withLocale( Locale.UK )
                    .withZone( ZoneId.systemDefault() );


    private List<Event> events;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EventCardViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView eventImage;
        public TextView eventNameText;
        public TextView eventCategoryText;
        public TextView eventAvailablePlaceText;
        public TextView eventPriceText;
        public TextView eventDateText;
        public TextView eventLocationText;

        public EventCardViewHolder(View view) {
            super(view);
            eventImage = view.findViewById(R.id.event_image);
            eventCategoryText = view.findViewById(R.id.event_category_text);
            eventNameText = view.findViewById(R.id.event_name_text);
            eventAvailablePlaceText = view.findViewById(R.id.event_available_place_text);
            eventPriceText = view.findViewById(R.id.event_price_text);
            eventDateText = view.findViewById(R.id.event_date_text);
            eventLocationText = view.findViewById(R.id.event_location_text);
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new EventCardViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull EventCardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.eventNameText.setText(events.get(position).getName());
        if (!events.get(position).getCategories().isEmpty()) {
            holder.eventCategoryText.setText(events.get(position).getCategories().get(0).getCategory());
        }
        holder.eventPriceText.setText(String.format("Eintritt: CHF %1$,.2f", events.get(position).getPrice()));
        holder.eventAvailablePlaceText.setText("/" + events.get(position).getPlace().toString());
        String eventDate = formatter.format(events.get(position).getStartTime()) + " - " +
                formatter.format(events.get(position).getEndTime());
        holder.eventDateText.setText(eventDate);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.size();
    }
}