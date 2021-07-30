package it.adriano.tumino.gamepoint.ui.showgame;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.databinding.FragmentDescriptionBinding;

public class DescriptionFragment extends Fragment {
    private static final String TAG = "DescriptionFragment";
    private final static String embedURL = "https://www.youtube.com/embed/";

    private FragmentDescriptionBinding binding;

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
        showGameDescription();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!storeGame.getVideoUrl().isEmpty()) {
            if (storeGame.getVideoUrl().contains("youtube.com")) {
                setYoutubeVideoTrailer(storeGame.getVideoUrl());
                binding.trailerMP4Layout.setVisibility(View.GONE);
            } else {
                setVideoTrailer(storeGame.getVideoUrl(), storeGame.getThumbnail());
                binding.trailerYoutubeLayout.setVisibility(View.GONE);
            }
        } else {
            binding.trailerYoutubeLayout.setVisibility(View.GONE);
            binding.trailerMP4Layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    private static String createYoutubeFrame(@NonNull String videoUrl, float width, float height) {
        return "<html>" +
                "<body style='margin:0;padding:0;'>" +
                "<iframe " +
                "width=\"" + width + "\"" +
                "height=\"" + height + "\"" +
                "src=\"" + videoUrl + "\"" +
                "frameborder=\"0\" allowfullscreen>" +
                "</iframe>" +
                "</body>" +
                "</html>";
    }

    private void showGameDescription() {
        TextView descriptionTextView = binding.descriptionText;
        String body = storeGame.getDescription();
        PicassoImageGetter picassoImageGetter = new PicassoImageGetter(descriptionTextView);
        descriptionTextView.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, picassoImageGetter, null));
        descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        descriptionTextView.setLinksClickable(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setYoutubeVideoTrailer(String videoTrailer) {
        if (!videoTrailer.contains("embed")) {
            String[] split = videoTrailer.split("=");
            if (split.length > 1) {
                String videoID = split[1];
                videoTrailer = embedURL + videoID;
            }
        }

        WebView webView = binding.youtubeWebView;

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = webView.getWidth() / density;
        float dpHeight = webView.getHeight() / density;

        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(createYoutubeFrame(videoTrailer, dpWidth, dpHeight), "text/html", "utf-8");
    }

    private void setVideoTrailer(String videoUrl, String thumbnail) {
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

        binding.descriptionLayout.getViewTreeObserver().addOnScrollChangedListener(mediaController::hide);
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