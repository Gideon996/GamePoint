package it.adriano.tumino.gamepoint.backgroundprocesses.catchgame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.adriano.tumino.gamepoint.backgroundprocesses.AsyncResponse;
import it.adriano.tumino.gamepoint.data.Game;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class CatchGameFromEShop extends TaskRunner<Integer, String> {

    private static final String BASE_URL = "https://www.nintendo.it";

    private final String finalUrl;
    private final Game game;

    public AsyncResponse<Game> delegate = null;

    public CatchGameFromEShop(String url, String price) {
        finalUrl = BASE_URL + url;
        game = new Game();
        game.setPrice(price+"€");
    }

    @Override
    public String doInBackground(Integer... integers) {

        Document document;
        try {
            document = Jsoup.connect(finalUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36") //per visualizzare tutto correttamente
                    .get();
        } catch (IOException exception) {
            return null;
        }

        Elements content = document.getElementsByClass("tab-content");
        //Banner information
        Elements header = content.select(".gamepage-banner");

        Elements video = header.select("iframe");
        String videoUrl = "";
        if (!video.isEmpty()) videoUrl = video.first().attributes().get("src");


        Elements img = header.select("img");
        String imageHeader = "";
        if (!img.isEmpty()) imageHeader = img.first().attributes().get("src");
        game.setImage(imageHeader.replaceAll("//", "https://"));


        Elements classification = header.select(".age-rating").select("span");
        String pegi = "";
        if (!classification.isEmpty()) pegi = classification.text();


        //GamePageHeader
        Elements gamePageHeader = content.select("#gamepage-header").select(".gamepage-header-info");
        String title = gamePageHeader.select("h1").text();
        String console = gamePageHeader.select("span").first().text();
        String releaseDate = gamePageHeader.select("span").last().text();
        game.setName(title);
        game.setDate(releaseDate);

        //Panoramica
        Elements panoramica = content.select("#Panoramica");
        Element gameSectionContents = panoramica.select("div").first();
        Element div = gameSectionContents.children().first();

        Elements information = div.children().select(".row-content");
        String text = information.text();
        String[] sezioni = text.split("  "); //l'unica soluzione possibile per prendere i testi -> pe fozza
        StringBuilder builder = new StringBuilder();
        for (String sezione : sezioni) {
            if (!sezione.isEmpty()) builder.append(sezione + "<br/>");
        }
        game.setDescription(builder.toString());


        //Galleria_immagini
        Elements immagini = content.select("#Galleria_immagini");
        ArrayList<String> screenshotsUrl = new ArrayList<>();
        if (immagini != null && !immagini.isEmpty()) {
            Elements galleria = immagini.select("div.row").select("ul").first().children();
            for (Element tmp : galleria) {
                Element immagine = tmp.select("img.img-responsive").first();
                if (immagine != null) screenshotsUrl.add(immagine.attributes().get("data-xs").replaceAll("//", "https://"));
            }
        }
        game.setScreenshots(screenshotsUrl);

        //gameDetails
        Elements dettagli = content.select("#gameDetails");
        Elements systemInfo = dettagli.select(".system_info").not("div.amiibo_info");
        ArrayList<String> gameInfoTitle = new ArrayList<>();
        ArrayList<String> gameInfoText = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : systemInfo) {
            String gameTitle = "";
            String gameText = "";

            if (element.select(".game_info_title").hasText()){
                stringBuilder.append("<p><strong>").append(element.select(".game_info_title").first().text()).append(": ").append("</strong>");
            }
                //gameTitle = element.select(".game_info_title").first().text();
            if (element.select(".game_info_text").hasText()){
                stringBuilder.append(element.select(".game_info_text").first().text()).append("</p>");
                //gameText = element.select(".game_info_text").first().text();
            }
            if (element.children().hasClass("age-rating")){
                stringBuilder.append(element.select("span").text()).append("</p>");
                //gameText = element.select("span").text();
            }

            gameInfoTitle.add(title);
            gameInfoText.add(text);
        }
        game.setMinimumRequirement(stringBuilder.toString());
        game.setGenres(gameInfoTitle);
        game.setCategories(gameInfoText);

        systemInfo = content.select("div.row").select("div.game_info");
        Elements schede = systemInfo.select(".listwheader-container");
        //game.setRecommendedRequirement(schede.html());

        stringBuilder = new StringBuilder();
        for (int i = 0; i < schede.size(); i++) {
            Element scheda = schede.get(i);
            Elements figli = scheda.children();
            if (figli.hasClass("content-section-header")) {
                Element titolo = figli.select("h2").first();
                stringBuilder.append("<h3>"+titolo.text()+"</h3>");
            }
            if (figli.hasClass("game_info_container")) {
                HashMap<String, String> hashMap = new HashMap<>();
                Elements gameInfo = scheda.select(".game_info_container").first().children();
                for (Element tmp : gameInfo) {
                    String gameTitle = tmp.select(".game_info_title").text();
                    stringBuilder.append("<p> <strong>"+gameTitle+": </strong>");
                    String gameText = tmp.select(".game_info_text").text();
                    stringBuilder.append(gameText + "</p>");
                }
            }
            stringBuilder.append("<br/><br/>");
        }
        game.setRecommendedRequirement(stringBuilder.toString());

        return null;
    }

    @Override
    public void onPostExecute(String o) {
        delegate.processFinish(game);
    }
}
