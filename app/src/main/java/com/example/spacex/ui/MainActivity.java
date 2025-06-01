package com.example.spacex.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.spacex.R;
import com.example.spacex.databinding.ActivityMainBinding;
import com.example.spacex.ui.utils.MyNavigator;


public class MainActivity extends AppCompatActivity implements MyNavigator {

    private ActivityMainBinding binding;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.root);
        navController = Navigation.findNavController(this, R.id.root);
        navController = NavHostFragment.findNavController(getSupportFragmentManager().getFragments().get(0));


        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.loginFragment || navDestination.getId() == R.id.registerFragment) {
                binding.bottomNavigationView.setVisibility(View.GONE);
            } else {
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.profileFragment) {
                navController.navigate(R.id.profileFragment);
            }
            if (item.getItemId() == R.id.articlesListFragment) {
                navController.navigate(R.id.articlesListFragment);
            }
            if (item.getItemId() == R.id.eventListFragment) {
                navController.navigate(R.id.fragment_list);
            }
            if (item.getItemId() == R.id.launchListFragment) {
                navController.navigate(R.id.launchListFragment);
            }
            return true;
        });

    }

    @Override
    public void onLogout() {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        navController.navigate(R.id.loginFragment);
    }

}