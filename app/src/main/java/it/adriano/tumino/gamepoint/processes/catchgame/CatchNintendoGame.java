package it.adriano.tumino.gamepoint.processes.catchgame;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.data.storegame.NintendoStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.processes.TaskRunner;

public class CatchNintendoGame extends TaskRunner<Void, StoreGame> implements WebScrapping<NintendoStoreGame> {
    private static final String TAG = "CatchGameFromEShop";

    private static final String BASE_URL = "https://www.nintendo.it";

    private final String finalURL;
    private final String price;

    public AsyncResponse<StoreGame> delegate = null;

    public CatchNintendoGame(String url, String price) {
        finalURL = BASE_URL + url;
        this.price = price;
    }

    @Override
    public StoreGame doInBackground(Void... integers) {
        return getWebPageAndParsing(finalURL, TAG);
    }

    @Override
    public NintendoStoreGame scrapping(@NotNull Document document) {
        NintendoStoreGame game = new NintendoStoreGame();
        game.setPrice(price);

        Elements content = document.getElementsByClass("tab-content");
        Elements header = content.select(".gamepage-banner");

        Elements video = header.select("iframe");
        String videoUrl = "https://www.youtube.com/embed/DKBK4OnvjX0?rel=0&theme=light&modestbranding=1&showinfo=0&autohide=1&autoplay=2&cc_load_policy=0&cc_lang_pref=it&enablejsapi=1";
        if (!video.isEmpty()) videoUrl = video.first().attributes().get("src");
        game.setVideoUrl(videoUrl);


        Elements imageElements = header.select("img");
        String imageHeader = "https://cdn02.nintendo-europe.com/media/images/10_share_images/others_3/nintendo_eshop_5/H2x1_NintendoeShop_WebsitePortal_itIT.jpg";
        if (!imageElements.isEmpty()) imageHeader = imageElements.first().attributes().get("src");
        game.setImageHeaderURL(imageHeader.replaceAll("//", "https://"));
        game.setThumbnail(imageHeader.replaceAll("//", "https://"));

        Elements classification = header.select(".age-rating").select("span");
        String pegi = "pegi3";
        if (!classification.isEmpty()) pegi = classification.text();
        game.setPegi(pegi);

        Elements gamePageHeader = content.select("#gamepage-header").select(".gamepage-header-info");
        String title = gamePageHeader.select("h1").text();
        String console = gamePageHeader.select("span").first().text();
        String releaseDate = gamePageHeader.select("span").last().text();
        game.setTitle(title);
        game.setConsole(console);
        game.setReleaseData(releaseDate);

        Elements panoramica = content.select("#Panoramica");
        Element gameSectionContents = panoramica.select("div").first();
        Element div = gameSectionContents.children().first();

        Elements information = div.children().select(".row-content");
        String text = information.text();
        String[] sezioni = text.split(" {2}");
        StringBuilder builder = new StringBuilder();
        for (String sezione : sezioni) {
            if (!sezione.isEmpty()) builder.append(sezione).append("<br/>");
        }
        game.setDescription(builder.toString());

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
    public void onPostExecute(StoreGame output) {
        delegate.processFinish(output);
    }
}
