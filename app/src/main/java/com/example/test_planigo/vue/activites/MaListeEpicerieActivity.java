package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.VueModele.RecetteViewModel;
import com.example.test_planigo.VueModele.StockageViewModel;
import com.example.test_planigo.modeles.entitees.Produit;
import com.example.test_planigo.vue.adaptateurs.ItemIngredientAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;
import com.example.test_planigo.vue.adaptateurs.GroceryListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaListeEpicerieActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView groceryListView;
    private StockageViewModel viewModel;
    private ItemIngredientAdapter adapter;
    private RecyclerView compteneurListeEpicerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_liste_epicerie);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(StockageViewModel.class);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        compteneurListeEpicerie = findViewById(R.id.groceryListView);

        compteneurListeEpicerie.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView.setSelectedItemId(R.id.nav_courses);

        //Récupéré la liste d'épicerie pour l'initialisation
        viewModel.getListeProduit().observe(this, produits -> {
            adapter = new ItemIngredientAdapter(Arrays.asList(produits));
            compteneurListeEpicerie.setAdapter(adapter);
        });

        viewModel.chargerListeEpicerie();

        /*
        List<Produit> groceryItemList = new ArrayList<>();
        groceryItemList.add(new Produit("Lait", 250, "ml", "lait"));
        groceryItemList.add(new Produit("Patates", 4, "unité", "patates"));
        groceryItemList.add(new Produit("Beurre", 10, "g", "beurre"));
        groceryItemList.add(new Produit("Farine", 30, "g", "farine"));
        groceryItemList.add(new Produit("Steak", 4, "unité", "steak"));
        groceryItemList.add(new Produit("Oeuf", 5, "unité", "oeuf"));
        groceryItemList.add(new Produit("Tomates", 1, "unité", "tomates"));
        groceryItemList.add(new Produit("Carottes", 3, "unité", "carottes"));
        groceryItemList.add(new Produit("Saucisses", 2, "unité", "saucisses"));


        GroceryListAdapter adapter = new GroceryListAdapter(this, groceryItemList);
        groceryListView.setAdapter(adapter);*/


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