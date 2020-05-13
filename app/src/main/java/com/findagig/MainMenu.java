package com.findagig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    CardView search;
    CardView history;
    CardView see_map;
    CardView settings;
    CardView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search_card);
        history = findViewById(R.id.history_card);
        see_map = findViewById(R.id.see_map_card);
        settings = findViewById(R.id.settings_card);
        logout = findViewById(R.id.logout_card);

        search.setOnClickListener( new CardView.OnClickListener () {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(i);
            }
        });

        history.setOnClickListener( new CardView.OnClickListener () {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(i);
            }
        });

        see_map.setOnClickListener( new CardView.OnClickListener () {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        settings.setOnClickListener( new CardView.OnClickListener () {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener( new CardView.OnClickListener () {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LogInPage.class);
                startActivity(i);
            }
        });

    }
}
