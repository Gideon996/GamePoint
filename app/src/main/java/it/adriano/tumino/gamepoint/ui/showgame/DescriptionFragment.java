package it.adriano.tumino.gamepoint.ui.showgame;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.data.storegame.NintendoStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.PlayStationStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.SteamStoreGame;
import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.databinding.FragmentDescriptionBinding;

public class DescriptionFragment extends Fragment {
    private static final String TAG = "DescriptionFragment";

    private FragmentDescriptionBinding binding;
    private LinearLayout linearLayout;

    private StoreGame storeGame;

    public DescriptionFragment() {

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDescriptionBinding.inflate(inflater, container, false);
        linearLayout = binding.descriptionLayout;
        showGameDescription();
        switch (storeGame.getStore()) {
            case "STEAM":
                SteamStoreGame steamStoreGame = (SteamStoreGame) storeGame;
                if (!steamStoreGame.getVideoUrl().contains("youtube.com")) {
                    binding.trailerLayout.setVisibility(View.VISIBLE);
                }
                break;
            case "PSN":
                PlayStationStoreGame playStationStoreGame = (PlayStationStoreGame) storeGame;
                if (!playStationStoreGame.getVideoUrl().contains("youtube.com")) {
                    binding.trailerLayout.setVisibility(View.VISIBLE);
                }
                break;
            case "ESHOP":
                binding.trailerYoutubeLayout.setVisibility(View.VISIBLE);
                break;
        }
        return binding.getRoot();
    }



    private void showGameDescription() {
        TextView descriptionTextView = binding.descriptionTextView;
        String body = storeGame.getDescription();
        PicassoImageGetter picassoImageGetter = new PicassoImageGetter(descriptionTextView);
        descriptionTextView.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, picassoImageGetter, null));
        descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        descriptionTextView.setLinksClickable(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (storeGame.getStore()) {
            case "STEAM":
                SteamStoreGame steamStoreGame = (SteamStoreGame) storeGame;
                if (!steamStoreGame.getVideoUrl().contains("youtube.com")) {
                    setVideoTrailer(steamStoreGame.getVideoUrl(), steamStoreGame.getThumbnail());
                }
                break;
            case "PSN":
                PlayStationStoreGame playStationStoreGame = (PlayStationStoreGame) storeGame;
                if (!playStationStoreGame.getVideoUrl().contains("youtube.com")) {
                    setVideoTrailer(playStationStoreGame.getVideoUrl(), playStationStoreGame.getShots());
                }
                break;
            case "ESHOP":
                NintendoStoreGame nintendoStoreGame = (NintendoStoreGame) storeGame;
                setYoutubeVideoTrailer(nintendoStoreGame.getVideoTrailerUrl());
                break;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setYoutubeVideoTrailer(String videoTrailer) {
        //binding.trailerYoutubeLayout.setVisibility(View.VISIBLE);
        //Esempio url: https://www.youtube.com/watch?v=DKBK4OnvjX0
        //Originale: https://www.youtube.com/embed/SebD2MQVT1c?rel=0&theme=light&modestbranding=1&showinfo=0&autohide=1&autoplay=2&cc_load_policy=0&cc_lang_pref=it&enablejsapi=1
        WebView webView = binding.webView;

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = webView.getWidth() / density;
        float dpHeight = webView.getHeight() / density;
        Log.e("TEST", "" + dpWidth + " " + dpHeight);

        String frameVideo = "<html>" +
                                "<body style='margin:0;padding:0;'>" +
                                    "<iframe " +
                                        "width=\"" + dpWidth + "\"" +
                                        " height=\"" + dpHeight + "\"" +
                                        " src=\"" + videoTrailer + "\"" +
                "                        frameborder=\"0\" allowfullscreen>" +
                                    "</iframe>" +
                                "</body>" +
                            "</html>";
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(frameVideo, "text/html", "utf-8");
    }

    private void setVideoTrailer(String videoUrl, String thumbnail) {
        binding.trailerLayout.setVisibility(View.VISIBLE);
        Picasso.get().load(thumbnail).fit().into(binding.thumbnailVideoTrailer);

        VideoView videoView = binding.trailerVideoView;
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
        videoView.requestFocus();

        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        binding.playVideoTrailer.setOnClickListener(v -> {
            binding.playVideoTrailer.setVisibility(View.GONE);
            binding.thumbnailVideoTrailer.setVisibility(View.GONE);
            videoView.start();
        });

        linearLayout.getViewTreeObserver().addOnScrollChangedListener(mediaController::hide);
    }

    public class PicassoImageGetter implements Html.ImageGetter {

        private final TextView textView;

        public PicassoImageGetter(TextView target) {
            textView = target;
        }

        @Override
        public Drawable getDrawable(String source) {
            Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder(getResources(), bitmap);
            Picasso.get().load(source).resize(textView.getWidth(), 500).centerInside().into(drawable);
            return drawable;
        }

        private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

            protected Drawable drawable;

            public BitmapDrawablePlaceHolder(Resources resources, Bitmap bitmap) {
                super(resources, bitmap);
            }

            @Override
            public void draw(final Canvas canvas) {
                if (drawable != null) {
                    drawable.draw(canvas);
                }
            }

            public void setDrawable(Drawable drawable) {
                this.drawable = drawable;
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                drawable.setBounds(0, 0, width, height);
                setBounds(0, 0, width, height);
                if (textView != null) {
                    textView.setText(textView.getText());
                }
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                setDrawable(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, e.getMessage());
                setDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        }
    }
}