package com.example.test_planigo.vue.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.PlanRepasSommaire;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlanRepasAdapter extends RecyclerView.Adapter<PlanRepasAdapter.PlanRepasViewHolder>{

    private List<PlanRepasSommaire> listePlanRepasSommaire = new ArrayList<>();
    private final Consumer<PlanRepasSommaire> onItemClick;

    public PlanRepasAdapter(List<PlanRepasSommaire> listePlanRepasSommaire, Consumer<PlanRepasSommaire> onItemClick){
        this.listePlanRepasSommaire = listePlanRepasSommaire;
        this.onItemClick = onItemClick;
    }

    public void setData(List<PlanRepasSommaire> nouvelleListe){
        this.listePlanRepasSommaire = nouvelleListe != null ? nouvelleListe : new ArrayList<>();
        notifyDataSetChanged();
    }

    public PlanRepasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // Crée la vue à partir du layout XML 'item_recipe_card.xml'
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_planificateur_liste, parent, false);
        return new PlanRepasViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull PlanRepasViewHolder holder, int position){
        if(listePlanRepasSommaire != null && position < listePlanRepasSommaire.size()){
            PlanRepasSommaire planRepasSommaire = listePlanRepasSommaire.get(position);

            holder.titre.setText(planRepasSommaire.getTitre());
            holder.description.setText(planRepasSommaire.getDescriptions());

            holder.itemView.setOnClickListener(v -> onItemClick.accept(planRepasSommaire));
        }
    }

    public int getItemCount() {
        return listePlanRepasSommaire != null ? listePlanRepasSommaire.size() : 0;
    }

    public static class PlanRepasViewHolder extends RecyclerView.ViewHolder{
        TextView titre;
        TextView description;

        public PlanRepasViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.nomPlanificateurTextView);
            description = itemView.findViewById(R.id.descriptionPlanificateurTextView);
        }
    }
}
