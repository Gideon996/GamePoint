package it.adriano.tumino.gamepoint.ui.showgame;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
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
    private final static String TAG = "GameSpecificationsFragment";

    private StoreGame storeGame;
    private TextView textView;

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
        Log.i(TAG, "Check store to show");
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
        Log.i(TAG, "Fill object for Steam Store");
        SteamStoreGame steamGame = (SteamStoreGame) storeGame;

        LinearLayout linearLayout = view.findViewById(R.id.steamSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        textView = view.findViewById(R.id.steamDeveloper);
        textView.setText(String.join(", ", steamGame.getDevelopers()));

        textView = view.findViewById(R.id.steamPublisher);
        textView.setText(String.join(", ", steamGame.getPublishers()));

        textView = view.findViewById(R.id.steamWebUrl);
        textView.setText(steamGame.getWebsite());

        textView = view.findViewById(R.id.steamMetacriticScore);
        textView.setText(steamGame.getScoreMetacritic());

        textView = view.findViewById(R.id.playstationCategories);
        textView.setText(Html.fromHtml(fromListToHTML(steamGame.getCategories()), Html.FROM_HTML_MODE_LEGACY));

        textView = view.findViewById(R.id.steamGenres);
        textView.setText(Html.fromHtml(fromListToHTML(steamGame.getGenres()), Html.FROM_HTML_MODE_LEGACY));

        textView = view.findViewById(R.id.steamLanguages);
        textView.setText(Html.fromHtml(fromListToHTML(Arrays.asList(steamGame.getLanguages().split(",").clone())), Html.FROM_HTML_MODE_LEGACY));

        textView = view.findViewById(R.id.steamMinimum);
        textView.setText(Html.fromHtml(steamGame.getMinimumRequirement(), Html.FROM_HTML_MODE_LEGACY));
        textView = view.findViewById(R.id.steamRecommended);
        if (steamGame.getRecommendedRequirement().isEmpty()) {
            textView.setVisibility(View.GONE);
        }
        textView.setText(Html.fromHtml(steamGame.getRecommendedRequirement(), Html.FROM_HTML_MODE_LEGACY));

    }

    private void eShopGame(View view) {
        Log.i(TAG, "Fill object for Nintendo Store");
        NintendoStoreGame nintendoGame = (NintendoStoreGame) storeGame;

        LinearLayout linearLayout = view.findViewById(R.id.nintendoSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        Drawable pegi = getRatingImage(nintendoGame.getPegi());
        ImageView imageView = view.findViewById(R.id.nintendoPegiImage);
        imageView.setImageDrawable(pegi);

        textView = view.findViewById(R.id.nintendoCharacteristics);
        textView.setText(Html.fromHtml(nintendoGame.getSystemInfo(), Html.FROM_HTML_MODE_LEGACY));

        LinearLayout characteristicsForConsole = view.findViewById(R.id.nintendoCharacteristicsForConsole);
        for (int i = 0; i < nintendoGame.getFeatureSheets().size(); i++) {
            String console = nintendoGame.getFeatureSheets().get(i);
            TextView textView = new TextView(getContext());
            textView.setText(Html.fromHtml(console, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
            characteristicsForConsole.addView(textView);
        }
    }

    private void psnGame(View view) {
        Log.i(TAG, "Fill object for PlayStation Store");
        PlayStationStoreGame playStationGame = (PlayStationStoreGame) storeGame;

        LinearLayout linearLayout = view.findViewById(R.id.playstationSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        ImageView imageView = view.findViewById(R.id.playstationPegiImage);
        imageView.setImageDrawable(getRatingImage(playStationGame.getRating()));

        textView = view.findViewById(R.id.playstationCategories);
        textView.setText(Html.fromHtml(fromListToHTML(playStationGame.getCategories()), Html.FROM_HTML_MODE_LEGACY));

        textView = view.findViewById(R.id.playstationGenres);
        textView.setText(Html.fromHtml(fromListToHTML(playStationGame.getGenres()), Html.FROM_HTML_MODE_LEGACY));

        textView = view.findViewById(R.id.playstationSubGenres);
        textView.setText(Html.fromHtml(fromListToHTML(playStationGame.getSubGenreList()), Html.FROM_HTML_MODE_LEGACY));

        textView = view.findViewById(R.id.playstationVoiceLanguages);
        textView.setText(Html.fromHtml(fromListToHTML(fromAcronymToFullName(playStationGame.getVoiceLanguages())), Html.FROM_HTML_MODE_COMPACT));
        textView = view.findViewById(R.id.playstationSubTitleLanguages);
        textView.setText(Html.fromHtml(fromListToHTML(fromAcronymToFullName(playStationGame.getSubtitleLanguages())), Html.FROM_HTML_MODE_COMPACT));

        textView = view.findViewById(R.id.playstationConsole);
        textView.setText(Html.fromHtml(fromListToHTML(playStationGame.getPlatforms()), Html.FROM_HTML_MODE_LEGACY));
    }

    private void mcsGame(View view) {
        Log.i(TAG, "Fill object for Microsoft Store");
        MicrosoftStoreGame microsoftGame = (MicrosoftStoreGame) storeGame;
        LinearLayout linearLayout = view.findViewById(R.id.microsoftSpecificationLayout);
        linearLayout.setVisibility(View.VISIBLE);

        ImageView pegi = view.findViewById(R.id.microsoftPegiImage);
        Drawable pegiImage = getRatingImage(microsoftGame.getPegi());
        pegi.setImageDrawable(pegiImage);

        textView = view.findViewById(R.id.microsoftCategories);
        textView.setText(Html.fromHtml(fromListToHTML(microsoftGame.getCategories()), Html.FROM_HTML_MODE_LEGACY));

        textView = view.findViewById(R.id.microsoftMetadata);
        textView.setText(Html.fromHtml(fromListToHTML(microsoftGame.getMetadata()), Html.FROM_HTML_MODE_LEGACY));

        textView = view.findViewById(R.id.microsoftRequirement);
        textView.setText(Html.fromHtml(microsoftGame.getSystemRequirement(), Html.FROM_HTML_MODE_LEGACY));
    }

    private Drawable getRatingImage(String rating) {
        Log.i(TAG, "Get Pegi drawable for the game");
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

    private static String fromListToHTML(List<String> list) {
        StringBuilder string = new StringBuilder("<ul>");
        for (int i = 0; i < list.size(); i++)
            string.append("<li>").append(list.get(i)).append("</li>");
        string.append("</ul>");
        return string.toString();
    }

    private static List<String> fromAcronymToFullName(List<String> list) {
        List<String> fullName = new ArrayList<>();
        if (list.size() == 1 && list.get(0).equals("N.A.")) {
            fullName.addAll(list);
        } else {
            for (String languageCode : list) {
                Locale locale = new Locale(languageCode);
                fullName.add(locale.getDisplayLanguage());
            }
        }

        return fullName;
    }
}