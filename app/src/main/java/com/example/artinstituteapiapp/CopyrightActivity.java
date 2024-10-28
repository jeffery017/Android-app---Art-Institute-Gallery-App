package com.example.artinstituteapiapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.artinstituteapiapp.databinding.ActivityCopyrightBinding;

public class CopyrightActivity extends AppCompatActivity {
    private ActivityCopyrightBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCopyrightBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Linkify.addLinks(binding.copyrightApiUrlText, Linkify.WEB_URLS);
        Linkify.addLinks(binding.copyrightFontUrlText, Linkify.WEB_URLS);

    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}