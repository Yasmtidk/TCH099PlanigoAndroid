package com.yasmi.tch099planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yasmi.tch099planigo.R;
import com.yasmi.tch099planigo.VueModele.PlanigoViewModel;
import com.yasmi.tch099planigo.modeles.entitees.Recette;
import com.yasmi.tch099planigo.vue.adaptateurs.RecetteAdapter;

import org.json.JSONException;

public class RechercheRecetteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {

    private EditText searchRecipeEditText;
    private Spinner filterCategorySpinner, filterIngredientsSpinner, filterPriceSpinner;
    private TextView resultatsDeRechercheTextView;
    private RecyclerView recipesRecyclerView;
    private ImageView recipeButton;
    private BottomNavigationView homeButton;
    private BottomNavigationView bottomNavigationView;
    private PlanigoViewModel viewModel;
    private RecetteAdapter recetteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_recette);

        searchRecipeEditText = findViewById(R.id.searchRecipeEditText);
        filterCategorySpinner = findViewById(R.id.filterCategorySpinner);
        filterIngredientsSpinner = findViewById(R.id.filterIngredientsSpinner);
        filterPriceSpinner = findViewById(R.id.filterPriceSpinner);
        resultatsDeRechercheTextView = findViewById(R.id.resultats_de_recherche);
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        recipeButton = findViewById(R.id.searchRecipeImageView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_recettes);


        recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recetteAdapter = new RecetteAdapter(this);
        recipesRecyclerView.setAdapter(recetteAdapter);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PlanigoViewModel.class);

        loadRecipes();

        filterCategorySpinner.setOnItemSelectedListener(this);
        filterIngredientsSpinner.setOnItemSelectedListener(this);
        filterPriceSpinner.setOnItemSelectedListener(this);
        searchRecipeEditText.addTextChangedListener(this);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_recettes) {
                bottomNavigationView.setSelectedItemId(R.id.nav_recettes);
                return true;

            } else if (id == R.id.nav_stock) {
                Intent intent = new Intent(RechercheRecetteActivity.this, MonStockIngredientsActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(RechercheRecetteActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_courses) {
                Intent intent = new Intent(RechercheRecetteActivity.this, MaListeEpicerieActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_accueil) {
                Intent intent = new Intent(RechercheRecetteActivity.this, AccueilActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void loadRecipes() {
        viewModel.chargerRecettes();
        viewModel.getRecettes().observe(this, recettes -> {
            if (recettes != null) {
                recetteAdapter.setRecettes(recettes);
                resultatsDeRechercheTextView.setText(getString(R.string.resultats_de_recherche) + " (" + recettes.size() + ")");
            } else {
                resultatsDeRechercheTextView.setText(getString(R.string.resultats_de_recherche) + " (0)");
                Toast.makeText(this, "Erreur lors du chargement des recettes", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedFilter = parent.getItemAtPosition(position).toString();
        int spinnerId = parent.getId();

        if (spinnerId == R.id.filterCategorySpinner) {
            viewModel.setCategoryFilter(selectedFilter);
        } else if (spinnerId == R.id.filterIngredientsSpinner) {
            viewModel.setIngredientFilter(selectedFilter);
        } else if (spinnerId == R.id.filterPriceSpinner) {
            viewModel.setPriceFilter(selectedFilter);
        }

        loadRecipes();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        viewModel.setSearchTextFilter(s.toString());
        loadRecipes();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}