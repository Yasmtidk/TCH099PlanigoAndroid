package com.example.test_planigo.vue.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.example.test_planigo.vue.adaptateurs.RecetteAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MaListeRecettesActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView myRecipesRecyclerView;
    private RecetteAdapter recetteAdapter;
    private ImageView backButton;
    private boolean isSelectionMode = false;
    private String slotKeyToUpdate = null;

    // Simulation: Liste statique des recettes sauvegardées (only temporary for now)
    static List<RecetteAbrege> savedRecipesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_liste_recettes);

        isSelectionMode = getIntent().getBooleanExtra("SELECTION_MODE", false);
        if (isSelectionMode) {
            slotKeyToUpdate = getIntent().getStringExtra("SLOT_KEY");
            Log.d("MaListeRecettes", "Mode Sélection activé pour slot: " + slotKeyToUpdate);
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        myRecipesRecyclerView = findViewById(R.id.myRecipesRecyclerView);
        backButton = findViewById(R.id.backButtonMyRecipes);

        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                if (isSelectionMode) {
                    setResult(Activity.RESULT_CANCELED);
                }
                finish();
            });
        }

        setupRecyclerView();
        setupBottomNavigationListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedRecipes();
    }

    // Simulation: Ajout à la liste statique
    public static void addRecipeToStaticList(RecetteAbrege recipe) {
        if (recipe != null) {
            boolean found = savedRecipesList.stream().anyMatch(r -> r.getId() == recipe.getId());
            if (!found) {
                savedRecipesList.add(recipe);
                Log.d("MaListeRecettes", "Recette ajoutée (simulé): " + recipe.getNom());
            } else {
                Log.d("MaListeRecettes", "Recette déjà présente (simulé): " + recipe.getNom());
            }
        }
    }

    private void setupRecyclerView() {
        recetteAdapter = new RecetteAdapter(new ArrayList<>(), recette -> {
            if (isSelectionMode) {
                // Renvoyer la recette sélectionnée
                Intent resultIntent = new Intent();
                if (recette instanceof Parcelable) {
                    resultIntent.putExtra("SELECTED_RECIPE", (Parcelable) recette);
                } else {
                    resultIntent.putExtra("SELECTED_RECIPE_ID", recette.getId());
                }
                resultIntent.putExtra("SLOT_KEY", slotKeyToUpdate);
                setResult(Activity.RESULT_OK, resultIntent);
                Log.d("MaListeRecettes", "Recette '" + recette.getNom() + "' sélectionnée pour " + slotKeyToUpdate);
                finish();
            } else {
                // Afficher les détails
                Intent intent = new Intent(MaListeRecettesActivity.this, RecipeDetailPageActivity.class);
                intent.putExtra("ID", recette.getId());
                startActivity(intent);
            }
        });
        myRecipesRecyclerView.setAdapter(recetteAdapter);
        myRecipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    // Simulation: Chargement depuis la liste statique
    private void loadSavedRecipes() {
        Log.d("MaListeRecettes", "Chargement de " + savedRecipesList.size() + " recettes sauvegardées (simulé).");
        recetteAdapter.setData(new ArrayList<>(savedRecipesList));
    }

    private void setupBottomNavigationListener() {
        if (isSelectionMode) {
            bottomNavigationView.setSelectedItemId(R.id.nav_planner);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (isSelectionMode) {
                Log.d("MaListeRecettes", "Navigation pendant mode sélection: Annulation.");
                setResult(Activity.RESULT_CANCELED);
                finish();
                return true;
            }

            // Navigation normale
            Intent intent = null;
            if (id == R.id.nav_accueil) intent = new Intent(this, AccueilActivity.class);
            else if (id == R.id.nav_stock) intent = new Intent(this, MonStockIngredientsActivity.class);
            else if (id == R.id.nav_planner) intent = new Intent(this, WeeklyPlannerActivity.class);
            else if (id == R.id.nav_recettes) intent = new Intent(this, RechercheRecetteActivity.class);
            else if (id == R.id.nav_profile) intent = new Intent(this, ProfileActivity.class);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}