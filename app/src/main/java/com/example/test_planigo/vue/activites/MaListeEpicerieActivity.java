package com.example.test_planigo.vue.activites;

// Importer les classes nécessaires
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast; // Ajout pour message d'erreur

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.StockageViewModel;
// Produit n'est pas directement utilisé ici, mais par l'adapter
import com.example.test_planigo.vue.adaptateurs.ItemIngredientAdapter; // Adapter pour afficher les produits
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList; // Pour initialisation de l'adapter
import java.util.Arrays;

public class MaListeEpicerieActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView compteneurListeEpicerie; // Utilisation de RecyclerView
    private StockageViewModel viewModel; // ViewModel pour les données
    private ItemIngredientAdapter adapter; // Adaptateur pour le RecyclerView
    private ImageView backButton; // Bouton retour optionnel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assurez-vous que ce layout contient un RecyclerView avec l'ID @+id/groceryListView
        setContentView(R.layout.activity_ma_liste_epicerie);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Utilisation du RecyclerView (assurez-vous que l'ID est correct dans le XML)
        compteneurListeEpicerie = findViewById(R.id.groceryListView);
        backButton = findViewById(R.id.backButtonGrocery); // Si ce bouton existe dans votre layout

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(StockageViewModel.class);

        // Configurer le RecyclerView
        compteneurListeEpicerie.setLayoutManager(new LinearLayoutManager(this));
        // Initialiser l'adaptateur (sera remplacé dans l'observateur)
        adapter = new ItemIngredientAdapter(new ArrayList<>()); // Commencer avec une liste vide
        compteneurListeEpicerie.setAdapter(adapter);

        // Sélectionner l'item approprié dans la barre de navigation
        // Mettre à jour si 'nav_planner' n'est pas le bon item pour l'épicerie
        bottomNavigationView.setSelectedItemId(R.id.nav_planner);

        // Configurer le bouton retour s'il existe
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // Observer les changements de la liste d'épicerie (LiveData)
        viewModel.getListeProduit().observe(this, produits -> {
            // Met à jour l'UI quand les données changent
            if (produits != null) {
                // Mettre à jour l'adaptateur avec la nouvelle liste
                // En supposant que ItemIngredientAdapter accepte List<Produit>
                adapter = new ItemIngredientAdapter(Arrays.asList(produits));
                compteneurListeEpicerie.setAdapter(adapter); // Réaffecter l'adaptateur avec les nouvelles données
            } else {
                // Gérer le cas d'erreur ou de liste vide si nécessaire
                Toast.makeText(this, "Impossible de charger la liste d'épicerie", Toast.LENGTH_SHORT).show();
                // Optionnellement, vider l'adaptateur
                adapter = new ItemIngredientAdapter(new ArrayList<>());
                compteneurListeEpicerie.setAdapter(adapter);
            }
        });

        // Charger les données initiales de la liste d'épicerie
        viewModel.chargerListeEpicerie(); // Appeler la méthode spécifique pour l'épicerie

        // Gérer la navigation via la barre inférieure
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null; // Déclarer l'intent ici

            if (id == R.id.nav_accueil) {
                intent = new Intent(MaListeEpicerieActivity.this, AccueilActivity.class);
            } else if (id == R.id.nav_stock) {
                intent = new Intent(MaListeEpicerieActivity.this, MonStockIngredientsActivity.class);
            } else if (id == R.id.nav_recettes) {
                intent = new Intent(MaListeEpicerieActivity.this, RechercheRecetteActivity.class);
            } else if (id == R.id.nav_profile) {
                intent = new Intent(MaListeEpicerieActivity.this, ProfileActivity.class);
            } else if (id == R.id.nav_planner) {
                return false; // Déjà sur cet écran (ou l'écran lié)
            }

            if (intent != null) {
                // Utiliser des flags pour éviter de recréer l'activité si elle existe déjà
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true; // Indiquer que l'événement a été géré
            }
            // Retourner false si aucun élément n'est géré ou s'il s'agit de l'élément actuel
            return false;
        });
    }
}