package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.findagig.ui.history.HistoryFragment;
import com.findagig.ui.home.HomeFragment;
import com.findagig.ui.menu.MenuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selctedFragment = null;

            switch (menuItem.getItemId()){

                case R.id.nav_home:

                    selctedFragment = new HomeFragment();
                    break;
                case R.id.nav_history:

                    selctedFragment = new HistoryFragment();
                    break;
                case R.id.nav_menu:

                    selctedFragment = new MenuFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selctedFragment).commit();
            return true;
        }
    };
}
