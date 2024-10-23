package com.example.artinstituteapiapp;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArtworkDownloader {
    private static final String TAG = "ArtworkDownloader";
    private static RequestQueue queue;
    private static MainActivity mainActivity;

    public static void downloadArtworks(MainActivity mainActivityIn ,String query) {
        mainActivity = mainActivityIn;
        queue = Volley.newRequestQueue(mainActivity);

        // create URL
        String artworkURL = "https://api.artic.edu/api/v1/artworks/search";
        Uri.Builder builder = Uri.parse(artworkURL).buildUpon();
        builder.appendQueryParameter("q", query);
        builder.appendQueryParameter("limit", "15");
        builder.appendQueryParameter("fields", "title, date_display, artist_display, medium_display, artwork_type_title, image_id, dimensions, department_title, credit_line, place_of_origin, gallery_title, gallery_id, id, api_link");

        String url = builder.build().toString();
        Response.Listener<JSONObject> responseListener =
                response -> {
                    parseJSON(response.toString());
                };
        Response.ErrorListener errorListener =
                error -> mainActivity.clearArtworks();

        // Request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        queue.add(jsonObjectRequest);

    }

    private static void parseJSON(String s) {
        Log.d(TAG, "parseJSON: ");
        mainActivity.clearArtworks();
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONObject(s).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Artwork art = gson.fromJson(jsonArray.getJSONObject(i).toString(), Artwork.class);
                mainActivity.addArtwork(art);
            }
            //Toast.makeText(mainActivity, "Found " + jsonArray.length() + " artworks", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Log.e("JSON", "fail");
        }
    }
}
