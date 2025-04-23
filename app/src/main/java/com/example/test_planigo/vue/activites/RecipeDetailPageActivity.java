package com.example.test_planigo.vue.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.VueModele.RecetteViewModel;
import com.example.test_planigo.modeles.entitees.Produit;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.example.test_planigo.modeles.entitees.RecetteComplete;
import com.example.test_planigo.vue.adaptateurs.ItemUniqueAdapter;
import com.example.test_planigo.vue.adaptateurs.ItemStepAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Locale;

public class RecipeDetailPageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageRecette;
    private TextView recipeNameDetailTextView, recipeTimeTextView, recipeDifficultyTextView, recipePortionTextView, recipeTypeTextView, createurRecetteTextView, descriptionRecetteTextView;
    private RecyclerView listeRestrictions, listeEtapes;
    private LinearLayout ingredientsListLayout;
    private BottomNavigationView bottomNavigationView;
    private ImageView backButton;
    private Button ajouterListeRecetteButton;
    private RecetteComplete currentRecipe;
    private RecetteViewModel viewModel;
    private ItemUniqueAdapter restrictionsAdapter;
    private ItemStepAdapter etapeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_recipe_detail);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(RecetteViewModel.class);

        imageRecette = findViewById(R.id.recipeDetailImageView);
        recipeNameDetailTextView = findViewById(R.id.recipeNameDetailTextView);
        recipeTimeTextView = findViewById(R.id.recipeTimeTextView);
        recipeDifficultyTextView = findViewById(R.id.recipeDifficultyTextView);
        recipePortionTextView = findViewById(R.id.recipePortionTextView);
        recipeTypeTextView = findViewById(R.id.recipeTypeTextView);
        createurRecetteTextView = findViewById(R.id.recipeCreatorTextView);
        descriptionRecetteTextView = findViewById(R.id.descriptionRecette);
        listeRestrictions = findViewById(R.id.listeRestrictionRecette);
        ingredientsListLayout = findViewById(R.id.ingredientsListContainerLayout);
        listeEtapes = findViewById(R.id.etapesListContainer);
        backButton = findViewById(R.id.backButtonRecipeDetail);
        ajouterListeRecetteButton = findViewById(R.id.ajouterListeRecetteButton);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Listeners
        if (backButton != null) {
            backButton.setOnClickListener(this);
        }
        if (ajouterListeRecetteButton != null) {
            ajouterListeRecetteButton.setOnClickListener(this);
        }

        listeRestrictions.setLayoutManager(new LinearLayoutManager(this));
        listeEtapes.setLayoutManager(new LinearLayoutManager(this));
        listeRestrictions.setNestedScrollingEnabled(false);
        listeEtapes.setNestedScrollingEnabled(false);

        bottomNavigationView.setSelectedItemId(R.id.nav_recettes);

        // Récupération ID recette
        int recipeId = getIntent().getIntExtra("ID", -1);
        if (recipeId != -1) {
            Log.d("RecipeDetail", "Requesting recipe details for ID: " + recipeId);
            viewModel.setRecetteActuel(recipeId);
        } else {
            Log.e("RecipeDetail", "No valid recipe ID found in Intent extras.");
            Toast.makeText(this, "Erreur: ID de recette introuvable!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.getRecetteCompleteActuel().observe(this, recette -> {
            if (recette == null) {
                Log.e("RecipeDetail", "Received null recipe object from ViewModel for ID: " + recipeId);
                Toast.makeText(this, "Erreur: Impossible de charger les détails de la recette.", Toast.LENGTH_SHORT).show();
                return;
            }
            currentRecipe = recette;
            Log.d("RecipeDetail", "Displaying recipe details for: " + recette.getNom());

            recipeNameDetailTextView.setText(recette.getNom());
            recipeTimeTextView.setText(recette.getTemps_de_cuisson() + " minutes");
            recipeDifficultyTextView.setText(recette.getDifficulter());
            recipePortionTextView.setText(String.valueOf(recette.getPortions()) + " portions");
            recipeTypeTextView.setText(recette.getType());
            createurRecetteTextView.setText(recette.getCreateur_nom_utilisateur());
            descriptionRecetteTextView.setText(recette.getDescription());

            if (recette.getImage() != null && !recette.getImage().isEmpty()) {
                Picasso.get().load(recette.getImage()).placeholder(R.drawable.planigologo).error(R.drawable.planigologo).into(imageRecette);
            } else {
                imageRecette.setImageResource(R.drawable.planigologo);
            }

            restrictionsAdapter = new ItemUniqueAdapter(Arrays.asList(recette.getRestrictions()));
            listeRestrictions.setAdapter(restrictionsAdapter);

            remplirListeIngredients(recette.getIngredients());

            etapeAdapter = new ItemStepAdapter(Arrays.asList(recette.getEtapes()));
            listeEtapes.setAdapter(etapeAdapter);

        });

        viewModel.getResultatErreurAPILiveData().observe(this, error -> {
            if (error instanceof String) {
                Log.e("RecipeDetail", "API Error reported: " + error);
                Toast.makeText(this, "Erreur API: " + error, Toast.LENGTH_LONG).show();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_accueil) {
                intent = new Intent(RecipeDetailPageActivity.this, AccueilActivity.class);
            } else if (id == R.id.nav_stock) {
                intent = new Intent(RecipeDetailPageActivity.this, MonStockIngredientsActivity.class);
            } else if (id == R.id.nav_recettes) {
                return true;
            } else if (id == R.id.nav_planner) {
                intent = new Intent(RecipeDetailPageActivity.this, WeeklyPlannerActivity.class);
            } else if (id == R.id.nav_profile) {
                intent = new Intent(RecipeDetailPageActivity.this, ProfileActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    private void remplirListeIngredients(Produit[] ingredients) {
        if (ingredientsListLayout == null) return;

        ingredientsListLayout.removeAllViews();

        if (ingredients == null || ingredients.length == 0) {
            TextView noIngredientsText = new TextView(this);
            noIngredientsText.setText("Aucun ingrédient listé.");
            noIngredientsText.setPadding(16, 8, 16, 8);
            ingredientsListLayout.addView(noIngredientsText);
            return;
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Produit produit : ingredients) {
            View itemView = inflater.inflate(R.layout.list_item_recipe_detail, ingredientsListLayout, false);

            TextView nameTextView = itemView.findViewById(R.id.ingredientNameTextView);
            TextView quantityTextView = itemView.findViewById(R.id.ingredientQuantityTextView);

            nameTextView.setText(produit.getName());
            String quantityString = String.format(Locale.getDefault(), "%.2f", produit.getQuantity()).replaceAll("\\.?0*$", "");
            quantityTextView.setText(String.format("%s %s", quantityString, produit.getUnit()));

            ingredientsListLayout.addView(itemView);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.backButtonRecipeDetail) {
            finish();
        } else if (id == R.id.ajouterListeRecetteButton) {
            if (currentRecipe != null) {
                // Créer un RecetteAbrege à partir de RecetteComplete pour l'ajout simulé
                RecetteAbrege recetteAbrege = new RecetteAbrege(
                        currentRecipe.getId(),
                        currentRecipe.getNom(),
                        currentRecipe.getTemps_de_cuisson(),
                        currentRecipe.getType(),
                        currentRecipe.getImage()
                );
                MaListeRecettesActivity.addRecipeToStaticList(recetteAbrege);
                Toast.makeText(this, "'" + currentRecipe.getNom() + "' ajoutée à vos recettes (simulé)!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erreur: Impossible d'ajouter la recette.", Toast.LENGTH_SHORT).show();
            }
            // TODO: Remplacer par un appel ViewModel/Repository pour sauvegarde réelle en DB
        }
    }
}