package it.adriano.tumino.gamepoint.data.storegame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SteamStoreGame extends StoreGame {

    private String videoUrl;
    private String languages;
    private String website;
    private String minimumRequirement;
    private String recommendedRequirement;
    private String scoreMetacritic;
    private ArrayList<String> developers;
    private ArrayList<String> publishers;
    private ArrayList<String> categories;
    private ArrayList<String> genres;

    public SteamStoreGame() {
        setStore("STEAM");
    }

    private SteamStoreGame(Parcel in) {
        super(in);
        videoUrl = in.readString();
        languages = in.readString();
        website = in.readString();
        minimumRequirement = in.readString();
        recommendedRequirement = in.readString();
        scoreMetacritic = in.readString();
        developers = in.createStringArrayList();
        publishers = in.createStringArrayList();
        categories = in.createStringArrayList();
        genres = in.createStringArrayList();
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(videoUrl);
        out.writeString(languages);
        out.writeString(website);
        out.writeString(minimumRequirement);
        out.writeString(recommendedRequirement);
        out.writeString(scoreMetacritic);
        out.writeStringList(developers);
        out.writeStringList(publishers);
        out.writeStringList(categories);
        out.writeStringList(genres);
    }

    public static final Parcelable.Creator<SteamStoreGame> CREATOR = new Parcelable.Creator<SteamStoreGame>() {
        public SteamStoreGame createFromParcel(Parcel in) {
            return new SteamStoreGame(in);
        }

        public SteamStoreGame[] newArray(int size) {
            return new SteamStoreGame[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public String getScoreMetacritic() {
        return scoreMetacritic;
    }

    public void setScoreMetacritic(String scoreMetacritic) {
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
}
