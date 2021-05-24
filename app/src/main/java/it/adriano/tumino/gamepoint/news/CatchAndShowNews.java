package it.adriano.tumino.gamepoint.news;


import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.adriano.tumino.gamepoint.ui.news.NewsViewModel;

public class CatchAndShowNews extends AsyncTask<Integer, Integer, List<GameNews>> {


    private final NewsViewModel newsViewModel;
    private final NewsAdapterRecycle newsAdapterRecycle; //nuovo

    public CatchAndShowNews(NewsViewModel newsViewModel, NewsAdapterRecycle newsAdapterRecycle) { //nuovo
        this.newsViewModel = newsViewModel;
        this.newsAdapterRecycle = newsAdapterRecycle;
    }

    @Override
    protected List<GameNews> doInBackground(Integer... integers) {
        String url = "https://www.everyeye.it/notizie/?pagina=" + integers[0];
        ArrayList<GameNews> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("fvideogioco");
            for (Element element : elements) {
                Element image = element.getElementsByClass("lazy").get(0); //immagine
                Attributes attributes = image.attributes();
                String imageURL = attributes.get("data-src"); //nullo se non è possibile trovare il tag

                Element notizia = element.getElementsByClass("testi_notizia").get(0); //dati della notizia
                Element span = notizia.getElementsByTag("span").get(0); //tipo e data
                String data = span.text().split(",")[1].substring(1); //prendo solo la data, senza lo spazio iniziale

                Element link = notizia.getElementsByTag("a").get(0); //link notizia e titolo
                attributes = link.attributes();
                String notiziaUrl = attributes.get("href");
                String titolo = attributes.get("title");
                String testo = notizia.getElementsByTag("p").get(0).text();

                GameNews gameNews = new GameNews(titolo, testo, imageURL, data, notiziaUrl, "everyeye.it");
                list.add(gameNews);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<GameNews> result) {
        newsViewModel.setList(result);
        newsAdapterRecycle.notifyDataSetChanged(); //nuovo
    }
}
