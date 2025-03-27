package com.yasmi.tch099planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yasmi.tch099planigo.R;
import com.yasmi.tch099planigo.VueModele.PlanigoViewModel;
import com.yasmi.tch099planigo.modeles.entitees.Recette;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipeDetailPageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButtonRecipeDetail, recipeDetailImageView;
    private TextView recipeNameDetailTextView, recipeTimeTextView, recipeDifficultyTextView, recipePortionTextView;
    private TextView ingredientsTitleTextView, etapesTitleTextView;
    private LinearLayout ingredientsListContainer, etapesListContainer;
    private Button ajouterListeRecetteButton;
    private BottomNavigationView bottomNavigationView;
    private PlanigoViewModel viewModel;

    private Recette recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_recipe_detail);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PlanigoViewModel.class);

        backButtonRecipeDetail = findViewById(R.id.backButtonRecipeDetail);
        recipeDetailImageView = findViewById(R.id.recipeDetailImageView);
        recipeNameDetailTextView = findViewById(R.id.recipeNameDetailTextView);
        recipeTimeTextView = findViewById(R.id.recipeTimeTextView);
        recipeDifficultyTextView = findViewById(R.id.recipeDifficultyTextView);
        recipePortionTextView = findViewById(R.id.recipePortionTextView);
        ingredientsTitleTextView = findViewById(R.id.ingredientsTitleTextView);
        etapesTitleTextView = findViewById(R.id.etapesTitleTextView);
        ingredientsListContainer = findViewById(R.id.ingredientsListContainer);
        etapesListContainer = findViewById(R.id.etapesListContainer);
        ajouterListeRecetteButton = findViewById(R.id.ajouterListeRecetteButton);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        backButtonRecipeDetail.setOnClickListener(this);
        ajouterListeRecetteButton.setOnClickListener(this);

        bottomNavigationView.setSelectedItemId(R.id.nav_recettes);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        if (recipeId != -1) {
            viewModel.getRecettes().observe(this, recettes -> {
                if (recettes != null) {
                    for (Recette r : recettes) {
                        if (r.getId() == recipeId) {
                            recipe = r;
                            populateRecipeDetails(recipe);
                            return;
                        }
                    }
                    Toast.makeText(this, "Recipe not found!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error loading!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Recipe ID not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_accueil) {
                Intent intent = new Intent(RecipeDetailPageActivity.this, AccueilActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_stock) {
                Intent intent = new Intent(RecipeDetailPageActivity.this, MonStockIngredientsActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_recettes) {
                bottomNavigationView.setSelectedItemId(R.id.nav_recettes);
                return true;

            } else if (id == R.id.nav_courses) {
                Intent intent = new Intent(RecipeDetailPageActivity.this, MaListeEpicerieActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(RecipeDetailPageActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void populateRecipeDetails(Recette recipe) {
        recipeNameDetailTextView.setText(recipe.getNom());
        recipeTimeTextView.setText(recipe.getTemps_de_cuisson() + " minutes");
        recipeDifficultyTextView.setText("Facile");
        recipePortionTextView.setText("3 portions");
        Picasso.get()
                .load(recipe.getImage_url())
                .placeholder(R.drawable.planigologo)
                .error(R.drawable.planigologo)
                .into(recipeDetailImageView);

        ingredientsListContainer.removeAllViews();
        for (String ingredientName : recipe.getIngredients()) {
            View ingredientItemView = getLayoutInflater().inflate(R.layout.list_item_recipe_detail, ingredientsListContainer, false);
            TextView ingredientNameTextView = ingredientItemView.findViewById(R.id.ingredientNameTextView);
            ingredientNameTextView.setText(ingredientName);
             ingredientsListContainer.addView(ingredientItemView);
        }

        etapesListContainer.removeAllViews();
        List<String> steps = recipe.getEtapes();

        int stepNumber = 1;
        for (String stepText : steps) {
            TextView stepTextView = new TextView(this);
            stepTextView.setText(stepNumber + ". " + stepText);
            stepTextView.setTextColor(ContextCompat.getColor(this, R.color.black_text));
            stepTextView.setTextSize(16);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 16);
            stepTextView.setLayoutParams(params);
            etapesListContainer.addView(stepTextView);
            stepNumber++;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.backButtonRecipeDetail) {
            finish();

        }  if (id == R.id.ajouterListeRecetteButton) {
             Toast.makeText(this, "TODO smt!", Toast.LENGTH_SHORT).show();
        }
    }


}