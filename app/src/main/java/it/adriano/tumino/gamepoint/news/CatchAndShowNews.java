package it.adriano.tumino.gamepoint.news;


import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.adriano.tumino.gamepoint.ui.news.NewsViewModel;

public class CatchAndShowNews extends AsyncTask<Integer, Void, List<GameNews>> {


    private final NewsViewModel newsViewModel;
    private final NewsAdapterRecycle newsAdapterRecycle; //nuovo

    public CatchAndShowNews(NewsViewModel newsViewModel, NewsAdapterRecycle newsAdapterRecycle) { //nuovo
        this.newsViewModel = newsViewModel;
        this.newsAdapterRecycle = newsAdapterRecycle;
    }

    @Override
    protected List<GameNews> doInBackground(Integer... integers) {
        ArrayList<GameNews> list = getEveryeye(integers[0]);
        list.addAll(getMultiplayer(integers[0]));
        Collections.shuffle(list);
        return list;
    }

    public ArrayList<GameNews> getEveryeye(int page) {
        String url = "https://www.everyeye.it/notizie/?pagina=" + page;
        ArrayList<GameNews> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("fvideogioco");
            for (Element element : elements) {
                String imageURL = element.getElementsByClass("lazy").get(0).attributes().get("data-src"); //nullo se non è possibile trovare il tag

                Element notizia = element.getElementsByClass("testi_notizia").get(0); //dati della notizia
                String data = notizia.getElementsByTag("span").get(0).text().split(",")[1].substring(1); //prendo solo la data, senza lo spazio iniziale

                Element link = notizia.getElementsByTag("a").get(0); //link notizia e titolo
                String notiziaUrl = link.attributes().get("href");
                String titolo = link.attributes().get("title");
                String testo = notizia.getElementsByTag("p").get(0).text();

                GameNews gameNews = new GameNews(titolo, testo, imageURL, data, notiziaUrl, "everyeye.it");
                list.add(gameNews);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<GameNews> getMultiplayer(int page) {
        String[] mesi = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
        String url = "https://multiplayer.it/articoli/notizie/?page=" + page;
        ArrayList<GameNews> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36") //per visualizzare tutto correttamente
                    .get();

            Elements elements = document.getElementsByClass("media");
            for (Element element : elements) {
                String imageURL = element.getElementsByTag("img").get(0).attributes().get("data-src"); //nullo se non è possibile trovare il tag
                Element mediaBody = element.getElementsByClass("media-body").get(0);
                String newsUrl = "https://multiplayer.it" + mediaBody.getElementsByTag("a").get(0).attributes().get("href");
                String titolo = mediaBody.getElementsByTag("a").get(0).text();
                String[] subString = mediaBody.getElementsByTag("p").text().split("\\|");
                String testo = subString[subString.length - 1].trim();
                String data = subString[0].split(" - ")[subString[0].split(" - ").length - 1];

                if(!data.matches("([0-9]{2})\\\\([0-9]{2})\\\\([0-9]{4})")){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    String string[] = sdf.format(Calendar.getInstance().getTime()).split("/");
                    data = string[0] + " " + mesi[Integer.parseInt(string[1]) - 1] + " " + string[string.length - 1];
                }
                if(!testo.isEmpty()){
                    GameNews gameNews = new GameNews(titolo, testo, imageURL, data, newsUrl, "multiplayer.it");
                    list.add(gameNews);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<GameNews> result) {
        newsViewModel.getShimmerFrameLayout().stopShimmer();
        newsViewModel.getShimmerFrameLayout().setVisibility(View.GONE);
        newsViewModel.getRecyclerView().setVisibility(View.VISIBLE);

        newsViewModel.setList(result);
        newsAdapterRecycle.notifyDataSetChanged(); //nuovo
    }
}
