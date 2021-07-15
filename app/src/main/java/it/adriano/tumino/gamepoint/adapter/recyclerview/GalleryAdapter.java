package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.databinding.GalleryItemBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.GalleryHolder;
import it.adriano.tumino.gamepoint.ui.showgame.gallery.GalleryDialog;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryHolder> {

    private final List<String> list;
    private final Context context;

    public GalleryAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GalleryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.gallery_item, parent, false);
        return new GalleryHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {
        String url = list.get(position);
        holder.bind(url);
        holder.binding.galleryItemLayout.setOnClickListener(v -> openDialog(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void openDialog(int position){
        GalleryDialog galleryDialog = new GalleryDialog(position, list);
        galleryDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "Gallery Dialog");
    }

}

