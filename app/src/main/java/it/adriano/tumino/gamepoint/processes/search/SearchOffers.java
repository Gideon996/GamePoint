package it.adriano.tumino.gamepoint.processes.search;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.handler.PlayStationHandler;
import it.adriano.tumino.gamepoint.processes.handler.SteamHandler;
import it.adriano.tumino.gamepoint.processes.TaskRunner;

public class SearchOffers extends TaskRunner<Void, List<GameOffers>> {
    private static final String TAG = "SearchOffers";

    public AsyncResponse<List<GameOffers>> delegate;

    public SearchOffers() {

    }

    @Override
    public List<GameOffers> doInBackground(Void... input) {
        Callable<List<GameOffers>> callableSteam = SteamHandler::topSellerAndSpecialsSteam;
        Callable<List<GameOffers>> callablePlayStation = PlayStationHandler::playStationOffers;

        List<Callable<List<GameOffers>>> taskList = new ArrayList<>();
        taskList.add(callableSteam);
        taskList.add(callablePlayStation);

        Log.d(TAG, "Start search in all selected stores");
        ExecutorService executor = Executors.newFixedThreadPool(taskList.size()); //I create one thread for store
        try {
            List<Future<List<GameOffers>>> results = executor.invokeAll(taskList);
            ArrayList<GameOffers> list = new ArrayList<>();

            for (Future<List<GameOffers>> result : results) {
                list.addAll(result.get());
            }

            Collections.shuffle(list);

            return list;
        } catch (InterruptedException | ExecutionException ie) {
            Log.e(TAG, ie.getMessage());
            return null;
        }
    }

    @Override
    public void onPostExecute(List<GameOffers> output) {
        Log.d(TAG, "Delegation of the results");
        if (output == null) { //In case I found 0 games or an exception occurred
            delegate.processFinish(new ArrayList<>()); //return new list without any element
        } else {
            delegate.processFinish(output); //return the results
        }
    }
}
