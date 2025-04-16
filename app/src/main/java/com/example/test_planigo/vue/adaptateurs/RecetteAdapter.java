package com.example.test_planigo.vue.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.function.Consumer;

public class RecetteAdapter extends RecyclerView.Adapter<RecetteAdapter.RecetteViewHolder> {

    private List<RecetteAbrege> recetteAbreges;
    private final Consumer<RecetteAbrege> onItemClick;

    public RecetteAdapter(List<RecetteAbrege> recetteAbreges, Consumer<RecetteAbrege> onItemClick) {
        this.recetteAbreges = recetteAbreges;
        this.onItemClick = onItemClick;
    }

    public void setData(List<RecetteAbrege> recettesAbreges) {
        this.recetteAbreges = recettesAbreges;
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
        if (recetteAbreges != null && position < recetteAbreges.size()) {
            RecetteAbrege recetteAbrege = recetteAbreges.get(position);
            holder.recetteListeNom.setText(recetteAbrege.getNom());
            holder.recetteListeType.setText(recetteAbrege.getType());
            holder.recetteListeTempsPreparation.setText(recetteAbrege.getTemps_de_cuisson() + " minutes");
            Picasso.get()
                    .load(recetteAbrege.getImage())
                    .placeholder(R.drawable.planigologo)
                    .error(R.drawable.planigologo)
                    .into(holder.recetteListeImage);

            // Appelle le callback pour informer l'Activity
            holder.itemView.setOnClickListener(v -> onItemClick.accept(recetteAbrege));
        }
    }

    @Override
    public int getItemCount() {
        return recetteAbreges != null ? recetteAbreges.size() : 0;
    }

    public static class RecetteViewHolder extends RecyclerView.ViewHolder {
        TextView recetteListeNom;
        TextView recetteListeTempsPreparation;
        TextView recetteListeType;
        ImageView recetteListeImage;

        public RecetteViewHolder(@NonNull View itemView) {
            super(itemView);
            recetteListeNom = itemView.findViewById(R.id.recetteListeNom);
            recetteListeTempsPreparation = itemView.findViewById(R.id.recetteListeTempsPreparation);
            recetteListeType = itemView.findViewById(R.id.recetteListeType);
            recetteListeImage = itemView.findViewById(R.id.recetteListeImage);
        }
    }
}