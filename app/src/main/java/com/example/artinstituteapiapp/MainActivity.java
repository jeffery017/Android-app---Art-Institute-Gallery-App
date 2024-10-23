package com.example.artinstituteapiapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artinstituteapiapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private final ArrayList<Artwork> artworks = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArtworksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // recyclerView
        recyclerView = binding.artworkRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArtworksAdapter(artworks, this);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreate: set");

        // set btn onclick function
    }

    @Override
    public void onClick(View view) {
        int selectedPosition = recyclerView.getChildAdapterPosition(view);
        Artwork selectedArtwork = artworks.get(selectedPosition);

        Intent intent = new Intent(this, ArtworkActivity.class);
        intent.putExtra("artwork", selectedArtwork);
        startActivity(intent);
    }

    public void searchArtworks(View view) {
        String query = binding.searchBarEditText.getText().toString();
        //Toast.makeText(this, String.format("Search query: %s", query), Toast.LENGTH_SHORT).show();
        // Volley request artworks data
        ArtworkDownloader.downloadArtworks(this, query);

    }

    public void clearArtworks() {
        artworks.clear();
        adapter.notifyItemRangeChanged(0, artworks.size());
    }

    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
        adapter.notifyItemRangeChanged(0, artworks.size());
    }

    // define btn onclick function

    // bundleSave

    // bundleRestore

    // Volley request artworks data

}