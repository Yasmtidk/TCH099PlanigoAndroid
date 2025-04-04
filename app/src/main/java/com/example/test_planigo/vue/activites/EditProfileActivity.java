package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;

public class EditProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        backButton = findViewById(R.id.backButton);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                return true;

            } else if (id == R.id.nav_stock) {
                Intent intent = new Intent(EditProfileActivity.this, MonStockIngredientsActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_recettes) {
                Intent intent = new Intent(EditProfileActivity.this, RechercheRecetteActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_courses) {
                Intent intent = new Intent(EditProfileActivity.this, MaListeEpicerieActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_accueil) {
                Intent intent = new Intent(EditProfileActivity.this, AccueilActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        backButton.setOnClickListener(v -> {
            finish();
        });
    }
}