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
// Utiliser le ViewModel combiné ou un ViewModel spécifique
import com.example.test_planigo.VueModele.PlanigoViewModel;
import com.example.test_planigo.modeles.entitees.Client;

import org.json.JSONException;


public class SignupActivity extends AppCompatActivity {

    // Utiliser PlanigoViewModel si c'est le bon
    private PlanigoViewModel viewModel;
    private EditText editTextNomUtilisateur;
    private EditText editTextPasswordSignup;
    private EditText editTextConfirmPassword;
    private EditText editTextPrenom;
    private EditText editTextNom;
    private Button buttonSignUp;
    private TextView buttonSignInFromSignUp; // Lien pour retourner à la connexion
    private TextView msgErreur; // TextView pour afficher les erreurs

    @SuppressLint("MissingInflatedId") // Garder si les IDs sont corrects
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PlanigoViewModel.class);

        // Récupérer les vues
        editTextNomUtilisateur = findViewById(R.id.editTextNomUtilisateur);
        editTextPasswordSignup = findViewById(R.id.editTextPasswordSignup);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextNom = findViewById(R.id.editTextNom);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignInFromSignUp = findViewById(R.id.buttonSignInFromSignUp);
        msgErreur = findViewById(R.id.msgErreurSigup);

        // Observer le résultat de la tentative d'inscription
        viewModel.getConnexion().observe(this, reponseInscription -> {
            if (reponseInscription instanceof Boolean) {
                Boolean inscriptionReussie = (Boolean) reponseInscription;
                if (inscriptionReussie) {
                    // Inscription réussie
                    Toast.makeText(SignupActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité d'inscription après succès
                } else {
                    // Gérer le cas où l'API retourne false explicitement (si c'est possible)
                    msgErreur.setText("L'inscription a échoué pour une raison inconnue.");
                }
            } else if (reponseInscription instanceof String) {
                // Afficher le message d'erreur renvoyé par l'API
                String messageErreurApi = (String) reponseInscription;
                msgErreur.setText(messageErreurApi);
            }
            // Si null, on attend encore la réponse
        });

        // Configurer le bouton d'inscription
        // Vérifier si tous les champs sont remplis, puis envoyer la requête d'inscription du nouveau client
        buttonSignUp.setOnClickListener(v -> {
            // Récupérer et nettoyer les entrées utilisateur
            String nomUtilisateur = editTextNomUtilisateur.getText().toString().trim();
            String motDePasse = editTextPasswordSignup.getText().toString(); // Ne pas trimmer mdp
            String confirmerMotDePasse = editTextConfirmPassword.getText().toString();
            String prenom = editTextPrenom.getText().toString().trim();
            String nom = editTextNom.getText().toString().trim();
            msgErreur.setText(""); // Effacer les erreurs précédentes


            // Validation des champs
            if (nomUtilisateur.isEmpty() || motDePasse.isEmpty() || confirmerMotDePasse.isEmpty() || prenom.isEmpty() || nom.isEmpty()) {
                msgErreur.setText("Veuillez remplir tous les champs !");
                return; // Arrêter si un champ est vide
            }

            // Vérifier si les mots de passe correspondent
            if (!motDePasse.equals(confirmerMotDePasse)) {
                msgErreur.setText("Les mots de passe ne correspondent pas.");
                return; // Arrêter si les mots de passe diffèrent
            }

            // Créer l'objet Client avec les informations
            Client client = new Client(nom, prenom, nomUtilisateur, motDePasse);

            // Appeler la méthode d'inscription du ViewModel
            try {
                // La méthode postClient dans le ViewModel gère l'appel au Repository
                viewModel.postClient(client);
            } catch (JSONException e) {
                // Ce catch est probablement inutile si postClient ne lève pas directement JSONException
                msgErreur.setText("Erreur lors de la préparation de l'inscription.");
                e.printStackTrace(); // Logger pendant le développement
            }
        });

        // Configurer le lien pour retourner à la page de connexion
        buttonSignInFromSignUp.setOnClickListener(v -> {
            // Retourner à l'activité de connexion
            Intent intent = new Intent(SignupActivity.this, ConnectionActivity.class);
            // Utiliser des flags pour s'assurer qu'on ne crée pas plusieurs instances de connexion
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // Fermer l'activité d'inscription
        });
    }
}