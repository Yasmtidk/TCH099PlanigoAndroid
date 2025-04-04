package com.example.test_planigo.modeles.dao;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.entitees.Ingredient;
import com.example.test_planigo.modeles.entitees.Produit;
import com.example.test_planigo.modeles.singleton.ClientActuel;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StockageRepository {

    private static String URL_POINT_ENTREE = "http://10.0.2.2:80/H2025_TCH099_02_C1/api/";
    private final OkHttpClient okHttpClient = new OkHttpClient();

    private final MutableLiveData<Object> resultatErreurAPILiveData = new MutableLiveData<>();
    private final MutableLiveData<Produit[]> listeProduit = new MutableLiveData<>();
    private LinkedList listeProduitTravail = new LinkedList<>();

    private final MutableLiveData<Ingredient[]> listeIngredient = new MutableLiveData<>();

    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final ObjectMapper mapper = new ObjectMapper();
    private Context context;

    //Log.d("DEBUG_TAG", "Passage dans chargerListeProduit() ok");

    public StockageRepository(Application application) {
        this.context = application.getApplicationContext();
    }

    /*Getter les listes d'Objet*/

    public LiveData<Produit[]> getListeProduit(){
        return listeProduit;
    }
    public LiveData<Ingredient[]> getListeIngredient(){
        return listeIngredient;
    }


    /*Getter les résultats des routes*/

    public LiveData<Object> getResultatErreurAPILiveData() {
        return resultatErreurAPILiveData;
    }

    /*Exécuter les routes*/

    /**Récupérer tout les produits dans le stockage du client connecter et initialiser la liste local*/
    public void chargerListeProduit(){
        (new Thread(){
            public void run(){
                try{
                    //préparer l'objet (information nécessaire pour la route)
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    //Envoyer la requete et récupéré la réponse
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/recuperer-produit/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    //Retourner true si c'est réussit, sinon retourner le message d'erreur.
                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        //Update la liste des produits
                        ObjectMapper mapper = new ObjectMapper();
                        JSONArray stockArray = new JSONObject(stringResponce).getJSONArray("stock_ingredient");
                        Produit[] listeProduitRequete = mapper.readValue(stockArray.toString(), Produit[].class);
                        listeProduitTravail = new LinkedList<>(Arrays.asList(listeProduitRequete));
                        listeProduit.postValue(listeProduitRequete);
                    }

                }catch (JSONException e) {
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }catch(IOException e){
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Récupérer tout les produits dans le stockage du client connecter et initialiser la liste local
     */
    public void chargerListeIngredient(){
        (new Thread(){
            public void run(){
                try{
                    Log.d("DEBUG_TAG", "Passage dans chargerListeIngredient");
                    //Envoyer la requete et récupéré la réponse
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/recuperer-ingredient/")
                            .get()
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    //Retourner true si c'est réussit, sinon retourner le message d'erreur.
                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        //Update la liste des ingrédients
                        ObjectMapper mapper = new ObjectMapper();
                        JSONArray stockArray = new JSONObject(stringResponce).getJSONArray("listeIngredient");
                        Ingredient[] listeIngredientRequete = mapper.readValue(stockArray.toString(), Ingredient[].class);
                        listeIngredient.postValue(listeIngredientRequete);
                    }

                }catch (JSONException e) {
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }catch(IOException e){
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /** Supprimer le produit donné en paramètre du stockage du client actuel dans la base de donné
     * @param produit L'objet Produit à supprimer de la base de donné*/
    public void deleteProduit(Produit produit){
        (new Thread(){
            public void run(){
                try{
                    Log.d("DEBUG_TAG", "Passage dans la suppression du produit");
                    //préparer l'objet (information nécessaire pour la route)
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("ingredient", produit.getName());
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    //Envoyer la requete et récupéré la réponse
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/supprimer-produit/")
                            .delete(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    //Changer la liste des produit si c'est réussit, sinon retourner le message d'erreur.
                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        listeProduitTravail.remove(produit);
                        listeProduit.postValue((Produit[]) listeProduitTravail.toArray(new Produit[0]));
                        resultatErreurAPILiveData.postValue(true);
                    }

                }catch (JSONException e) {
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }catch(IOException e){
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /** Ajouter le produit donné en paramètre aux stockage du client actuel
     * @param produit L'objet produit à ajouter dans la base de donné
     */
    public void ajouterProduit(Produit produit){
        (new Thread(){
            public void run(){
                try{
                    Log.d("DEBUG_TAG", "passage dans ajouterProduit");
                    /*Récupéré l'id du produit rechercher*/

                    //Envoyer la requete et récupéré la réponse
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/recuperer-id-produit/" + produit.getName())
                            .get()
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    //Retourner true si c'est réussit, sinon retourner le message d'erreur.
                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        /*Maintenant qu'on a l'id du produit, l'ajouter aux stockage du client actuel*/

                        //préparer l'objet (information nécessaire pour la route)
                        JSONObject postObj = new JSONObject();
                        postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                        postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                        postObj.put("idProduit", produit.getName());
                        postObj.put("quantite", produit.getQuantity());
                        RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                        //Envoyer la requete et récupéré la réponse
                        postRequete = new Request.Builder()
                                .url(URL_POINT_ENTREE + "stockage.php/ajouter-produit/")
                                .post(corpsPostRequete)
                                .build();
                        response = okHttpClient.newCall(postRequete).execute();
                        responseBody = response.body();
                        stringResponce = responseBody.string();
                        statutRequete = new JSONObject(stringResponce).getString("statut");

                        //Retourner true si c'est réussit, sinon retourner le message d'erreur.
                        if(statutRequete.equals("error")){
                            String messageRequete = new JSONObject(stringResponce).getString("message");
                            resultatErreurAPILiveData.postValue(messageRequete);
                        }else if(statutRequete.equals("success")){

                            resultatErreurAPILiveData.postValue(true);
                        }
                    }
                }catch (JSONException e) {
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }catch(IOException e){
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void modifierProduit(Produit produit){
        (new Thread(){
            public void run(){
                try{
                    Log.d("DEBUG_TAG", "Passage dans modifierProduit");
                    //préparer l'objet (information nécessaire pour la route)
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("nomProduit", produit.getName());
                    postObj.put("quantite", produit.getQuantity());
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    //Envoyer la requete et récupéré la réponse
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/update-produit/")
                            .put(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    //Retourner true si c'est réussit, sinon retourner le message d'erreur.
                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        resultatErreurAPILiveData.postValue(true);
                    }

                }catch (JSONException e) {
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }catch(IOException e){
                    resultatErreurAPILiveData.postValue(false);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}