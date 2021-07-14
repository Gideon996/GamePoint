package it.adriano.tumino.gamepoint.processes;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.utils.Utils;

public class CatchNews extends TaskRunner<Integer, List<News>> {
    public static final String TAG = "CatchNews";

    private static final String URL_EVERYEYE = "https://www.everyeye.it/notizie/?pagina=";
    private static final String URL_MULTIPLAYER = "https://multiplayer.it/articoli/notizie/?page=";

    private static final String[] mounth = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};

    public AsyncResponse<List<News>> delegate;

    public CatchNews(AsyncResponse<List<News>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<News> doInBackground(@NonNull Integer... integers) {
        Log.i(TAG, "Starting catch news");
        Callable<List<News>> callableEveryeye = () -> getEveryeye(integers[0]);
        Callable<List<News>> callableMultiplayer = () -> getMultiplayer(integers[0]);

        List<Callable<List<News>>> taskList = new ArrayList<>();
        taskList.add(callableEveryeye);
        taskList.add(callableMultiplayer);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Future<List<News>>> results = executor.invokeAll(taskList);
            ArrayList<News> list = new ArrayList<>();
            for (Future<List<News>> result : results) {
                list.addAll(result.get());
            }
            return list;
        } catch (InterruptedException | ExecutionException ie) {
            return new ArrayList<>();
        }
    }

    @Override
    public void onPostExecute(List<News> gameNewsList) {
        Log.i(TAG, "Retrived News, started delegation");
        delegate.processFinish(gameNewsList);
    }

    private ArrayList<News> getEveryeye(int page) {
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
    private ArrayList<News> getMultiplayer(int page) {
        String url = URL_MULTIPLAYER + page;
        ArrayList<News> list = new ArrayList<>();
        Document document = Utils.getDocumentFromUrl(url);
        if (document == null) return list;
        Log.i(TAG, "Starting parsing Multiplayer's HTML for the news");
        Elements elements = document.getElementsByClass("media");
        for (Element element : elements) {
            String imageURL = element.getElementsByTag("img").get(0).attributes().get("data-src"); //nullo se non è possibile trovare il tag
            Element mediaBody = element.getElementsByClass("media-body").get(0);
            String newsUrl = "https://multiplayer.it" + mediaBody.getElementsByTag("a").get(0).attributes().get("href");
            String title = mediaBody.getElementsByTag("a").get(0).text();
            String[] subString = mediaBody.getElementsByTag("p").text().split("\\|");
            String body = subString[subString.length - 1].trim();
            String date = subString[0].split(" - ")[subString[0].split(" - ").length - 1];

            if (!date.matches("([0-9]{2})\\\\([0-9]{2})\\\\([0-9]{4})")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                String[] string = sdf.format(Calendar.getInstance().getTime()).split("/");
                date = string[0] + " " + mounth[Integer.parseInt(string[1]) - 1] + " 20" + string[string.length - 1];
            }
            if (!body.isEmpty()) {
                News gameNews = new News(title, body, imageURL, date, newsUrl, "multiplayer.it");
                list.add(gameNews);
            }
        }

        return list;
    }

}
