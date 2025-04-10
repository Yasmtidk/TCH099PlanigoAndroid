package com.example.test_planigo.vue.activites;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView txtUniteMesure;
    private Button btnAnnuler;
    private Button btnOk;

    private String action;
    private ArrayList<Ingredient> listeIngredient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_produit);

        //récupérer les composants
        txtQuantite = findViewById(R.id.quantityEditText);
        txtUniteMesure = findViewById(R.id.uniteMesureTextView);
        composantListeIngredient = (AutoCompleteTextView) findViewById(R.id.listeIngredientAutoComplete);
        msgErreur = findViewById(R.id.msgErreurPopupStockage);
        btnAnnuler = findViewById(R.id.buttonCancel);
        btnOk = findViewById(R.id.buttonOk);

        //initialiser les composants selon le type d'Action voulu
        Intent intent = getIntent();
        action = intent.getStringExtra("ACTION");

        if(action.equals("ADD")){
            txtUniteMesure.setText("Selon l'ingredient");

        } else if (action.equals("Edit")) {

            //Récuprérer les informations supplémentaires
            String ingredient = intent.getStringExtra("INGREDIENT");
            String unite = intent.getStringExtra("UNITE");
            double quantite = intent.getDoubleExtra("QUANTITE", 0.0);

            composantListeIngredient.setEnabled(false);
            composantListeIngredient.setText(ingredient);
            txtQuantite.setText(quantite + "");
            txtUniteMesure.setText(unite);
        }

        stockageViewModel = new ViewModelProvider(this).get(StockageViewModel.class);

        //configurer les actions selon le type d'Action
        if(action.equals("ADD")){
            //Récupéré la liste initials des ingrédients et intitialiser la liste du composantpermettre la modifications intéractif de la liste des ingrédients
            stockageViewModel.chargerListeIngredient();
            stockageViewModel.getListeIngredient().observe(this, ingredients -> {
                if(ingredients != null){
                    msgErreur.setText("");
                    String[] noms = new String[ingredients.length];
                    for(int i = 0; i< noms.length; i++){
                        noms[i] = ingredients[i].getName();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_liste_ingredient_popup, noms);
                    composantListeIngredient.setAdapter(adapter);
                    composantListeIngredient.setThreshold(1);
                    listeIngredient = new ArrayList<>(Arrays.asList(ingredients));
                }else{
                    msgErreur.setText("Erreur lors du chargement des ingrédients de la liste");
                }
            });

            //Modifier l'unité de mesure selon l'ingrédient et le type de la quantite (entier ou décimaux)
            composantListeIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String ingredientChoisi = (String) parent.getItemAtPosition(position);
                    for(int i = 0; i < listeIngredient.size(); i++){
                        if(listeIngredient.get(i).getName().equals(ingredientChoisi)){
                            msgErreur.setText("");
                            String uniteDeMesure = listeIngredient.get(i).getUniteMesure();
                            txtUniteMesure.setText(uniteDeMesure);

                            if(uniteDeMesure.equals("unite")){
                                txtQuantite.setText("0");
                                txtQuantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                            }else{
                                txtQuantite.setText("0");
                                txtQuantite.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            }
                            return;
                        }
                    }
                }
            });
        }

        //Afficher l'erreur ou retourner à la page précédanteModifier selon le succès à sauvegarder les modifications
        stockageViewModel.getResultatErreurAPILiveData().observe(this, reponse -> {
            if (reponse instanceof Boolean) {
                Boolean ajoutReussi = (Boolean) reponse;
                if (ajoutReussi) {
                    //modifier la zone de succès
                    msgErreur.setText("");
                    Toast.makeText(this, "Modification sauvegarder", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else if (reponse instanceof String) {
                String messageErreur = (String) reponse;
                msgErreur.setText(messageErreur);
            }
        });

        //Retourner à la page précédente si l'utilisateur annule sa requête
        btnAnnuler.setOnClickListener(v -> {
            finish();
        });

        //Lancer la requete de modification/création du produit si on respecte les critères
        btnOk.setOnClickListener(v -> {

            double quantiteRecupere = Double.parseDouble(txtQuantite.getText().toString());
            if (quantiteRecupere <= 0) {
                msgErreur.setText("La quantite n'est pas valide");
                return;
            }

            if(action.equals("Edit")){
                if(intent.getDoubleExtra("QUANTITE", 0) == quantiteRecupere){
                    finish();
                }
                //lancer la requête de modification de l'objet sur la base de donné
                Produit produit = new Produit(composantListeIngredient.getText().toString(), quantiteRecupere, txtUniteMesure.getText().toString());
                stockageViewModel.updateProduit(produit);
            } else if (action.equals("ADD")) {

                //vérifier que l'ingrédient est dans la liste avant d'essayer de l'ajouter
                Ingredient nouveau = new Ingredient(composantListeIngredient.getText().toString().trim(), txtUniteMesure.getText().toString());
                if(listeIngredient.contains(nouveau)){
                    Produit nouveauProduit = new Produit(nouveau.getName(), Double.parseDouble(txtQuantite.getText().toString()), nouveau.getUniteMesure());
                    stockageViewModel.ajouterProduit(nouveauProduit);
                }else{
                    msgErreur.setText("Le nom de l'ingrédient et/ou l'unité n'est pas valide.");
                }
            }

        });
    }
}
