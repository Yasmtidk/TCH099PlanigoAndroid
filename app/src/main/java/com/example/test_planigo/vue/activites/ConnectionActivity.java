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

public class ConnectionActivity extends AppCompatActivity {
    private PlanigoViewModel viewModel;
    private EditText editTextNomUtilisateur;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private TextView buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PlanigoViewModel.class);

        editTextNomUtilisateur = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        viewModel.getConnexion().observe(this, reponseConnexion -> {
            if (reponseConnexion instanceof Client) {
                Client clientConnecte = (Client) reponseConnexion;
                ClientActuel.setClientConnecter(clientConnecte);
                Toast.makeText(ConnectionActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConnectionActivity.this, AccueilActivity.class);
                startActivity(intent);

            } else if (reponseConnexion instanceof String) {
                String messageErreur = (String) reponseConnexion;
                Toast.makeText(ConnectionActivity.this, messageErreur, Toast.LENGTH_LONG).show();
            }
        });


        buttonSignIn.setOnClickListener(v -> {
            String nomUtilisateur = editTextNomUtilisateur.getText().toString();
            String motDePasse = editTextPassword.getText().toString();

            if (nomUtilisateur.isEmpty() || motDePasse.isEmpty()) {
                Toast.makeText(ConnectionActivity.this, "Veuillez remplir tous les champs !", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                viewModel.postConnexion(nomUtilisateur, motDePasse);
            } catch (JSONException e) {
                Toast.makeText(ConnectionActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ConnectionActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}