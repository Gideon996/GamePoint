package it.adriano.tumino.gamepoint.holder;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import it.adriano.tumino.gamepoint.R;

public class FooterNewsHolder extends RecyclerView.ViewHolder {
    private final Button button;

    public FooterNewsHolder(View view) {
        super(view);
        button = view.findViewById(R.id.viewMoreNewsButton);
    }

    public Button getButton() {
        return button;
    }
}
