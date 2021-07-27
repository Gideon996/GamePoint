package it.adriano.tumino.gamepoint.processes.handler;

import android.annotation.SuppressLint;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.utils.Utils;

public class NewsHandler {
    private static final String TAG = "NewsHandler";

    private static final String URL_EVERYEYE = "https://www.everyeye.it/notizie/?pagina=";
    private static final String URL_MULTIPLAYER = "https://multiplayer.it/articoli/notizie/?page=";
    private static final String URL_PCGAMER = "https://www.pcgamer.com/news/page/";

    public static ArrayList<News> getPcGamerNews(int page) {
        String url = URL_PCGAMER + page;
        ArrayList<News> list = new ArrayList<>();
        Document document = Utils.getDocumentFromUrl(url);
        if (document == null) return list;
        Log.i(TAG, "Starting parsing PCGAMER's HTML for the news");
        Element content = document.getElementById("content");
        Elements news = content.select("section.news");
        if (news.size() > 0) {
            Elements elements = news.select("div.small");
            if (elements.size() > 0) {
                for (Element element : elements) {
                    if (!element.hasClass("sponsored-post")) {
                        Element tagA = element.getElementsByTag("a").first();
                        String newsUrl = tagA.attributes().get("href");
                        String imageUrl = tagA.select("img.optional-image").first().attributes().get("data-pin-media");
                        String title = tagA.select("h3.article-name").text();
                        String body = tagA.select("p.synopsis").text();
                        String date = HandlerUtils.normalizePCGamerDate(tagA.select("time.published-date").first().attributes().get("data-published-date"));

                        News news1 = new News(title, body, imageUrl, date, newsUrl, "PcGamer.com");
                        list.add(news1);
                    }
                }
            }
        }
        return list;
    }

    public static ArrayList<News> getEveryeye(int page) {
        String url = URL_EVERYEYE + page;
        ArrayList<News> list = new ArrayList<>();
        Document document = Utils.getDocumentFromUrl(url);
        if (document == null) return list;
        Log.i(TAG, "Starting parsing Everyeye's HTML for the news");
        Elements elements = document.getElementsByClass("fvideogioco");
        for (Element element : elements) {
            String imageURL = element.getElementsByClass("lazy").get(0).attributes().get("data-src");
            Element news = element.getElementsByClass("testi_notizia").get(0);
            String date = news.getElementsByTag("span").get(0).text().split(",")[1].substring(1);
            String[] a = date.split(" ");
            if (a[0].length() < 2) a[0] = "0" + a[0];
            date = String.join(" ", a);

            Element link = news.getElementsByTag("a").get(0);
            String newsUrl = link.attributes().get("href");
            String title = link.attributes().get("title");
            String body = news.getElementsByTag("p").get(0).text();

            News gameNews = new News(title, body, imageURL, date, newsUrl, "everyeye.it");
            list.add(gameNews);
        }
        return list;
    }

    @SuppressLint("SimpleDateFormat")
    public static ArrayList<News> getMultiplayer(int page) {
        String url = URL_MULTIPLAYER + page;
        ArrayList<News> list = new ArrayList<>();
        Document document = Utils.getDocumentFromUrl(url);
        if (document == null) return list;
        Log.i(TAG, "Starting parsing Multiplayer's HTML for the news");
        Elements elements = document.getElementsByClass("media");
        for (Element element : elements) {
            String imageURL = element.getElementsByTag("img").get(0).attributes().get("data-src");
            Element mediaBody = element.getElementsByClass("media-body").get(0);
            String newsUrl = "https://multiplayer.it" + mediaBody.getElementsByTag("a").get(0).attributes().get("href");
            String title = mediaBody.getElementsByTag("a").get(0).text();
            String[] subString = mediaBody.getElementsByTag("p").text().split("\\|");
            String body = subString[subString.length - 1].trim();
            String date = subString[0].split(" - ")[subString[0].split(" - ").length - 1];

            if (!date.matches("([0-9]{2})\\\\([0-9]{2})\\\\([0-9]{4})")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                String[] string = sdf.format(Calendar.getInstance().getTime()).split("/");
                date = string[0] + " " + HandlerUtils.fromNumberToName(string[1]) + " 20" + string[string.length - 1];
            }
            if (!body.isEmpty()) {
                News gameNews = new News(title, body, imageURL, date, newsUrl, "multiplayer.it");
                list.add(gameNews);
            }
        }

        return list;
    }


}
