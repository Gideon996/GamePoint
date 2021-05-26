package it.adriano.tumino.gamepoint.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.holder.FavoriteHolder;
import it.adriano.tumino.gamepoint.data.FavoriteGames;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteHolder> {
    public static final String TAG = "FavoriteAdapter";
    private FavoriteGames[] favoriteGames;

    public FavoriteAdapter() {
        Log.i(TAG, "Generazione Favorite Adapter");

        favoriteGames = new FavoriteGames[10];

        favoriteGames[0] = new FavoriteGames("Zelda", "https://cdn02.nintendo-europe.com/media/images/10_share_images/portals_3/SI_Hub_Zelda_Portal_image1600w.jpg", null, null);
        favoriteGames[1] = new FavoriteGames("Monster Hunter", "https://cdn-ext.fanatical.com/production/product/1280x720/7f6e4bbc-69a4-4910-a424-8b4f14100c12.jpeg", null, null);
        favoriteGames[2] = new FavoriteGames("League of Legends", "https://images.contentstack.io/v3/assets/blt731acb42bb3d1659/bltcfa4652c8d383f56/5e21837f63d1b6503160d39b/Home-page.jpg", null, null);
        favoriteGames[3] = new FavoriteGames("Valorant", "https://img.redbull.com/images/c_crop,w_1620,h_1080,x_144,y_0,f_auto,q_auto/c_scale,w_1500/redbullcom/2020/6/5/ctsejxmdtw9inp8zqqqd/valorant", null, null);
        favoriteGames[4] = new FavoriteGames("Hearthstone", "https://d2vkoy1na2a6o6.cloudfront.net/_next/static/images/default-4fff3c606c794dc03a915b9071f562d3.jpg", null, null);
        favoriteGames[5] = new FavoriteGames("Dragon Ball", "https://i2.wp.com/www.gamesplus.it/wp-content/uploads/2020/01/DRAGON-BALL-Z-KAKAROT-recensione-ps4-cover.jpg?fit=1280%2C720&ssl=1", null, null);
        favoriteGames[6] = new FavoriteGames("Naruto", "https://upload.wikimedia.org/wikipedia/it/3/3e/Naruto1.jpg", null, null);
        favoriteGames[7] = new FavoriteGames("Call Of Duty", "https://image.api.playstation.com/vulcan/img/rnd/202008/1900/lTSvbByTYMqy6R22teoybKCg.png", null, null);
        favoriteGames[8] = new FavoriteGames("My Hero Academia", "https://upload.wikimedia.org/wikipedia/it/1/1a/My_Hero_Academia_manga.jpg", null, null);
        favoriteGames[9] = new FavoriteGames("binding of isaac", "https://store-images.s-microsoft.com/image/apps.58856.69039762475408619.1f9f3938-799b-4ad8-a607-676e85baba27.95ed1dc8-4ddf-43fb-8a87-f54c81f96879", null, null);
    }

    @NotNull
    @Override
    public FavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "Inserimento Favorite Layout");

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.favorite_game_layout, parent, false);

        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteHolder holder, int position) {
        Log.i(TAG, "Riempimento Favorite Item");

        position = position % favoriteGames.length;

        Picasso.get()
                .load(favoriteGames[position].getImageURL())
                .resize(500, 500)
                .centerCrop(Gravity.CENTER)
                .into(holder.getImageView());
        holder.getTitle().setText(favoriteGames[position].getTitle());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

}
