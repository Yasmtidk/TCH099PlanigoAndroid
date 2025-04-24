package com.example.test_planigo.modeles.dao;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test_planigo.modeles.entitees.Client;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.example.test_planigo.modeles.singleton.ClientActuel;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class ClientRepository {
    private static String URL_POINT_ENTREE = "http://10.0.2.2:80/H2025_TCH099_02_C1/api/";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MutableLiveData<Object> connexionLiveData = new MutableLiveData<>();
    private final MutableLiveData<Client> clientInfo = new MutableLiveData<>();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private Context context; // Gardé pour l'instant, bien qu'inutilisé dans les méthodes montrées

    public ClientRepository(Application application) {
        this.context = application.getApplicationContext();
    }

    public LiveData<Object> getConnexion() {
        return connexionLiveData;
    }
    public LiveData<Client> getInfoClient(){return clientInfo;}

    /**
     * Créer un nouvel utilisateur dans la base de données.
     * Modifie connexionLiveData à true si c'est un succès ou envoie un string (message d'erreur) s'il y a une erreur.
     * @param client Le client à ajouter dans la base de données.
     */
    public void postNouveauClient(Client client) throws JSONException { // JSONException peut être levée par postObj.put
        (new Thread() {
            @Override
            public void run() {
                try {
                    // Préparer l'objet JSON pour la requête
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", client.getNom_utilisateur());
                    postObj.put("motDePasse", client.getMot_de_passe());
                    postObj.put("nom", client.getNom());
                    postObj.put("prenom", client.getPrenom());

                    // Envoyer la requête et récupérer la réponse
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "inscription.php/inscrire/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    if (!response.isSuccessful()) throw new IOException("Code inattendu " + response); // Gestion erreur HTTP

                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce); // Analyser une seule fois
                    String statutRequete = jsonResponse.getString("statut");

                    // Si le nom d'utilisateur est déjà présent (identifiant) ou autre erreur : affiche erreur
                    // Sinon, on renvoie true
                    if (statutRequete.equals("error")) {
                        String messageRequete = jsonResponse.getString("message");
                        connexionLiveData.postValue(messageRequete);
                    } else if (statutRequete.equals("success")) {
                        connexionLiveData.postValue(true); // Indiquer le succès
                    } else {
                        // Gérer les statuts inattendus
                        connexionLiveData.postValue("Réponse inattendue du serveur.");
                    }

                } catch (IOException | JSONException e) {
                    // Envoyer un message d'erreur générique
                    connexionLiveData.postValue("Erreur de communication ou de formatage.");
                    // Envisager de logger l'exception: e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * Vérifie si les champs d'entrée d'une connexion sont valides.
     * Modifie connexionLiveData selon le résultat.
     * Envoie le client récupéré en cas de succès ou envoie un string (message d'erreur) en cas d'erreur.
     * @param nomUtilisateur Le nom d'utilisateur présumé du client.
     * @param motDePasse  Le mot de passe présumé du client.
     */
    public void connexion(String nomUtilisateur, String motDePasse) throws JSONException { // Peut être levée par postObj.put
        (new Thread() {
            @Override
            public void run() {
                try {
                    // Préparer l'objet JSON pour la requête
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", nomUtilisateur);
                    postObj.put("motDePasse", motDePasse);

                    // Envoyer la requête et récupérer la réponse
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "login.php/login/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    if (!response.isSuccessful()) throw new IOException("Code inattendu " + response); // Gestion erreur HTTP

                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce); // Analyser une seule fois
                    String statutRequete = jsonResponse.getString("statut");

                    // Afficher le message d'erreur ou créer le client selon le statut de la réponse
                    if (statutRequete.equals("error")) {
                        String messageRequete = jsonResponse.getString("message");
                        connexionLiveData.postValue(messageRequete);
                    } else if (statutRequete.equals("success")) {
                        // Créer le nouveau client avec les informations reçues
                        String nomRequete = jsonResponse.getString("nom");
                        String prenomRequete = jsonResponse.getString("prenom");
                        Client clientConnecte = new Client(nomRequete, prenomRequete, nomUtilisateur, motDePasse);
                        // Potentiellement récupérer et définir d'autres champs si l'API les fournit
                        // if (jsonResponse.has("description")) clientConnecte.setDescription(jsonResponse.getString("description"));
                        // if (jsonResponse.has("age")) clientConnecte.setAge(jsonResponse.getInt("age"));
                        ClientActuel.setClientConnecter(clientConnecte);
                        connexionLiveData.postValue(true); // Envoyer l'objet Client complet
                    } else {
                        // Gérer les statuts inattendus
                        connexionLiveData.postValue("Réponse inattendue du serveur.");
                    }

                } catch (IOException | JSONException e) {
                    connexionLiveData.postValue("Erreur de connexion au serveur");
                    // Envisager de logger l'exception: e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Récupérer les informations du client
     * @param utilisateurAConsulter L'utilisateur qu'on veut regarder
     */
    public void recupereInfoClient(String utilisateurAConsulter){
        (new Thread(){
            public void run(){
                try{
                    // Préparer l'objet JSON pour la requête
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("userAVoir", utilisateurAConsulter);

                    // Envoyer la requête et récupérer la réponse
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "profil.php/afficher-compte/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce); // Analyser une seule fois
                    String statutRequete = jsonResponse.getString("statut");

                    // Afficher le message d'erreur ou créer le client selon le statut de la réponse
                    if (statutRequete.equals("error")) {
                        String messageRequete = jsonResponse.getString("message");
                        connexionLiveData.postValue(messageRequete);
                    } else if (statutRequete.equals("success")) {
                        //Update la liste des recette abrégé
                        ObjectMapper mapper = new ObjectMapper();
                        JSONObject clientObject = new JSONObject(stringResponce).getJSONObject("client");
                        Client client = mapper.readValue(clientObject.toString(), Client.class);
                        clientInfo.postValue(client);
                    } else {
                        // Gérer les statuts inattendus
                        connexionLiveData.postValue("Réponse inattendue du serveur.");
                    }

                } catch (JSONException e){
                    connexionLiveData.postValue("Erreur de connexion au serveur");
                }
                catch (IOException e) {
                    connexionLiveData.postValue("Erreur de connexion au serveur");
                    // Envisager de logger l'exception: e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Modifier les informations sur le client
     * @param newIdentifiant Le nouveau identifiant du client
     * @param newMotDePasse Le nouveau mot de passe du client
     * @param newDescription La nouvelle description du client
     */
    public void setInfoClient(String newIdentifiant, String newMotDePasse, String newDescription){
        (new Thread(){
            public void run(){
                try{
                    // Préparer l'objet JSON pour la requête
                    JSONObject postObj = new JSONObject();
                    postObj.put("identifiant", ClientActuel.getClientConnecter().getNom_utilisateur());
                    postObj.put("motDePasse", ClientActuel.getClientConnecter().getMot_de_passe());
                    postObj.put("nouveau_identifiant", newIdentifiant);
                    postObj.put("nouveau_motDePasse", newMotDePasse);
                    postObj.put("description", newDescription);

                    // Envoyer la requête et récupérer la réponse
                    RequestBody corpsPostRequete = RequestBody.create(postObj.toString(), JSON);
                    Request postRequete = new Request.Builder()
                            .url(URL_POINT_ENTREE + "profil.php/modifer-client/")
                            .post(corpsPostRequete)
                            .build();
                    Response response = okHttpClient.newCall(postRequete).execute();
                    ResponseBody responseBody = response.body();
                    String stringResponce = responseBody.string();
                    JSONObject jsonResponse = new JSONObject(stringResponce); // Analyser une seule fois
                    String statutRequete = jsonResponse.getString("statut");

                    // Afficher le message d'erreur ou créer le client selon le statut de la réponse
                    if (statutRequete.equals("error")) {
                        String messageRequete = jsonResponse.getString("message");
                        connexionLiveData.postValue(messageRequete);
                    } else if (statutRequete.equals("success")) {
                        //Update le client actuel
                        JSONObject clientObject = new JSONObject(stringResponce).getJSONObject("client");
                        ClientActuel.getClientConnecter().setNom_utilisateur(newIdentifiant);
                        ClientActuel.getClientConnecter().setMot_de_passe(newMotDePasse);
                        ClientActuel.getClientConnecter().setDescription(newDescription);
                        connexionLiveData.postValue(true);
                    } else {
                        // Gérer les statuts inattendus
                        connexionLiveData.postValue("Réponse inattendue du serveur.");
                    }

                } catch (JSONException e){
                    connexionLiveData.postValue("Erreur de connexion au serveur");
                }
                catch (IOException e) {
                    connexionLiveData.postValue("Erreur de connexion au serveur");
                    // Envisager de logger l'exception: e.printStackTrace();
                }
            }
        }).start();
    }
}