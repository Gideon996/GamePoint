package it.adriano.tumino.gamepoint.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Game extends Information implements Parcelable {

    private String name, description, languages, image, website, minimumRequirement, recommendedRequirement,  price, date;
    private int scoreMetacritic;
    private ArrayList<String> developers, publishers, categories, genres, screenshots;

    public Game() {
    }

    protected Game(Parcel in) {
        name = in.readString();
        description = in.readString();
        languages = in.readString();
        image = in.readString();
        website = in.readString();
        minimumRequirement = in.readString();
        recommendedRequirement = in.readString();
        price = in.readString();
        date = in.readString();
        scoreMetacritic = in.readInt();
        developers = in.createStringArrayList();
        publishers = in.createStringArrayList();
        categories = in.createStringArrayList();
        genres = in.createStringArrayList();
        screenshots = in.createStringArrayList();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMinimumRequirement() {
        return minimumRequirement;
    }

    public void setMinimumRequirement(String minimumRequirement) {
        this.minimumRequirement = minimumRequirement;
    }

    public String getRecommendedRequirement() {
        return recommendedRequirement;
    }

    public void setRecommendedRequirement(String recommendedRequirement) {
        this.recommendedRequirement = recommendedRequirement;
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
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(languages);
        dest.writeString(image);
        dest.writeString(website);
        dest.writeString(minimumRequirement);
        dest.writeString(recommendedRequirement);
        dest.writeString(price);
        dest.writeString(date);
        dest.writeInt(scoreMetacritic);
        dest.writeStringList(developers);
        dest.writeStringList(publishers);
        dest.writeStringList(categories);
        dest.writeStringList(genres);
        dest.writeStringList(screenshots);
    }
}
