package com.example.test_planigo.VueModele;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.dao.RecipeRepository;
import com.example.test_planigo.modeles.dao.StockageRepository;
import com.example.test_planigo.modeles.entitees.Ingredient;
import com.example.test_planigo.modeles.entitees.Produit;

import org.json.JSONException;

import java.util.List;

public class StockageViewModel extends AndroidViewModel {

    private MutableLiveData<Object> resultatErreurAPILiveData;
    private final LiveData<Produit[]> listeProduit;
    private final LiveData<Ingredient[]> listeIngredient;
    private StockageRepository stockageRepository;

    private final MutableLiveData<Produit> allerPopupProduit = new MutableLiveData<>();

    public StockageViewModel(Application application){
        super(application);
        stockageRepository = new StockageRepository(application);
        resultatErreurAPILiveData = (MutableLiveData<Object>) stockageRepository.getResultatErreurAPILiveData();
        listeProduit = stockageRepository.getListeProduit();
        listeIngredient = stockageRepository.getListeIngredient();
    }

    /*Récupéré les résultats des routes*/

    public LiveData<Object> getResultatErreurAPILiveData() {
        return resultatErreurAPILiveData;
    }

    public LiveData<Produit[]> getListeProduit() {
        return listeProduit;
    }

    public LiveData<Ingredient[]> getListeIngredient() {
        return listeIngredient;
    }

    /*Exécuter les routes*/

    public void chargerListeProduit(){
        stockageRepository.chargerListeProduit();
    }

    public void chargerListeIngredient(){
        stockageRepository.chargerListeIngredient();
    }

    public void deleteProduit(Produit produit){
        stockageRepository.deleteProduit(produit);
    }

    public void updateProduit(Produit produit){
        stockageRepository.modifierProduit(produit);
    }

    public void ajouterProduit(Produit produit){
        stockageRepository.ajouterProduit(produit);
    }

    /*Transition vers une autre Activity*/

    public LiveData<Produit> getAllerPopupProduit(){
        return allerPopupProduit;
    }
    public void setAllerPopupProduit(Produit produit){
        allerPopupProduit.setValue(produit);
    }
}
