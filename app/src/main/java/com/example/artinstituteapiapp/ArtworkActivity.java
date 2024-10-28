package com.example.artinstituteapiapp;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.artinstituteapiapp.databinding.ActivityArtworkBinding;

public class ArtworkActivity extends AppCompatActivity {
    private static final String TAG = "ArtworkActivity";
    private ActivityArtworkBinding binding;
    private Artwork artwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityArtworkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("artwork")) {
            artwork = (Artwork) intent.getSerializableExtra("artwork");
            if (artwork != null) {
                binding.artworkViewTitleText.setText(artwork.getTitle());
                binding.artworkViewDateText.setText(artwork.getDate_display());
                binding.artworkViewArtistText.setText(artwork.getArtist_display());
                binding.artworkViewDepartmentText.setText(artwork.getDepartment_title());

                if (artwork.getGallery_title() != null) {
                    binding.artworkViewGalleryLink.setText(artwork.getGallery_title());
                    binding.artworkViewGalleryLink.setPaintFlags(binding.artworkViewGalleryLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                else {
                    // set artworkViewGalleryLink to invisible

                    binding.artworkViewGalleryLink.setVisibility(View.GONE);
                    binding.artworkViewGalleryLinkIcon.setVisibility(View.GONE);
                }

                binding.artworkViewPlaceOfOriginText.setText(artwork.getPlace_of_origin());
                binding.artworkViewMediumText.setText(artwork.getMedium_display());
                binding.artworkViewDimensionsText.setText(artwork.getDimensions());
                binding.artworkViewCreditLineText.setText(artwork.getCredit_line());
                // load the artwork image
                if (artwork.getImage_id() != null) {
                    Glide.with(this).load(artwork.getFullImageUrl()).into(binding.artworkViewImage);
                }
                else {
                    binding.artworkViewImage.setImageResource(R.drawable.not_available);
                    binding.artworkViewImage.setOnClickListener(null);
                }


            }
        }

    }

    public void openImageView(View view) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("artwork", artwork);
        startActivity(intent);

    }

    public void openGallery(View view) {
        Log.d(TAG, "openGallery: ");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.artic.edu/galleries/" + artwork.getGalleryId()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "startActivity ");
            startActivity(intent);
        }
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


}