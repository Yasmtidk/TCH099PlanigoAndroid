package com.example.test_planigo.VueModele;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.dao.PlanRepasRepository;
import com.example.test_planigo.modeles.entitees.PlanRepasSommaire;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;

public class PlanRepasViewModel  extends AndroidViewModel {

    MutableLiveData<Object> erreurOuFinActivity;
    private final LiveData<PlanRepasSommaire[]> listePlanRepasSommaire;
    private PlanRepasRepository planRepasRepository;

    private final LiveData<RecetteAbrege[]> listeRecetteLundi;
    private final LiveData<RecetteAbrege[]> listeRecetteMardi;
    private final LiveData<RecetteAbrege[]> listeRecetteMercredi;
    private final LiveData<RecetteAbrege[]> listeRecetteJeudi;
    private final LiveData<RecetteAbrege[]> listeRecetteVendredi;
    private final LiveData<RecetteAbrege[]> listeRecetteSamedi;
    private final LiveData<RecetteAbrege[]> listeRecetteDimanche;
    private final LiveData<PlanRepasSommaire> planRepasSomaire;

    public PlanRepasViewModel(Application application) {
        super(application);
        planRepasRepository = new PlanRepasRepository(application);
        erreurOuFinActivity = (MutableLiveData<Object>) planRepasRepository.getErreurOuFinActivity();
        listePlanRepasSommaire = planRepasRepository.getListePlanRepasSommaire();

        planRepasSomaire = planRepasRepository.getPlanRepasSommaire();
        listeRecetteLundi = planRepasRepository.getListeLundi();
        listeRecetteMardi = planRepasRepository.getListeMardi();
        listeRecetteMercredi = planRepasRepository.getListeMercredi();
        listeRecetteJeudi = planRepasRepository.getListeJeudi();
        listeRecetteVendredi = planRepasRepository.getListeVendredi();
        listeRecetteSamedi = planRepasRepository.getListeSamedi();
        listeRecetteDimanche = planRepasRepository.getListeDimanche();
    }

    /*Getter les liveData*/
    public LiveData<Object> getErreurOuFinActivity(){return erreurOuFinActivity;}
    public LiveData<PlanRepasSommaire[]> getListePlanRepasSommaire(){return listePlanRepasSommaire;}

    public LiveData<PlanRepasSommaire> getPlanRepasSommaire(){return planRepasSomaire;}
    public LiveData<RecetteAbrege[]> getListeLundi(){return listeRecetteLundi;}
    public LiveData<RecetteAbrege[]> getListeMardi(){return listeRecetteMardi;}
    public LiveData<RecetteAbrege[]> getListeMercredi(){return listeRecetteMercredi;}
    public LiveData<RecetteAbrege[]> getListeJeudi(){return listeRecetteJeudi;}
    public LiveData<RecetteAbrege[]> getListeVendredi(){return listeRecetteVendredi;}
    public LiveData<RecetteAbrege[]> getListeSamedi(){return listeRecetteSamedi;}
    public LiveData<RecetteAbrege[]> getListeDimanche(){return listeRecetteDimanche;}


    /*Exécuter les routes*/

    public void chargerListePlanRepasSommaire(){
        planRepasRepository.chargerListePlanRepasSommaire();
    }

    public void chargerPlanRepasSpecifique(int idPlanRepas){
        planRepasRepository.chargerPlanRepasSpecifique(idPlanRepas);
    }
}

