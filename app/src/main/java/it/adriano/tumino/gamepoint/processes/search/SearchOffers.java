package it.adriano.tumino.gamepoint.processes.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameOffers;
import it.adriano.tumino.gamepoint.processes.AsyncResponse;
import it.adriano.tumino.gamepoint.processes.handler.NintendoHandler;
import it.adriano.tumino.gamepoint.processes.handler.SteamHandler;
import it.adriano.tumino.gamepoint.processes.TaskRunner;

public class SearchOffers extends TaskRunner<Void, List<GameOffers>> {
    private static final String TAG = "SearchOffers";

    private final Context context;

    public AsyncResponse<List<GameOffers>> delegate;

    public SearchOffers(Context context) {
        this.context = context;
    }

    @Override
    public List<GameOffers> doInBackground(Void... input) {
        String[] defaultValues = context.getResources().getStringArray(R.array.store_values);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> selections = sharedPreferences.getStringSet("shopSelection", new HashSet<>(Arrays.asList(defaultValues)));
        Log.d(TAG, "Get Shop Preferences. Current preferences are: " + selections.toString());

        if (selections.size() == 0) return null;

        Callable<List<GameOffers>> callableSteam = SteamHandler::topSellerAndSpecialsSteam;
        //Callable<List<GameOffers>> callableNintendo = NintendoHandler::nintendoOffers;
        //Callable<List<BasicGameInformation>> callableMicrosoft = () -> StoresSearchHandler.microsoftGames();
        //Callable<List<BasicGameInformation>> callablePlayStation = () -> StoresSearchHandler.playStationGames();

        List<Callable<List<GameOffers>>> taskList = new ArrayList<>();
        for (String selection : selections) {
            switch (selection) {
                case "STEAM":
                    taskList.add(callableSteam);
                    break;
                case "ESHOP":
                    //taskList.add(callableNintendo);
                    break;
                case "MCS":
                    //taskList.add(callableMicrosoft);
                    break;
                case "PSN":
                    //taskList.add(callablePlayStation);
                    break;
            }
        }

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
