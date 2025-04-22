package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.test_planigo.VueModele.StockageViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;
import com.example.test_planigo.vue.adaptateurs.StockProduitAdapter;

public class MonStockIngredientsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView stockIngredientsListView;
    private StockageViewModel stockageViewModel;
    private TextView msgErreur;
    private LinearLayout ajouterItem;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_stock);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        stockIngredientsListView = findViewById(R.id.stockIngredientsListView);
        msgErreur = findViewById(R.id.msgErreurMonStock);
        ajouterItem = findViewById(R.id.ajouterItemMonStockage);

        if (ajouterItem == null) {
            Log.e("MonStockActivity", "Could not find view with ID ajouterItemMonStockage");
            return;
        }

        bottomNavigationView.setSelectedItemId(R.id.nav_stock);


        //Récupéré les produits et garnir la liste view
        stockageViewModel = new ViewModelProvider(this).get(StockageViewModel.class);
        stockageViewModel.chargerListeProduit();
        //Recharger la liste des produit au retour d'une activité
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    stockageViewModel.chargerListeProduit();
                }
        );


        //Créer les produits de la liste lorsque la liste des produit change
        stockageViewModel.getListeProduit().observe(this, produits -> {
           if(produits != null){
               msgErreur.setText("");
               StockProduitAdapter adapter = new StockProduitAdapter(this, R.layout.list_item, produits, stockageViewModel);
               stockIngredientsListView.setAdapter(adapter);
           }else{
               msgErreur.setText("Erreur lors du chargement des items");
           }
        });


        //Modifier selon le succès de la requête lors de chaque modification avec l'API
        stockageViewModel.getResultatErreurAPILiveData().observe(this, reponse -> {
            if (reponse instanceof Boolean) {
                Boolean deleteReussie = (Boolean) reponse;
                if (deleteReussie) {
                    //modifier la zone de succès
                    msgErreur.setText("");
                    Toast.makeText(this, "Modification sauvegarder", Toast.LENGTH_LONG).show();
                }
            } else if (reponse instanceof String) {
                String messageErreur = (String) reponse;
                msgErreur.setText(messageErreur);
            }
        });


        //Lorsqu'un item veut être modifier par l'utilisateur
        stockageViewModel.getAllerPopupProduit().observe(this, reponse ->{
            Intent allerPopupStockage = new Intent(MonStockIngredientsActivity.this, PopupStockageActivity.class);
            allerPopupStockage.putExtra("ACTION", "Edit");
            allerPopupStockage.putExtra("INGREDIENT", reponse.getName());
            allerPopupStockage.putExtra("UNITE", reponse.getUnit());
            allerPopupStockage.putExtra("QUANTITE", reponse.getQuantity());
            launcher.launch(allerPopupStockage);
        });


        //Lorsqu'on click sur l'Ajout d'un produit
        ajouterItem.setOnClickListener(view -> {
            Intent allerPopupStockage = new Intent(MonStockIngredientsActivity.this, PopupStockageActivity.class);
            allerPopupStockage.putExtra("ACTION", "ADD");
            launcher.launch(allerPopupStockage);
        });



        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_accueil) {
                Intent intent = new Intent(MonStockIngredientsActivity.this, AccueilActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_stock) {
                bottomNavigationView.setSelectedItemId(R.id.nav_stock);
                return true;

            } else if (id == R.id.nav_recettes) {
                Intent intent = new Intent(MonStockIngredientsActivity.this, RechercheRecetteActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_courses) {
                Intent intent = new Intent(MonStockIngredientsActivity.this, MaListeEpicerieActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(MonStockIngredientsActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}