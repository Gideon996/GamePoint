package it.adriano.tumino.gamepoint.processes.catchgame;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.data.storegame.NintendoStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.processes.ProcessUtils;
import it.adriano.tumino.gamepoint.processes.TaskRunner;
import it.adriano.tumino.gamepoint.processes.handler.NintendoHandler;

public class CatchNintendoGame extends TaskRunner<Void, StoreGame> implements WebScrapping<NintendoStoreGame> {
    private static final String TAG = "CatchGameFromEShop";

    private final String finalURL;
    private final String price;

    public AsyncResponse<StoreGame> delegate = null;

    public CatchNintendoGame(String url, String price) {
        finalURL = NintendoHandler.generateGameUrl(url);
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
        HashMap<String, String> hashMap = NintendoHandler.webClassName();

        Elements content = document.getElementsByClass(hashMap.get("tabContent"));
        Elements header = content.select(hashMap.get("gameBannerClass"));

        Elements video = header.select(hashMap.get("iframe"));
        String videoUrl = "https://www.youtube.com/embed/DKBK4OnvjX0?rel=0&theme=light&modestbranding=1&showinfo=0&autohide=1&autoplay=2&cc_load_policy=0&cc_lang_pref=it&enablejsapi=1";
        if (!video.isEmpty()) videoUrl = video.first().attributes().get(hashMap.get("src"));
        game.setVideoUrl(videoUrl);


        Elements imageElements = header.select(hashMap.get("img"));
        String imageHeader = "https://cdn02.nintendo-europe.com/media/images/10_share_images/others_3/nintendo_eshop_5/H2x1_NintendoeShop_WebsitePortal_itIT.jpg";
        if (!imageElements.isEmpty())
            imageHeader = imageElements.first().attributes().get(hashMap.get("src"));
        game.setImageHeaderURL(imageHeader.replaceAll("//", "https://"));
        game.setThumbnail(imageHeader.replaceAll("//", "https://"));

        Elements classification = header.select(hashMap.get("ageRatingClass")).select(hashMap.get("span"));
        String pegi = "pegi3";
        if (!classification.isEmpty()) pegi = classification.text();
        game.setPegi(pegi);

        Elements gamePageHeader = content.select(hashMap.get("gamePageHeaderId")).select(hashMap.get("gamePageHeaderInfoClass"));
        String title = gamePageHeader.select(hashMap.get("h1")).text();
        String console = gamePageHeader.select(hashMap.get("span")).first().text();
        String releaseDate = gamePageHeader.select(hashMap.get("span")).last().text();
        game.setTitle(title);
        game.setConsole(console);
        game.setReleaseData(ProcessUtils.normalizeNintendoDate(releaseDate));

        Elements panoramica = content.select(hashMap.get("overviewId"));
        Element gameSectionContents = panoramica.select(hashMap.get("div")).first();
        Element div = gameSectionContents.children().first();

        Elements information = div.children().select(hashMap.get("rowContentClass"));
        String text = information.text();
        String[] sezioni = text.split(" {2}");
        StringBuilder builder = new StringBuilder();
        for (String sezione : sezioni) {
            if (!sezione.isEmpty()) builder.append(sezione).append("<br/>");
        }
        game.setDescription(builder.toString());

        Elements immagini = content.select(hashMap.get("galleryId"));
        ArrayList<String> screenshotsUrl = new ArrayList<>();
        if (immagini != null && !immagini.isEmpty()) {
            Elements galleria = immagini.select(hashMap.get("divRowSelector")).select(hashMap.get("ul")).first().children();
            for (Element tmp : galleria) {
                Element immagine = tmp.select(hashMap.get("imgResponsiveSelector")).first();
                if (immagine != null)
                    screenshotsUrl.add(immagine.attributes().get(hashMap.get("dataXS")).replaceAll("//", "https://"));
            }
        }
        game.setScreenshotsUrl(screenshotsUrl);

        Elements dettagli = content.select(hashMap.get("gameDetailsID"));
        Elements systemInfo = dettagli.select(hashMap.get("systemInfoClass")).not(hashMap.get("divAmiiboSelector"));
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : systemInfo) {
            if (element.select(hashMap.get("gameInfoTitleClass")).hasText()) {
                stringBuilder.append("<p><strong>").append(element.select(hashMap.get("gameInfoTitleClass")).first().text()).append(": ").append("</strong>");
            }

            if (element.select(hashMap.get("gameInfoTextClass")).hasText()) {
                stringBuilder.append(element.select(hashMap.get("gameInfoTextClass")).first().text()).append("</p>");
            }

            if (element.children().hasClass(hashMap.get("ageRating"))) {
                stringBuilder.append(element.select(hashMap.get("span")).text()).append("</p>");
            }
        }
        game.setSystemInfo(stringBuilder.toString());

        systemInfo = content.select(hashMap.get("divRowSelector")).select(hashMap.get("divGameInfoSelector"));
        Elements schede = systemInfo.select(hashMap.get("listWheaderClass"));

        ArrayList<String> featureSheets = new ArrayList<>();
        for (int i = 0; i < schede.size(); i++) {
            StringBuilder featureSheetsBuilder = new StringBuilder();
            Element scheda = schede.get(i);
            Elements figli = scheda.children();

            if (figli.hasClass(hashMap.get("contentSectionHeader"))) {
                Element titolo = figli.select(hashMap.get("h2")).first();
                featureSheetsBuilder.append("<h3>").append(titolo.text()).append("</h3>");
            }

            if (figli.hasClass(hashMap.get("gameInfoContainer"))) {
                Elements gameInfo = scheda.select(hashMap.get("gameInfoContainerClass")).first().children();
                for (Element tmp : gameInfo) {
                    String gameTitle = tmp.select(hashMap.get("gameInfoTitleClass")).text();
                    featureSheetsBuilder.append("<p> <strong>").append(gameTitle).append(": </strong>");
                    String gameText = tmp.select(hashMap.get("gameInfoTextClass")).text();
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
