package com.example.test_planigo.VueModele;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.dao.RecetteRepository;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.example.test_planigo.modeles.entitees.RecetteComplete;

public class RecetteViewModel extends AndroidViewModel {

    private MutableLiveData<Object> resultatErreurAPILiveData;
    private RecetteRepository recetteRepository;
    private final LiveData<RecetteAbrege[]> listeRecetteAbrege;
    private final LiveData<RecetteComplete> recetteCompleteActuel;

    public RecetteViewModel(Application application){
        super(application);
        recetteRepository = new RecetteRepository(application);
        resultatErreurAPILiveData = (MutableLiveData<Object>) recetteRepository.getResultatErreurAPILiveData();
        listeRecetteAbrege = recetteRepository.getListeRecetteAbrege();
        recetteCompleteActuel = recetteRepository.getRecetteCompleteActuel();
    }

    /*Récupéré les résultats des routes*/

    public LiveData<Object> getResultatErreurAPILiveData() {
        return resultatErreurAPILiveData;
    }

    public LiveData<RecetteAbrege[]> getListeRecetteAbrege() {
        return listeRecetteAbrege;
    }
    public LiveData<RecetteComplete> getRecetteCompleteActuel(){ return recetteCompleteActuel;}


    /*Exécuter les routes*/
    public void setListeRecetteAbrege(String type, String restriction){
        recetteRepository.setListeRecetteAbrege(type, restriction);
    }
    public void setRecetteActuel(int id){
        recetteRepository.setRecetteCompleteActuel(id);
    }

}
