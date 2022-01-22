package pt.ua.tripfinder_android;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class TripListAdapter extends ListAdapter<Trips, TripViewHolder> {

    public TripListAdapter(DiffUtil.ItemCallback<Trips> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TripViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        Trips current = getItem(position);
        holder.bind(current);
    }

    static class TripDiff extends DiffUtil.ItemCallback<Trips> {

        @Override
        public boolean areItemsTheSame(@NonNull Trips oldItem, @NonNull Trips newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame( Trips oldItem, Trips newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}
