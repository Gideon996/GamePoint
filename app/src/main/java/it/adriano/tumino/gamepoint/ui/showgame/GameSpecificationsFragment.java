package it.adriano.tumino.gamepoint.ui.showgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.Game;


public class GameSpecificationsFragment extends Fragment {

    private Bundle information;
    private String store;
    private Game game;

    public GameSpecificationsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            information = getArguments();
            store = information.getString("store");
            game = information.getParcelable("game");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_specifications, container, false);
        switch (store.toUpperCase()) {
            case "STEAM":
                steamGame(view);
                break;
            case "ESHOP":
                eShopGame(view);
                break;
        }
        return view;
    }

    private void eShopGame(View view){
        LinearLayout linearLayout = view.findViewById(R.id.eshopSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        TextView caratteristiche = view.findViewById(R.id.caratteristicheTextView);
        caratteristiche.setText(Html.fromHtml(game.getMinimumRequirement(), Html.FROM_HTML_MODE_LEGACY));

        TextView textView = view.findViewById(R.id.serieDiConsole);
        textView.setText(Html.fromHtml(game.getRecommendedRequirement(), Html.FROM_HTML_MODE_LEGACY));
    }

    private void steamGame(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.steamSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        TextView sviluppatoreTextView = view.findViewById(R.id.developerTextView);
        String string = String.join(", ", game.getDevelopers());
        sviluppatoreTextView.setText(string);

        TextView editoreTextView = view.findViewById(R.id.publisherTextView);
        string = String.join(", ", game.getPublishers());
        editoreTextView.setText(string);

        TextView webSiteTextView = view.findViewById(R.id.webSiteTextView);
        webSiteTextView.setText(game.getWebsite());

        TextView categoriesTextView = view.findViewById(R.id.categoryTextView);
        string = fromListToHTML(game.getCategories());
        categoriesTextView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY));

        TextView generiTextView = view.findViewById(R.id.generiTextView);
        string = fromListToHTML(game.getGenres());
        generiTextView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY));

        TextView languagesTextView = view.findViewById(R.id.languagesTextView);
        languagesTextView.setText(Html.fromHtml(game.getLanguages(), Html.FROM_HTML_MODE_LEGACY));

        TextView minimumTextView = view.findViewById(R.id.minimumTextView);
        minimumTextView.setText(Html.fromHtml(game.getMinimumRequirement(), Html.FROM_HTML_MODE_LEGACY));
        TextView reccommendedTextView = view.findViewById(R.id.reccommendetTextView);
        TextView scrittaRaccomandatiTextView = view.findViewById(R.id.scrittaRaccomandatiTextView);
        if(game.getRecommendedRequirement().isEmpty()){
            scrittaRaccomandatiTextView.setVisibility(View.GONE);
            reccommendedTextView.setVisibility(View.GONE);
        }
        reccommendedTextView.setText(Html.fromHtml(game.getRecommendedRequirement(), Html.FROM_HTML_MODE_LEGACY));

    }

    private String fromListToHTML(List<String> list) {
        String string = "<ul>";
        for (int i = 0; i < list.size(); i++)
            string += "<li>" + list.get(i) + "</li>";
        string += "</ul>";
        return string;
    }
}