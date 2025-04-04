// ProfileActivity.java
package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test_planigo.modeles.singleton.ClientActuel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.test_planigo.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    private ImageView settingsButton;
    private LinearLayout mesRecettesButton;
    private LinearLayout monStockIngredientsButton;
    private LinearLayout maListeEpicerieButton;
    private LinearLayout monPlanDeSemaineButton;
    private Button deconnecterButton;
    private TextView nomPrenom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        settingsButton = findViewById(R.id.settingsButton);
        mesRecettesButton = findViewById(R.id.mes_recettes_button);
        monStockIngredientsButton = findViewById(R.id.mon_stock_ingredients_button);
        maListeEpicerieButton = findViewById(R.id.ma_liste_epicerie_button);
        monPlanDeSemaineButton = findViewById(R.id.mon_plan_de_semaine_button);
        deconnecterButton = findViewById(R.id.deconnecterButton);
        nomPrenom = findViewById(R.id.userNameTextView);

        //Prévenir les accès non autorisé
        if(ClientActuel.getClientConnecter() == null){
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        nomPrenom.setText(ClientActuel.getClientConnecter().getPrenom() + " " + ClientActuel.getClientConnecter().getNom());

        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        mesRecettesButton.setOnClickListener(this);
        monStockIngredientsButton.setOnClickListener(this);
        maListeEpicerieButton.setOnClickListener(this);
        monPlanDeSemaineButton.setOnClickListener(this);
        deconnecterButton.setOnClickListener(this);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                return true;

            } else if (id == R.id.nav_stock) {
                Intent intent = new Intent(ProfileActivity.this, MonStockIngredientsActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_recettes) {
                Intent intent = new Intent(ProfileActivity.this, RechercheRecetteActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_courses) {
                Intent intent = new Intent(ProfileActivity.this, MaListeEpicerieActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_accueil) {
                Intent intent = new Intent(ProfileActivity.this, AccueilActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();

        if (id == R.id.mes_recettes_button) {
            intent = new Intent(ProfileActivity.this, RechercheRecetteActivity.class);

        } else if (id == R.id.mon_stock_ingredients_button) {
            intent = new Intent(ProfileActivity.this, MonStockIngredientsActivity.class);

        } else if (id == R.id.ma_liste_epicerie_button) {
            intent = new Intent(ProfileActivity.this, MaListeEpicerieActivity.class);

        } else if (id == R.id.mon_plan_de_semaine_button) {
            Toast.makeText(this, "Mon plan de semaine feature coming soon!", Toast.LENGTH_SHORT).show();
            return;

        } else if (id == R.id.deconnecterButton) {
            intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}