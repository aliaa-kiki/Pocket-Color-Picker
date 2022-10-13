package com.example.pocketcolorpicker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pocketcolorpicker.R;
import com.example.pocketcolorpicker.fragments.colorsFragment;
import com.example.pocketcolorpicker.fragments.huesFragment;
import com.example.pocketcolorpicker.fragments.palettesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initializing the bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        title=findViewById(R.id.toolbar_title);

        // placing the fragments
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragmentContainerView, new colorsFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.colorsFragment);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.colorsFragment:
                        fragment = new colorsFragment();
                        title.setText("All Colors");
                        break;
                    case R.id.palettesFragment:
                        fragment = new palettesFragment();
                        title.setText("My Palettes");
                        break;
                    case R.id.huesFragment:
                        fragment = new huesFragment();
                        title.setText("Hue Groups");
                        break; }
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragmentContainerView, fragment).commit();

                return true;
            }

        });
    }
}