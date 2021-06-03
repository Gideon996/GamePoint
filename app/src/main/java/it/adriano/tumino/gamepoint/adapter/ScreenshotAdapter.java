package it.adriano.tumino.gamepoint.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameShow;
import it.adriano.tumino.gamepoint.holder.ScreenshotHolder;


public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotHolder> {
    private ArrayList<String> screenShot;

    public ScreenshotAdapter(GameShow gameShow) {
        this.screenShot = gameShow.getScreenshots();
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
        position = position % screenShot.size();
        Picasso.get()
                .load(screenShot.get(position))
                .resize(1255, 705)
                .onlyScaleDown()
                .into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
