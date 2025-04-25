package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.PlanRepasViewModel;
import com.example.test_planigo.VueModele.RecetteViewModel;
import com.example.test_planigo.vue.adaptateurs.RecetteAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailPlanificateurActivity extends AppCompatActivity {

    private RecyclerView lundiRecyclerView, mardiRecyclerView, mercrediRecyclerView, jeudiRecyclerView, vendrediRecyclerView, samediRecyclerView, dimancheRecyclerView;
    private RecetteAdapter lundiAdaptateur, mardiAdaptateur, mercrediAdaptateur, jeudiAdaptateur, vendrediAdaptateur, samediAdaptateur, dimancheAdaptateur;
    private TextView titre;
    private ImageView boutonRetour;
    private PlanRepasViewModel viewModelPlanRepas;
    private Button genereListeEpicerie;
    private int idPlanRepas;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_planificateur);

        // Récupérer l'ID de la recette passé via l'Intent, si inconnu on fini l'activity
        int recipeId = getIntent().getIntExtra("ID", -1); // -1 comme valeur par défaut si non trouvé
        if (recipeId != -1) {
            idPlanRepas = recipeId;
        } else {
            // Gérer le cas où l'ID n'a pas été passé correctement
            Toast.makeText(this, "Erreur: ID de plan repas!", Toast.LENGTH_SHORT).show();
            finish(); // Fermer l'activité si l'ID est manquant
            return;
        }

        lundiRecyclerView = findViewById(R.id.lundiRecyclerView);
        mardiRecyclerView = findViewById(R.id.mardiRecyclerView);
        mercrediRecyclerView = findViewById(R.id.mercrediRecyclerView);
        jeudiRecyclerView = findViewById(R.id.jeudiRecyclerView);
        vendrediRecyclerView = findViewById(R.id.vendrediRecyclerView);
        samediRecyclerView = findViewById(R.id.samediRecyclerView);
        dimancheRecyclerView = findViewById(R.id.dimancheRecyclerView);
        titre = findViewById(R.id.titreDetailPlanificateurTextView);
        boutonRetour = findViewById(R.id.boutonRetourDetailPlanificateur);
        genereListeEpicerie = findViewById(R.id.boutonGenererListeEpicerie);

        viewModelPlanRepas = new ViewModelProvider(this).get(PlanRepasViewModel.class);

        //Configurer l'Action de retour
        boutonRetour.setOnClickListener(v ->{
            finish();
        });

        //Configurer l'action  de généré la liste d'épicerie
        genereListeEpicerie.setOnClickListener(v ->{
            Intent intent = new Intent(this, MaListeEpicerieActivity.class);
            intent.putExtra("ID", idPlanRepas); // Passer l'ID du plan repas à l'activité de détail
            startActivity(intent);
        });

        /*Initialiser les adapteur*/
        lundiAdaptateur = new RecetteAdapter(new ArrayList<>(), itemListe ->{
            //code pour le la recherche pur et simple de recette: aller consulter le détails de la recette
            Intent intent = new Intent(this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", itemListe.getId()); // Passer l'ID de la recette à l'activité de détail
            startActivity(intent);
        });
        mardiAdaptateur = new RecetteAdapter(new ArrayList<>(), itemListe ->{
            //code pour le la recherche pur et simple de recette: aller consulter le détails de la recette
            Intent intent = new Intent(this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", itemListe.getId()); // Passer l'ID de la recette à l'activité de détail
            startActivity(intent);
        });
        mercrediAdaptateur = new RecetteAdapter(new ArrayList<>(), itemListe ->{
            //code pour le la recherche pur et simple de recette: aller consulter le détails de la recette
            Intent intent = new Intent(this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", itemListe.getId()); // Passer l'ID de la recette à l'activité de détail
            startActivity(intent);
        });
        jeudiAdaptateur = new RecetteAdapter(new ArrayList<>(), itemListe ->{
            //code pour le la recherche pur et simple de recette: aller consulter le détails de la recette
            Intent intent = new Intent(this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", itemListe.getId()); // Passer l'ID de la recette à l'activité de détail
            startActivity(intent);
        });
        vendrediAdaptateur = new RecetteAdapter(new ArrayList<>(), itemListe ->{
            //code pour le la recherche pur et simple de recette: aller consulter le détails de la recette
            Intent intent = new Intent(this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", itemListe.getId()); // Passer l'ID de la recette à l'activité de détail
            startActivity(intent);
        });
        samediAdaptateur = new RecetteAdapter(new ArrayList<>(), itemListe ->{
            //code pour le la recherche pur et simple de recette: aller consulter le détails de la recette
            Intent intent = new Intent(this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", itemListe.getId()); // Passer l'ID de la recette à l'activité de détail
            startActivity(intent);
        });
        dimancheAdaptateur = new RecetteAdapter(new ArrayList<>(), itemListe ->{
            //code pour le la recherche pur et simple de recette: aller consulter le détails de la recette
            Intent intent = new Intent(this, RecipeDetailPageActivity.class);
            intent.putExtra("ID", itemListe.getId()); // Passer l'ID de la recette à l'activité de détail
            startActivity(intent);
        });

        /*Initialiser les RecyclerView*/
        lundiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lundiRecyclerView.setAdapter(lundiAdaptateur);
        mardiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mardiRecyclerView.setAdapter(mardiAdaptateur);
        mercrediRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mercrediRecyclerView.setAdapter(mercrediAdaptateur);
        jeudiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jeudiRecyclerView.setAdapter(jeudiAdaptateur);
        vendrediRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vendrediRecyclerView.setAdapter(vendrediAdaptateur);
        samediRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        samediRecyclerView.setAdapter(samediAdaptateur);
        dimancheRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dimancheRecyclerView.setAdapter(dimancheAdaptateur);

        /*Observer les modifications*/
        viewModelPlanRepas.getPlanRepasSommaire().observe(this, planRepasSommaire ->{
            if(planRepasSommaire != null){
                titre.setText(planRepasSommaire.getTitre());
            }
        });
        viewModelPlanRepas.getListeLundi().observe(this, recettesAbreges ->{
            if(recettesAbreges != null){
                lundiAdaptateur.setData(Arrays.asList(recettesAbreges));
            }
        });
        viewModelPlanRepas.getListeMardi().observe(this, recettesAbreges ->{
            if(recettesAbreges != null){
                mardiAdaptateur.setData(Arrays.asList(recettesAbreges));
            }
        });
        viewModelPlanRepas.getListeMercredi().observe(this, recettesAbreges ->{
            if(recettesAbreges != null){
                mercrediAdaptateur.setData(Arrays.asList(recettesAbreges));
            }
        });
        viewModelPlanRepas.getListeJeudi().observe(this, recettesAbreges ->{
            if(recettesAbreges != null){
                jeudiAdaptateur.setData(Arrays.asList(recettesAbreges));
            }
        });
        viewModelPlanRepas.getListeVendredi().observe(this, recettesAbreges ->{
            if(recettesAbreges != null){
                vendrediAdaptateur.setData(Arrays.asList(recettesAbreges));
            }
        });
        viewModelPlanRepas.getListeSamedi().observe(this, recettesAbreges ->{
            if(recettesAbreges != null){
                samediAdaptateur.setData(Arrays.asList(recettesAbreges));
            }
        });
        viewModelPlanRepas.getListeDimanche().observe(this, recettesAbreges ->{
            if(recettesAbreges != null){
                dimancheAdaptateur.setData(Arrays.asList(recettesAbreges));
            }
        });

        viewModelPlanRepas.chargerPlanRepasSpecifique(idPlanRepas);


    }



}
