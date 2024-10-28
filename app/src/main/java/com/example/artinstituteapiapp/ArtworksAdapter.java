package com.example.artinstituteapiapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArtworksAdapter extends RecyclerView.Adapter<ArtworksAdapter.ArtworkViewHolder> {
    private static final String TAG = "ArtworksAdapter";
    private final List<Artwork> artworks;
    private final MainActivity mainActivity;

    public ArtworksAdapter(List<Artwork> artworks, MainActivity mainActivity) {
        Log.d(TAG, "ArtworksAdapter: called");
        this.artworks = artworks;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artwork_list_item_view, parent, false);
        view.setOnClickListener(mainActivity);

        return new ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Artwork artwork = artworks.get(position);
        holder.title.setText(artwork.getTitle());
        if (artwork.getImage_id() != null) {
            Glide.with(mainActivity).load(artwork.getThumbnailUrl()).into(holder.image);
        }
        else {
            holder.image.setImageResource(R.drawable.not_available);
        }
    }

    @Override
    public int getItemCount() {
        return artworks.size();
    }


    public static class ArtworkViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ArtworkViewHolder";
        public TextView title;
        public ImageView image;

        public ArtworkViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ArtworkViewHolder: called");
            title = itemView.findViewById(R.id.listItemText);
            image = itemView.findViewById(R.id.listItemImage);
        }
    }

}
