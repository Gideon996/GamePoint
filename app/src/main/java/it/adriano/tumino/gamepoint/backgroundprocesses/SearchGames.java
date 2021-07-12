package it.adriano.tumino.gamepoint.backgroundprocesses;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.data.BasicGameInformation;

public class SearchGames extends TaskRunner<String, String> {

    private final String title;

    private List<BasicGameInformation> steamGames = new ArrayList<>();
    private List<BasicGameInformation> microsoftGames = new ArrayList<>();
    private List<BasicGameInformation> playstationGames = new ArrayList<>();
    private List<BasicGameInformation> nintendoGames = new ArrayList<>();

    public SearchGames(String title) {
        this.title = title;
    }

    @Override
    public String doInBackground(String... input) {
        //controllo quali devo cercare secondo le preferenze


        //devo unisco qui i risultati
        //faccio uno shuffle
        return null;
    }

    @Override
    public void onPostExecute(String output) {
        //ritorno in output al sistema
    }
}
