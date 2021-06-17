package it.adriano.tumino.gamepoint.ui.showgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.Game;

public class DescriptionFragment extends Fragment {

    private Bundle information;
    private String store;
    private Game game;

    public DescriptionFragment() {

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
        View view = inflater.inflate(R.layout.fragment_description, container, false);
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

    private void steamGame(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.descriptionSteam);
        linearLayout.setVisibility(View.VISIBLE);
        TextView textView = view.findViewById(R.id.descriptionTextView);
        String body = game.getDescription();
        PicassoImageGetter picassoImageGetter = new PicassoImageGetter(textView);
        textView.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, picassoImageGetter, null));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setLinksClickable(true);
    }

    private void eShopGame(View view){
        LinearLayout linearLayout = view.findViewById(R.id.descriptionEshop);
        linearLayout.setVisibility(View.VISIBLE);
        TextView textView = view.findViewById(R.id.descriptionEshopTextView);
        String body = game.getDescription();
        PicassoImageGetter picassoImageGetter = new PicassoImageGetter(textView);
        textView.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, picassoImageGetter, null));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setLinksClickable(true);
    }

    public class PicassoImageGetter implements Html.ImageGetter {

        private final TextView textView;

        public PicassoImageGetter(TextView target) {
            textView = target;
        }

        @Override
        public Drawable getDrawable(String source) {
            BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
            Picasso.get().load(source).resize(textView.getWidth(), 500).centerInside().into(drawable);
            return drawable;
        }

        private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target { //non è deprecato

            protected Drawable drawable;

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

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        }
    }
}