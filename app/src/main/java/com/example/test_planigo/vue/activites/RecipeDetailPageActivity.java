package com.example.test_planigo.vue.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.test_planigo.vue.adaptateurs.ItemUniqueAdapter; // Adapter pour restrictions
import com.example.test_planigo.vue.adaptateurs.ItemStepAdapter;   // Adapter pour étapes
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;
// Picasso n'est plus utilisé car l'image est en Base64

import java.util.ArrayList; // Pour initialiser les adapters si besoin
import java.util.Arrays;
import java.util.Locale;

public class RecipeDetailPageActivity extends AppCompatActivity implements View.OnClickListener {

    // Vues de l'interface
    private ImageView imageRecette;
    private TextView recipeNameDetailTextView, recipeTimeTextView, recipeDifficultyTextView, recipePortionTextView, recipeTypeTextView, createurRecetteTextView, descriptionRecetteTextView;
    private RecyclerView listeRestrictions, listeEtapes; // RecyclerView pour listes
    private LinearLayout ingredientsListLayout; // LinearLayout pour afficher les ingrédients dynamiquement
    private BottomNavigationView bottomNavigationView;
    private ImageView backButton;
    private Button ajouterListeRecetteButton; // Bouton pour sauvegarder la recette

    // Données et logique
    private RecetteComplete currentRecipe; // La recette actuellement affichée
    private RecetteViewModel viewModel; // ViewModel pour obtenir les données
    private ItemUniqueAdapter restrictionsAdapter; // Adapter pour la liste des restrictions
    private ItemStepAdapter etapeAdapter; // Adapter pour la liste des étapes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_recipe_detail); // Layout pour l'affichage détaillé

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(RecetteViewModel.class);

        // Récupérer les références des vues
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

        // Configurer les écouteurs de clics
        if (backButton != null) {
            backButton.setOnClickListener(this);
        }
        if (ajouterListeRecetteButton != null) {
            ajouterListeRecetteButton.setOnClickListener(this);
        }

        // Configurer les RecyclerViews
        listeRestrictions.setLayoutManager(new LinearLayoutManager(this));
        listeEtapes.setLayoutManager(new LinearLayoutManager(this));
        // Désactiver le scrolling imbriqué si les RecyclerViews sont dans un ScrollView
        listeRestrictions.setNestedScrollingEnabled(false);
        listeEtapes.setNestedScrollingEnabled(false);

        // Sélectionner l'item correct dans la barre de navigation
        bottomNavigationView.setSelectedItemId(R.id.nav_recettes);

        // Récupérer l'ID de la recette passé via l'Intent
        int recipeId = getIntent().getIntExtra("ID", -1); // -1 comme valeur par défaut si non trouvé
        if (recipeId != -1) {
            Log.d("RecipeDetail", "Demande des détails pour la recette ID: " + recipeId);
            // Demander au ViewModel de charger les détails de cette recette
            viewModel.setRecetteActuel(recipeId);
        } else {
            // Gérer le cas où l'ID n'a pas été passé correctement
            Log.e("RecipeDetail", "Aucun ID de recette valide trouvé dans l'Intent.");
            Toast.makeText(this, "Erreur: ID de recette introuvable!", Toast.LENGTH_SHORT).show();
            finish(); // Fermer l'activité si l'ID est manquant
            return;
        }

        // Observer les changements de la recette complète chargée par le ViewModel
        viewModel.getRecetteCompleteActuel().observe(this, recette -> {
            // Mettre à jour l'UI lorsque les données de la recette sont disponibles
            if (recette == null) {
                Log.e("RecipeDetail", "Objet recette reçu du ViewModel est null pour ID: " + recipeId);
                Toast.makeText(this, "Erreur: Impossible de charger les détails de la recette.", Toast.LENGTH_SHORT).show();
                // Optionnellement, on pourrait fermer l'activité : finish();
                return;
            }
            currentRecipe = recette; // Stocker la recette actuelle
            Log.d("RecipeDetail", "Affichage des détails pour : " + recette.getNom());

            // Remplir les champs de texte simples
            recipeNameDetailTextView.setText(recette.getNom());
            recipeTimeTextView.setText(String.format(Locale.getDefault(),"%d minutes", recette.getTemps_de_cuisson()));
            recipeDifficultyTextView.setText(recette.getDifficulter());
            // Assumer que 'portions' est une chaîne dans RecetteComplete, sinon convertir
            recipePortionTextView.setText(String.format("%s portions", recette.getPortions()));
            recipeTypeTextView.setText(recette.getType());
            createurRecetteTextView.setText(recette.getCreateur_nom_utilisateur());
            descriptionRecetteTextView.setText(recette.getDescription());

            // --- Logique de chargement d'image depuis Base64 ---
            String base64Image = recette.getImage();
            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    // Supprimer le préfixe "data:image/..." si présent
                    if (base64Image.startsWith("data:")) {
                        base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                    }
                    // Décoder la chaîne Base64 en tableau de bytes
                    byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    // Convertir le tableau de bytes en Bitmap
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    // Afficher le Bitmap dans l'ImageView
                    imageRecette.setImageBitmap(decodedImage);
                } catch (IllegalArgumentException e) {
                    // Erreur si la chaîne Base64 n'est pas valide
                    Log.e("RecipeDetail", "Erreur de décodage Base64: " + e.getMessage());
                    imageRecette.setImageResource(R.drawable.planigologo); // Image de repli
                } catch (Exception e) { // Capturer d'autres erreurs potentielles (ex: OutOfMemory)
                    Log.e("RecipeDetail", "Erreur lors du traitement de l'image: " + e.getMessage());
                    imageRecette.setImageResource(R.drawable.planigologo); // Image de repli
                }
            } else {
                // Si l'image est vide ou nulle, afficher l'image par défaut
                imageRecette.setImageResource(R.drawable.planigologo);
            }
            // --- Fin de la logique de chargement d'image ---

