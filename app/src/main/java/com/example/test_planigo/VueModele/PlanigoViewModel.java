package com.example.test_planigo.VueModele;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.dao.ClientRepository;
import com.example.test_planigo.modeles.dao.RecetteRepository;
import com.example.test_planigo.modeles.dao.StockageRepository;
import com.example.test_planigo.modeles.entitees.Client;
import com.example.test_planigo.modeles.entitees.Recette;

import org.json.JSONException;

import java.util.List;


public class PlanigoViewModel extends AndroidViewModel {

    private ClientRepository clientRepository;
    private MutableLiveData<Object> connexionLiveData;
    private RecetteRepository recipeRepository;
    private StockageRepository stockageRepository;
    private LiveData<List<Recette>> recettes;

    // Filter LiveData
    private MutableLiveData<String> categoryFilter = new MutableLiveData<>("Tous");
    private MutableLiveData<String> ingredientFilter = new MutableLiveData<>("Tous");
    private MutableLiveData<String> priceFilter = new MutableLiveData<>("Tous");
    private MutableLiveData<String> searchText = new MutableLiveData<>("");


    public PlanigoViewModel(Application application) {
        super(application);
        clientRepository = new ClientRepository(application);
        recipeRepository = new RecetteRepository(application);
        connexionLiveData = (MutableLiveData<Object>) clientRepository.getConnexion();
        recettes = recipeRepository.getRecettes();
    }

    public LiveData<Object> getConnexion() {
        return connexionLiveData;
    }


    public void postConnexion(String nomUtilisateur, String motDePasse) throws JSONException {
        clientRepository.connexion(nomUtilisateur, motDePasse);
    }

    public void postClient(Client client) throws JSONException {
        clientRepository.postNouveauClient(client);
    }

     public LiveData<List<Recette>> getRecettes() {
        return recettes;
    }

    public void chargerRecettes() {
         String currentCategoryFilter = categoryFilter.getValue();
        String currentIngredientFilter = ingredientFilter.getValue();
        String currentPriceFilter = priceFilter.getValue();
        String currentSearchText = searchText.getValue();

         recipeRepository.chargerRecettes(
                currentCategoryFilter,
                currentIngredientFilter,
                currentPriceFilter,
                currentSearchText
        );
    }

     public void setCategoryFilter(String category) {
        categoryFilter.setValue(category);
    }

    public void setIngredientFilter(String ingredientCount) {
        ingredientFilter.setValue(ingredientCount);
    }

    public void setPriceFilter(String priceRange) {
        priceFilter.setValue(priceRange);
    }

    public void setSearchTextFilter(String text) {
        searchText.setValue(text);
    }


}