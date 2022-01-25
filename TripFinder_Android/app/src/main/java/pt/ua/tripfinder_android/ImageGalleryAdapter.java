package pt.ua.tripfinder_android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder>
{

    private List<StorageReference> items;

    public ImageGalleryAdapter(List<StorageReference> items){
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        public static  final String tripId = "tripId";

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.

            imageView = (ImageView) v.findViewById(R.id.img_gallery);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public ImageView getImage() {
            return imageView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imagegallery, parent, false);

        return new ImageGalleryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        try {
//            Log.d("msg", "url:" + items.get(position).getDownloadUrl().toString());
//            InputStream is = (InputStream) new URL(items.get(position).getDownloadUrl().toString()).getContent();
//            Drawable d = Drawable.createFromStream(is, "image");
//            holder.getImage().setImageDrawable(d);

            Glide.with(holder.getImage().getContext() /* context */)
                    .load(items.get(position).getDownloadUrl().toString())
                    .into(holder.getImage());

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
