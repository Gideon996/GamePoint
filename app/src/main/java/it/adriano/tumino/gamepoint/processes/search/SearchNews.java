package it.adriano.tumino.gamepoint.processes.search;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.TaskRunner;
import it.adriano.tumino.gamepoint.processes.handler.NewsHandler;

public class SearchNews extends TaskRunner<Integer, List<News>> {
    public static final String TAG = "CatchNews";

    public AsyncResponse<List<News>> delegate;

    public SearchNews(AsyncResponse<List<News>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<News> doInBackground(@NonNull Integer... integers) {
        String language = Locale.getDefault().getLanguage();
        Log.i(TAG, "Starting catch news");

        Callable<List<News>> callableEveryeye = () -> NewsHandler.getEveryeye(integers[0]);
        Callable<List<News>> callableMultiplayer = () -> NewsHandler.getMultiplayer(integers[0]);
        Callable<List<News>> callablePCGames = () -> NewsHandler.getPcGamerNews(integers[0]);
        List<Callable<List<News>>> taskList = new ArrayList<>();

        if (language.equals("it")) {
            taskList.add(callableEveryeye);
            taskList.add(callableMultiplayer);
        } else {
            taskList.add(callablePCGames);
        }

        ExecutorService executor = Executors.newFixedThreadPool(taskList.size());
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


}
