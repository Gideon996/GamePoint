package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.adapter.vievpager.ImageGalleryAdapter;
import it.adriano.tumino.gamepoint.holder.recyclerview.GalleryHolder;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) { //imposto la vista e setto il click con il dialogo
        String url = list.get(position);
        Glide.with(context).load(url).apply(new RequestOptions().override(300, 300)).centerCrop().into(holder.getImageView());
        holder.getLayout().setOnClickListener(v -> openImageDialog(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void openImageDialog(int position) {
        final Dialog nagDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar); //Creo la finestra dove inserire il tutto
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Dico che deve essere full screen
        nagDialog.setCancelable(true); //torno indietro e chiudo
        nagDialog.setCanceledOnTouchOutside(true); //Chiude quando tocco fuori (inutile meglio il bottone)
        nagDialog.setContentView(R.layout.screenshot_show_layout); //imposto il layout
        ViewPager2 viewPager2 = nagDialog.findViewById(R.id.imageShowViewPager2); //prelevo il viewpager2 dal layout
        viewPager2.setAdapter(new ImageGalleryAdapter(list, context)); //setto il nuovo adapter
        viewPager2.setCurrentItem(position);

        Button chiudi = nagDialog.findViewById(R.id.chiudiBottone);
        chiudi.setOnClickListener(k -> nagDialog.dismiss());

        nagDialog.show();
    }
}

