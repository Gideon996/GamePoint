package it.adriano.tumino.gamepoint.data.storegame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public abstract class Game implements Parcelable {

    private String title;
    private String description;
    private String imageHeaderUrl;
    private String price;
    private String releaseData;
    private ArrayList<String> screenshotsUrl;

    public Game() {
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(description);
        out.writeString(imageHeaderUrl);
        out.writeString(price);
        out.writeString(releaseData);
        out.writeStringList(screenshotsUrl);
    }

    protected Game(Parcel in) { //devono essere in ordine di come li scrivo
        title = in.readString();
        description = in.readString();
        imageHeaderUrl = in.readString();
        price = in.readString();
        releaseData = in.readString();
        screenshotsUrl = in.createStringArrayList();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHeaderUrl() {
        return imageHeaderUrl;
    }

    public void setImageHeaderUrl(String imageHeaderUrl) {
        this.imageHeaderUrl = imageHeaderUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(String releaseData) {
        this.releaseData = releaseData;
    }

    public ArrayList<String> getScreenshotsUrl() {
        return screenshotsUrl;
    }

    public void setScreenshotsUrl(ArrayList<String> screenshotsUrl) {
        this.screenshotsUrl = screenshotsUrl;
    }
}
