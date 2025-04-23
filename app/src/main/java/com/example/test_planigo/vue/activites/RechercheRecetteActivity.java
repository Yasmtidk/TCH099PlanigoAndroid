package com.example.test_planigo.vue.activites;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.VueModele.RecetteViewModel;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;
import com.example.test_planigo.vue.adaptateurs.RecetteAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RechercheRecetteActivity extends AppCompatActivity {

    private EditText searchRecipeEditText;
    private Spinner filterTypeSpinner, filterRestrictionSpinner;
    private TextView resultatsDeRechercheTextView;
    private RecyclerView recipesRecyclerView;
    private ImageView recipeButton;
    private BottomNavigationView homeButton;
    private BottomNavigationView bottomNavigationView;
    private RecetteViewModel recetteViewModel;
    private RecetteAdapter recetteAdapter;
    private List<RecetteAbrege> recetteAbregesLocal;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_recette);

        searchRecipeEditText = findViewById(R.id.searchRecipeEditText);
        filterTypeSpinner = findViewById(R.id.filterTypeSpinner);
        filterRestrictionSpinner = findViewById(R.id.filterRestrictionSpinner);
        resultatsDeRechercheTextView = findViewById(R.id.resultats_de_recherche);
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        recipeButton = findViewById(R.id.searchRecipeImageView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_recettes);

        recetteViewModel = new ViewModelProvider(this).get(RecetteViewModel.class);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Créer l'adapter de la liste et faire en sorte qu'elle lance une nouvelle activité lorsqu'on click sur l'un des items de la liste
        recetteAdapter = new RecetteAdapter(Arrays.asList(), itemListe ->{
            Intent intent = new Intent(this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", itemListe.getId());
            Log.d("DEBUG_TAG", "id envoyer : " + itemListe.getId());
            startActivity(intent);
        });
        recipesRecyclerView.setAdapter(recetteAdapter);

        //Notifier l'adapter lorsque la liste change pour que tout les items soit proprement afficher
        recetteViewModel.getListeRecetteAbrege().observe(this, recetteAbreges -> {
            if(recetteAbreges != null){
                recetteAbregesLocal = Arrays.asList(recetteAbreges);
                //Trier la sélection selon le nom de la recette
                List<RecetteAbrege> recettes = recetteAbregesLocal.stream()
                        .filter(recette -> recette.getNom().toLowerCase().contains(searchRecipeEditText.getText().toString().toLowerCase()))
                        .collect(Collectors.toList());
                recetteAdapter.setData(recetteAbregesLocal);
                resultatsDeRechercheTextView.setText(getString(R.string.resultats_de_recherche) + " (" + recetteAbregesLocal.size() + ")");
            } else {
                resultatsDeRechercheTextView.setText(getString(R.string.resultats_de_recherche) + " (0)");
                Toast.makeText(this, "Erreur lors du chargement des recettes", Toast.LENGTH_SHORT).show();
            }
        });

        //Afficher un message d'erreur lorsqu'il survient du coté de l'API
        recetteViewModel.getResultatErreurAPILiveData().observe(this, reponse -> {
            if (reponse instanceof String) {
                Toast.makeText(this, reponse + "", Toast.LENGTH_LONG).show();
            }
        });

        //Notifier le view model pour aller aller chercher la liste des recettes en fonction des nouveaux critères (Type et Restriction)
        filterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                recetteViewModel.setListeRecetteAbrege(filterTypeSpinner.getSelectedItem().toString(), filterRestrictionSpinner.getSelectedItem().toString());
            }
        });
        filterRestrictionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                recetteViewModel.setListeRecetteAbrege(filterTypeSpinner.getSelectedItem().toString(), filterRestrictionSpinner.getSelectedItem().toString());
            }
        });

        //Lorsque le texte change on tri les recettes en fonction du texte de la barre de recherche
        searchRecipeEditText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<RecetteAbrege> recettes = recetteAbregesLocal.stream()
                        .filter(recette -> recette.getNom().toLowerCase().contains(searchRecipeEditText.getText().toString().toLowerCase()))
                        .collect(Collectors.toList());
                recetteAdapter.setData(recettes);
                resultatsDeRechercheTextView.setText(getString(R.string.resultats_de_recherche) + " (" + recettes.size() + ")");
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
}