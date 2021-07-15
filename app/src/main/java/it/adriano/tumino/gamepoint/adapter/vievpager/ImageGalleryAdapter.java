package it.adriano.tumino.gamepoint.adapter.vievpager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.holder.viewpager.ImageGalleryHolder;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryHolder> {

    private final List<String> list;

    public ImageGalleryAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ImageGalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screenshot_layout, parent, false);
        return new ImageGalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageGalleryHolder holder, int position) {
        String image = list.get(position);
        Picasso.get().load(image).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
