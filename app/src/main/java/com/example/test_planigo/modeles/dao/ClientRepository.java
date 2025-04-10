package com.example.test_planigo.modeles.dao;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.entitees.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class ClientRepository {
    private static String URL_POINT_ENTREE = "http://10.0.2.2:80/H2025_TCH099_02_C1/api/";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MutableLiveData<Object> connexionLiveData = new MutableLiveData<>();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final ObjectMapper mapper = new ObjectMapper();

    private Context context;

    public ClientRepository(Application application) {
        this.context = application.getApplicationContext();
    }

    public LiveData<Object> getConnexion() {
        return connexionLiveData;
    }

    /**
     * Créer un nouvelle utilisateur dans la base de donné
     * Modifie connexionLiveData à true si c'Est un succès ou envoie un string s'il y a une erreur
     * @param client Le client à rojouter dans la base de donné
     */
    public void postNouveauClient(Client client) throws JSONException {

        (new Thread() {
            @Override
            public void run() {

                //vérifier que le nom d'utilisateur (identifiant) est unique
                try{
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", client.getNom_utilisateur());
                    postObj.put("motDePasse", client.getMot_de_passe());
                    postObj.put("nom", client.getNom());
                    postObj.put("prenom", client.getPrenom());

                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "inscription.php/inscrire/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    //Si le nom d'utilisateur est déjà présent (identifiant) ou tout autre erreur en lien avec le mot de passe : affiche erreur
                    //Sinon, afficher un message de succès
                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        connexionLiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        connexionLiveData.postValue(true);
                    }

                } catch (IOException | JSONException e) {
                    connexionLiveData.postValue(false);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**Vérifier si les champs d'entrés d'une connextion sont valide
     *  Modifie connexionLiveData selon le résultat
     *    Envoie le client récupéré en cas de succes ou envoie un string en cas d'erreur
     * @param nomUtilisateur Le nom d'utilisateur présumé du client
     * @param motDePasse  Le mot de passe présumé du client
     */
    public void connexion(String nomUtilisateur, String motDePasse) throws JSONException {
        (new Thread() {
            @Override
            public void run() {

                try{
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", nomUtilisateur);
                    postObj.put("motDePasse", motDePasse);

                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "login.php/login/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    String statutRequete = new JSONObject(stringResponce).getString("statut");

                    if(statutRequete.equals("error")){
                        String messageRequete = new JSONObject(stringResponce).getString("message");
                        connexionLiveData.postValue(messageRequete);
                    }else if(statutRequete.equals("success")){

                        //Créer le nouveau client
                        String nomRequete = new JSONObject(stringResponce).getString("nom");
                        String prenomRequete = new JSONObject(stringResponce).getString("prenom");
                        Client clientConnecte = new Client(nomRequete, prenomRequete, nomUtilisateur, motDePasse);
                        connexionLiveData.postValue(clientConnecte);
                    }

                }catch (IOException | JSONException e) {
                    connexionLiveData.postValue("Erreur de connexion au serveur");
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}

