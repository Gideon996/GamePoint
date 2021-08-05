package it.adriano.tumino.gamepoint.processes.catchgame;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.data.storegame.MicrosoftStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.processes.TaskRunner;

public class CatchMicrosoftGame extends TaskRunner<Void, StoreGame> implements WebScrapping<MicrosoftStoreGame> {
    private static final String TAG = "CatchGameFromMCS";

    private final String finalURL;

    public AsyncResponse<StoreGame> delegate = null;

    public CatchMicrosoftGame(String url) {
        finalURL = url;
    }

    @Override
    public StoreGame doInBackground(Void... i) {
        return getWebPageAndParsing(finalURL, TAG);
    }

    @Override
    public MicrosoftStoreGame scrapping(@NotNull Document document) {
        Log.i(TAG, "Starting scrapping microsoft's html page");
        MicrosoftStoreGame game = new MicrosoftStoreGame();

        Elements body = document.select("#pdp");
        Elements image = body.select(".pi-product-image");
        String imageUrl = image.select("img").get(0).attributes().get("src");
        game.setImageHeaderURL(imageUrl);
        game.setVideoUrl("");
        game.setThumbnail("");

        Elements titleGroup = body.select("#productTitle");
        String title = titleGroup.text();
        game.setTitle(title);

        Elements maturityRating = body.select("#maturityRatings");
        String pegi = maturityRating.select("a").text();
        game.setPegi(pegi);

        Elements productPrice = body.select("#productPrice");
        String price = productPrice.select("span").get(0).text();
        final String trim = price.substring(0, price.length() - 1).trim();
        if (trim.matches("^[+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)$")) {
            price = trim;
        }
        game.setPrice(price);

        Elements overviewTab = body.select("#pivot-OverviewTab");
        Elements available = overviewTab.select("#AvailableOnModule");
        String console = available.select("a").text();
        game.setConsole(console);

        Elements capabilities = overviewTab.select("#CapabilitiesModule");
        Elements categoriesElements = capabilities.select("a");
        ArrayList<String> categories = new ArrayList<>();
        for (Element element : categoriesElements) {
            categories.add(element.text());
        }
        game.setCategories(categories);

        Elements descriptionProduct = overviewTab.select("#product-description");
        String description = descriptionProduct.text();
        game.setDescription(description);

        Elements screenshotsElements = overviewTab.select("#responsiveScreenshots");
        ArrayList<String> screenshots = new ArrayList<>();
        screenshots.add("https://images-na.ssl-images-amazon.com/images/I/51ilsGij0KL._AC_SX679_.jpg");
        if (screenshotsElements != null && !screenshotsElements.isEmpty()) {
            Elements screenshotsList = screenshotsElements.select(".cli_screenshot_gallery");
            if (screenshotsList != null && !screenshotsList.isEmpty()) {
                Elements list = screenshotsList.select("ul").get(0).children();
                screenshots.clear();
                for (Element screen : list) {
                    String img = screen.select("img").get(0).attributes()
                            .get("data-src").replaceAll("//", "https://");
                    screenshots.add(img);
                }
            }
        }
        game.setScreenshotsUrl(screenshots);

        Elements information = overviewTab.select("#information");
        Elements additionalInformation = information.select(".m-additional-information");
        Elements divisions = additionalInformation.select(".c-content-toggle");

        ArrayList<String> metadata = new ArrayList<>();
        for (Element div : divisions) {
            Elements h4 = div.select("h4");
            String string = "";
            string += "<h4>" + h4.text() + "</h4>";
            Elements span = div.select("span");
            string += "<p>" + span.text() + "</p>";
            if (h4.text().equals("Data di uscita") || h4.text().equals("Release date"))
                game.setReleaseData(span.text());
            metadata.add(string);
        }
        game.setMetadata(metadata);

        Elements systemRequirement = body.select("#system-requirements");
        Elements requirement = systemRequirement.select(".c-table");
        StringBuilder req = new StringBuilder("<section>");
        for (Element element : requirement) {
            Element table = element.select("table").get(0);
            Element caption = table.select("caption").get(0);
            String captionText = caption.text();
            Elements trs = table.select("tr");
            StringBuilder stringBuilder = new StringBuilder();
            for (Element tr : trs) {
                Elements th = tr.select("th");
                stringBuilder.append("<h4>").append(th.text()).append("</h4>");
                Elements td = tr.select("td");
                stringBuilder.append("<p>").append(td.text()).append("</p>");
            }
            req.append("<h3>").append(captionText).append("</h3>").append(stringBuilder);
        }
        req.append("</section>");
        game.setSystemRequirement(req.toString());

        return game;
    }

    @Override
    public void onPostExecute(StoreGame output) {
        Log.i(TAG, "Delegation of microsoft's result");
        delegate.processFinish(output);
    }
}
