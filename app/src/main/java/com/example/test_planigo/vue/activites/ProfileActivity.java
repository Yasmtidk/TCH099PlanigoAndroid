package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.PlanigoViewModel;
import com.example.test_planigo.VueModele.RecetteViewModel;
import com.example.test_planigo.VueModele.StockageViewModel;
import com.example.test_planigo.modeles.entitees.Client;
import com.example.test_planigo.modeles.singleton.ClientActuel;
import com.example.test_planigo.vue.adaptateurs.RecetteAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView settingsButton;
    private Button deconnecterButton;
    private TextView nomPrenom;
    private TextView userBioTextView;
    private ImageView profileImageView;
    private RecyclerView userRecipesRecyclerView;
    private RecetteAdapter userRecipeAdapter;
    private PlanigoViewModel viewModel;
    private RecetteViewModel recetteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Initialisation des vues
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        settingsButton = findViewById(R.id.settingsButton);
        deconnecterButton = findViewById(R.id.deconnecterButton);
        nomPrenom = findViewById(R.id.userNameTextView);
        userBioTextView = findViewById(R.id.userBioTextView);
        profileImageView = findViewById(R.id.profileImageView);
        userRecipesRecyclerView = findViewById(R.id.userRecipesRecyclerView);

        viewModel = new ViewModelProvider(this).get(PlanigoViewModel.class);
        recetteViewModel = new ViewModelProvider(this).get(RecetteViewModel.class);

        loadProfileData();
        setupRecyclerView();

        // Listeners
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        deconnecterButton.setOnClickListener(v -> {
            ClientActuel.setClientConnecter(null); // Simuler la déconnexion
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        setupBottomNavigationListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
        recetteViewModel.setListeRecetteAbrege("tout", "miennes");
    }

    // Charge et affiche les données du profil actuel
    private void loadProfileData() {

        Client verifierClient = ClientActuel.getClientConnecter();
        if (verifierClient == null) {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        viewModel.getInfoClient().observe(this, client -> {
            nomPrenom.setText(client.getPrenom() + " " + client.getNom());
            userBioTextView.setText(client.getDescription());
            userBioTextView.setVisibility(View.VISIBLE);
        });

        viewModel.chargerInfoClient(ClientActuel.getClientConnecter().getNom_utilisateur());

        /*nomPrenom.setText(client.getPrenom() + " " + client.getNom());

        // Afficher la bio ou un texte par défaut
        if (client.getDescription() != null && !client.getDescription().isEmpty()) {
            userBioTextView.setText(client.getDescription());
            userBioTextView.setVisibility(View.VISIBLE);
        } else {
            userBioTextView.setText("Aucune bio");
            userBioTextView.setVisibility(View.VISIBLE);
        }*/

        // Afficher l'image de profil
        /*if (client.getProfileImageUrl() != null && !client.getProfileImageUrl().isEmpty()) {
            Picasso.get().load(client.getProfileImageUrl()).placeholder(R.drawable.userpfp).error(R.drawable.userpfp).into(profileImageView);
        } else {
            profileImageView.setImageResource(R.drawable.userpfp);
        }*/
        profileImageView.setImageResource(R.drawable.userpfp);
    }

    private void setupRecyclerView() {

        userRecipeAdapter = new RecetteAdapter(new ArrayList<>(), recette -> {
            Intent intent = new Intent(ProfileActivity.this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", recette.getId());
            startActivity(intent);
        });

        //Modifier les données des recettes selon les recettes récupéré
        recetteViewModel.getListeRecetteAbrege().observe(this, recetteAbreges -> {
            userRecipeAdapter.setData(Arrays.asList(recetteAbreges));
        });
        recetteViewModel.setListeRecetteAbrege("tout", "miennes");

        userRecipesRecyclerView.setAdapter(userRecipeAdapter);
        userRecipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        userRecipesRecyclerView.setNestedScrollingEnabled(false);
    }


    private void setupBottomNavigationListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                return false;
            }

            Intent intent = null;
            if (id == R.id.nav_accueil) intent = new Intent(this, AccueilActivity.class);
            else if (id == R.id.nav_stock) intent = new Intent(this, MonStockIngredientsActivity.class);
            else if (id == R.id.nav_planner) intent = new Intent(this, WeeklyPlannerActivity.class);
            else if (id == R.id.nav_recettes) intent = new Intent(this, RechercheRecetteActivity.class);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}