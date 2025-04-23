package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.VueModele.RecetteViewModel;
import com.example.test_planigo.modeles.entitees.Produit;
import com.example.test_planigo.vue.adaptateurs.ItemIngredientAdapter;
import com.example.test_planigo.vue.adaptateurs.ItemUniqueAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.PlanigoViewModel;
import com.example.test_planigo.modeles.entitees.Recette;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;


public class RecipeDetailPageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageRecette;
    private TextView recipeNameDetailTextView, recipeTimeTextView, recipeDifficultyTextView, recipePortionTextView, recipeTypeTextView, createurRecetteTextView, descriptionRecetteTextView;
    private RecyclerView listeRestrictions, listeIngredients, listeEtapes;
    private BottomNavigationView bottomNavigationView;
    private RecetteViewModel viewModel;
    private ItemUniqueAdapter restrictionsAdapter, etapeAdapter;
    private ItemIngredientAdapter itemProduitAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_recipe_detail);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(RecetteViewModel.class);

        //Créer les composants
        imageRecette = findViewById(R.id.recipeDetailImageView);
        recipeNameDetailTextView = findViewById(R.id.recipeNameDetailTextView);
        recipeTimeTextView = findViewById(R.id.recipeTimeTextView);
        recipeDifficultyTextView = findViewById(R.id.recipeDifficultyTextView);
        recipePortionTextView = findViewById(R.id.recipePortionTextView);
        recipeTypeTextView = findViewById(R.id.recipeTypeTextView);
        createurRecetteTextView = findViewById(R.id.recipeCreatorTextView);
        listeRestrictions = findViewById(R.id.listeRestrictionRecette);
        descriptionRecetteTextView = findViewById(R.id.descriptionRecette);
        listeIngredients = findViewById(R.id.ingredientsListContainer);
        listeEtapes = findViewById(R.id.etapesListContainer);

        listeRestrictions.setLayoutManager(new LinearLayoutManager(this));
        listeIngredients.setLayoutManager(new LinearLayoutManager(this));
        listeEtapes.setLayoutManager(new LinearLayoutManager(this));

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_recettes);

        //lancer la requete de récupération de la recette si on trouve l'id, sinon on termine l'activité
        int recipeId = getIntent().getIntExtra("ID", -1);
        if (recipeId != -1) {
            viewModel.setRecetteActuel(recipeId);
        } else {
            Toast.makeText(this, "Recipe ID not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Remplir les composants selon la recette complète actuel
        viewModel.getRecetteCompleteActuel().observe(this, recette ->{

            recipeNameDetailTextView.setText(recette.getNom());
            recipeTimeTextView.setText(recette.getTemps_de_cuisson() + " minutes");
            recipeDifficultyTextView.setText(recette.getDifficulter());
            recipePortionTextView.setText(recette.getPortions() + " portions");
            recipeTypeTextView.setText(recette.getType());
            createurRecetteTextView.setText(recette.getCreateur_nom_utilisateur());
            descriptionRecetteTextView.setText(recette.getDescription());

            restrictionsAdapter = new ItemUniqueAdapter(Arrays.asList(recette.getRestrictions()));
            etapeAdapter = new ItemUniqueAdapter(Arrays.asList(recette.getEtapes()));
            itemProduitAdapter = new ItemIngredientAdapter(Arrays.asList(recette.getIngredients()));

            listeRestrictions.setAdapter(restrictionsAdapter);
            listeEtapes.setAdapter(etapeAdapter);
            listeIngredients.setAdapter(itemProduitAdapter);

            //Afficher l'image
            String base64Image = recette.getImage();

            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    // Supprimer le préfixe si présent (facultatif, selon ton backend)
                    if (base64Image.startsWith("data:")) {
                        base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                    }

                    // Décoder le Base64
                    byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);

                    // Convertir en Bitmap
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Afficher dans l'ImageView
                    imageRecette.setImageBitmap(decodedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                    // En cas d'erreur, tu peux afficher une image par défaut
                    imageRecette.setImageResource(R.drawable.planigologo);
                }
            } else {
                // Image vide ou nulle → image par défaut
                imageRecette.setImageResource(R.drawable.planigologo);
            }

        });


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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.backButtonRecipeDetail) {
            finish();

        }/*  if (id == R.id.ajouterListeRecetteButton) {
             Toast.makeText(this, "TODO smt!", Toast.LENGTH_SHORT).show();
        }*/
    }


}