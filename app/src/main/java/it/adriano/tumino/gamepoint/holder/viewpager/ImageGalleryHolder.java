package it.adriano.tumino.gamepoint.holder.viewpager;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.databinding.ScreenshotLayoutBinding;

public class ImageGalleryHolder extends RecyclerView.ViewHolder {

    public ScreenshotLayoutBinding binding;

    public ImageGalleryHolder(ScreenshotLayoutBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj){
        binding.setVariable(BR.screenshot, obj);
        binding.executePendingBindings();
    }
}
