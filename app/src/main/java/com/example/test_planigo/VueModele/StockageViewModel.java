package com.example.test_planigo.VueModele;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.dao.StockageRepository;
import com.example.test_planigo.modeles.entitees.Ingredient;
import com.example.test_planigo.modeles.entitees.Produit;

public class StockageViewModel extends AndroidViewModel {

    private MutableLiveData<Object> resultatErreurAPILiveData;
    private final LiveData<Produit[]> listeProduit;
    private final LiveData<Ingredient[]> listeIngredient;
    private StockageRepository stockageRepository;

    // LiveData pour déclencher la navigation vers le popup d'édition/ajout
    private final MutableLiveData<Produit> allerPopupProduit = new MutableLiveData<>();

    public StockageViewModel(Application application){
        super(application);
        stockageRepository = new StockageRepository(application);
        resultatErreurAPILiveData = (MutableLiveData<Object>) stockageRepository.getResultatErreurAPILiveData();
        listeProduit = stockageRepository.getListeProduit();
        listeIngredient = stockageRepository.getListeIngredient();
    }

    /* Récupérer les résultats des routes */
    public LiveData<Object> getResultatErreurAPILiveData() {
        return resultatErreurAPILiveData;
    }

    public LiveData<Produit[]> getListeProduit() {
        return listeProduit;
    }

    public LiveData<Ingredient[]> getListeIngredient() {
        return listeIngredient;
    }

    /* Exécuter les routes */

    /**
     * Charge la liste des produits du stock de l'utilisateur.
     */
    public void chargerListeProduit(){
        stockageRepository.chargerListeProduit();
    }

    /**
     * Charge la liste de tous les ingrédients disponibles (pour l'ajout).
     */
    public void chargerListeIngredient(){
        stockageRepository.chargerListeIngredient();
    }

    /**
     * Charge la liste d'épicerie de l'utilisateur.
     * TODO: Vérifier que le endpoint et la logique dans StockageRepository sont corrects.
     */
    public void chargerListeEpicerie() {
        stockageRepository.chargerListeEpicerie();
    }

    /**
     * Demande la suppression d'un produit du stock.
     * @param produit Le produit à supprimer.
     */
    public void deleteProduit(Produit produit){
        stockageRepository.deleteProduit(produit);
    }

    /**
     * Demande la mise à jour (modification) d'un produit dans le stock.
     * @param produit Le produit avec les nouvelles informations (quantité).
     */
    public void updateProduit(Produit produit){
        stockageRepository.modifierProduit(produit);
    }

    /**
     * Demande l'ajout d'un nouveau produit au stock.
     * @param produit Le produit à ajouter.
     */
    public void ajouterProduit(Produit produit){
        stockageRepository.ajouterProduit(produit);
    }

    /* Transition vers une autre Activity */

    /**
     * LiveData pour observer quand naviguer vers le popup d'édition/ajout.
     * @return LiveData contenant le produit à éditer, ou null pour un ajout.
     */
    public LiveData<Produit> getAllerPopupProduit(){
        return allerPopupProduit;
    }

    /**
     * Déclenche la navigation vers le popup pour éditer un produit existant.
     * @param produit Le produit à éditer.
     */
    public void setAllerPopupProduit(Produit produit){
        allerPopupProduit.setValue(produit);
    }
}