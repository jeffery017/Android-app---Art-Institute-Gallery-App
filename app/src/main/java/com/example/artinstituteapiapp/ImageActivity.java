package com.example.artinstituteapiapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.artinstituteapiapp.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {
    private ActivityImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        Artwork artwork = (Artwork) intent.getSerializableExtra("artwork");
        if (artwork != null) {
            binding.imageViewTitleText.setText(artwork.getTitle());
            binding.imageViewArtistText.setText(artwork.getArtist_display());

            Glide.with(this).load(artwork.getFullImageUrl()).into(binding.imageViewImage);
        }
    }
}