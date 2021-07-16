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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.storegame.MicrosoftStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.NintendoStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.PlayStationStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.SteamStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;


public class GameSpecificationsFragment extends Fragment {

    private StoreGame storeGame;

    public GameSpecificationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle information = getArguments();
            storeGame = information.getParcelable("game");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_specifications, container, false);
        switch (storeGame.getStore()) {
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

    private void steamGame(View view) {
        SteamStoreGame steamGame = (SteamStoreGame) storeGame;

        LinearLayout linearLayout = view.findViewById(R.id.steamSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        TextView developersTextView = view.findViewById(R.id.developerTextView);
        String string = String.join(", ", steamGame.getDevelopers());
        developersTextView.setText(string);

        TextView publishersTextView = view.findViewById(R.id.publisherTextView);
        string = String.join(", ", steamGame.getPublishers());
        publishersTextView.setText(string);

        TextView webSiteTextView = view.findViewById(R.id.webSiteTextView);
        webSiteTextView.setText(steamGame.getWebsite());

        TextView metacriticScoreTextView = view.findViewById(R.id.metacriticScoreTextView);
        metacriticScoreTextView.setText(steamGame.getScoreMetacritic());

        TextView categoriesTextView = view.findViewById(R.id.categoryPSNTextView);
        string = fromListToHTML(steamGame.getCategories());
        categoriesTextView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY));

        TextView genresTextView = view.findViewById(R.id.generiTextView);
        string = fromListToHTML(steamGame.getGenres());
        genresTextView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY));

        TextView languagesTextView = view.findViewById(R.id.languagesTextView);
        List<String> languagesList = Arrays.asList(steamGame.getLanguages().split(",").clone());
        languagesTextView.setText(Html.fromHtml(fromListToHTML(languagesList), Html.FROM_HTML_MODE_LEGACY));

        TextView minimumTextView = view.findViewById(R.id.minimumTextView);
        minimumTextView.setText(Html.fromHtml(steamGame.getMinimumRequirement(), Html.FROM_HTML_MODE_LEGACY));
        TextView reccommendedTextView = view.findViewById(R.id.reccommendetTextView);
        if (steamGame.getRecommendedRequirement().isEmpty()) {
            reccommendedTextView.setVisibility(View.GONE);
        }
        reccommendedTextView.setText(Html.fromHtml(steamGame.getRecommendedRequirement(), Html.FROM_HTML_MODE_LEGACY));

    }

    private void eShopGame(View view) {
        NintendoStoreGame nintendoGame = (NintendoStoreGame) storeGame;

        LinearLayout linearLayout = view.findViewById(R.id.eshopSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        Drawable pegi = getRatingImage(nintendoGame.getPegi());
        ImageView imageView = view.findViewById(R.id.pegiNintendo);
        imageView.setImageDrawable(pegi);

        TextView caratteristiche = view.findViewById(R.id.caratteristicheTextView);
        caratteristiche.setText(Html.fromHtml(nintendoGame.getSystemInfo(), Html.FROM_HTML_MODE_LEGACY));

        LinearLayout caratteristichePerConsole = view.findViewById(R.id.caratterostocheConsoleLayout);
        for (int i = 0; i < nintendoGame.getFeatureSheets().size(); i++) {
            String console = nintendoGame.getFeatureSheets().get(i);
            TextView textView = new TextView(getContext());
            textView.setText(Html.fromHtml(console, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
            caratteristichePerConsole.addView(textView);
        }
    }

    private void psnGame(View view) {
        PlayStationStoreGame playStationGame = (PlayStationStoreGame) storeGame;

        LinearLayout linearLayout = view.findViewById(R.id.psnSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        ImageView imageView = view.findViewById(R.id.ratingImageView);
        imageView.setImageDrawable(getRatingImage(playStationGame.getRating()));

        TextView categoriesTextView = view.findViewById(R.id.categoryPSNTextView);
        categoriesTextView.setText(Html.fromHtml(fromListToHTML(playStationGame.getCategories()), Html.FROM_HTML_MODE_LEGACY));

        TextView genresTextView = view.findViewById(R.id.generiPSNTextView);
        genresTextView.setText(Html.fromHtml(fromListToHTML(playStationGame.getGenres()), Html.FROM_HTML_MODE_LEGACY));

        TextView subGenresTextView = view.findViewById(R.id.subGenresPSNTextView);
        subGenresTextView.setText(Html.fromHtml(fromListToHTML(playStationGame.getSubGenreList()), Html.FROM_HTML_MODE_LEGACY));

        TextView voiceLanguagesTextView = view.findViewById(R.id.voiceLanguagesTextView);
        TextView subTitleTextView = view.findViewById(R.id.subTitleTextView);
        List<String> displayVoice = fromAcronimoToEsteso(playStationGame.getVoiceLanguages());
        voiceLanguagesTextView.setText(Html.fromHtml(fromListToHTML(displayVoice), Html.FROM_HTML_MODE_COMPACT));
        List<String> subTitle = fromAcronimoToEsteso(playStationGame.getSubtitleLanguages());
        subTitleTextView.setText(Html.fromHtml(fromListToHTML(subTitle), Html.FROM_HTML_MODE_COMPACT));

        TextView console = view.findViewById(R.id.consolePSNTextView);
        console.setText(Html.fromHtml(fromListToHTML(playStationGame.getPlatforms()), Html.FROM_HTML_MODE_LEGACY));


    }

    private void mcsGame(View view) {
        MicrosoftStoreGame microsoftGame = (MicrosoftStoreGame) storeGame;
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
        StringBuilder string = new StringBuilder("<ul>");
        for (int i = 0; i < list.size(); i++)
            string.append("<li>").append(list.get(i)).append("</li>");
        string.append("</ul>");
        return string.toString();
    }

    private List<String> fromAcronimoToEsteso(List<String> list) {
        List<String> estesi = new ArrayList<>();
        if (list.size() == 1 && list.get(0).equals("N.A.")) {
            estesi.addAll(list);
        } else {
            for (String languageCode : list) {
                Locale locale = new Locale(languageCode);
                estesi.add(locale.getDisplayLanguage());
            }
        }

        return estesi;
    }
}