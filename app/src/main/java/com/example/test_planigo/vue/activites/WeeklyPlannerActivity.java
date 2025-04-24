package com.example.test_planigo.vue.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class WeeklyPlannerActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Button generateGroceryListButton;
    private ActivityResultLauncher<Intent> selectRecipeLauncher;

    // Simulation: Stockage statique du plan (only temporary for now)
    private static Map<String, RecetteAbrege> currentPlan = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_planner);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        generateGroceryListButton = findViewById(R.id.generateGroceryListButton);

        // Initialisation du launcher pour le résultat de la sélection
        selectRecipeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("Planner", "Retour sélection: Code = " + result.getResultCode());
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        RecetteAbrege selectedRecipe = null;
                        if (data.hasExtra("SELECTED_RECIPE")) {
                            selectedRecipe = data.getParcelableExtra("SELECTED_RECIPE");
                        } else if (data.hasExtra("SELECTED_RECIPE_ID")) {
                            int recipeId = data.getIntExtra("SELECTED_RECIPE_ID", -1);
                            // TODO: Si seulement l'ID est passé, il faudrait recharger les détails ici
                            Log.w("Planner", "Reçu seulement ID: " + recipeId + " (logique de chargement manquante)");
                        }

                        String slotKey = data.getStringExtra("SLOT_KEY");

                        Log.d("Planner", "Recette reçue: " + (selectedRecipe != null ? selectedRecipe.getNom() : "null") + " pour slot: " + slotKey);

                        if (selectedRecipe != null && slotKey != null && !slotKey.isEmpty()) {
                            currentPlan.put(slotKey, selectedRecipe);
                            updateSpecificCardByKey(slotKey, selectedRecipe);
                        } else {
                            Log.w("Planner", "Données retour invalides.");
                        }
                    } else {
                        Log.d("Planner", "Sélection annulée.");
                    }
                });

        bottomNavigationView.setSelectedItemId(R.id.nav_planner);
        setupBottomNavigationListener();

        generateGroceryListButton.setOnClickListener(v -> {
            Intent intent = new Intent(WeeklyPlannerActivity.this, MaListeEpicerieActivity.class);
            intent.putExtra("HIGHLIGHT_PLANNER", true);
            Toast.makeText(this, "TODO: Générer liste épicerie", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

        loadOrInitializePlanner();
    }

    private void setupBottomNavigationListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_planner) {
                return false;
            }

            Intent intent = null;
            if (id == R.id.nav_accueil) intent = new Intent(this, AccueilActivity.class);
            else if (id == R.id.nav_stock) intent = new Intent(this, MonStockIngredientsActivity.class);
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

    // Simulation: Chargement / Initialisation
    private void loadOrInitializePlanner() {

        populateAllCards();
    }

    // Remplit toutes les cartes de recttes
    private void populateAllCards() {
        String[] days = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        int[] slotIds = {R.id.lundi_repas1, R.id.lundi_repas2, R.id.lundi_repas3,
                R.id.mardi_repas1, R.id.mardi_repas2, R.id.mardi_repas3,
                R.id.mercredi_repas1, R.id.mercredi_repas2, R.id.mercredi_repas3,
                R.id.jeudi_repas1, R.id.jeudi_repas2, R.id.jeudi_repas3,
                R.id.vendredi_repas1, R.id.vendredi_repas2, R.id.vendredi_repas3,
                R.id.samedi_repas1, R.id.samedi_repas2, R.id.samedi_repas3,
                R.id.dimanche_repas1, R.id.dimanche_repas2, R.id.dimanche_repas3};
        int idIndex = 0;
        for (String day : days) {
            for (int i = 0; i < 3; i++) {
                String key = day + "_" + i;
                RecetteAbrege recipe = currentPlan.get(key);
                View cardView = findViewById(slotIds[idIndex++]);
                populateMealCard(cardView, recipe, key);
            }
        }
    }

    // Met à jour une carte de recette specific
    private void updateSpecificCardByKey(String slotKey, RecetteAbrege recipe) {
        int viewId = getViewIdForSlotKey(slotKey);
        if (viewId != 0) {
            View cardView = findViewById(viewId);
            populateMealCard(cardView, recipe, slotKey);
        } else {
            Log.e("Planner", "ID de vue non trouvé pour clé: " + slotKey);
        }
    }

    // Trouve l'ID de la vue pour un carte de recette donné
    private int getViewIdForSlotKey(String slotKey) {
        String[] parts = slotKey.split("_");
        if (parts.length != 2) return 0;
        String day = parts[0];
        int slotIndex;
        try {
            slotIndex = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) { return 0; }

        String resourceName = day.toLowerCase() + "_repas" + (slotIndex + 1);
        try {
            return getResources().getIdentifier(resourceName, "id", getPackageName());
        } catch (Exception e) {
            Log.e("Planner", "Erreur getIdentifier pour " + resourceName, e);
            return 0;
        }
    }

    // Remplit une carte de recette (slot)
    private void populateMealCard(View cardRootView, RecetteAbrege recipe, final String slotKey) {
        if (cardRootView == null) return;

        ImageView imageView = cardRootView.findViewById(R.id.recetteListeImage);
        TextView nameTextView = cardRootView.findViewById(R.id.recetteListeNom);
        TextView timeTextView = cardRootView.findViewById(R.id.recetteListeTempsPreparation);
        TextView typeTextView = cardRootView.findViewById(R.id.recetteListeType);

        // Slot vide
        if (recipe == null || recipe.getId() == 0) {
            nameTextView.setText("Ajouter repas");
            timeTextView.setText("");
            typeTextView.setText("");
            imageView.setImageResource(R.drawable.plus);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(16, 16, 16, 16);

            cardRootView.setOnClickListener(v -> {
                Intent intent = new Intent(WeeklyPlannerActivity.this, RechercheRecetteActivity.class);
                intent.putExtra("SELECTION_MODE", true);
                intent.putExtra("SLOT_KEY", slotKey);
                Log.d("Planner", "Lancement sélection pour slot: " + slotKey);
                selectRecipeLauncher.launch(intent); // Utilisation du Launcher
            });
            return;
        }

        // Slot rempli
        nameTextView.setText(recipe.getNom());
        timeTextView.setText(recipe.getTemps_de_cuisson() + " minutes");
        typeTextView.setText(recipe.getType());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(0, 0, 0, 0);

        if (recipe.getImage() != null && !recipe.getImage().isEmpty() && !recipe.getImage().startsWith("URL")) {
            Picasso.get().load(recipe.getImage()).placeholder(R.drawable.planigologo).error(R.drawable.planigologo).fit().centerCrop().into(imageView);
        } else {
            imageView.setImageResource(R.drawable.planigologo);
        }

        cardRootView.setOnClickListener(v -> {
            Intent intent = new Intent(WeeklyPlannerActivity.this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", recipe.getId());
            startActivity(intent);
        });
    }
}