package com.example.artinstituteapiapp;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import java.io.Serializable;

public class Artwork  implements Serializable {
    private final String title;
    private final String credit_line;
    private final String medium_display;
    private final String department_title;
    private final String artist_display;
    private final String api_link;
    private final String date_display;
    private final String image_id;
    private final String gallery_title;
    private final String gallery_id;
    private final String place_of_origin;
    private final String dimensions;

    Artwork(String title, String credit_line, String medium_display, String department_title, String artist_display, String api_link, String date_display, String image_id, String gallery_title, String galleryId, String place_of_origin, String dimensions) {
        this.title = title;
        this.credit_line = credit_line;
        this.medium_display = medium_display;
        this.department_title = department_title;
        this.artist_display = artist_display;
        this.api_link = api_link;
        this.date_display = date_display;
        this.image_id = image_id;
        this.gallery_title = gallery_title;
        gallery_id = galleryId;
        this.place_of_origin = place_of_origin;
        this.dimensions = dimensions;
    }

    // Getters
    public String getTitle() {
        return title;
    }
    public String getDate_display() {
        return date_display;
    }
    public String getImage_id() {return image_id;}
    public String getArtist_display() {
        return artist_display;
    }
    public String getDepartment_title() {
        return department_title;
    }
    public String getGallery_title() {
        return gallery_title;
    }
    public String getPlace_of_origin() {
        return place_of_origin;
    }
    public String getMedium_display() {
        return medium_display;
    }
    public String getDimensions() {
        return dimensions;
    }
    public String getCredit_line() {
        return credit_line;
    }
    public String getApi_link() {
        return api_link;
    }

    public String getGalleryId() {
        return gallery_id;
    }

    public String getThumbnailUrl() {
        return "https://www.artic.edu/iiif/2/" + image_id + "/full/200,/0/default.jpg";
    }
    public String getFullImageUrl() {
        return "https://www.artic.edu/iiif/2/" + image_id + "/full/843,/0/default.jpg";
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
