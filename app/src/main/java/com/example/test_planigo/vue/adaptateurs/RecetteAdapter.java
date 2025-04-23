package com.example.test_planigo.vue.adaptateurs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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

            //Afficher l'image
            String base64Image = recetteAbrege.getImage();

            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    // Supprimer le préfixe si présent (facultatif, selon ton backend)
                    if (base64Image.startsWith("data:")) {
                        base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                    }

                    // Décoder le Base64
                    byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);

                    // Convertir en Bitmap
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Afficher dans l'ImageView
                    holder.recetteListeImage.setImageBitmap(decodedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                    // En cas d'erreur, tu peux afficher une image par défaut
                    holder.recetteListeImage.setImageResource(R.drawable.planigologo);
                }
            } else {
                // Image vide ou nulle → image par défaut
                holder.recetteListeImage.setImageResource(R.drawable.planigologo);
            }

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