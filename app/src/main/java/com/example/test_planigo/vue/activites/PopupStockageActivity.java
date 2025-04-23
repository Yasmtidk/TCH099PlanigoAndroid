package com.example.test_planigo.vue.activites;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.StockageViewModel;
import com.example.test_planigo.modeles.entitees.Ingredient;
import com.example.test_planigo.modeles.entitees.Produit;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class PopupStockageActivity extends AppCompatActivity {

    //private BottomNavigationView bottomNavigationView;
    private StockageViewModel stockageViewModel;
    private TextView msgErreur;


    private AutoCompleteTextView composantListeIngredient;
    private EditText txtQuantite;
    private TextInputEditText txtUniteMesureEditText;
    private Button btnAnnuler;
    private Button btnOk;
    private ImageView backButton;
    private TextView headerTitle;
    private String action;
    private ArrayList<Ingredient> listeIngredient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_produit);

        // Récupérer les composants
        txtQuantite = findViewById(R.id.quantityEditText);
        txtUniteMesureEditText = findViewById(R.id.uniteMesureEditText);
        composantListeIngredient = findViewById(R.id.listeIngredientAutoComplete);
        msgErreur = findViewById(R.id.msgErreurPopupStockage);
        btnAnnuler = findViewById(R.id.buttonCancel);
        btnOk = findViewById(R.id.buttonOk);
        backButton = findViewById(R.id.backButtonPopupStockage);
        headerTitle = findViewById(R.id.popupTitleTextView);

        // Configurer le bouton retour
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        stockageViewModel = new ViewModelProvider(this).get(StockageViewModel.class);

        // Initialiser les composants selon le type d'Action voulu
        Intent intent = getIntent();
        action = intent.getStringExtra("ACTION");

        if (action != null) {
            if(action.equals("ADD")){
                txtUniteMesureEditText.setText("Selon l'ingredient");
                headerTitle.setText(R.string.ajouter_un_element);

                // Logique pour charger les ingrédients pour l'ajout
                stockageViewModel.chargerListeIngredient();
                stockageViewModel.getListeIngredient().observe(this, ingredients -> {
                    if(ingredients != null){
                        Log.d("PopupStockage", "Ingredients received: " + ingredients.length);
                        msgErreur.setText("");
                        String[] noms = new String[ingredients.length];
                        for(int i = 0; i < noms.length; i++){
                            noms[i] = ingredients[i].getName();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_liste_ingredient_popup, R.id.txtItemIngredient, noms);
                        composantListeIngredient.setAdapter(adapter);
                        composantListeIngredient.setThreshold(1);
                        listeIngredient = new ArrayList<>(Arrays.asList(ingredients));
                        if (noms.length == 0) {
                            Log.w("PopupStockage", "Ingredients list is empty!");
                        }
                    }else{
                        Log.e("PopupStockage", "Ingredients list is NULL");
                        msgErreur.setText("Erreur lors du chargement des ingrédients de la liste");
                    }
                });

                // Listener pour mettre à jour l'unité et le type de quantité
                composantListeIngredient.setOnItemClickListener((parent, view, position, id) -> {
                    String ingredientChoisi = (String) parent.getItemAtPosition(position);
                    for(int i = 0; i < listeIngredient.size(); i++){
                        if(listeIngredient.get(i).getName().equals(ingredientChoisi)){
                            msgErreur.setText("");
                            String uniteDeMesure = listeIngredient.get(i).getUniteMesure();
                            txtUniteMesureEditText.setText(uniteDeMesure);
                            if(uniteDeMesure.equals("unité") || uniteDeMesure.equals("unite")){
                                txtQuantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                // Si la valeur actuelle contient un point, la réinitialiser à 0
                                if (txtQuantite.getText().toString().contains(".")) {
                                    txtQuantite.setText("0");
                                }
                            } else {
                                txtQuantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            }
                            return;
                        }
                    }
                });

            } else if (action.equals("Edit")) {
                headerTitle.setText(R.string.editer_un_element);
                // Récupérer les informations supplémentaires
                String ingredient = intent.getStringExtra("INGREDIENT");
                String unite = intent.getStringExtra("UNITE");
                double quantite = intent.getDoubleExtra("QUANTITE", 0.0);

                composantListeIngredient.setEnabled(false);
                composantListeIngredient.setText(ingredient);
                txtQuantite.setText(String.valueOf(quantite));
                txtUniteMesureEditText.setText(unite);

                if(unite.equals("unité") || unite.equals("unite")){
                    txtQuantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                } else {
                    txtQuantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }

            }
        } else {
            headerTitle.setText("Détails Produit");
            Log.e("PopupStockageActivity", "Action non fournie via Intent");
        }


        stockageViewModel.getResultatErreurAPILiveData().observe(this, reponse -> {
            if (reponse instanceof Boolean) {
                Boolean actionReussie = (Boolean) reponse;
                if (actionReussie) {
                    msgErreur.setText("");
                    Toast.makeText(this, "Modification sauvegardée", Toast.LENGTH_LONG).show();
                    finish();
                }

            } else if (reponse instanceof String) {
                String messageErreur = (String) reponse;
                msgErreur.setText(messageErreur);
            }
        });

        // Bouton Annuler
        btnAnnuler.setOnClickListener(v -> {
            finish();
        });

        // Bouton OK (Sauvegarder/Ajouter)
        btnOk.setOnClickListener(v -> {
            String quantiteStr = txtQuantite.getText().toString();
            String ingredientName = composantListeIngredient.getText().toString().trim();
            String unite = txtUniteMesureEditText.getText().toString();

            if (ingredientName.isEmpty()) {
                msgErreur.setText("Veuillez sélectionner ou entrer un nom d'ingrédient.");
                return;
            }
            if (unite.isEmpty() || unite.equals("Selon l'ingredient")) {
                msgErreur.setText("Unité de mesure non définie. Sélectionnez un ingrédient valide.");
                return;
            }

            double quantiteRecupere;
            try {
                quantiteRecupere = Double.parseDouble(quantiteStr);
            } catch (NumberFormatException e) {
                msgErreur.setText("La quantité entrée n'est pas valide.");
                return;
            }

            if (quantiteRecupere <= 0) {
                msgErreur.setText("La quantité doit être supérieure à zéro.");
                return;
            }

            // Logique spécifique à l'action
            if(action.equals("Edit")){
                 if(intent.getDoubleExtra("QUANTITE", 0) == quantiteRecupere){
                    finish();
                    return;
                }
                 Produit produit = new Produit(ingredientName, quantiteRecupere, unite);
                stockageViewModel.updateProduit(produit);

            } else if (action.equals("ADD")) {
                 boolean isValidIngredient = false;
                if (listeIngredient != null) {
                    for (Ingredient ing : listeIngredient) {
                        if (ing.getName().equalsIgnoreCase(ingredientName) && ing.getUniteMesure().equals(unite)) {
                            isValidIngredient = true;
                            break;
                        }
                    }
                }

                if(isValidIngredient){
                    Produit nouveauProduit = new Produit(ingredientName, quantiteRecupere, unite);
                    stockageViewModel.ajouterProduit(nouveauProduit);
                } else {
                    msgErreur.setText("L'ingrédient sélectionné n'est pas valide ou l'unité ne correspond pas.");
                }
            }
        });
    }
}