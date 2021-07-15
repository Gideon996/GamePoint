package it.adriano.tumino.gamepoint.ui.showgame.gallery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private final int initialPosition;
    private final List<String> screenshots;
    private final ImageView[] dots;

    private Drawable dot;
    private Drawable sferaDelDrago;

    public GalleryDialog(int initialPosition, List<String> screenshots) {
        this.initialPosition = initialPosition;
        this.screenshots = screenshots;
        dots = new ImageView[screenshots.size()];
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.screenshot_show_layout, null);
        builder.setView(view);
        builder.setCancelable(true);

        ViewPager2 viewPager2 = view.findViewById(R.id.imageShowViewPager2);
        viewPager2.setAdapter(new ImageGalleryAdapter(screenshots));
        viewPager2.setCurrentItem(initialPosition);

        dot = getResources().getDrawable(R.drawable.ic_pallina, requireContext().getTheme());
        sferaDelDrago = getResources().getDrawable(R.drawable.sfera_del_drago, requireContext().getTheme());

        setIndicators(view);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                selectedDots(position);
                super.onPageSelected(position);
            }
        });


        Button chiudi = view.findViewById(R.id.chiudiBottone);
        chiudi.setOnClickListener(k -> Objects.requireNonNull(getDialog()).dismiss());

        return builder.create();
    }

    private void selectedDots(int position) {
        dots[position].setImageDrawable(sferaDelDrago);
        if (position - 1 >= 0) dots[position - 1].setImageDrawable(dot);
        if (position + 1 != screenshots.size()) dots[position + 1].setImageDrawable(dot);
    }

    private void setIndicators(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.dots_container);

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(dot);
            if (i == 0) dots[i].setImageDrawable(sferaDelDrago);
            linearLayout.addView(dots[i]);
        }

    }


}
