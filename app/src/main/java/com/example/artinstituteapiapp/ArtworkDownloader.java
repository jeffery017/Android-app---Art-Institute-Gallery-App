package com.example.artinstituteapiapp;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
    private static ArrayList<Artwork> artworks = new ArrayList<>();
    private static ArrayList<Integer> galleryIds = new ArrayList<>();

    public static void downloadRandomArtwork(MainActivity mainActivityIn) {
        Log.d(TAG, "downloadRandomArtwork: ");
        mainActivity = mainActivityIn;
        queue = Volley.newRequestQueue(mainActivity);
        artworks.clear();
        if (galleryIds.isEmpty()) {
            downloadGalleryId(1);
        }
        else {
            int galleryId = galleryIds.get((int)(Math.random() * galleryIds.size()));
            downloadRandomArtworkByGalleryId(galleryId, 1);
        }
    }

    private static void downloadGalleryId(int page) {
        Log.d(TAG, "getArtworkIdByPage: " + page);
        // create URL
        final String baseUrl = "https://api.artic.edu/api/v1/galleries";
        Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
        builder.appendQueryParameter("fields", "id");
        builder.appendQueryParameter("limit", "100");
        builder.appendQueryParameter("page", String.valueOf(page));
        String url = builder.build().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            (res) -> {
                ArrayList<Integer> resIds = parseJsonIds(res.toString());
                if (!resIds.isEmpty()) {
                    //Log.d(TAG, "getArtworkIdByPage: receive page " + page);
                    galleryIds.addAll(resIds);
                    downloadGalleryId(page + 1);
                } else {
                    downloadRandomArtwork(mainActivity);
                }
            },
            (err) -> {
                Log.d(TAG, "getArtworkIdByPage: request fail");
            }
        );
        queue.add(jsonObjectRequest);
    }

    public static void downloadRandomArtworkByGalleryId(int gallery_id, int page) {
        Log.d(TAG, "downloadRandomArtworkByGalleryId: page" + page);
        if (galleryIds.isEmpty()) {
            Log.d(TAG, "downloadRandomArtwork: no artwork id");
            return;
        }

        // create URL
        String artworkURL = "https://api.artic.edu/api/v1/artworks/search";
        Uri.Builder builder = Uri.parse(artworkURL).buildUpon();
        builder.appendQueryParameter("query[term][gallery_id]", gallery_id + "");
        builder.appendQueryParameter("limit", "100");
        builder.appendQueryParameter("page", page + "");
        builder.appendQueryParameter("fields", "title, date_display, artist_display, medium_display, artwork_type_title, image_id, dimensions, department_title, credit_line, place_of_origin, gallery_title, gallery_id, id, api_link");
        String url = builder.build().toString();

        // Request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            (res) -> {
                Gson gson = new Gson();
                try {
                    JSONObject json = new JSONObject(res.toString());
                    int id = json.getJSONObject("pagination").getInt("total");
                    if (id == 0) {
                        downloadRandomArtwork(mainActivity);
                        return;
                    }
                    id = (int)(Math.random() * id);
                    String tmp = json.getJSONArray("data").getJSONObject(id).toString();
                    artworks.add(gson.fromJson(tmp, Artwork.class));
                    mainActivity.updateArtworks(artworks);
                } catch (Exception e) {
                    Log.e("JSON", "fail");
                }

            },
            (err) -> {
                Log.d(TAG, "downloadArtworks: request fail");
            }
        );
        queue.add(jsonObjectRequest);
    }

    public static void downloadArtworksByQuery(MainActivity mainActivityIn ,String query) {
        mainActivity = mainActivityIn;
        queue = Volley.newRequestQueue(mainActivity);
        artworks.clear();
        // create URL
        String artworkURL = "https://api.artic.edu/api/v1/artworks/search";
        Uri.Builder builder = Uri.parse(artworkURL).buildUpon();
        builder.appendQueryParameter("q", query);
        builder.appendQueryParameter("limit", "15");
        builder.appendQueryParameter("fields", "title, date_display, artist_display, medium_display, artwork_type_title, image_id, dimensions, department_title, credit_line, place_of_origin, gallery_title, gallery_id, id, api_link");

        String url = builder.build().toString();

        // Request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            (res) -> {
                ArrayList<Artwork> artworks = parseJSON(res.toString());
                updateArtworks(artworks);
            },
            err -> {
                Log.d(TAG, "downloadArtworks: request fail");
            }
        );
        queue.add(jsonObjectRequest);
    }



    private static ArrayList<Artwork> parseJSON(String jsonString) {
        Log.d(TAG, "parseJSON: ");
        ArrayList<Artwork> artworks = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                artworks.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Artwork.class));
            }
        } catch (Exception e) {
            Log.e("JSON", "fail");
        }
        return artworks;
    }

    private static ArrayList<Integer> parseJsonIds(String jsonString) {
        ArrayList<Integer> ids = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                int id = jsonArray.getJSONObject(i).getInt("id");
                ids.add(id);
            }
        } catch (Exception e) {
            Log.e("JSON", "fail");
        }
        return ids;
    }

    private static void updateArtworks(ArrayList<Artwork> artworks) {
        artworks.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
        mainActivity.updateArtworks(artworks);
    }
}
