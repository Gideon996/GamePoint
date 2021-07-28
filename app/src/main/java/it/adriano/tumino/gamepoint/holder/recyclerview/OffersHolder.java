package it.adriano.tumino.gamepoint.holder.recyclerview;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.BR;
import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.BasicGameInformation;
import it.adriano.tumino.gamepoint.databinding.OffersLayoutBinding;

public class OffersHolder extends RecyclerView.ViewHolder {
    public OffersLayoutBinding binding;

    public OffersHolder(OffersLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.game, obj);
        binding.executePendingBindings();
        binding.offerOriginalPrice.setPaintFlags(binding.offerOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.offerGameLayout.setOnClickListener(v -> {
            BasicGameInformation basicGameInformation = (BasicGameInformation) obj;
            Bundle bundle = new Bundle();
            bundle.putString("title", basicGameInformation.getTitle());
            bundle.putParcelable("game", basicGameInformation);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.navigate_to_favorite, bundle);
        });
    }
}
