package it.adriano.tumino.gamepoint;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import it.adriano.tumino.gamepoint.database.DBManager;
import it.adriano.tumino.gamepoint.database.DataBaseValues;
import it.adriano.tumino.gamepoint.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createFavoriteDatabase();
        createLastResearchDatabase();
        //this.deleteDatabase(DataBaseValues.FAVORITE_TABLE.getName());
        //this.deleteDatabase(DataBaseValues.ULITME_RICERCHE.getName());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_news, R.id.navigation_search, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void createFavoriteDatabase(){
        DBManager manager = new DBManager(this, DataBaseValues.FAVORITE_TABLE.getName());
    }

    private void createLastResearchDatabase(){
        DBManager manager = new DBManager(this, DataBaseValues.ULITME_RICERCHE.getName());
    }

}