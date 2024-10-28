package com.example.artinstituteapiapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artinstituteapiapp.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private final ArrayList<Artwork> artworks = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArtworksAdapter adapter;
    private ConnectivityManager connectivityManager;

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

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        setBackground();

        // recyclerView
        recyclerView = binding.artworkRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArtworksAdapter(artworks, this);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreate: set");

        // set copyright link
        binding.mainCopyrightText.setPaintFlags(
                binding.mainCopyrightText.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG
        );
    }

    @Override
    public void onClick(View view) {
        if (!isNetworkConnected()) {
            showNoNetworkDialog();
            return;
        }
        int selectedPosition = recyclerView.getChildAdapterPosition(view);
        Artwork selectedArtwork = artworks.get(selectedPosition);

        Intent intent = new Intent(this, ArtworkActivity.class);
        intent.putExtra("artwork", selectedArtwork);
        startActivity(intent);
    }

    public void openCopyright(View view) {
        Intent intent = new Intent(this, CopyrightActivity.class);
        startActivity(intent);
    }

    public void handleSearchArtworksByQuery(View view) {
        if (!isNetworkConnected()) {
            showNoNetworkDialog();
            return;
        }
        String query = binding.searchBarEditText.getText().toString().trim();
        // if query is empty, do nothing
        if (query.isEmpty()) {
            return;
        }

        // remove background
        removeBackground();

        if (query.length() < 3) {
            showQueryTooShortDialog();
        }
        else {
            binding.progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "searchArtworks: " + query);
            // Volley request artworks data
            ArtworkDownloader.downloadArtworksByQuery(this, query);
        }

    }


    public void handleGetRandomArtwork(View view) {
        if (!isNetworkConnected()) {
            showNoNetworkDialog();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        removeBackground();
        ArtworkDownloader.downloadRandomArtwork(this);
    }

     public boolean isNetworkConnected() {
        Network currentNetwork = connectivityManager.getActiveNetwork();
        return currentNetwork != null;
     }

    public void updateArtworks(List<Artwork> artworks) {
        this.artworks.clear();
        binding.progressBar.setVisibility(View.GONE);
        if (artworks == null || artworks.isEmpty()) {
            // show the background
            showNotResultFoundDialog();
        }
        else {
            removeBackground();
            this.artworks.addAll(artworks);
            adapter.notifyDataSetChanged();
        }
    }

     // Alert Dialogs
    public void showQueryTooShortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("Search string too short");
        builder.setMessage("Please try a longer search string");
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            setBackground();
        });
        builder.setCancelable(false);
        builder.show();
    }

     public void showNoNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("NoConnectionError");
        builder.setMessage("No network connection present - cannot contact Art Institute API server.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
     }

    public void showNotResultFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("No search results found");
        builder.setMessage( String.format("No results for '%s'. Please try another search query.", binding.searchBarEditText.getText().toString().trim()));
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            setBackground();
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("artworks", artworks.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Gson gson = new Gson();
        String jsonString = savedInstanceState.getString("artworks");
        Artwork[] artworks = gson.fromJson(jsonString, Artwork[].class);
        if (artworks.length > 0) {
            updateArtworks(Arrays.asList(artworks));
        }
    }

    private void setBackground() {
        binding.artworkRecyclerView.setAlpha(0.2f);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.artworkRecyclerView.setBackgroundResource(R.drawable.bwlions);
        }
        else {
            binding.artworkRecyclerView.setBackgroundResource(R.drawable.bwlions_land);
        }
    }

    private void removeBackground() {
        binding.artworkRecyclerView.setAlpha(1.0f);
        binding.artworkRecyclerView.setBackground(null);
    }

}