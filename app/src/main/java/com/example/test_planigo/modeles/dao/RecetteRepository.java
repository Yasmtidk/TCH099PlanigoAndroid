package com.example.test_planigo.modeles.dao;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.entitees.Produit;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.example.test_planigo.modeles.entitees.RecetteComplete;
import com.example.test_planigo.modeles.singleton.ClientActuel;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class RecetteRepository {

    private static String URL_POINT_ENTREE = "http://10.0.2.2:80/H2025_TCH099_02_C1/api/";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Context context;

    private final MutableLiveData<Object> resultatErreurAPILiveData = new MutableLiveData<>();
    private final MutableLiveData<RecetteAbrege[]> listeRecetteAbrege = new MutableLiveData<>();
    private final MutableLiveData<RecetteComplete> recetteCompleteActuel = new MutableLiveData<>();

    public RecetteRepository(Application application) {
        this.context = application.getApplicationContext();
    }

    /*Getter les listes d'Objet et des routes*/
    public LiveData<RecetteAbrege[]> getListeRecetteAbrege(){return listeRecetteAbrege;}
    public LiveData<RecetteComplete> getRecetteCompleteActuel(){return recetteCompleteActuel;}
    public LiveData<Object> getResultatErreurAPILiveData() {
        return resultatErreurAPILiveData;
    }

    /*Exécuter les routes*/

    /**
     * Récupéré toutes les recettes (abrégés) de la base de donné selon le type et la restriction donné
     * En cas de succès: listeRecetteAbrege est modifier par la nouvelle liste récupéré
     * En cas d'echec: resultatErreurAPILiveData est modifer par un string coorespondant au message d'erreur
     */
    public void setListeRecetteAbrege(String type, String restriction){
        (new Thread(){
            public void run(){
                try{
                    //préparer l'objet (information nécessaire pour la route)
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("filtreType", type);
                    postObj.put("filtreRestriction", restriction);
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    //Envoyer la requete et récupéré la réponse
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "CreationRecettes.php/recuperer-recettes-filtrer")
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

                        //Update la liste des recette abrégé
                        ObjectMapper mapper = new ObjectMapper();
                        JSONArray listeArrays = new JSONObject(stringResponce).getJSONArray("listeRecette");
                        RecetteAbrege[] listeRecetteAbregeRequete = mapper.readValue(listeArrays.toString(), RecetteAbrege[].class);
                        listeRecetteAbrege.postValue(listeRecetteAbregeRequete);
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
     * Setter la nouvelle recette où on désir connaitre toutes ses informations
     * @param id L'id de la recette où on désire connaitres toutes ses informations
     * En cas de succès: recetteCompleteActuel est modifier par la nouvelle recette complète récupéré
     * En cas d'echec: resultatErreurAPILiveData est modifer par un string coorespondant au message d'erreur
     */
    public void setRecetteCompleteActuel(int id){
        (new Thread(){
            public void run(){
                try{
                    //préparer l'objet (information nécessaire pour la route)
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("idRecette", id);
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    //Envoyer la requete et récupéré la réponse
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "CreationRecettes.php/recuperer-recette-complete")
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

                        //Update la recette complète actuel
                        ObjectMapper mapper = new ObjectMapper();
                        JSONArray recetteComplete = new JSONObject(stringResponce).getJSONArray("recetteComplete");
                        RecetteComplete listeRecetteAbregeRequete = mapper.readValue(recetteComplete.toString(), RecetteComplete.class);
                        recetteCompleteActuel.postValue(listeRecetteAbregeRequete);
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
