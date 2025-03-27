package com.yasmi.tch099planigo.modeles.dao;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yasmi.tch099planigo.modeles.entitees.Recette;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RecipeRepository {

    private final MutableLiveData<List<Recette>> recettesLiveData = new MutableLiveData<>();
    private Context context;
    private ObjectMapper mapper = new ObjectMapper();


    public RecipeRepository(Application application) {
        this.context = application.getApplicationContext();
    }

    public LiveData<List<Recette>> getRecettes() {
        return recettesLiveData;
    }

    public void chargerRecettes(String categoryFilter, String ingredientFilter, String priceFilter, String searchText) {
        new Thread(() -> {
            try {
                InputStream inputStream = context.getAssets().open("planigo_db.json");
                JsonNode rootNode = mapper.readTree(inputStream);
                JsonNode recettesArray = rootNode.get("recettes");

                List<Recette> allRecipes = new ArrayList<>();
                if (recettesArray.isArray()) {
                    for (JsonNode recetteNode : recettesArray) {
                        Recette recette = mapper.readValue(recetteNode.toString(), Recette.class);
                        // Parse etapes from JsonNode
                        JsonNode etapesNode = recetteNode.get("etapes");
                        if (etapesNode != null && etapesNode.isArray()) {
                            List<String> etapesList = new ArrayList<>();
                            for (JsonNode etapeNode : etapesNode) {
                                etapesList.add(etapeNode.asText());
                            }
                            recette.setEtapes(etapesList);
                        }
                        allRecipes.add(recette);
                    }
                }

                List<Recette> filteredRecipes = filterRecipes(
                        allRecipes,
                        categoryFilter,
                        ingredientFilter,
                        priceFilter,
                        searchText
                );

                recettesLiveData.postValue(filteredRecipes);

            } catch (IOException e) {
                e.printStackTrace();
                recettesLiveData.postValue(null);
            }
        }).start();
    }


    private List<Recette> filterRecipes(List<Recette> allRecipes, String categoryFilter, String ingredientFilter, String priceFilter, String searchText) {
        List<Recette> filteredList = new ArrayList<>(allRecipes);

        if (!categoryFilter.equals("Catégorie") && !categoryFilter.equals("Tous")) {
            filteredList.removeIf(recette -> !recette.getCategorie().equalsIgnoreCase(categoryFilter));
        }

        if (!ingredientFilter.equals("Nombre d'ingrédients") && !ingredientFilter.equals("Tous")) {
            int ingredientCount;
            if (ingredientFilter.equals("Moins de 5")) {
                ingredientCount = 5;
                filteredList.removeIf(recette -> recette.getIngredients().size() >= ingredientCount);
            } else if (ingredientFilter.equals("5-10")) {
                filteredList.removeIf(recette -> recette.getIngredients().size() < 5 || recette.getIngredients().size() > 10);
            } else if (ingredientFilter.equals("Plus de 10")) {
                filteredList.removeIf(recette -> recette.getIngredients().size() <= 10);
            }
        }

        if (!searchText.trim().isEmpty()) {
            Pattern pattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
            filteredList.removeIf(recette -> !pattern.matcher(recette.getNom()).find());
        }

        return filteredList;
    }
}