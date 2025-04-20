package com.example.test_planigo.vue.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;

import java.util.List;

public class ItemUniqueAdapter extends RecyclerView.Adapter<ItemUniqueAdapter.ItemUniqueViewHolder> {

    public List<String> listeItem;

    public ItemUniqueAdapter(List<String> listeItem) {
        this.listeItem = listeItem;
    }

    @NonNull
    @Override
    public ItemUniqueAdapter.ItemUniqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_liste_ingredient_popup, parent, false);
        return new ItemUniqueAdapter.ItemUniqueViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ItemUniqueAdapter.ItemUniqueViewHolder holder, int position){
        if(listeItem != null && position < listeItem.size()){
            String nom = listeItem.get(position);
            holder.produitListeNom.setText(nom);
        }
    }

    public int getItemCount(){
        return listeItem != null ? listeItem.size():0;
    }

    public class ItemUniqueViewHolder extends RecyclerView.ViewHolder{
        TextView produitListeNom;
        public ItemUniqueViewHolder(@NonNull View itemView){
            super(itemView);
            produitListeNom = itemView.findViewById(R.id.txtItemIngredient);
        }
    }

}
