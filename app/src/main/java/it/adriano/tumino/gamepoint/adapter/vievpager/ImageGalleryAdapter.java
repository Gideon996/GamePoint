package it.adriano.tumino.gamepoint.adapter.vievpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.holder.viewpager.ImageGalleryHolder;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryHolder> {

    private final List<String> list;
    private final Context context;

    public ImageGalleryAdapter(List<String> list, Context context) { //Costruttore per la viewPager2
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageGalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) { //Creo l'adapter per il layout corretto
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screenshot_layout, parent, false);
        return new ImageGalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageGalleryHolder holder, int position) { //Lunghezza infinita e mostro l'img
        String image = list.get(position);
        Glide.with(context).load(image).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
