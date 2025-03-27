package com.yasmi.tch099planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yasmi.tch099planigo.R;
import com.yasmi.tch099planigo.vue.adaptateurs.GroceryListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MaListeEpicerieActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView groceryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_liste_epicerie);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        groceryListView = findViewById(R.id.groceryListView);

        bottomNavigationView.setSelectedItemId(R.id.nav_courses);

        List<GroceryListAdapter.GroceryItem> groceryItemList = new ArrayList<>();
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Lait", 250, "ml", "lait"));
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Patates", 4, "unité", "patates"));
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Beurre", 10, "g", "beurre"));
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Farine", 30, "g", "farine"));
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Steak", 4, "unité", "steak"));
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Oeuf", 5, "unité", "oeuf"));
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Tomates", 1, "unité", "tomates"));
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Carottes", 3, "unité", "carottes"));
        groceryItemList.add(new GroceryListAdapter.GroceryItem("Saucisses", 2, "unité", "saucisses"));


        GroceryListAdapter adapter = new GroceryListAdapter(this, groceryItemList);
        groceryListView.setAdapter(adapter);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_accueil) {
                Intent intent = new Intent(MaListeEpicerieActivity.this, AccueilActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_stock) {
                Intent intent = new Intent(MaListeEpicerieActivity.this, MonStockIngredientsActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_recettes) {
                Intent intent = new Intent(MaListeEpicerieActivity.this, RechercheRecetteActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_courses) {
                bottomNavigationView.setSelectedItemId(R.id.nav_courses);
                return true;

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(MaListeEpicerieActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}