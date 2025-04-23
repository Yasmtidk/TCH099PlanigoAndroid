package com.example.test_planigo.vue.activites;

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
import com.example.test_planigo.modeles.singleton.ClientActuel;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private PlanigoViewModel viewModel;
    private EditText editTextNomUtilisateur;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private TextView buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PlanigoViewModel.class);

        editTextNomUtilisateur = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        //Aller à la page d'acceuil si la réponse est un client, sinon affiche un message d'erreur
        viewModel.getConnexion().observe(this, reponseConnexion -> {
            if (reponseConnexion instanceof Client) {
                Client clientConnecte = (Client) reponseConnexion;
                ClientActuel.setClientConnecter(clientConnecte);
                Toast.makeText(MainActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AccueilActivity.class);
                startActivity(intent);

            } else if (reponseConnexion instanceof String) {
                String messageErreur = (String) reponseConnexion;
                Toast.makeText(MainActivity.this, messageErreur, Toast.LENGTH_LONG).show();
            }
        });


        //Vérifier que tout les champs sont remplie avant d'envoyer une demande de connaction
        buttonSignIn.setOnClickListener(v -> {
            String nomUtilisateur = editTextNomUtilisateur.getText().toString();
            String motDePasse = editTextPassword.getText().toString();

            if (nomUtilisateur.isEmpty() || motDePasse.isEmpty()) {
                Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs !", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                viewModel.postConnexion(nomUtilisateur, motDePasse);
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });

        //Aller à la page de création du compte si composant cliquer.
        buttonCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}