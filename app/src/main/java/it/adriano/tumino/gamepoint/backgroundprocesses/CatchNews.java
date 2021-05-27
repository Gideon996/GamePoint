package it.adriano.tumino.gamepoint.backgroundprocesses;

import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import it.adriano.tumino.gamepoint.adapter.NewsAdapter;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.ui.news.NewsViewModel;
import it.adriano.tumino.gamepoint.utils.TaskRunner;

public class CatchNews extends TaskRunner<Integer, List<News>> {
    public static final String TAG = "CatchNews";

    private static final String urlEveryeye = "https://www.everyeye.it/notizie/?pagina=";
    private static final String urlMultiplayer = "https://multiplayer.it/articoli/notizie/?page=";

    private static final String[] mounth = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};

    private final NewsViewModel newsViewModel;
    private final NewsAdapter newsAdapter;

    public CatchNews(NewsViewModel newsViewModel, NewsAdapter newsAdapter) {
        Log.i(TAG, "Creazione Catch News");
        this.newsViewModel = newsViewModel;
        this.newsAdapter = newsAdapter;
    }

    @Override
    public List<News> doInBackground(Integer... integers) {
        Log.i(TAG, "Prelevamento Notizie");
        ArrayList<News> list = getEveryeye(integers[0]);
        list.addAll(getMultiplayer(integers[0]));
        Collections.shuffle(list);
        return list;
    }

    @Override
    public void onPostExecute(List<News> gameNewsList) {
        Log.i(TAG, "Inserimento Notizie");
        newsViewModel.getShimmerFrameLayout().stopShimmer();
        newsViewModel.getShimmerFrameLayout().setVisibility(View.GONE);
        newsViewModel.getRecyclerView().setVisibility(View.VISIBLE);

        newsViewModel.setList(gameNewsList);
        newsAdapter.notifyDataSetChanged();
    }

    private ArrayList<News> getEveryeye(int page) {
        String url = urlEveryeye + page;
        ArrayList<News> list = new ArrayList<>();
        try {
            Document document = getFromUrl(url);

            Elements elements = document.getElementsByClass("fvideogioco");
            for (Element element : elements) {
                String imageURL = element.getElementsByClass("lazy").get(0).attributes().get("data-src"); //nullo se non è possibile trovare il tag

                Element news = element.getElementsByClass("testi_notizia").get(0); //dati della notizia
                String date = news.getElementsByTag("span").get(0).text().split(",")[1].substring(1); //prendo solo la data, senza lo spazio iniziale

                Element link = news.getElementsByTag("a").get(0); //link notizia e titolo
                String newsUrl = link.attributes().get("href");
                String title = link.attributes().get("title");
                String body = news.getElementsByTag("p").get(0).text();

                News gameNews = new News(title, body, imageURL, date, newsUrl, "everyeye.it");
                list.add(gameNews);
            }
        } catch (IOException e) {
            Log.e(TAG, "Errore Prelevamento Notizie Everyeye: " + e.getMessage());
        }
        return list;
    }

    private ArrayList<News> getMultiplayer(int page) {
        Log.i(TAG, "Prelevamento Notizie da Multiplayer");
        String url = urlMultiplayer + page;
        ArrayList<News> list = new ArrayList<>();
        try {
            Document document = getFromUrl(url);

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
                    date = string[0] + " " + mounth[Integer.parseInt(string[1]) - 1] + " " + string[string.length - 1];
                }
                if (!body.isEmpty()) {
                    News gameNews = new News(title, body, imageURL, date, newsUrl, "multiplayer.it");
                    list.add(gameNews);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Errore Prelevamento Notizie Multiplayer: " + e.getMessage());
        }
        return list;
    }

    private Document getFromUrl(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                .get();
    }
}
