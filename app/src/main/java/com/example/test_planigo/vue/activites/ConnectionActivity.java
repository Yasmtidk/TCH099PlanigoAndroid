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
// Utiliser le ViewModel combiné (si vous en avez un) ou un ViewModel spécifique à la connexion/inscription
import com.example.test_planigo.VueModele.PlanigoViewModel;
import com.example.test_planigo.modeles.entitees.Client;
import com.example.test_planigo.modeles.singleton.ClientActuel;

import org.json.JSONException;

public class ConnectionActivity extends AppCompatActivity {
    // Utiliser PlanigoViewModel si c'est bien le ViewModel gérant la connexion
    private PlanigoViewModel viewModel;
    private EditText editTextNomUtilisateur;
    private EditText editTextPassword;
    private Button buttonSignIn;
    // Assurez-vous que l'ID buttonCreateAccount dans le layout correspond à un TextView
    private TextView buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(PlanigoViewModel.class);

        // Récupérer les vues
        editTextNomUtilisateur = findViewById(R.id.editTextEmail); // Assurez-vous que l'ID est correct
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount); // Assurez-vous que l'ID est correct

        // Observer le résultat de la tentative de connexion
        // Aller à la page d'accueil si la réponse est un client, sinon afficher un message d'erreur
        viewModel.getConnexion().observe(this, reponseConnexion -> {
            if (reponseConnexion instanceof Client) {
                Client clientConnecte = (Client) reponseConnexion;
                ClientActuel.setClientConnecter(clientConnecte); // Stocker le client connecté globalement
                Toast.makeText(ConnectionActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                // Naviguer vers l'accueil
                Intent intent = new Intent(ConnectionActivity.this, AccueilActivity.class);
                // Vider la pile d'activités pour empêcher le retour à la connexion avec le bouton back
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Fermer l'activité de connexion

            } else if (reponseConnexion instanceof String) {
                // Afficher le message d'erreur renvoyé par l'API
                String messageErreur = (String) reponseConnexion;
                Toast.makeText(ConnectionActivity.this, messageErreur, Toast.LENGTH_LONG).show();
            } else if (reponseConnexion != null) {
                // Gérer d'autres types de réponses ou erreurs si nécessaire
                Toast.makeText(ConnectionActivity.this, "Réponse inattendue du serveur.", Toast.LENGTH_SHORT).show();
            }
            // Si reponseConnexion est null, ne rien faire (attente de la réponse)
        });

        // Configurer le bouton de connexion
        // Vérifier que tous les champs sont remplis avant d'envoyer une demande de connexion
        buttonSignIn.setOnClickListener(v -> {
            String nomUtilisateur = editTextNomUtilisateur.getText().toString().trim(); // Nettoyer les espaces
            String motDePasse = editTextPassword.getText().toString(); // Ne pas trimmer le mot de passe

            // Validation simple des champs
            if (nomUtilisateur.isEmpty() || motDePasse.isEmpty()) {
                Toast.makeText(ConnectionActivity.this, "Veuillez remplir tous les champs !", Toast.LENGTH_LONG).show();
                return; // Arrêter l'exécution si les champs sont vides
            }

            // Appeler la méthode de connexion du ViewModel
            try {
                // La méthode postConnexion dans le ViewModel gère l'appel à ClientRepository
                viewModel.postConnexion(nomUtilisateur, motDePasse);
            } catch (JSONException e) {
                // Ce catch est probablement inutile si ClientRepository gère JSONException
                Toast.makeText(ConnectionActivity.this, "Erreur de formatage de la requête.", Toast.LENGTH_SHORT).show();
                e.printStackTrace(); // Logger l'erreur pendant le développement
            }
        });

        // Configurer le bouton/lien pour créer un compte
        // Aller à la page de création du compte si le composant est cliqué.
        buttonCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ConnectionActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}