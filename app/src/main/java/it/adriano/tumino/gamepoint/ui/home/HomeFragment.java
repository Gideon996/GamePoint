package it.adriano.tumino.gamepoint.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import it.adriano.tumino.gamepoint.adapter.FavoriteAdapter;
import it.adriano.tumino.gamepoint.data.FavoriteGames;
import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DataBaseValues;
import it.adriano.tumino.gamepoint.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ShimmerFrameLayout shimmerFrameLayout;
    private DBManager dbManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Generazione vista Home");
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //generateDB();
        //generateUltimeRicerche();

        shimmerFrameLayout = binding.shimmerLayoutFavorite;
        shimmerFrameLayout.startShimmer();
        homeViewModel.setShimmerFrameLayout(shimmerFrameLayout);

        RecyclerView recyclerView = binding.favoriteRecycleView;

        //Center Item in RecycleView
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);

        //Create Circular Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.scrollToPosition(Integer.MAX_VALUE / 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Finalize RecycleView inizialization
        recyclerView.setHasFixedSize(true);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter();
        recyclerView.setAdapter(favoriteAdapter);
        homeViewModel.setRecyclerView(recyclerView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Start della vista Home");
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        homeViewModel.getRecyclerView().setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generateDB(){
        dbManager = new DBManager(getContext(), DataBaseValues.FAVORITE_TABLE.getName());
        Cursor cursor = dbManager.query();
        FavoriteGames[] favoriteGames = new FavoriteGames[10];
        favoriteGames[0] = new FavoriteGames("Zelda: Breath of the Wild", "https://static.posters.cz/image/750/poster/zelda-breath-of-the-wild-hyrule-scene-landscape-i34557.jpg", "https://www.nintendo.it/Giochi/Nintendo-Switch/The-Legend-of-Zelda-Breath-of-the-Wild-1173609.html", "Nintendo");
        favoriteGames[1] = new FavoriteGames("Monster Hunter: World", "https://cdn-ext.fanatical.com/production/product/1280x720/7f6e4bbc-69a4-4910-a424-8b4f14100c12.jpeg", "https://store.steampowered.com/app/582010/Monster_Hunter_World/", "Steam");
        favoriteGames[2] = new FavoriteGames("League of Legends", "https://images.contentstack.io/v3/assets/blt731acb42bb3d1659/bltcfa4652c8d383f56/5e21837f63d1b6503160d39b/Home-page.jpg", "https://euw.leagueoflegends.com/it-it/", "RIOT");
        favoriteGames[3] = new FavoriteGames("Valorant", "https://img.redbull.com/images/c_crop,w_1620,h_1080,x_144,y_0,f_auto,q_auto/c_scale,w_1500/redbullcom/2020/6/5/ctsejxmdtw9inp8zqqqd/valorant", "https://playvalorant.com/it-it/", "RIOT");
        favoriteGames[4] = new FavoriteGames("Hearthstone", "https://d2vkoy1na2a6o6.cloudfront.net/_next/static/images/default-4fff3c606c794dc03a915b9071f562d3.jpg", "https://playhearthstone.com/it-it", "BattleNet");
        favoriteGames[5] = new FavoriteGames("Dragon Ball Z: Kakarot", "https://i2.wp.com/www.gamesplus.it/wp-content/uploads/2020/01/DRAGON-BALL-Z-KAKAROT-recensione-ps4-cover.jpg?fit=1280%2C720&ssl=1", "https://store.steampowered.com/app/851850/DRAGON_BALL_Z_KAKAROT/", "Steam");
        favoriteGames[6] = new FavoriteGames("Naruto: Ninja Storm 4", "https://s2.gaming-cdn.com/images/products/7492/orig/naruto-shippuden-ultimate-ninja-storm-4-road-to-boruto-cover.jpg", "https://store.playstation.com/it-it/product/EP0700-CUSA06210_00-NARUTOUNS4RTB000", "PlayStation Store");
        favoriteGames[7] = new FavoriteGames("Call Of Duty: Black Ops", "https://s1.gaming-cdn.com/images/products/60/orig/call-of-duty-black-ops-cover.jpg", "https://store.steampowered.com/app/42700/Call_of_Duty_Black_Ops/", "Steam");
        favoriteGames[8] = new FavoriteGames("FIFA 21", "https://cdn-products.eneba.com/resized-products/hT4DQNlnn9KL3x03qGK7l717ETDkSr2-T_wmbEVoicE_350x200_1x-0.jpeg", "https://store.playstation.com/it-it/product/EP0006-PPSA01326_00-POSTLAUNCHCHAMPN", "PlayStation Store");
        favoriteGames[9] = new FavoriteGames("Binding of isaac", "https://store-images.s-microsoft.com/image/apps.58856.69039762475408619.1f9f3938-799b-4ad8-a607-676e85baba27.95ed1dc8-4ddf-43fb-8a87-f54c81f96879", "https://store.steampowered.com/app/250900/The_Binding_of_Isaac_Rebirth/", "Steam");

        for(FavoriteGames favorite : favoriteGames){
            dbManager.save(favorite.getTitle(), favorite.getImageURL(), favorite.getStore(), favorite.getUrl());
        }
    }

    private void generateUltimeRicerche(){
        DBManager dbManager = new DBManager(getContext(), DataBaseValues.ULITME_RICERCHE.getName());
        Cursor cursor = dbManager.query();

        FavoriteGames[] favoriteGames = new FavoriteGames[10];
        favoriteGames[0] = new FavoriteGames("Zelda: Breath of the Wild", "https://static.posters.cz/image/750/poster/zelda-breath-of-the-wild-hyrule-scene-landscape-i34557.jpg", "https://www.nintendo.it/Giochi/Nintendo-Switch/The-Legend-of-Zelda-Breath-of-the-Wild-1173609.html", "Nintendo");
        favoriteGames[1] = new FavoriteGames("Monster Hunter: World", "https://cdn-ext.fanatical.com/production/product/1280x720/7f6e4bbc-69a4-4910-a424-8b4f14100c12.jpeg", "https://store.steampowered.com/app/582010/Monster_Hunter_World/", "Steam");
        favoriteGames[2] = new FavoriteGames("League of Legends", "https://images.contentstack.io/v3/assets/blt731acb42bb3d1659/bltcfa4652c8d383f56/5e21837f63d1b6503160d39b/Home-page.jpg", "https://euw.leagueoflegends.com/it-it/", "RIOT");
        favoriteGames[3] = new FavoriteGames("Valorant", "https://img.redbull.com/images/c_crop,w_1620,h_1080,x_144,y_0,f_auto,q_auto/c_scale,w_1500/redbullcom/2020/6/5/ctsejxmdtw9inp8zqqqd/valorant", "https://playvalorant.com/it-it/", "RIOT");
        favoriteGames[4] = new FavoriteGames("Hearthstone", "https://d2vkoy1na2a6o6.cloudfront.net/_next/static/images/default-4fff3c606c794dc03a915b9071f562d3.jpg", "https://playhearthstone.com/it-it", "BattleNet");
        favoriteGames[5] = new FavoriteGames("Dragon Ball Z: Kakarot", "https://i2.wp.com/www.gamesplus.it/wp-content/uploads/2020/01/DRAGON-BALL-Z-KAKAROT-recensione-ps4-cover.jpg?fit=1280%2C720&ssl=1", "https://store.steampowered.com/app/851850/DRAGON_BALL_Z_KAKAROT/", "Steam");
        favoriteGames[6] = new FavoriteGames("Naruto: Ninja Storm 4", "https://s2.gaming-cdn.com/images/products/7492/orig/naruto-shippuden-ultimate-ninja-storm-4-road-to-boruto-cover.jpg", "https://store.playstation.com/it-it/product/EP0700-CUSA06210_00-NARUTOUNS4RTB000", "PlayStation Store");
        favoriteGames[7] = new FavoriteGames("Call Of Duty: Black Ops", "https://s1.gaming-cdn.com/images/products/60/orig/call-of-duty-black-ops-cover.jpg", "https://store.steampowered.com/app/42700/Call_of_Duty_Black_Ops/", "Steam");
        favoriteGames[8] = new FavoriteGames("FIFA 21", "https://cdn-products.eneba.com/resized-products/hT4DQNlnn9KL3x03qGK7l717ETDkSr2-T_wmbEVoicE_350x200_1x-0.jpeg", "https://store.playstation.com/it-it/product/EP0006-PPSA01326_00-POSTLAUNCHCHAMPN", "PlayStation Store");
        favoriteGames[9] = new FavoriteGames("Binding of isaac", "https://store-images.s-microsoft.com/image/apps.58856.69039762475408619.1f9f3938-799b-4ad8-a607-676e85baba27.95ed1dc8-4ddf-43fb-8a87-f54c81f96879", "https://store.steampowered.com/app/250900/The_Binding_of_Isaac_Rebirth/", "Steam");

        for(FavoriteGames favorite : favoriteGames){
            dbManager.save(favorite.getTitle(), favorite.getImageURL(), favorite.getStore(), favorite.getUrl());
        }
    }
}