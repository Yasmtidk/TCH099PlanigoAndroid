package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout modifierMonStockCard;
    private LinearLayout rechercheDeRecettesCard;
    private LinearLayout listeDeMesRecettesCard;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_page);

        modifierMonStockCard = findViewById(R.id.modifier_mon_stock_card);
        rechercheDeRecettesCard = findViewById(R.id.recherche_de_recettes_card);
        listeDeMesRecettesCard = findViewById(R.id.liste_de_mes_recettes_card);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        rechercheDeRecettesCard.setOnClickListener(this);
        listeDeMesRecettesCard.setOnClickListener(this);

         bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_accueil) {
                 bottomNavigationView.setSelectedItemId(R.id.nav_accueil);
                 return true;

            } else if (id == R.id.nav_stock) {
                Intent intent = new Intent(AccueilActivity.this, MonStockIngredientsActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_recettes) {
                Intent intent = new Intent(AccueilActivity.this, RechercheRecetteActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_courses) {
                Intent intent = new Intent(AccueilActivity.this, MaListeEpicerieActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(AccueilActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
             return false;
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        int id = v.getId();
        if (id == R.id.modifier_mon_stock_card) {
            intent = new Intent(AccueilActivity.this, MonStockIngredientsActivity.class);
            startActivity(intent);

        } else if (id == R.id.recherche_de_recettes_card) {
            intent = new Intent(AccueilActivity.this, RechercheRecetteActivity.class);
            startActivity(intent);

        } else if (id == R.id.liste_de_mes_recettes_card) {
            intent = new Intent(AccueilActivity.this, RechercheRecetteActivity.class);
            startActivity(intent);
        }
    }
}