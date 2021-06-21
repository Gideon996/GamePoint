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
import it.adriano.tumino.gamepoint.data.storegame.NintendoGame;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class CatchGameFromEShop extends TaskRunner<Void, Game> {
    private static final String TAG = "CatchGameFromEShop";

    private static final String BASE_URL = "https://www.nintendo.it";

    private final String finalURL;
    private final String price;

    public AsyncResponse<Game> delegate = null;

    public CatchGameFromEShop(String url, String price) {
        finalURL = BASE_URL + url;
        this.price = price;
    }

    @Override
    public Game doInBackground(Void... integers) {
        return getGame();
    }

    private NintendoGame getGame() {
        Document document;

        NintendoGame game = new NintendoGame();
        game.setPrice(price);

        try {
            document = Jsoup.connect(finalURL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36") //per visualizzare tutto correttamente
                    .get();
        } catch (IOException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }

        Elements content = document.getElementsByClass("tab-content");
        Elements header = content.select(".gamepage-banner");

        Elements video = header.select("iframe");
        String videoUrl = "";
        if (!video.isEmpty()) videoUrl = video.first().attributes().get("src");
        game.setVideoTrailerUrl(videoUrl);

        Elements img = header.select("img");
        String imageHeader = "";
        if (!img.isEmpty()) imageHeader = img.first().attributes().get("src");
        game.setImageHeaderUrl(imageHeader.replaceAll("//", "https://"));

        Elements classification = header.select(".age-rating").select("span");
        String pegi = "";
        if (!classification.isEmpty()) pegi = classification.text();
        game.setPegi(pegi);

        //GamePageHeader
        Elements gamePageHeader = content.select("#gamepage-header").select(".gamepage-header-info");
        String title = gamePageHeader.select("h1").text();
        String console = gamePageHeader.select("span").first().text();
        String releaseDate = gamePageHeader.select("span").last().text();
        game.setTitle(title);
        game.setConsole(console);
        game.setReleaseData(releaseDate);

        //Panoramica
        Elements panoramica = content.select("#Panoramica");
        Element gameSectionContents = panoramica.select("div").first();
        Element div = gameSectionContents.children().first();

        Elements information = div.children().select(".row-content");
        String text = information.text();
        String[] sezioni = text.split(" {2}"); //l'unica soluzione possibile per prendere i testi -> pe fozza
        StringBuilder builder = new StringBuilder();
        for (String sezione : sezioni) {
            if (!sezione.isEmpty()) builder.append(sezione).append("<br/>");
        }
        game.setDescription(builder.toString());


        //Galleria_immagini
        Elements immagini = content.select("#Galleria_immagini");
        ArrayList<String> screenshotsUrl = new ArrayList<>();
        if (immagini != null && !immagini.isEmpty()) {
            Elements galleria = immagini.select("div.row").select("ul").first().children();
            for (Element tmp : galleria) {
                Element immagine = tmp.select("img.img-responsive").first();
                if (immagine != null)
                    screenshotsUrl.add(immagine.attributes().get("data-xs").replaceAll("//", "https://"));
            }
        }
        game.setScreenshotsUrl(screenshotsUrl);

        //gameDetails
        Elements dettagli = content.select("#gameDetails");
        Elements systemInfo = dettagli.select(".system_info").not("div.amiibo_info");
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : systemInfo) {
            if (element.select(".game_info_title").hasText()) {
                stringBuilder.append("<p><strong>").append(element.select(".game_info_title").first().text()).append(": ").append("</strong>");
            }

            if (element.select(".game_info_text").hasText()) {
                stringBuilder.append(element.select(".game_info_text").first().text()).append("</p>");
            }

            if (element.children().hasClass("age-rating")) {
                stringBuilder.append(element.select("span").text()).append("</p>");
            }
        }
        game.setSystemInfo(stringBuilder.toString());

        systemInfo = content.select("div.row").select("div.game_info");
        Elements schede = systemInfo.select(".listwheader-container");

        ArrayList<String> featureSheets = new ArrayList<>();
        for (int i = 0; i < schede.size(); i++) {
            StringBuilder featureSheetsBuilder = new StringBuilder();
            Element scheda = schede.get(i);
            Elements figli = scheda.children();

            if (figli.hasClass("content-section-header")) {
                Element titolo = figli.select("h2").first();
                featureSheetsBuilder.append("<h3>").append(titolo.text()).append("</h3>");
            }

            if (figli.hasClass("game_info_container")) {
                Elements gameInfo = scheda.select(".game_info_container").first().children();
                for (Element tmp : gameInfo) {
                    String gameTitle = tmp.select(".game_info_title").text();
                    featureSheetsBuilder.append("<p> <strong>").append(gameTitle).append(": </strong>");
                    String gameText = tmp.select(".game_info_text").text();
                    featureSheetsBuilder.append(gameText).append("</p>");
                }
            }

            featureSheetsBuilder.append("<br/>");
            featureSheets.add(featureSheetsBuilder.toString());
        }
        game.setFeatureSheets(featureSheets);

        return game;
    }

    @Override
    public void onPostExecute(Game o) {
        delegate.processFinish(o);
    }

}