            // Mettre à jour l'adapter des restrictions
            restrictionsAdapter = new ItemUniqueAdapter(recette.getRestrictions() != null ? Arrays.asList(recette.getRestrictions()) : new ArrayList<>());
            listeRestrictions.setAdapter(restrictionsAdapter);

            // Remplir dynamiquement la liste des ingrédients
            remplirListeIngredients(recette.getIngredients());

            // Mettre à jour l'adapter des étapes
            etapeAdapter = new ItemStepAdapter(recette.getEtapes() != null ? Arrays.asList(recette.getEtapes()) : new ArrayList<>());
            listeEtapes.setAdapter(etapeAdapter);
        });

        // Observer les erreurs potentielles venant de l'API
        viewModel.getResultatErreurAPILiveData().observe(this, error -> {
            if (error instanceof String) {
                Log.e("RecipeDetail", "Erreur API signalée: " + error);
                Toast.makeText(this, "Erreur API: " + error, Toast.LENGTH_LONG).show();
            }
        });

        // Gérer la navigation via la barre inférieure
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_recettes) {
                return false; // Déjà sur cet écran

            } else if (id == R.id.nav_accueil) {
                intent = new Intent(RecipeDetailPageActivity.this, AccueilActivity.class);
            } else if (id == R.id.nav_stock) {
                intent = new Intent(RecipeDetailPageActivity.this, MonStockIngredientsActivity.class);
            } else if (id == R.id.nav_planner) {
                intent = new Intent(RecipeDetailPageActivity.this, WeeklyPlannerActivity.class);
            } else if (id == R.id.nav_profile) {
                intent = new Intent(RecipeDetailPageActivity.this, ProfileActivity.class);
            }

            if (intent != null) {
                // Utiliser des flags pour une navigation propre
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true; // Indiquer que l'événement a été géré
            }
            return false; // Retour par défaut
        });
    }

    /**
     * Remplit le LinearLayout `ingredientsListLayout` avec les ingrédients fournis.
     * Crée une vue pour chaque ingrédient.
     * @param ingredients Tableau des produits (ingrédients avec quantité/unité) de la recette.
     */
    private void remplirListeIngredients(Produit[] ingredients) {
        if (ingredientsListLayout == null) return; // Sécurité

        ingredientsListLayout.removeAllViews(); // Vider les anciennes vues

        if (ingredients == null || ingredients.length == 0) {
            // Afficher un message si pas d'ingrédients
            TextView noIngredientsText = new TextView(this);
            noIngredientsText.setText("Aucun ingrédient listé.");
            // Ajouter un peu de padding pour l'esthétique
            noIngredientsText.setPadding(16, 8, 16, 8);
            ingredientsListLayout.addView(noIngredientsText);
            return;
        }

        // Obtenir un LayoutInflater pour créer des vues à partir du layout XML
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Boucler sur chaque ingrédient et créer sa vue
        for (Produit produit : ingredients) {
            // Créer la vue à partir du layout 'list_item_recipe_detail.xml'
            // Le 'false' à la fin indique de ne pas attacher la vue immédiatement au parent (LinearLayout)
            View itemView = inflater.inflate(R.layout.list_item_recipe_detail, ingredientsListLayout, false);

            // Récupérer les TextViews dans la vue de l'item créé
            TextView nameTextView = itemView.findViewById(R.id.ingredientNameTextView);
            TextView quantityTextView = itemView.findViewById(R.id.ingredientQuantityTextView);

            // Définir le texte pour le nom et la quantité/unité
            nameTextView.setText(produit.getName());
            // Formater la quantité pour enlever les ".0" inutiles et ajouter l'unité
            String quantityString = String.format(Locale.getDefault(), "%.2f", produit.getQuantity())
                    .replaceAll("\\.?0*$", ""); // Enlève .00 ou .X0
            quantityTextView.setText(String.format("%s %s", quantityString, produit.getUnit()));

            // Ajouter la vue de l'ingrédient au LinearLayout parent
            ingredientsListLayout.addView(itemView);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.backButtonRecipeDetail) {
            // Si le bouton retour est cliqué, fermer l'activité
            finish();
        } else if (id == R.id.ajouterListeRecetteButton) {
            // Si le bouton "Ajouter à mes recettes" est cliqué
            if (currentRecipe != null) {
                // Créer un RecetteAbrege à partir de RecetteComplete pour l'ajout (simulation)
                RecetteAbrege recetteAbrege = new RecetteAbrege(
                        currentRecipe.getId(),
                        currentRecipe.getNom(),
                        currentRecipe.getTemps_de_cuisson(),
                        currentRecipe.getType(),
                        currentRecipe.getImage() // Passer l'image base64
                );
                // Ajouter à la liste statique (simulation de sauvegarde)
                // TODO: Remplacer par un appel réel au ViewModel/Repository pour sauvegarder
                MaListeRecettesActivity.addRecipeToStaticList(recetteAbrege);
                Toast.makeText(this, "'" + currentRecipe.getNom() + "' ajoutée à vos recettes (simulé)!", Toast.LENGTH_SHORT).show();
            } else {
                // Gérer le cas où la recette n'a pas encore été chargée
                Toast.makeText(this, "Erreur: Impossible d'ajouter la recette.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}