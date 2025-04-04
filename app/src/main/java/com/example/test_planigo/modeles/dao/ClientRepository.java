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

    public void postNouveauClient(Client client) throws JSONException {

        (new Thread() {
            @Override
            public void run() {

                //long newId = 0;

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
/*
                Request requeteId = new Request.Builder().url(URL_POINT_ENTREE + "inscription.php/").build();
                try (Response reponseId = okHttpClient.newCall(requeteId).execute()) {
                    ResponseBody corpsReponseId = reponseId.body();
                    if(corpsReponseId != null) {
                        newId = new JSONObject(corpsReponseId.string()).getLong("id") + 1;
                    }

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }


                try {
                    JSONObject postObj = new JSONObject();
                    postObj.put("id", newId);
                    postObj.put("nom", client.getNom());
                    postObj.put("prenom", client.getPrenom());
                    postObj.put("nom_utilisateur", client.getNom_utilisateur());
                    postObj.put("mot_de_passe", client.getMot_de_passe());

                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);
                    Request postRequete = new Request.Builder().url(URL_POINT_ENTREE).post(corpsPostRequete).build();
                    Response putReponse = okHttpClient.newCall(postRequete).execute();
                    putReponse.close();

                    JSONObject putObj = new JSONObject();
                    putObj.put("id", newId);

                    RequestBody corpsPutRequete = RequestBody.create(putObj.toString(), JSON);
                    Request putRequeteIdPut = new Request.Builder().url(URL_POINT_ENTREE + "-id").put(corpsPutRequete).build();
                    Response postReponseIdPut =  okHttpClient.newCall(putRequeteIdPut).execute();
                    postReponseIdPut.close();

                    connexionLiveData.postValue(true);

                } catch (IOException | JSONException e) {
                    connexionLiveData.postValue(false);
                    throw new RuntimeException(e);
                }*/
            }
        }).start();
    }

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

                /*
                Request requete = new Request.Builder().url(URL_POINT_ENTREE).build();
                try (Response reponse = okHttpClient.newCall(requete).execute()) {

                    Client clientConnecte = null;

                    ResponseBody corpsReponse = reponse.body();
                    if(corpsReponse != null) {

                        Client[] clients = mapper.readValue(corpsReponse.string(), Client[].class);
                        for (Client client : clients) {
                            if (client.getNom_utilisateur().equals(nomUtilisateur) && client.getMot_de_passe().equals(motDePasse)) {

                                clientConnecte = client;
                                break;
                            }
                        }

                        if (clientConnecte != null) {
                            connexionLiveData.postValue(clientConnecte);
                        } else {
                            connexionLiveData.postValue("Les informations entrées sont incorrects");
                        }
                    }
                } catch (IOException e) {
                    connexionLiveData.postValue("Erreur de connexion au serveur");
                    throw new RuntimeException(e);
                }*/
            }
        }).start();
    }
}

