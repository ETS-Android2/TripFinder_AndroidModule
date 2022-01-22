package pt.ua.tripfinder_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;

public class TripViewHolder extends RecyclerView.ViewHolder {
    private final TextView trip_descrp;
    private final TextView tripName;
    private final ImageView trip_image;
    private int id;
    public static  final String tripId = "tripId";

    public TripViewHolder(View itemView) {
        super(itemView);
        tripName = itemView.findViewById(R.id.tripName);
        trip_descrp = itemView.findViewById(R.id.trip_descrp);
        trip_image = itemView.findViewById(R.id.trip_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Holder", "Element " + getAdapterPosition() + " clicked.");
                v.getContext().startActivity(new Intent(v.getContext(), TripInfo_Activity.class).putExtra(tripId, String.valueOf(id)));

            }
        });
    }

    public void bind(Trips trip) {
        tripName.setText(trip.getTitle());
        trip_descrp.setText(trip.getContentShort());
        id = trip.getId();
        new DownloadImageTask(trip_image)
                .execute(trip.getImageurl());
    }

    static TripViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trips, parent, false);
        return new TripViewHolder(view);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
