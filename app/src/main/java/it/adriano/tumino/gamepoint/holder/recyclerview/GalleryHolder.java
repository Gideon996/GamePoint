package it.adriano.tumino.gamepoint.holder.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.databinding.GalleryItemBinding;

public class GalleryHolder extends RecyclerView.ViewHolder {
    public final GalleryItemBinding binding;

    public GalleryHolder(GalleryItemBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj){
        binding.setVariable(BR.screenshot, obj);
        binding.executePendingBindings();
    }
}
