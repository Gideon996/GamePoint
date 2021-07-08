package it.adriano.tumino.gamepoint.data;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Game implements Parcelable {
    private String title;
    private String imageHeaderURL;
    private String price;

    public Game(){}

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(imageHeaderURL);
        out.writeString(price);
    }

    protected Game(Parcel in) { //devono essere in ordine di come li scrivo
        title = in.readString();
        imageHeaderURL = in.readString();
        price = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageHeaderURL() {
        return imageHeaderURL;
    }

    public void setImageHeaderURL(String imageHeaderURL) {
        this.imageHeaderURL = imageHeaderURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
