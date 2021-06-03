package it.adriano.tumino.gamepoint.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GameShow extends Information implements Parcelable {
    private String type, description, languages, website, minimumWindows, minimumMac, minimumLinux, price, date;
    private int finalPrice, discountPercent, scoreMetacritic;
    private ArrayList<String> developers, publishers, categories, genres, screenshots;

    public GameShow(){}

    protected GameShow(Parcel in) {
        type = in.readString();
        description = in.readString();
        languages = in.readString();
        website = in.readString();
        minimumWindows = in.readString();
        minimumMac = in.readString();
        minimumLinux = in.readString();
        price = in.readString();
        date = in.readString();
        finalPrice = in.readInt();
        discountPercent = in.readInt();
        scoreMetacritic = in.readInt();
        developers = in.createStringArrayList();
        publishers = in.createStringArrayList();
        categories = in.createStringArrayList();
        genres = in.createStringArrayList();
        screenshots = in.createStringArrayList();
    }

    public static final Creator<GameShow> CREATOR = new Creator<GameShow>() {
        @Override
        public GameShow createFromParcel(Parcel in) {
            return new GameShow(in);
        }

        @Override
        public GameShow[] newArray(int size) {
            return new GameShow[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMinimumWindows() {
        return minimumWindows;
    }

    public void setMinimumWindows(String minimumWindows) {
        this.minimumWindows = minimumWindows;
    }

    public String getMinimumMac() {
        return minimumMac;
    }

    public void setMinimumMac(String minimumMac) {
        this.minimumMac = minimumMac;
    }

    public String getMinimumLinux() {
        return minimumLinux;
    }

    public void setMinimumLinux(String minimumLinux) {
        this.minimumLinux = minimumLinux;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getScoreMetacritic() {
        return scoreMetacritic;
    }

    public void setScoreMetacritic(int scoreMetacritic) {
        this.scoreMetacritic = scoreMetacritic;
    }

    public ArrayList<String> getDevelopers() {
        return developers;
    }

    public void setDevelopers(ArrayList<String> developers) {
        this.developers = developers;
    }

    public ArrayList<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(ArrayList<String> publishers) {
        this.publishers = publishers;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(ArrayList<String> screenshots) {
        this.screenshots = screenshots;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(description);
        dest.writeString(languages);
        dest.writeString(website);
        dest.writeString(minimumWindows);
        dest.writeString(minimumMac);
        dest.writeString(minimumLinux);
        dest.writeString(price);
        dest.writeString(date);
        dest.writeInt(finalPrice);
        dest.writeInt(discountPercent);
        dest.writeInt(scoreMetacritic);
        dest.writeStringList(developers);
        dest.writeStringList(publishers);
        dest.writeStringList(categories);
        dest.writeStringList(genres);
        dest.writeStringList(screenshots);
    }
}
