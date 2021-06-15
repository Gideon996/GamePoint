package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    private List<Item> list = new ArrayList<>();
    private Context context;

    public GalleryAdapter(Context context) {
        this.context = context;
        list.add(new Item("image 1", "https://i.pinimg.com/originals/20/79/03/2079033abc8314be554f9d24f562a199.jpg"));
        list.add(new Item("image 2", "https://i.redd.it/lfov4zuifn551.jpg"));
        list.add(new Item("image 3", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLAgy1lM_mcUwf8EnfpHYx3boWGV96GIR_HiC9mfXQpjOrRO9vlr8UXzlq2b9CXxQDFnA&usqp=CAU"));
        list.add(new Item("image 4", "https://fsb.zobj.net/crop.php?r=xw_IHFA1nB677jxVk44bCjvI-bJd3S6fOC6XCK2vGwfOBxRRVqRyhr7Kkrc5fAPYQ4DcNnEDqwT0r6l1QMhy3tv5feK5mrZgsFPPCDReynIDANAMj7mQPKhFG-8OsPC_uu0s-FJAKAfda6eP"));
        list.add(new Item("image 5", "https://images-eu.ssl-images-amazon.com/images/I/81YDuTWSHyL.png"));
        list.add(new Item("image 6", "https://cdn.wallpapersafari.com/48/16/xiG3yg.jpg"));
        list.add(new Item("image 7", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRh5IcwScWD40XsYHPp-Z44XxQb8Puvxcz9ow&usqp=CAU"));
        list.add(new Item("image 8", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1jNZ36wIfpqnekgemBEbJNNExYBfh2bwdLg&usqp=CAU"));
        list.add(new Item("image 9", "https://c4.wallpaperflare.com/wallpaper/764/505/66/baby-groot-4k-hd-superheroes-wallpaper-preview.jpg"));
        list.add(new Item("image 10", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAfpR8N1ApbQ_-42-qNylAFLIlP_esOM24eOjXnXz4omn5NwCvkn64UgiWx7ynWAlpHn4&usqp=CAU"));
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) { //imposto la vista e setto il click con il dialogo
        Item item = list.get(position);
        holder.getTextView().setText(item.text);
        Glide.with(context).load(item.url).apply(new RequestOptions().override(300, 300)).centerCrop().into(holder.getImageView());
        holder.getLayout().setOnClickListener(v -> {
            //ViewPager2
            openImageDialog(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Item {
        public final String text;
        public final String url;

        public Item(String text, String url) {
            this.text = text;
            this.url = url;
        }
    }

    private void openImageDialog(int position){
        final Dialog nagDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar); //Creo la finestra dove inserire il tutto
        //nagDialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS); //Dico che deve essere full screen
        nagDialog.setCancelable(true); //torno indietro e chiudo
        nagDialog.setCanceledOnTouchOutside(true); //Chiude quando tocco fuori (inutile meglio il bottone)
        nagDialog.setContentView(R.layout.screenshot_show_layout); //imposto il layout
        ViewPager2 viewPager2 = nagDialog.findViewById(R.id.imageShowViewPager2); //prelevo il viewpager2 dal layout
        viewPager2.setAdapter(new ImageGalleryAdapter(list, context)); //setto il nuovo adapter
        viewPager2.setCurrentItem(position);
        //ImageButton close = nagDialog.findViewById(R.id.closeGalleryButton);
        Button chiudi = nagDialog.findViewById(R.id.chiudiBottone);
        chiudi.setOnClickListener(k -> nagDialog.dismiss());
        //close.setOnClickListener(k -> nagDialog.dismiss());
        nagDialog.show();
    }
}

