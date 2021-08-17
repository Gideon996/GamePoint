package it.adriano.tumino.gamepoint.processes.handler;

import android.annotation.SuppressLint;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.processes.ProcessUtils;
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
                        String dataPublishedDate = tagA.select("time.published-date").first().attributes().get("data-published-date");

                        String date = ProcessUtils.normalizePCGamerDate(dataPublishedDate);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH:mm", Locale.getDefault());
                        String currentTime = sdf.format(new Date());
                        if (currentTime.split("/")[0].equals(dataPublishedDate.split("T")[0])) {
                            String currentH = currentTime.split("/")[1].split(":")[0];
                            String newsH = dataPublishedDate.split("T")[1].split(":")[0];

                            int difference = Integer.parseInt(currentH) - Integer.parseInt(newsH);
                            if (difference <= 0)
                                date = "Less than an hour ago";
                            else if (difference == 1)
                                date = "An hour ago";
                            else
                                date = difference + " hours ago";
                        }

                        News news1 = new News(title, body, imageUrl, date, newsUrl, "PcGamer.com", 0);
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

            int comparator = Integer.parseInt(a[0]) + 24;

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy/HH:mm", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            if (date.equalsIgnoreCase(currentTime.split("/")[0])) {
                String currentH = currentTime.split("/")[1].split(":")[0];
                String newsH = news.getElementsByTag("span").get(0).text().split(",")[0];
                Pattern pattern = Pattern.compile("(2[0-3]|[01]?[0-9]):([0-5]?[0-9])$");
                Matcher matcher = pattern.matcher(newsH);

                if (matcher.find()) {
                    newsH = Objects.requireNonNull(matcher.group(0)).split(":")[0];
                    int difference = Integer.parseInt(currentH) - Integer.parseInt(newsH);
                    if (difference <= 0) {
                        comparator = 0;
                        date = "Meno di un'ora fa";
                    } else if (difference == 1) {
                        comparator = 1;
                        date = "Un'ora fa";
                    } else {
                        comparator = difference;
                        date = difference + " ore fa";
                    }
                }
            }

            Element link = news.getElementsByTag("a").get(0);
            String newsUrl = link.attributes().get("href");
            String title = link.attributes().get("title");
            String body = news.getElementsByTag("p").get(0).text();

            News gameNews = new News(title, body, imageURL, date, newsUrl, "everyeye.it", comparator);
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

            int comparator;

            if (!date.matches("([0-9]{2})\\\\([0-9]{2})\\\\([0-9]{4})")) {
                if(date.contains("ora")) {
                    comparator = 1;
                }else if(date.contains("ore")){
                    comparator = Integer.parseInt(date.split(" ")[0]);
                }else {
                    comparator = 0;
                }
                date = (date.contains("or")) ? date : "Meno di un'ora fa";
            }else{
                comparator = Integer.parseInt(date.split(" ")[0]) + 24;
            }

            if (!body.isEmpty()) {
                News gameNews = new News(title, body, imageURL, date, newsUrl, "multiplayer.it", comparator);
                list.add(gameNews);
            }
        }

        return list;
    }


}
