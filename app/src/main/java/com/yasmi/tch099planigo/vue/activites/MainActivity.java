package com.yasmi.tch099planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.yasmi.tch099planigo.R;
import com.yasmi.tch099planigo.VueModele.PlanigoViewModel;
import com.yasmi.tch099planigo.modeles.entitees.Client;

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

        viewModel.getConnexion().observe(this, reponseConnexion -> {
            if (reponseConnexion instanceof Client) {
                Client clientConnecte = (Client) reponseConnexion;
                Toast.makeText(MainActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AccueilActivity.class);
                startActivity(intent);
                finish();

            } else if (reponseConnexion instanceof String) {
                String messageErreur = (String) reponseConnexion;
                Toast.makeText(MainActivity.this, messageErreur, Toast.LENGTH_LONG).show();
            }
        });


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

        buttonCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}