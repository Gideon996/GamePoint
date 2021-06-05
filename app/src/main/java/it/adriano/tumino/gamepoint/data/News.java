package it.adriano.tumino.gamepoint.data;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class News extends Information {

    private String body;
    private String date;
    private String website;

    public News(String title, String body, String imageUrl, String date, String newsUrl, String website) {
        super(title, imageUrl, newsUrl);
        this.body = body;
        this.date = date;
        this.website = website;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
