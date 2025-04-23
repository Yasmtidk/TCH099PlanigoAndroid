package com.example.test_planigo.vue.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.PlanigoViewModel;
import com.example.test_planigo.modeles.entitees.Client;

import org.json.JSONException;


public class SignupActivity extends AppCompatActivity {

    private PlanigoViewModel viewModel;
    private EditText editTextNomUtilisateur;
    private EditText editTextPasswordSignup;
    private EditText editTextConfirmPassword;
    private EditText editTextPrenom;
    private EditText editTextNom;
    private Button buttonSignUp;
    private TextView buttonSignInFromSignUp;
    private TextView msgErreur;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PlanigoViewModel.class);

        editTextNomUtilisateur = findViewById(R.id.editTextNomUtilisateur);
        editTextPasswordSignup = findViewById(R.id.editTextPasswordSignup);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextNom = findViewById(R.id.editTextNom);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignInFromSignUp = findViewById(R.id.buttonSignInFromSignUp);
        msgErreur = findViewById(R.id.msgErreurSigup);

        viewModel.getConnexion().observe(this, reponseInscription -> {
            if (reponseInscription instanceof Boolean) {
                Boolean inscriptionReussie = (Boolean) reponseInscription;
                if (inscriptionReussie) {
                    Toast.makeText(SignupActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (reponseInscription instanceof String) {
                String messageErreur = (String) reponseInscription;
                msgErreur.setText(messageErreur);
                //Toast.makeText(SignupActivity.this, messageErreur, Toast.LENGTH_LONG).show();
            }
        });


        buttonSignUp.setOnClickListener(v -> {
            String nomUtilisateur = editTextNomUtilisateur.getText().toString();
            String motDePasse = editTextPasswordSignup.getText().toString();
            String confirmerMotDePasse = editTextConfirmPassword.getText().toString();
            String prenom = editTextPrenom.getText().toString();
            String nom = editTextNom.getText().toString();
            msgErreur.setText("");


            if (nomUtilisateur.isEmpty() || motDePasse.isEmpty() || confirmerMotDePasse.isEmpty() || prenom.isEmpty() || nom.isEmpty()) {
                //Toast.makeText(SignupActivity.this, "Veuillez remplir tous les champs !", Toast.LENGTH_LONG).show();
                msgErreur.setText("Veuillez remplir tous les champs !");
                return;
            }

            if (!motDePasse.equals(confirmerMotDePasse)) {
                //Toast.makeText(SignupActivity.this, "Les mots de passe ne correspondent pas.", Toast.LENGTH_LONG).show();
                msgErreur.setText("Les mots de passe ne correspondent pas.");
                return;
            }

            Client client = new Client();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setNom_utilisateur(nomUtilisateur);
            client.setMot_de_passe(motDePasse);

            try {
                viewModel.postClient(client);
            } catch (JSONException e) {
                Toast.makeText(SignupActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSignInFromSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, ConnectionActivity.class);
            startActivity(intent);
        });
    }
}