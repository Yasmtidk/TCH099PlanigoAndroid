package com.example.test_planigo.vue.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.PlanigoViewModel;
import com.example.test_planigo.modeles.entitees.Client;
import com.example.test_planigo.modeles.singleton.ClientActuel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView backButton;
    private ImageView profileImageView;
    private ImageView editNomButton, editPrenomButton, editAgeButton, editGenreButton, editDescriptionButton;
    private TextView nomTextView, prenomTextView, ageTextView, genreTextView, descriptionTextView;
    private Button saveButton;

    private PlanigoViewModel viewModel; // TODO: Remplacer par ProfileViewModel si créé
    private Client currentClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        viewModel = new ViewModelProvider(this).get(PlanigoViewModel.class);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        backButton = findViewById(R.id.backButton);
        profileImageView = findViewById(R.id.editProfileImageView);
        nomTextView = findViewById(R.id.nomTextView);
        editNomButton = findViewById(R.id.editNomButton);
        prenomTextView = findViewById(R.id.prenomTextView);
        editPrenomButton = findViewById(R.id.editPrenomButton);
        ageTextView = findViewById(R.id.ageTextView);
        editAgeButton = findViewById(R.id.editAgeButton);
        genreTextView = findViewById(R.id.genreTextView);
        editGenreButton = findViewById(R.id.editGenreButton);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        editDescriptionButton = findViewById(R.id.editDescriptionButton);
        saveButton = findViewById(R.id.saveButton);

        loadInitialData();
        setupNavigation();
        setupEditListeners();
        setupSaveListener();
    }

    private void loadInitialData() {
        currentClient = ClientActuel.getClientConnecter();
        if (currentClient == null) {
            Toast.makeText(this, "Erreur: Utilisateur non connecté.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        nomTextView.setText(currentClient.getNom());
        prenomTextView.setText(currentClient.getPrenom());
        //ageTextView.setText(currentClient.getAge() != null ? String.valueOf(currentClient.getAge()) : "Non défini");
        //genreTextView.setText(currentClient.getGenre() != null ? currentClient.getGenre() : "Non défini");
        descriptionTextView.setText(currentClient.getDescription() != null ? currentClient.getDescription() : "Aucune description");

        // Afficher image existante
        /*if (currentClient.getProfileImageUrl() != null && !currentClient.getProfileImageUrl().isEmpty()) {
            Picasso.get().load(currentClient.getProfileImageUrl()).placeholder(R.drawable.userpfp).error(R.drawable.userpfp).into(profileImageView);
        } else {
            profileImageView.setImageResource(R.drawable.userpfp);
        }*/profileImageView.setImageResource(R.drawable.userpfp);
    }

    private void setupNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) return false;
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
        backButton.setOnClickListener(v -> finish());
    }

    private void setupEditListeners() {
        editNomButton.setOnClickListener(v -> showEditDialog("Nom", nomTextView, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS));
        editPrenomButton.setOnClickListener(v -> showEditDialog("Prénom", prenomTextView, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS));
        editAgeButton.setOnClickListener(v -> showEditDialog("Âge", ageTextView, InputType.TYPE_CLASS_NUMBER));
        editGenreButton.setOnClickListener(v -> showGenreSelectionDialog());
        editDescriptionButton.setOnClickListener(v -> showEditDialog("Description (Bio)", descriptionTextView, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES));
    }

    private void showEditDialog(String title, TextView targetTextView, int inputType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier " + title);
        final EditText input = new EditText(this);
        input.setInputType(inputType);
        String currentValue = targetTextView.getText().toString();
        if (!currentValue.equals("Non défini") && !currentValue.equals("Aucune description")) {
            input.setText(currentValue);
        }
        input.setSelection(input.getText().length());
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String newValue = input.getText().toString().trim();
            if (newValue.isEmpty() && (targetTextView == ageTextView || targetTextView == genreTextView || targetTextView == descriptionTextView)) {
                targetTextView.setText(targetTextView == descriptionTextView ? "Aucune description" : "Non défini");
            } else if (!newValue.isEmpty()) {
                targetTextView.setText(newValue);
            } else if (targetTextView == nomTextView || targetTextView == prenomTextView) {
                Toast.makeText(this, title + " ne peut pas être vide", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showGenreSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir le Genre");
        final String[] genres = {"Homme", "Femme", "Autre", "Non spécifié"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres);
        final Spinner spinner = new Spinner(this, Spinner.MODE_DROPDOWN);
        spinner.setAdapter(adapter);
        String currentGenre = genreTextView.getText().toString();
        if (!currentGenre.equals("Non défini")) {
            for (int i = 0; i < genres.length; i++) {
                if (genres[i].equals(currentGenre)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } else {
            spinner.setSelection(genres.length - 1);
        }
        builder.setView(spinner);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String selectedGenre = (String) spinner.getSelectedItem();
            genreTextView.setText(selectedGenre.equals("Non spécifié") ? "Non défini" : selectedGenre);
        });
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void setupSaveListener() {
        saveButton.setOnClickListener(v -> {
            String finalNom = nomTextView.getText().toString();
            String finalPrenom = prenomTextView.getText().toString();
            String ageStr = ageTextView.getText().toString();
            String finalGenre = genreTextView.getText().toString();
            String finalDescription = descriptionTextView.getText().toString();

            if (finalNom.isEmpty() || finalPrenom.isEmpty()) {
                Toast.makeText(this, "Le nom et le prénom sont obligatoires.", Toast.LENGTH_SHORT).show();
                return;
            }

            Client updatedClient = new Client();
            updatedClient.setNom_utilisateur(currentClient.getNom_utilisateur());
            updatedClient.setMot_de_passe(currentClient.getMot_de_passe());
            updatedClient.setNom(finalNom);
            updatedClient.setPrenom(finalPrenom);

            /*if (!ageStr.equals("Non défini")) {
                try {
                    updatedClient.setAge(Integer.parseInt(ageStr));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Âge invalide.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                updatedClient.setAge(null);
            }

            if (!finalGenre.equals("Non défini")) {
                updatedClient.setGenre(finalGenre);
            } else {
                updatedClient.setGenre(null);
            }*/

            if (!finalDescription.equals("Aucune description")) {
                updatedClient.setDescription(finalDescription);
            } else {
                updatedClient.setDescription(null);
            }

            //updatedClient.setProfileImageUrl(currentClient.getProfileImageUrl());

            Log.d("EditProfile", "Préparation sauvegarde: " + updatedClient.getNom() + ", Age: " );

            Toast.makeText(this, "TODO: Appel API pour sauvegarder le profil", Toast.LENGTH_LONG).show();
            // TODO: Remplacer la simulation par un appel réel au ViewModel/Repository
            /*
             viewModel.updateProfile(updatedClient).observe(this, success -> { ... });
            */

            // Simulation de la sauvegarde en mettant à jour le singleton
            ClientActuel.setClientConnecter(updatedClient);
            Toast.makeText(this, "Profil mis à jour (simulation)!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}