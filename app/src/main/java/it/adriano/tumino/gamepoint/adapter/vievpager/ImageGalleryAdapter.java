package it.adriano.tumino.gamepoint.adapter.vievpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.recyclerview.GalleryAdapter;
import it.adriano.tumino.gamepoint.holder.viewpager.ImageGalleryHolder;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryHolder> {

    private List<GalleryAdapter.Item> list;
    private Context context;

    public ImageGalleryAdapter(List<GalleryAdapter.Item> list, Context context) { //Costruttore per la viewPager2
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
        position = position % list.size();
        GalleryAdapter.Item item = list.get(position);
        Glide.with(context).load(item.url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

}
