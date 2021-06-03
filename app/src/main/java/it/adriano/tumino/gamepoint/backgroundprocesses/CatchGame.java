package it.adriano.tumino.gamepoint.backgroundprocesses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.data.GameShow;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class CatchGame extends TaskRunner<Integer, String> {

    private String url;
    private ArrayList<String> image = new ArrayList<>();
    private String price;
    private String appID;
    private final String API = "https://store.steampowered.com/api/appdetails?appids=";
    private String retriveInformation;

    public AsyncResponse<GameShow> delegate = null;

    public CatchGame() {
        url = "https://store.steampowered.com/app/42700/Call_of_Duty_Black_Ops/";
        appID = url.replaceAll("[^0-9]", "");
        retriveInformation = API + appID;
    }

    @Override
    public String doInBackground(Integer... i) {
        InputStream is = null;
        try {
            is = new URL(retriveInformation).openStream();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        String jsonText = "";
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            jsonText = sb.toString();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return jsonText;
    }

    @Override
    public void onPostExecute(String json) {
        GameShow gameShow = new GameShow();
        gameShow.setUrl(retriveInformation);
        try {
            gameShow = jsonParser(json);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        delegate.processFinish(gameShow);
    }

    private GameShow jsonParser(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject result = jsonObject.getJSONObject(String.valueOf(appID));
        GameShow gameShow = new GameShow();
        if (!result.getBoolean("success")) {
            return null;
        }

        String type, name, description, languages, image, website, minimumWindows, minimumMac, minimumLinux, price, date;
        int finalPrice, discountPercent, scoreMetacritic;
        ArrayList<String> developers, publishers, categories, genres, screenshots;

        JSONObject data = result.getJSONObject("data");
        type = data.getString("type");
        name = data.getString("name");

        gameShow.setType(type);
        gameShow.setTitle(name);

        boolean isFree = data.getBoolean("is_free");
        gameShow.setPrice("FREE");
        if (!isFree) {
            price = data.getJSONObject("price_overview").getString("final_formatted");
            finalPrice = data.getJSONObject("price_overview").getInt("final");
            discountPercent = data.getJSONObject("price_overview").getInt("discount_percent");
            gameShow.setPrice(price);
            gameShow.setFinalPrice(finalPrice);
            gameShow.setDiscountPercent(discountPercent);
        }

        description = data.getString("detailed_description"); //Html.fromHtml(, Html.FROM_HTML_MODE_LEGACY).toString();
        languages = data.getString("supported_languages");
        image = data.getString("header_image");
        website = data.getString("website");
        gameShow.setDescription(description);
        gameShow.setLanguages(languages);
        gameShow.setImageURL(image);
        gameShow.setWebsite(website);

        minimumWindows = data.getJSONObject("pc_requirements").getString("minimum");
        minimumMac = data.getString("mac_requirements");
        minimumLinux = data.getString("linux_requirements");
        if (!minimumWindows.isEmpty()) gameShow.setMinimumWindows(minimumWindows);
        if (!minimumMac.isEmpty()) gameShow.setMinimumMac(minimumMac);
        if (!minimumLinux.isEmpty()) gameShow.setMinimumLinux(minimumLinux);

        developers = getValuesFromJSONArray(data.getJSONArray("developers"), "");
        publishers = getValuesFromJSONArray(data.getJSONArray("publishers"), "");
        gameShow.setDevelopers(developers);
        gameShow.setPublishers(publishers);

        scoreMetacritic = data.getJSONObject("metacritic").getInt("score");
        gameShow.setScoreMetacritic(scoreMetacritic);

        categories = getValuesFromJSONArray(data.getJSONArray("categories"), "description");
        genres = getValuesFromJSONArray(data.getJSONArray("genres"), "description");
        screenshots = getValuesFromJSONArray(data.getJSONArray("screenshots"), "path_full");
        gameShow.setCategories(categories);
        gameShow.setGenres(genres);
        gameShow.setScreenshots(screenshots);

        boolean comingSoon = data.getJSONObject("release_date").getBoolean("coming_soon");
        gameShow.setDate("Coming Soon");
        if (!comingSoon){
            date = data.getJSONObject("release_date").getString("date");
            gameShow.setDate(date);
        }

        return gameShow;
    }

    private ArrayList<String> getValuesFromJSONArray(JSONArray array, String... valuesName) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        if (valuesName[0].isEmpty()) { //simpleJsonArray
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } else { //JsonArray of object
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                for (String valueName : valuesName) {
                    list.add(object.getString(valueName));
                }
            }
        }
        return list;
    }
}