package it.adriano.tumino.gamepoint.ui.showgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.data.storegame.StoreGame;
import it.adriano.tumino.gamepoint.databinding.FragmentDescriptionBinding;

public class DescriptionFragment extends Fragment {
    private static final String TAG = "DescriptionFragment";

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

    private void showGameDescription() {
        TextView descriptionTextView = binding.descriptionTextView;
        String body = storeGame.getDescription();
        PicassoImageGetter picassoImageGetter = new PicassoImageGetter(descriptionTextView);
        descriptionTextView.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, picassoImageGetter, null));
        descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        descriptionTextView.setLinksClickable(true);
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