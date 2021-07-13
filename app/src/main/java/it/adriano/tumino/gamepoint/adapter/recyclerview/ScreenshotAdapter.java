package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.holder.recyclerview.ScreenshotHolder;


public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotHolder> {
    public static final String TAG = "ScreenshotAdapter";
    private ArrayList<String> screenshotsList;

    public ScreenshotAdapter(ArrayList<String> screenshotsList) {
        this.screenshotsList = screenshotsList;
    }

    @NonNull
    @NotNull
    @Override
    public ScreenshotHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.screenshot_layout, parent, false);
        return new ScreenshotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ScreenshotHolder holder, int position) {
        position = position % screenshotsList.size();
        if (!screenshotsList.get(position).isEmpty()) {
            Picasso.get()
                    .load(screenshotsList.get(position))
                    .resize(1255, 705)
                    .onlyScaleDown()
                    .into(holder.getImageView());
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(Color.BLACK);
            holder.getImageView().setImageDrawable(gd);
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
