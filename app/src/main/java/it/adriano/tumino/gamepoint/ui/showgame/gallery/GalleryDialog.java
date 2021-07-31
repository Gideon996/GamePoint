package it.adriano.tumino.gamepoint.ui.showgame.gallery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;
import java.util.Objects;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.vievpager.ImageGalleryAdapter;

public class GalleryDialog extends DialogFragment {
    private final static String TAG = "GalleryDialog";

    private final int initialPosition;
    private final List<String> screenshots;
    private final ImageView[] dots;

    private Drawable away;
    private Drawable medium;

    public GalleryDialog(int initialPosition, List<String> screenshots) {
        this.initialPosition = initialPosition;
        this.screenshots = screenshots;
        dots = new ImageView[screenshots.size()];
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Log.i(TAG, "Initialization Gallery Dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.screenshot_show_layout, null);
        builder.setView(view);
        builder.setCancelable(true);

        ViewPager2 viewPager2 = view.findViewById(R.id.imageShowPager);
        viewPager2.setAdapter(new ImageGalleryAdapter(screenshots));
        viewPager2.setCurrentItem(initialPosition);

        away = getResources().getDrawable(R.drawable.icon_small_circle, requireContext().getTheme());
        medium = getResources().getDrawable(R.drawable.icon_medium_circle, requireContext().getTheme());

        setIndicators(view, initialPosition);

        viewPager2.registerOnPageChangeCallback(pageSelector);

        ImageButton closeButton = view.findViewById(R.id.closeGalleryDialog);
        closeButton.setOnClickListener(k -> Objects.requireNonNull(getDialog()).dismiss());

        return builder.create();
    }

    private void selectedDots(int position) {
        dots[position].setImageDrawable(medium);
        if (position - 1 >= 0) dots[position - 1].setImageDrawable(away);
        if (position + 1 != screenshots.size()) dots[position + 1].setImageDrawable(away);
    }

    private void setIndicators(View view, int initialPosition) {
        Log.i(TAG, "Creation image indicators");
        LinearLayout linearLayout = view.findViewById(R.id.dots_container);

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(away);
            if (i == initialPosition) dots[i].setImageDrawable(medium);
            linearLayout.addView(dots[i]);
        }
    }

    final ViewPager2.OnPageChangeCallback pageSelector = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            selectedDots(position);
            super.onPageSelected(position);
        }
    };
}
