package com.example.test_planigo.modeles.singleton;

import com.example.test_planigo.modeles.entitees.Client;

public final class ClientActuel {

    private static Client clientConnecter = null;

    public static void setClientConnecter(Client nouveauClient){
        clientConnecter = nouveauClient;
    }

    public static Client getClientConnecter(){
        return clientConnecter;
    }
}
