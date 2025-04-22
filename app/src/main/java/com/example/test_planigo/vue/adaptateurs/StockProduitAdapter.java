package com.example.test_planigo.vue.adaptateurs;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test_planigo.R;
import com.example.test_planigo.VueModele.StockageViewModel;
import com.example.test_planigo.modeles.entitees.Produit;

public class StockProduitAdapter extends ArrayAdapter<Produit> {

    private Context context;
    private final Produit[] listeProduit;
    private final StockageViewModel stockageViewModel;
    private SparseBooleanArray checkedState = new SparseBooleanArray();

    public StockProduitAdapter(Context context, int viewRessourceId, Produit[] listeProduit, StockageViewModel stockageViewModel) {
        super(context, viewRessourceId, listeProduit);
        this.context = context;
        this.listeProduit = listeProduit;
        this.stockageViewModel = stockageViewModel;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvQuantite;
        ImageView ivIconDelete;
        ImageView ivIconEdit;
        ImageView ivIconImage;
        CheckBox itemCheckBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder();
            holder.tvName = view.findViewById(R.id.itemNameTextView);
            holder.tvQuantite = view.findViewById(R.id.itemQuantityTextView);
            holder.ivIconDelete = view.findViewById(R.id.itemDeleteImageView);
            holder.ivIconEdit = view.findViewById(R.id.itemEditImageView);
            holder.ivIconImage = view.findViewById(R.id.itemIconImageView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Produit produit = this.listeProduit[position];
        if (produit != null) {

            //Inscrire les bonnes informations de l'item selon la liste de produit
            holder.tvName.setText(produit.getName());
            holder.tvQuantite.setText(produit.getQuantity() + " " + produit.getUnit());
            // TODO: Set image based on produit.getIcon() if you have mapping logic

            //Supprimer l'item actuelle lorsqu'on click sur l'image delete
            holder.ivIconDelete.setOnClickListener(v -> {
                stockageViewModel.deleteProduit(produit);
            });

            //Aller à l'activite du popup produit lorsqu'on click sur l'Edit image
            holder.ivIconEdit.setOnClickListener(v -> {
                stockageViewModel.setAllerPopupProduit(produit);
            });
        }
        return view;
    }
}