package com.thcplusplus.covidyab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.thcplusplus.covidyab.databinding.ActivityMainBinding;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private NavController mNavController;
    public static final String MAIN_ACTIVITY_ERROR = "MAIN_ACTIVITY_ERROR";
    @Override
    public void onBackPressed() {
        // **********EDIT THIS
        // when on quiz => back pressing closes the quiz
        // when on patient?
        try {
            if (mNavController != null){
                Navigation.findNavController(this, R.id.main_fragment_container).navigate(R.id.action_healthQuizFragment_to_mainFragment);
                return;
            }

        }
        catch (Exception ex){
            Log.e(MAIN_ACTIVITY_ERROR, ex.getMessage());
        }

        super.onBackPressed();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        mNavController = Navigation.findNavController(this, R.id.main_fragment_container);

        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.main_fragment_container);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}