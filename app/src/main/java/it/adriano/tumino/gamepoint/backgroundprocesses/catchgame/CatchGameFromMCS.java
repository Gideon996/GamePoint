package it.adriano.tumino.gamepoint.backgroundprocesses.catchgame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.Game;
import it.adriano.tumino.gamepoint.utils.TaskRunner;
import it.adriano.tumino.gamepoint.utils.Utils;

public class CatchGameFromMCS extends TaskRunner<Integer, String> {


    private static final String url1 = "https://displaycatalog.mp.microsoft.com/v7.0/products?bigIds=";
    private static final String url2 = "&market=IT&languages=it-it&MS-CV=XXX";

    private String finalURL;
    private Game game;

    public AsyncResponse<Game> delegate = null;

    public CatchGameFromMCS(String appID) {
        finalURL = url1 + appID + url2;
        game = new Game();
    }

    @Override
    public String doInBackground(Integer... i) {
        try {
            jsonParser();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private Game jsonParser() throws IOException, JSONException {
        String json = Utils.getJsonFromUrl(finalURL);
        JSONObject jsonObject = new JSONObject(json);
        Game game = new Game();

        JSONArray products = jsonObject.getJSONArray("Products");
        if (products.length() < 1 || products == null) { //ho trovato la corrispondenza
            System.out.println("Vuoto");
            return null;
        }

        JSONObject object = products.getJSONObject(0);
        if (object.has("LocalizedProperties")) {
            JSONObject localizedProperties = object.getJSONArray("LocalizedProperties").getJSONObject(0);

            String developerName = "N.A.";
            if (localizedProperties.has("DeveloperName"))
                developerName = localizedProperties.getString("DeveloperName");
            String publisherName = "N.A";
            if (localizedProperties.has("PublisherName"))
                publisherName = localizedProperties.getString("PublisherName");
            String publisherWebSite = "N.A.";
            if (localizedProperties.has("PublisherWebsiteUri"))
                publisherWebSite = localizedProperties.getString("PublisherWebsiteUri");
            String supportURI = "N.A.";
            if (localizedProperties.has("SupportUri"))
                supportURI = localizedProperties.getString("SupportUri");


            ArrayList<String> urlScreenshots = new ArrayList<>();
            urlScreenshots.add("https://i.redd.it/sve2ienm0zs41.jpg");
            String imageHeader = "https://www.nerdlog.it/wp-content/uploads/2020/04/28e74576-78fd-4581-bc68-bd73848476ef.jpg";
            String copertina = "";
            String logo = "";
            if (localizedProperties.has("Images")) {
                JSONArray jsonArray = localizedProperties.getJSONArray("Images");
                urlScreenshots.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject image = jsonArray.getJSONObject(i);
                    switch (image.getString("ImagePurpose")) {
                        case "Screenshot":
                            urlScreenshots.add(image.getString("Uri"));
                            break;
                        case "Logo":
                            logo = image.getString("Uri");
                            break;
                        case "SuperHeroArt":
                            imageHeader = image.getString("Uri");
                            break;
                        case "BoxArt":
                            copertina = image.getString("Uri");
                            break;
                    }
                }
            }
            game.setImage(imageHeader);
            game.setScreenshots(urlScreenshots);

            String description = "N.A";
            if (localizedProperties.has("ProductDescription"))
                description = localizedProperties.getString("ProductDescription");
            game.setDescription(description);

            String titolo = "N.A";
            if (localizedProperties.has("ProductTitle"))
                titolo = localizedProperties.getString("ProductTitle");
            game.setName(titolo);

            ArrayList<String> languages = new ArrayList<>();
            languages.add("N.A.");
            if (localizedProperties.has("Markets")) {
                JSONArray jsonArray = localizedProperties.getJSONArray("Markets");
                languages.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    languages.add(jsonArray.getString(i));
                }
            }
            game.setLanguages(String.join(", ", languages));

            String ratingPEGI = "N.A.";
            if (object.has("MarketProperties")) {
                JSONObject marketProperties = object.getJSONArray("MarketProperties").getJSONObject(0);
                //ContentRatings
                JSONArray jsonArray = marketProperties.getJSONArray("ContentRatings");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject rating = jsonArray.getJSONObject(i);
                    if (rating.getString("RatingSystem").equals("PEGI")) {
                        ratingPEGI = rating.getString("RatingId");
                        break;
                    }
                }
            }

            String categories = "N.A.";
            ArrayList<String> attributes = new ArrayList<>();
            attributes.add("N.A.");
            if (object.has("Properties")) {
                JSONObject properties = object.getJSONObject("Properties");
                attributes.clear();
                JSONArray jsonArray = properties.getJSONArray("Attributes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    attributes.add(jsonArray.getJSONObject(i).getString("Name"));
                }
                categories = properties.getString("Category");
            }

            String listPrice = "N.A.";
            String releaseData = "N.A.";
            if (object.has("DisplaySkuAvailabilities")) {
                JSONObject primo = object.getJSONArray("DisplaySkuAvailabilities").getJSONObject(0);
                if (primo.has("Availabilities")) {
                    JSONObject price = primo.getJSONArray("Availabilities").getJSONObject(0);
                    if (price.has("OrderManagementData") && price.getJSONObject("OrderManagementData").has("Price"))
                        listPrice = "" + price.getInt("ListPrice");
                    if (price.has("Properties"))
                        releaseData = primo.getJSONObject("Properties").getString("OriginalReleaseDate");
                }
            }
            game.setPrice(listPrice);
            game.setDate(releaseData);

            return game;
        }

        return null;
    }

    @Override
    public void onPostExecute(String o) {
        delegate.processFinish(game);
    }
}
