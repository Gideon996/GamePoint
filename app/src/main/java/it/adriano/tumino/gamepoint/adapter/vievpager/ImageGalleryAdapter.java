package it.adriano.tumino.gamepoint.adapter.vievpager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.ScreenshotLayoutBinding;
import it.adriano.tumino.gamepoint.holder.viewpager.ImageGalleryHolder;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryHolder> {
    private final List<String> list;

    public ImageGalleryAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ImageGalleryHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ScreenshotLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.screenshot_layout, parent, false);
        return new ImageGalleryHolder(binding);
    }

    @Override
    public void onBindViewHolder(ImageGalleryHolder holder, int position) {
        String url = list.get(position);
        holder.bind(url);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
