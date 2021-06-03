package it.adriano.tumino.gamepoint.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameSearchResult;
import it.adriano.tumino.gamepoint.holder.RicercaHolder;

public class RicercaGiochiAdapter extends RecyclerView.Adapter<RicercaHolder> {
    private ArrayList<GameSearchResult> list;

    public RicercaGiochiAdapter(ArrayList<GameSearchResult> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public RicercaHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ultime_ricerche_layout, parent, false);
        return new RicercaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RicercaHolder holder, int position) {
        if (!list.get(position).getImageURL().isEmpty()) {
            Picasso.get().load(list.get(position).getImageURL()).resize(1500, 1500).onlyScaleDown().into(holder.getImageView());
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(Color.BLACK);
            holder.getImageView().setImageDrawable(gd);
        }
        holder.getTitolo().setText(list.get(position).getTitle());
        holder.getAltro().setText(list.get(position).getAppID());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
