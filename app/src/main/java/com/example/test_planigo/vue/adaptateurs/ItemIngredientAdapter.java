package com.example.test_planigo.vue.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.Produit;

import java.util.List;

public class ItemIngredientAdapter extends RecyclerView.Adapter<ItemIngredientAdapter.IngredientViewHolder> {
    private List<Produit> listeProduit;

    public ItemIngredientAdapter(List<Produit> listeProduit){
        this.listeProduit = listeProduit;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe_detail, parent, false);
        return new IngredientViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position){
        if(listeProduit != null && position < listeProduit.size()){
            Produit produit = listeProduit.get(position);
            holder.produitListeNom.setText(produit.getName());
            holder.produitListeQuantity.setText(produit.getQuantity() + " " + produit.getUnit());
        }
    }

    public int getItemCount(){
        return listeProduit != null ? listeProduit.size() : 0;
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder{
        TextView produitListeNom;
        TextView produitListeQuantity;

        public IngredientViewHolder(@NonNull View itemView){
            super(itemView);
            produitListeNom = itemView.findViewById(R.id.ingredientNameTextView);
            produitListeQuantity = itemView.findViewById(R.id.ingredientQuantityTextView);
        }
    }
}
