package com.yasmi.tch099planigo.vue.adaptateurs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yasmi.tch099planigo.R;
import com.yasmi.tch099planigo.modeles.entitees.Recette;
import com.yasmi.tch099planigo.vue.activites.RecipeDetailPageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecetteAdapter extends RecyclerView.Adapter<RecetteAdapter.RecetteViewHolder> {

    private Context context;
    private List<Recette> recettes;

    public RecetteAdapter(Context context) {
        this.context = context;
    }

    public void setRecettes(List<Recette> recettes) {
        this.recettes = recettes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecetteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_card, parent, false);
        return new RecetteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetteViewHolder holder, int position) {
        if (recettes != null && position < recettes.size()) {
            Recette recette = recettes.get(position);
            holder.recipeNameTextView.setText(recette.getNom());
            holder.recipeDescriptionTextView.setText(recette.getDescription());
            holder.recipeTimeTextView.setText(recette.getTemps_de_cuisson() + " minutes");
            Picasso.get()
                    .load(recette.getImage_url())
                    .placeholder(R.drawable.planigologo)
                    .error(R.drawable.planigologo)
                    .into(holder.recipeImageView);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, RecipeDetailPageActivity.class);
                intent.putExtra("recipe_id", recette.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return recettes != null ? recettes.size() : 0;
    }

    public static class RecetteViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameTextView;
        TextView recipeDescriptionTextView;
        TextView recipeTimeTextView;
        ImageView recipeImageView;

        public RecetteViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView);
            recipeDescriptionTextView = itemView.findViewById(R.id.recipeDescriptionTextView);
            recipeTimeTextView = itemView.findViewById(R.id.recipeTimeTextView);
            recipeImageView = itemView.findViewById(R.id.recipeImageView);
        }
    }
}