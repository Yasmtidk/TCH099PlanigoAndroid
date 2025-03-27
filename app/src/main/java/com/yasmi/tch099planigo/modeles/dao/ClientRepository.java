package com.yasmi.tch099planigo.modeles.dao;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yasmi.tch099planigo.modeles.entitees.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class ClientRepository {
    private static String URL_POINT_ENTREE = "http://10.0.2.2:3000/clients";
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

                long newId = 0;

                Request requeteId = new Request.Builder().url(URL_POINT_ENTREE + "-id").build();
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
                }
            }
        }).start();
    }

    public void connexion(String nomUtilisateur, String motDePasse) throws JSONException {
        (new Thread() {
            @Override
            public void run() {

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
                }
            }
        }).start();
    }
}