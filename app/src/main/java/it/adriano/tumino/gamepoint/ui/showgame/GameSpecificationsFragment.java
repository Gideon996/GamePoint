package it.adriano.tumino.gamepoint.ui.showgame;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.storegame.Game;
import it.adriano.tumino.gamepoint.data.storegame.MicrosoftGame;
import it.adriano.tumino.gamepoint.data.storegame.NintendoGame;
import it.adriano.tumino.gamepoint.data.storegame.PlayStationGame;
import it.adriano.tumino.gamepoint.data.storegame.SteamGame;


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
            case "PSN":
                psnGame(view);
                break;
            case "MCS":
                mcsGame(view);
                break;
        }
        return view;
    }

    private void mcsGame(View view){
        MicrosoftGame microsoftGame = (MicrosoftGame) game;
        LinearLayout linearLayout = view.findViewById(R.id.mcsSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        TextView pegi = view.findViewById(R.id.pegiMCSTextView);
        pegi.setText(microsoftGame.getPegi());
        TextView categories = view.findViewById(R.id.categoriesMCSTextView);
        categories.setText(Html.fromHtml(fromListToHTML(microsoftGame.getCategories()), Html.FROM_HTML_MODE_LEGACY));
        TextView metadata = view.findViewById(R.id.metadataMCSTextView);
        metadata.setText(Html.fromHtml(fromListToHTML(microsoftGame.getMetadata()), Html.FROM_HTML_MODE_LEGACY));
        TextView requirement = view.findViewById(R.id.requirementMCSTextView);
        requirement.setText(Html.fromHtml(microsoftGame.getSystemRequirement(), Html.FROM_HTML_MODE_LEGACY));
    }

    private void steamGame(View view) {
        SteamGame steamGame = (SteamGame) game;

        LinearLayout linearLayout = view.findViewById(R.id.steamSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        TextView sviluppatoreTextView = view.findViewById(R.id.developerTextView);
        String string = String.join(", ", steamGame.getDevelopers());
        sviluppatoreTextView.setText(string);

        TextView editoreTextView = view.findViewById(R.id.publisherTextView);
        string = String.join(", ", steamGame.getPublishers());
        editoreTextView.setText(string);

        TextView webSiteTextView = view.findViewById(R.id.webSiteTextView);
        webSiteTextView.setText(steamGame.getWebsite());

        TextView categoriesTextView = view.findViewById(R.id.categoryPSNTextView);
        string = fromListToHTML(steamGame.getCategories());
        categoriesTextView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY));

        TextView generiTextView = view.findViewById(R.id.generiTextView);
        string = fromListToHTML(steamGame.getGenres());
        generiTextView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY));

        TextView languagesTextView = view.findViewById(R.id.languagesTextView);
        languagesTextView.setText(Html.fromHtml(steamGame.getLanguages(), Html.FROM_HTML_MODE_LEGACY));

        TextView minimumTextView = view.findViewById(R.id.minimumTextView);
        minimumTextView.setText(Html.fromHtml(steamGame.getMinimumRequirement(), Html.FROM_HTML_MODE_LEGACY));
        TextView reccommendedTextView = view.findViewById(R.id.reccommendetTextView);
        TextView scrittaRaccomandatiTextView = view.findViewById(R.id.scrittaRaccomandatiTextView);
        if (steamGame.getRecommendedRequirement().isEmpty()) {
            scrittaRaccomandatiTextView.setVisibility(View.GONE);
            reccommendedTextView.setVisibility(View.GONE);
        }
        reccommendedTextView.setText(Html.fromHtml(steamGame.getRecommendedRequirement(), Html.FROM_HTML_MODE_LEGACY));

    }

    private void eShopGame(View view) {
        NintendoGame nintendoGame = (NintendoGame) game;

        LinearLayout linearLayout = view.findViewById(R.id.eshopSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        TextView caratteristiche = view.findViewById(R.id.caratteristicheTextView);
        caratteristiche.setText(Html.fromHtml(nintendoGame.getSystemInfo(), Html.FROM_HTML_MODE_LEGACY));

        TextView textView = view.findViewById(R.id.serieDiConsole);
        textView.setText(Html.fromHtml(fromListToHTML(nintendoGame.getFeatureSheets()), Html.FROM_HTML_MODE_LEGACY));
    }

    private void psnGame(View view) {
        PlayStationGame playStationGame = (PlayStationGame) game;
        LinearLayout linearLayout = view.findViewById(R.id.psnSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        TextView rating = view.findViewById(R.id.ratingTextView);
        rating.setText(playStationGame.getRating());
        TextView categorie = view.findViewById(R.id.categoryPSNTextView);
        String s = fromListToHTML(playStationGame.getCategories());
        categorie.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY));
        TextView generi = view.findViewById(R.id.generiPSNTextView);
        s = fromListToHTML(playStationGame.getGenres());
        generi.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY));
        TextView lingue = view.findViewById(R.id.languagePSNTextView);
        lingue.setText(Html.fromHtml(fromListToHTML(playStationGame.getVoiceLaunguage()), Html.FROM_HTML_MODE_LEGACY));
        TextView console = view.findViewById(R.id.consolePSNTextView);
        console.setText(fromListToHTML(playStationGame.getPlatforms()));

        ImageView imageView = view.findViewById(R.id.ratingImageView);
        imageView.setImageDrawable(getRatingImage(playStationGame.getRating()));
    }

    private Drawable getRatingImage(String rating) {
        Drawable d;
        String name = "pegi3.png";
        rating = rating.replaceAll("[^0-9]", "");

        int number = Integer.parseInt(rating);
        if (number >= 3 && number < 7) {
            name = "pegi3.png";
        } else if (number >= 7 && number < 12) {
            name = "pegi7.png";
        } else if (number >= 12 && number < 16) {
            name = "pegi12.png";
        } else if (number >= 16 && number < 18) {
            name = "pegi16.png";
        } else if (number >= 18) {
            name = "pegi18.png";
        }

        try {
            d = Drawable.createFromStream(requireContext().getAssets().open(name), null);
        } catch (IOException exception) {
            d = new ColorDrawable(Color.CYAN);
        }
        return d;
    }

    private String fromListToHTML(List<String> list) {
        String string = "<ul>";
        for (int i = 0; i < list.size(); i++)
            string += "<li>" + list.get(i) + "</li>";
        string += "</ul>";
        return string;
    }
}