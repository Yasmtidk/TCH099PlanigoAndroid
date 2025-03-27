package com.yasmi.tch099planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yasmi.tch099planigo.R;
import com.yasmi.tch099planigo.vue.adaptateurs.StockIngredientAdapter;

import java.util.ArrayList;
import java.util.List;

public class MonStockIngredientsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView stockIngredientsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_stock);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        stockIngredientsListView = findViewById(R.id.stockIngredientsListView);

        bottomNavigationView.setSelectedItemId(R.id.nav_stock);

        List<StockIngredientAdapter.StockIngredient> stockIngredientsList = new ArrayList<>();
        stockIngredientsList.add(new StockIngredientAdapter.StockIngredient("Lait", 250, "ml", "lait"));
        stockIngredientsList.add(new StockIngredientAdapter.StockIngredient("Patates", 4, "unité", "patates"));
        stockIngredientsList.add(new StockIngredientAdapter.StockIngredient("Beurre", 10, "g", "beurre"));
        stockIngredientsList.add(new StockIngredientAdapter.StockIngredient("Farine", 30, "g", "farine"));
        stockIngredientsList.add(new StockIngredientAdapter.StockIngredient("Steak", 4, "unité", "steak"));
        stockIngredientsList.add(new StockIngredientAdapter.StockIngredient("Oeuf", 5, "unité", "oeuf"));
        stockIngredientsList.add(new StockIngredientAdapter.StockIngredient("Tomates", 1, "unité", "tomates"));
        stockIngredientsList.add(new StockIngredientAdapter.StockIngredient("Carottes", 3, "unité", "carottes"));


        StockIngredientAdapter adapter = new StockIngredientAdapter(this, stockIngredientsList);
        stockIngredientsListView.setAdapter(adapter);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_accueil) {
                Intent intent = new Intent(MonStockIngredientsActivity.this, AccueilActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_stock) {
                bottomNavigationView.setSelectedItemId(R.id.nav_stock);
                return true;

            } else if (id == R.id.nav_recettes) {
                Intent intent = new Intent(MonStockIngredientsActivity.this, RechercheRecetteActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_courses) {
                Intent intent = new Intent(MonStockIngredientsActivity.this, MaListeEpicerieActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(MonStockIngredientsActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}