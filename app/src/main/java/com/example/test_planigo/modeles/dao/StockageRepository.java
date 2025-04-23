package com.example.test_planigo.modeles.dao;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.entitees.Ingredient;
import com.example.test_planigo.modeles.entitees.Produit;
import com.example.test_planigo.modeles.singleton.ClientActuel;
import com.fasterxml.jackson.databind.ObjectMapper; // Gardé car utilisé

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

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
    // Liste de travail pour modifications avant de poster sur LiveData (delete)
    private LinkedList<Produit> listeProduitTravail = new LinkedList<>();

    private final MutableLiveData<Ingredient[]> listeIngredient = new MutableLiveData<>();

    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final ObjectMapper mapper = new ObjectMapper(); // Gardé car utilisé
    private Context context; // Gardé pour l'instant

    public StockageRepository(Application application) {
        this.context = application.getApplicationContext();
    }

    /* Getter les listes d'Objets */
    public LiveData<Produit[]> getListeProduit(){
        return listeProduit;
    }
    public LiveData<Ingredient[]> getListeIngredient(){
        return listeIngredient;
    }

    /* Getter les résultats des routes (succès/erreur) */
    public LiveData<Object> getResultatErreurAPILiveData() {
        return resultatErreurAPILiveData;
    }

    /* Exécuter les routes */

    /**
     * Récupère tous les produits dans le stockage du client connecté et initialise la liste locale.
     * En cas de succès: listeProduit est modifiée par la liste des produits récupérés.
     * En cas d'échec: resultatErreurAPILiveData est modifié par un string correspondant au message d'erreur.
     */
    public void chargerListeProduit(){
        (new Thread(){
            public void run(){
                try{
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/recuperer-produit/") // Endpoint pour les produits du stock
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    if (!response.isSuccessful()) throw new IOException("Code inattendu " + response);

                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce);
                    String statutRequete = jsonResponse.getString("statut");

                    if(statutRequete.equals("error")){
                        String messageRequete = jsonResponse.getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){
                        // Vérifier si la clé existe avant de récupérer le tableau
                        if (jsonResponse.has("stock_ingredient")) {
                            JSONArray stockArray = jsonResponse.getJSONArray("stock_ingredient");
                            Produit[] listeProduitRequete = mapper.readValue(stockArray.toString(), Produit[].class);
                            listeProduitTravail = new LinkedList<>(Arrays.asList(listeProduitRequete));
                            listeProduit.postValue(listeProduitRequete);
                        } else {
                            // Cas où success est vrai mais la clé manque (peut arriver)
                            listeProduit.postValue(new Produit[0]); // Envoyer une liste vide
                            listeProduitTravail.clear();
                        }
                    } else {
                        resultatErreurAPILiveData.postValue("Réponse inattendue du serveur.");
                    }

                }catch (JSONException | IOException e) {
                    resultatErreurAPILiveData.postValue("Erreur lors du chargement des produits.");
                    // e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Récupère tous les produits de la liste d'épicerie.
     * En cas de succès: listeProduit est modifiée par la liste des produits récupérés (ATTENTION: utilise le même LiveData que le stock pour l'instant).
     * En cas d'échec: resultatErreurAPILiveData est modifié par un string correspondant au message d'erreur.
     * TODO: Vérifier l'endpoint API et la clé JSON. Séparer le LiveData si nécessaire.
     */
    public void chargerListeEpicerie() {
        (new Thread() {
            public void run() {
                try {
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    // *** IMPORTANT: Ajuster l'URL de l'API pour la liste d'épicerie ***
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "epicerie.php/recuperer-epicerie/") // TODO: Mettre le bon endpoint
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    if (!response.isSuccessful()) throw new IOException("Code inattendu " + response);

                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce);
                    String statutRequete = jsonResponse.getString("statut");

                    if (statutRequete.equals("error")) {
                        String messageRequete = jsonResponse.getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    } else if (statutRequete.equals("success")) {
                        // *** IMPORTANT: Ajuster la clé du tableau JSON pour la liste d'épicerie ***
                        if (jsonResponse.has("liste_epicerie")) { // TODO: Mettre la bonne clé
                            JSONArray epicerieArray = jsonResponse.getJSONArray("liste_epicerie");
                            Produit[] listeEpicerieRequete = mapper.readValue(epicerieArray.toString(), Produit[].class);
                            // Mettre à jour le LiveData (actuellement le même que le stock)
                            listeProduitTravail = new LinkedList<>(Arrays.asList(listeEpicerieRequete));
                            listeProduit.postValue(listeEpicerieRequete);
                            resultatErreurAPILiveData.postValue(true); // Indiquer le succès si nécessaire
                        } else {
                            listeProduit.postValue(new Produit[0]);
                            listeProduitTravail.clear();
                            resultatErreurAPILiveData.postValue(true); // Succès mais liste vide
                        }
                    } else {
                        resultatErreurAPILiveData.postValue("Réponse inattendue du serveur.");
                    }

                } catch (JSONException | IOException e) {
                    resultatErreurAPILiveData.postValue("Erreur lors du chargement de la liste d'épicerie.");
                    // e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * Récupère tous les ingrédients disponibles (pour l'ajout de produit au stock).
     * En cas de succès: listeIngredient est modifiée par la liste des ingrédients récupérés.
     * En cas d'échec: resultatErreurAPILiveData est modifié par un string correspondant au message d'erreur.
     */
    public void chargerListeIngredient(){
        (new Thread(){
            public void run(){
                try{
                    // Envoyer la requête GET pour récupérer la liste globale des ingrédients
                    Request getRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/recuperer-ingredient/")
                            .get() // Utiliser GET
                            .build();
                    Response response = okHttpClient.newCall(getRequete).execute();
                    if (!response.isSuccessful()) throw new IOException("Code inattendu " + response);

                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce);
                    String statutRequete = jsonResponse.getString("statut");

                    if(statutRequete.equals("error")){
                        String messageRequete = jsonResponse.getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){
                        if (jsonResponse.has("listeIngredient")) {
                            JSONArray stockArray = jsonResponse.getJSONArray("listeIngredient");
                            Ingredient[] listeIngredientRequete = mapper.readValue(stockArray.toString(), Ingredient[].class);
                            listeIngredient.postValue(listeIngredientRequete);
                        } else {
                            listeIngredient.postValue(new Ingredient[0]); // Liste vide
                        }
                    } else {
                        resultatErreurAPILiveData.postValue("Réponse inattendue du serveur.");
                    }

                }catch (JSONException | IOException e) {
                    resultatErreurAPILiveData.postValue("Erreur lors du chargement des ingrédients.");
                    // e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Supprime le produit donné en paramètre du stockage du client actuel dans la base de données.
     * @param produit L'objet Produit à supprimer (utilise produit.getName() pour identifier).
     * En cas de succès: resultatErreurAPILiveData renvoie true et la liste locale est mise à jour.
     * En cas d'échec: resultatErreurAPILiveData est modifié par un string correspondant au message d'erreur.
     */
    public void deleteProduit(Produit produit){
        (new Thread(){
            public void run(){
                try{
                    // Préparer l'objet JSON pour la requête DELETE
                    JSONObject postObj = new JSONObject(); // Même si c'est DELETE, on envoie des infos dans le corps
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("ingredient", produit.getName()); // Clé attendue par l'API pour identifier le produit
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    // Envoyer la requête DELETE
                    Request deleteRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/supprimer-produit/")
                            .delete(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(deleteRequete).execute();
                    if (!response.isSuccessful()) throw new IOException("Code inattendu " + response);

                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce);
                    String statutRequete = jsonResponse.getString("statut");

                    // Mettre à jour la liste locale si succès, sinon poster l'erreur.
                    if(statutRequete.equals("error")){
                        String messageRequete = jsonResponse.getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){
                        // Supprimer de la liste de travail locale
                        boolean removed = listeProduitTravail.removeIf(p -> p.getName().equals(produit.getName()));
                        if (removed) {
                            // Mettre à jour le LiveData avec la liste modifiée
                            listeProduit.postValue(listeProduitTravail.toArray(new Produit[0]));
                        }
                        resultatErreurAPILiveData.postValue(true); // Indiquer le succès de l'opération API
                    } else {
                        resultatErreurAPILiveData.postValue("Réponse inattendue du serveur.");
                    }

                }catch (JSONException | IOException e) {
                    resultatErreurAPILiveData.postValue("Erreur lors de la suppression du produit.");
                    // e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Ajoute le produit donné en paramètre au stockage du client actuel.
     * @param produit L'objet produit à ajouter dans la base de données.
     * En cas de succès: resultatErreurAPILiveData renvoie true et la liste est rechargée.
     * En cas d'échec: resultatErreurAPILiveData est modifié par un string correspondant au message d'erreur.
     */
    public void ajouterProduit(Produit produit){
        (new Thread(){
            public void run(){
                try{
                    /* Récupérer l'id du produit à ajouter (basé sur son nom) */
                    Request getIdRequest = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/recuperer-id-produit/" + produit.getName()) // Assumer que l'API prend le nom dans l'URL
                            .get()
                            .build();
                    Response idResponse = okHttpClient.newCall(getIdRequest).execute();
                    if (!idResponse.isSuccessful()) throw new IOException("Code inattendu (get ID) " + idResponse);

                    ResponseBody idResponseBody = idResponse.body();
                    String idStringResponse = idResponseBody.string();
                    JSONObject idJsonResponse = new JSONObject(idStringResponse);
                    String idStatutRequete = idJsonResponse.getString("statut");

                    if(idStatutRequete.equals("error")){
                        String messageRequete = idJsonResponse.getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete); // Erreur: Impossible de trouver l'ID
                    } else if(idStatutRequete.equals("success")){
                        // ID trouvé, procéder à l'ajout au stock
                        String idProduit = idJsonResponse.getString("idProduit");

                        /* Maintenant qu'on a l'id du produit, l'ajouter au stockage du client actuel */
                        JSONObject postObj = new JSONObject();
                        postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                        postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                        postObj.put("idProduit", idProduit); // Utiliser l'ID récupéré
                        postObj.put("quantite", produit.getQuantity());
                        RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                        // Envoyer la requête POST pour ajouter
                        Request postRequete = new Request.Builder()
                                .url(URL_POINT_ENTREE + "stockage.php/ajouter-produit/")
                                .post(corpsPostRequete)
                                .build();
                        Response response = okHttpClient.newCall(postRequete).execute();
                        if (!response.isSuccessful()) throw new IOException("Code inattendu (add) " + response);

                        ResponseBody responseBody = response.body();
                        String stringResponce = responseBody.string();
                        JSONObject jsonResponse = new JSONObject(stringResponce);
                        String statutRequete = jsonResponse.getString("statut");

                        // Gérer le résultat de l'ajout
                        if(statutRequete.equals("error")){
                            String messageRequete = jsonResponse.getString("message");
                            resultatErreurAPILiveData.postValue(messageRequete); // Erreur lors de l'ajout
                        } else if(statutRequete.equals("success")){
                            // Optionnellement, recharger la liste complète pour voir l'ajout
                            chargerListeProduit();
                            resultatErreurAPILiveData.postValue(true); // Indiquer le succès
                        } else {
                            resultatErreurAPILiveData.postValue("Réponse inattendue du serveur lors de l'ajout.");
                        }
                    } else {
                        resultatErreurAPILiveData.postValue("Réponse inattendue lors de la récupération de l'ID.");
                    }
                }catch (JSONException | IOException e) {
                    resultatErreurAPILiveData.postValue("Erreur lors de l'ajout du produit.");
                    // e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * Modifie le produit donné en paramètre dans la base de données (principalement sa quantité).
     * @param produit Le produit modifié (le nom doit déjà exister dans le stock de l'utilisateur).
     * En cas de succès: resultatErreurAPILiveData renvoie true et la liste est rechargée.
     * En cas d'échec: resultatErreurAPILiveData est modifié par un string correspondant au message d'erreur.
     */
    public void modifierProduit(Produit produit){
        (new Thread(){
            public void run(){
                try{
                    // Préparer l'objet JSON pour la requête PUT (mise à jour)
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("nomProduit", produit.getName()); // Identifier le produit par son nom
                    postObj.put("quantite", produit.getQuantity()); // La nouvelle quantité
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    // Envoyer la requête PUT
                    Request putRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "stockage.php/update-produit/")
                            .put(corpsPostRequete) // Utiliser PUT pour la mise à jour
                            .build();
                    Response response = okHttpClient.newCall(putRequete).execute();
                    if (!response.isSuccessful()) throw new IOException("Code inattendu " + response);

                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce);
                    String statutRequete = jsonResponse.getString("statut");

                    // Gérer le résultat de la modification
                    if(statutRequete.equals("error")){
                        String messageRequete = jsonResponse.getString("message");
                        resultatErreurAPILiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){
                        // Optionnellement, recharger la liste complète pour voir la modification
                        chargerListeProduit();
                        resultatErreurAPILiveData.postValue(true); // Indiquer le succès
                    } else {
                        resultatErreurAPILiveData.postValue("Réponse inattendue du serveur lors de la modification.");
                    }

                }catch (JSONException | IOException e) {
                    resultatErreurAPILiveData.postValue("Erreur lors de la modification du produit.");
                    // e.printStackTrace();
                }
            }
        }).start();
    }
}