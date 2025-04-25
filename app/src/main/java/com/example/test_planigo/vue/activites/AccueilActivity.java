package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.AccueilNavigationItem;
import com.example.test_planigo.vue.adaptateurs.AccueilAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends AppCompatActivity {

    private RecyclerView accueilRecyclerView;
    private AccueilAdapter accueilAdapter;
    private List<AccueilNavigationItem> navigationItems;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_page);

        accueilRecyclerView = findViewById(R.id.accueilRecyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        prepareNavigationItems();
        setupRecyclerView();
        setupBottomNavigationListener();
    }

    // Prépare la liste des éléments pour la grille
    private void prepareNavigationItems() {
        navigationItems = new ArrayList<>();
        navigationItems.add(new AccueilNavigationItem("Découvrir Recettes", R.drawable.recipe_book, RechercheRecetteActivity.class));
        navigationItems.add(new AccueilNavigationItem("Mes Recettes", R.drawable.myyyy_recipe, MaListeRecettesActivity.class));
        navigationItems.add(new AccueilNavigationItem("Planificateur", R.drawable.weekplanner, ListePlanificateurActivity.class));
        navigationItems.add(new AccueilNavigationItem("Mon Stock", R.drawable.mon_stock, MonStockIngredientsActivity.class));
        navigationItems.add(new AccueilNavigationItem("Mon Profil", R.drawable.usernavigation, ProfileActivity.class));
    }

    // Configure le RecyclerView
    private void setupRecyclerView() {
        accueilAdapter = new AccueilAdapter(this, navigationItems);
        accueilRecyclerView.setAdapter(accueilAdapter);
        accueilRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Configure la barre de navigation
    private void setupBottomNavigationListener() {
        bottomNavigationView.setSelectedItemId(R.id.nav_accueil);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_accueil) {
                return false;
            }

            Intent intent = null;
            if (id == R.id.nav_stock) intent = new Intent(this, MonStockIngredientsActivity.class);
            else if (id == R.id.nav_recettes) intent = new Intent(this, RechercheRecetteActivity.class);
            else if (id == R.id.nav_planner) intent = new Intent(this, WeeklyPlannerActivity.class);
            else if (id == R.id.nav_profile) intent = new Intent(this, ProfileActivity.class);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}