package it.adriano.tumino.gamepoint.backgroundprocesses.catchgame;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.storegame.Game;
import it.adriano.tumino.gamepoint.data.storegame.MicrosoftGame;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class CatchGameFromMCS extends TaskRunner<Integer, String> {

    private final String finalURL;
    private final MicrosoftGame game;

    public AsyncResponse<Game> delegate = null;

    public CatchGameFromMCS(String url) {
        finalURL = url;
        game = new MicrosoftGame();
    }

    @Override
    public String doInBackground(Integer... i) {
        try {
            getGame();
        } catch (IOException exception) {
            Log.e("TEST", exception.getMessage());
        }
        return null;
    }

    private void getGame() throws IOException {
        Document document = Jsoup.connect(finalURL)
                .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                .get();

        Elements body = document.select("#pdp");
        Elements image = body.select(".pi-product-image");
        String imageUrl = image.select("img").get(0).attributes().get("src");
        game.setImageHeaderUrl(imageUrl);

        Elements titleGroup = body.select("#productTitle");
        String title = titleGroup.text();
        game.setTitle(title);

        Elements maturityRating = body.select("#maturityRatings");
        String pegi = maturityRating.select("a").text();
        game.setPegi(pegi);

        Elements productPrice = body.select("#productPrice");
        String price = productPrice.select("span").get(0).text();
        game.setPrice(price);

        Elements overviewTab = body.select("#pivot-OverviewTab"); //contiene la descrizione
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

        Elements schreenshotsElements = overviewTab.select("#responsiveScreenshots");
        Elements schreenshotsList = schreenshotsElements.select(".cli_screenshot_gallery").select("ul").get(0).children();
        ArrayList<String> schreenshots = new ArrayList<>();
        for (Element screen : schreenshotsList) {
            Element tmp = screen.select("img").get(0);
            String s = tmp.attributes().get("data-src");
            s = s.replaceAll("//", "https://");
            schreenshots.add(s);
        }
        game.setScreenshotsUrl(schreenshots);

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
            metadata.add(string);
        }
        game.setMetadata(metadata);

        Elements systemRequirement = body.select("#system-requirements");
        Elements requirement = systemRequirement.select(".c-table");
        String req = "<section>";
        for (Element element : requirement) {
            Element table = element.select("table").get(0);
            Element caption = table.select("caption").get(0);
            String captionText = caption.text();
            Elements trs = table.select("tr");
            String corpo = "";
            for (Element tr : trs) {
                Elements th = tr.select("th");
                corpo += "<h4>" + th.text() + "</h4>";
                Elements td = tr.select("td");
                corpo += "<p>" + td.text() + "</p>";
            }
            req += "<h3>" + captionText + "</h3>" + corpo;
        }
        req += "</section>";
        game.setSystemRequirement(req);

    }

    @Override
    public void onPostExecute(String o) {
        delegate.processFinish(game);
    }
}
