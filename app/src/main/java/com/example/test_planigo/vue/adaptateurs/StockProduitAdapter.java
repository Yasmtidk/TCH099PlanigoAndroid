package com.example.test_planigo.vue.adaptateurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.StockageViewModel;
import com.example.test_planigo.modeles.entitees.Produit;

public class StockProduitAdapter extends ArrayAdapter<Produit> {

    private Context context;
    private final Produit[] listeProduit;
    private final StockageViewModel stockageViewModel;

    public StockProduitAdapter(Context context, int viewRessourceId, Produit[] listeProduit, StockageViewModel stockageViewModel) {
        super(context, viewRessourceId, listeProduit);
        this.context = context;
        this.listeProduit = listeProduit;
        this.stockageViewModel = stockageViewModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, parent, false);

        }

        final Produit produit = this.listeProduit[position];
        if (produit != null) {

            TextView tvName = (TextView) view.findViewById(R.id.itemNameTextView);
            TextView tvQuantite = (TextView) view.findViewById(R.id.itemQuantityTextView);
            ImageView ivIconDelete = (ImageView) view.findViewById(R.id.itemDeleteImageView);
            ImageView ivIconEdit = (ImageView) view.findViewById(R.id.itemEditImageView);
            ImageView ivIconImage = (ImageView) view.findViewById(R.id.itemIconImageView);

            //Inscrire les bonnes informations de l'item selon la liste de produit
            tvName.setText(produit.getName());
            tvQuantite.setText(produit.getQuantity() + " " + produit.getUnit());


            //Supprimer l'item actuelle lorsqu'on click sur l'image delete
            ivIconDelete.setOnClickListener(v -> {
                stockageViewModel.deleteProduit(produit);
            });

            //Aller à l'activite du popup produit lorsqu'on click sur l'Edit image
            ivIconEdit.setOnClickListener(v ->{
                stockageViewModel.setAllerPopupProduit(produit);
            });
        }

        return view;
    }
}