package com.example.test_planigo.vue.activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager; // Changé ici pour LinearLayout
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.VueModele.RecetteViewModel;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;
import com.example.test_planigo.vue.adaptateurs.RecetteAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RechercheRecetteActivity extends AppCompatActivity {

    private EditText searchRecipeEditText;
    private Spinner filterTypeSpinner, filterRestrictionSpinner;
    private TextView resultatsDeRechercheTextView;
    private RecyclerView recipesRecyclerView;
    private ImageView recipeButton; // Gardé au cas où il serait utilisé plus tard
    private BottomNavigationView bottomNavigationView;
    private RecetteViewModel recetteViewModel;
    private RecetteAdapter recetteAdapter;
    // Liste locale pour stocker les recettes chargées avant filtrage par texte
    private List<RecetteAbrege> recetteAbregesLocal = new ArrayList<>();


    @SuppressLint("MissingInflatedId") // Garder si nécessaire
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_recette);

        // Initialisation des vues
        searchRecipeEditText = findViewById(R.id.searchRecipeEditText);
        filterTypeSpinner = findViewById(R.id.filterTypeSpinner);
        filterRestrictionSpinner = findViewById(R.id.filterRestrictionSpinner);
        resultatsDeRechercheTextView = findViewById(R.id.resultats_de_recherche);
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        recipeButton = findViewById(R.id.searchRecipeImageView); // ID à vérifier dans le layout
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Sélectionner l'item correct dans la barre de navigation
        bottomNavigationView.setSelectedItemId(R.id.nav_recettes);

        // Initialiser le ViewModel
        recetteViewModel = new ViewModelProvider(this).get(RecetteViewModel.class);

        // Configurer le RecyclerView
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Utiliser LinearLayoutManager
        // Initialiser l'adaptateur avec une liste vide et définir le listener de clic
        recetteAdapter = new RecetteAdapter(new ArrayList<>(), itemListe ->{
             //Configurer l'Action du clik selon la provenance
            if(getIntent().hasExtra("SLOT_KEY")){
                //Code pour le retour à la plannification
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SELECTED_RECIPE", itemListe); // Parcelable
                resultIntent.putExtra("SLOT_KEY", getIntent().getStringExtra("SLOT_KEY"));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }else{
                //code pour le la recherche pur et simple de recette: aller consulter le détails de la recette
                Intent intent = new Intent(this, RecipeDetailPageActivity.class);
                intent.putExtra("ID", itemListe.getId()); // Passer l'ID de la recette à l'activité de détail
                startActivity(intent);
            }
        });
        recipesRecyclerView.setAdapter(recetteAdapter);

        // Observer les changements dans la liste des recettes abrégées venant du ViewModel
        recetteViewModel.getListeRecetteAbrege().observe(this, recetteAbreges -> {
            // Met à jour l'UI quand les données (filtrées par type/restriction) arrivent
            if(recetteAbreges != null){
                recetteAbregesLocal = Arrays.asList(recetteAbreges); // Stocker localement
                filterAndDisplayRecettes(); // Appliquer le filtre texte et mettre à jour l'adapter
            } else {
                // Gérer le cas d'erreur (API a retourné null ou une erreur)
                recetteAbregesLocal.clear(); // Vider la liste locale
                recetteAdapter.setData(new ArrayList<>()); // Vider l'adaptateur
                resultatsDeRechercheTextView.setText(getString(R.string.resultats_de_recherche) + " (0)");
                Toast.makeText(this, "Erreur lors du chargement des recettes", Toast.LENGTH_SHORT).show();
            }
        });

        // Observer les messages d'erreur venant de l'API (via ViewModel)
        recetteViewModel.getResultatErreurAPILiveData().observe(this, reponse -> {
            if (reponse instanceof String) {
                // Afficher le message d'erreur à l'utilisateur
                Toast.makeText(this, (String) reponse, Toast.LENGTH_LONG).show();
            }
        });

        // Listener commun pour les deux Spinners (filtres)
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Recharger les données depuis l'API lorsque l'un des filtres change
                recetteViewModel.setListeRecetteAbrege(
                        filterTypeSpinner.getSelectedItem().toString(),
                        filterRestrictionSpinner.getSelectedItem().toString()
                );
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Ne rien faire */ }
        };
        filterTypeSpinner.setOnItemSelectedListener(filterListener);
        filterRestrictionSpinner.setOnItemSelectedListener(filterListener);

        // Chargement initial des données basé sur les sélections par défaut des spinners
        recetteViewModel.setListeRecetteAbrege(
                filterTypeSpinner.getSelectedItem().toString(),
                filterRestrictionSpinner.getSelectedItem().toString()
        );

        // Listener pour la barre de recherche (filtrage textuel)
        searchRecipeEditText.addTextChangedListener(new TextWatcher(){
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Ignoré */ }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtrer la liste affichée localement à chaque changement de texte
                filterAndDisplayRecettes();
            }
            @Override public void afterTextChanged(Editable s) { /* Ignoré */ }
        });

        // Gérer la navigation via la barre inférieure
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null; // Déclarer ici

            if (id == R.id.nav_recettes) {
                return false; // Indiquer non géré ou déjà sélectionné

            } else if (id == R.id.nav_stock) {
                intent = new Intent(RechercheRecetteActivity.this, MonStockIngredientsActivity.class);
            } else if (id == R.id.nav_profile) {
                intent = new Intent(RechercheRecetteActivity.this, ProfileActivity.class);
            } else if (id == R.id.nav_planner) {
                intent = new Intent(RechercheRecetteActivity.this, WeeklyPlannerActivity.class);
            } else if (id == R.id.nav_accueil) {
                intent = new Intent(RechercheRecetteActivity.this, AccueilActivity.class);
            }

            if (intent != null) {
                // Utiliser des flags pour une navigation propre
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true; // Indiquer géré
            }
            return false; // Retour par défaut
        });
    }

    /**
     * Méthode d'aide pour filtrer la liste locale (`recetteAbregesLocal`)
     * basée sur le texte de recherche et mettre à jour l'adaptateur et le compteur.
     */
    private void filterAndDisplayRecettes() {
        if (recetteAbregesLocal == null) return; // Clause de garde

        String searchText = searchRecipeEditText.getText().toString().toLowerCase().trim();
        List<RecetteAbrege> recettesFiltrees;

        if (searchText.isEmpty()) {
            // Si la recherche est vide, afficher toutes les recettes chargées (filtrées par spinners)
            recettesFiltrees = new ArrayList<>(recetteAbregesLocal);
        } else {
            // Sinon, filtrer la liste locale par nom contenant le texte de recherche
            recettesFiltrees = recetteAbregesLocal.stream()
                    .filter(recette -> recette.getNom().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
        }

        // Mettre à jour l'adaptateur avec la liste filtrée
        recetteAdapter.setData(recettesFiltrees);
        // Mettre à jour le texte indiquant le nombre de résultats
        resultatsDeRechercheTextView.setText(getString(R.string.resultats_de_recherche) + " (" + recettesFiltrees.size() + ")");
    }
}