package it.adriano.tumino.gamepoint.ui.showgame.gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.vievpager.ImageGalleryAdapter;

public class GalleryDialog extends DialogFragment {

    private final int initialPosition;
    private final List<String> screenshots;
    public GalleryDialog(int initialPosition, List<String> screenshots) {
        this.initialPosition = initialPosition;
        this.screenshots = screenshots;
    }

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

        Button chiudi = view.findViewById(R.id.chiudiBottone);
        chiudi.setOnClickListener(k -> getDialog().dismiss());

        return builder.create();
    }


}
