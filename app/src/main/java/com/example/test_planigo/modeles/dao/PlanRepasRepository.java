package com.example.test_planigo.modeles.dao;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.entitees.PlanRepasSommaire;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.example.test_planigo.modeles.entitees.RecetteComplete;
import com.example.test_planigo.modeles.singleton.ClientActuel;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PlanRepasRepository {

    private static String URL_POINT_ENTREE = "http://10.0.2.2:80/H2025_TCH099_02_C1/api/";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final MutableLiveData<Object> erreurOuFinActivity = new MutableLiveData<>();
    private final MutableLiveData<PlanRepasSommaire[]> listePlanRepasSomaire = new MutableLiveData<>();

    private final MutableLiveData<RecetteAbrege[]> listeRecetteLundi = new MutableLiveData<>();
    private final MutableLiveData<RecetteAbrege[]> listeRecetteMardi = new MutableLiveData<>();
    private final MutableLiveData<RecetteAbrege[]> listeRecetteMercredi = new MutableLiveData<>();
    private final MutableLiveData<RecetteAbrege[]> listeRecetteJeudi = new MutableLiveData<>();
    private final MutableLiveData<RecetteAbrege[]> listeRecetteVendredi = new MutableLiveData<>();
    private final MutableLiveData<RecetteAbrege[]> listeRecetteSamedi = new MutableLiveData<>();
    private final MutableLiveData<RecetteAbrege[]> listeRecetteDimanche = new MutableLiveData<>();
    private final MutableLiveData<PlanRepasSommaire> planRepasSomaire = new MutableLiveData<>();


    private Context context;

    public PlanRepasRepository(Application application) {
        this.context = application.getApplicationContext();
    }

    /*Getter les liveData*/
    public LiveData<Object> getErreurOuFinActivity(){return erreurOuFinActivity;}
    public LiveData<PlanRepasSommaire[]> getListePlanRepasSommaire(){return listePlanRepasSomaire;}

    public LiveData<PlanRepasSommaire> getPlanRepasSommaire(){return planRepasSomaire;}
    public LiveData<RecetteAbrege[]> getListeLundi(){return listeRecetteLundi;}
    public LiveData<RecetteAbrege[]> getListeMardi(){return listeRecetteMardi;}
    public LiveData<RecetteAbrege[]> getListeMercredi(){return listeRecetteMercredi;}
    public LiveData<RecetteAbrege[]> getListeJeudi(){return listeRecetteJeudi;}
    public LiveData<RecetteAbrege[]> getListeVendredi(){return listeRecetteVendredi;}
    public LiveData<RecetteAbrege[]> getListeSamedi(){return listeRecetteSamedi;}
    public LiveData<RecetteAbrege[]> getListeDimanche(){return listeRecetteDimanche;}



    /*Exécuter les routes*/

    /**
     * Généré la liste des plan de repas de l'utilisateur actuel
     */
    public void chargerListePlanRepasSommaire(){
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
                            .url(URL_POINT_ENTREE + "CreationPlans.php/recuperer-plant-personnel/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    //Retourner true si c'est réussit, sinon retourner le message d'erreur.
                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        erreurOuFinActivity.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        //Update la liste des recette abrégé
                        ObjectMapper mapper = new ObjectMapper();
                        JSONArray listeArrays = new JSONObject(stringResponce).getJSONArray("listePlans");
                        PlanRepasSommaire[] listePlanRepasSommaire = mapper.readValue(listeArrays.toString(), PlanRepasSommaire[].class);
                        listePlanRepasSomaire.postValue(listePlanRepasSommaire);
                    }

                }catch (JSONException e) {
                    erreurOuFinActivity.postValue(false);
                    e.printStackTrace();
                }catch(IOException e){
                    erreurOuFinActivity.postValue(false);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void chargerPlanRepasSpecifique(int idPlanRepas){
        (new Thread(){
            public void run(){
                try{
                    //préparer l'objet (information nécessaire pour la route)
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("id", idPlanRepas);
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);

                    //Envoyer la requete et récupéré la réponse
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "CreationPlans.php/recuperer-plan-specifique/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    //Retourner true si c'est réussit, sinon retourner le message d'erreur.
                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        erreurOuFinActivity.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        //Update les données
                        ObjectMapper mapper = new ObjectMapper();
                        JSONObject objetPlanification = new JSONObject(stringResponce).getJSONObject("planification");
                        PlanRepasSommaire listePlanRepasSommaire = mapper.readValue(objetPlanification.toString(), PlanRepasSommaire.class);
                        planRepasSomaire.postValue(listePlanRepasSommaire);

                        JSONArray listeArrays = new JSONObject(stringResponce).getJSONArray("listeRecettesLundi");
                        RecetteAbrege[] listeRecetteAbregeRequeteLundi = mapper.readValue(listeArrays.toString(), RecetteAbrege[].class);
                        listeRecetteLundi.postValue(listeRecetteAbregeRequeteLundi);

                        listeArrays = new JSONObject(stringResponce).getJSONArray("listeRecettesMardi");
                        RecetteAbrege[] listeRecetteAbregeRequeteMardi = mapper.readValue(listeArrays.toString(), RecetteAbrege[].class);
                        listeRecetteMardi.postValue(listeRecetteAbregeRequeteMardi);

                        listeArrays = new JSONObject(stringResponce).getJSONArray("listeRecettesMercredi");
                        RecetteAbrege[] listeRecetteAbregeRequeteMercredi = mapper.readValue(listeArrays.toString(), RecetteAbrege[].class);
                        listeRecetteMercredi.postValue(listeRecetteAbregeRequeteMercredi);

                        listeArrays = new JSONObject(stringResponce).getJSONArray("listeRecettesJeudi");
                        RecetteAbrege[] listeRecetteAbregeRequeteJeudi = mapper.readValue(listeArrays.toString(), RecetteAbrege[].class);
                        listeRecetteJeudi.postValue(listeRecetteAbregeRequeteJeudi);

                        listeArrays = new JSONObject(stringResponce).getJSONArray("listeRecettesVendredi");
                        RecetteAbrege[] listeRecetteAbregeRequeteVendredi = mapper.readValue(listeArrays.toString(), RecetteAbrege[].class);
                        listeRecetteVendredi.postValue(listeRecetteAbregeRequeteVendredi);

                        listeArrays = new JSONObject(stringResponce).getJSONArray("listeRecettesSamedi");
                        RecetteAbrege[] listeRecetteAbregeRequeteSamedi = mapper.readValue(listeArrays.toString(), RecetteAbrege[].class);
                        listeRecetteSamedi.postValue(listeRecetteAbregeRequeteSamedi);

                        listeArrays = new JSONObject(stringResponce).getJSONArray("listeRecettesDimanche");
                        RecetteAbrege[] listeRecetteAbregeRequeteDimanche = mapper.readValue(listeArrays.toString(), RecetteAbrege[].class);
                        listeRecetteDimanche.postValue(listeRecetteAbregeRequeteDimanche);

                    }

                }catch (JSONException e) {
                    erreurOuFinActivity.postValue(false);
                    e.printStackTrace();
                }catch(IOException e){
                    erreurOuFinActivity.postValue(false);
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
