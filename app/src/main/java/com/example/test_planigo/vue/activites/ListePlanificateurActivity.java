package com.example.test_planigo.vue.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.PlanRepasViewModel;
import com.example.test_planigo.VueModele.RecetteViewModel;
import com.example.test_planigo.vue.adaptateurs.PlanRepasAdapter;
import com.example.test_planigo.vue.adaptateurs.RecetteAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ListePlanificateurActivity extends AppCompatActivity {

    private RecyclerView listePlanRepasRecyclerView;
    private PlanRepasAdapter adapterPlanRepas;
    private PlanRepasViewModel viewModelPlanRepas;
    private ImageView ajouterPlanImage;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_planificateurs);

        viewModelPlanRepas = new ViewModelProvider(this).get(PlanRepasViewModel.class);
        listePlanRepasRecyclerView = findViewById(R.id.planificateursRecyclerView);
        listePlanRepasRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ajouterPlanImage = findViewById(R.id.boutonAjouterPlanificateur);

        //Aller ajouter une planification hebdomadaire si l'image + est cliquer
        ajouterPlanImage.setOnClickListener(v ->{
            Intent intent = new Intent(this, WeeklyPlannerActivity.class);
            startActivity(intent);
        });

        //Configurer l'adapteur au Recycle view et configurer le lancement de l'activity
        adapterPlanRepas = new PlanRepasAdapter(new ArrayList<>(), itemListe ->{
            Intent intent = new Intent(this, DetailPlanificateurActivity.class);
            intent.putExtra("ID", itemListe.getId()); // Passer l'ID du plan repas à l'activité de détail
            startActivity(intent);
        });
        listePlanRepasRecyclerView.setAdapter(adapterPlanRepas);

        //Pour modifier la liste du plan de repas
        viewModelPlanRepas.getListePlanRepasSommaire().observe(this, planRepasSommaires -> {
            if(planRepasSommaires != null){
                adapterPlanRepas.setData(Arrays.asList(planRepasSommaires));
            }else{
                adapterPlanRepas.setData(new ArrayList<>());
            }
        });
        viewModelPlanRepas.chargerListePlanRepasSommaire();

        //pour les cas d'erreur
        viewModelPlanRepas.getErreurOuFinActivity().observe(this, reponse ->{
            if (reponse instanceof String) {
                // Afficher le message d'erreur renvoyé par l'API
                String messageErreur = (String) reponse;
                Toast.makeText(ListePlanificateurActivity.this, messageErreur, Toast.LENGTH_LONG).show();
            }
        });
    }
}
