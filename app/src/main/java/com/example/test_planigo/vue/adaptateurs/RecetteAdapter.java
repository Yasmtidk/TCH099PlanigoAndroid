package com.example.test_planigo.vue.adaptateurs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.RecetteAbrege;
// Picasso n'est plus utilisé, remplacé par le décodage Base64

import java.util.ArrayList; // Import pour l'initialisation
import java.util.List;
import java.util.Locale; // Import pour String.format
import java.util.function.Consumer; // Import pour le callback de clic

public class RecetteAdapter extends RecyclerView.Adapter<RecetteAdapter.RecetteViewHolder> {

    // Liste des recettes abrégées à afficher
    private List<RecetteAbrege> recetteAbreges = new ArrayList<>();
    // Callback à appeler lorsqu'un item est cliqué
    private final Consumer<RecetteAbrege> onItemClick;

    /**
     * Constructeur de l'adaptateur.
     * @param recetteAbreges Liste initiale de recettes (peut être vide).
     * @param onItemClick    Callback appelé lors d'un clic sur un item.
     */
    public RecetteAdapter(List<RecetteAbrege> recetteAbreges, Consumer<RecetteAbrege> onItemClick) {
        // S'assurer que la liste n'est jamais nulle
        this.recetteAbreges = recetteAbreges != null ? recetteAbreges : new ArrayList<>();
        this.onItemClick = onItemClick;
    }

    /**
     * Met à jour la liste des recettes affichées par l'adaptateur.
     * @param nouvellesRecettesAbreges La nouvelle liste de recettes.
     */
    public void setData(List<RecetteAbrege> nouvellesRecettesAbreges) {
        // S'assurer que la nouvelle liste n'est pas nulle
        this.recetteAbreges = nouvellesRecettesAbreges != null ? nouvellesRecettesAbreges : new ArrayList<>();
        // Notifie le RecyclerView que les données ont changé pour qu'il se redessine
        // Pour de meilleures performances sur de grandes listes, utiliser DiffUtil
        notifyDataSetChanged();
    }

    /**
     * Crée une nouvelle vue (ViewHolder) pour un item de recette.
     * Appelé par le LayoutManager lorsqu'il a besoin d'une nouvelle vue.
     */
    @NonNull
    @Override
    public RecetteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crée la vue à partir du layout XML 'item_recipe_card.xml'
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_card, parent, false);
        return new RecetteViewHolder(itemView);
    }

    /**
     * Remplit une vue (ViewHolder) existante avec les données d'une recette spécifique.
     * Appelé par le LayoutManager pour afficher les données à une position donnée.
     */
    @Override
    public void onBindViewHolder(@NonNull RecetteViewHolder holder, int position) {
        // Vérifier si la position est valide dans la liste
        if (recetteAbreges != null && position < recetteAbreges.size()) {
            // Récupérer la recette à cette position
            RecetteAbrege recetteAbrege = recetteAbreges.get(position);

            // Définir les textes dans le ViewHolder
            holder.recetteListeNom.setText(recetteAbrege.getNom());
            holder.recetteListeType.setText(recetteAbrege.getType());
            holder.recetteListeTempsPreparation.setText(String.format(Locale.getDefault(), "%d minutes", recetteAbrege.getTemps_de_cuisson()));

            // --- Logique de chargement d'image depuis Base64 ---
            String base64Image = recetteAbrege.getImage();
            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    // Supprimer le préfixe "data:image/..." si présent
                    if (base64Image.startsWith("data:")) {
                        base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                    }
                    // Décoder la chaîne Base64
                    byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    // Convertir en Bitmap
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    // Afficher l'image
                    holder.recetteListeImage.setImageBitmap(decodedImage);
                } catch (IllegalArgumentException e) {
                    // Erreur de décodage Base64
                    Log.e("RecetteAdapter", "Erreur décodage Base64 pour " + recetteAbrege.getNom() + ": " + e.getMessage());
                    holder.recetteListeImage.setImageResource(R.drawable.planigologo); // Image de repli
                } catch (Exception e) {
                    // Autres erreurs (mémoire, etc.)
                    Log.e("RecetteAdapter", "Erreur traitement image pour " + recetteAbrege.getNom() + ": " + e.getMessage());
                    holder.recetteListeImage.setImageResource(R.drawable.planigologo); // Image de repli
                }
            } else {
                // Si l'image est vide ou nulle, afficher l'image par défaut
                holder.recetteListeImage.setImageResource(R.drawable.planigologo);
            }
            // --- Fin de la logique de chargement d'image ---

            // Configurer le clic sur l'item pour appeler le callback
            holder.itemView.setOnClickListener(v -> onItemClick.accept(recetteAbrege));
        }
    }

    /**
     * Retourne le nombre total d'items dans la liste.
     * Appelé par le LayoutManager.
     */
    @Override
    public int getItemCount() {
        return recetteAbreges != null ? recetteAbreges.size() : 0;
    }

    /**
     * ViewHolder contenant les références aux vues d'un item de recette.
     * Permet d'éviter les appels répétitifs à findViewById.
     */
    public static class RecetteViewHolder extends RecyclerView.ViewHolder {
        TextView recetteListeNom;
        TextView recetteListeTempsPreparation;
        TextView recetteListeType;
        ImageView recetteListeImage;

        public RecetteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Récupérer les références des vues dans le layout de l'item
            recetteListeNom = itemView.findViewById(R.id.recetteListeNom);
            recetteListeTempsPreparation = itemView.findViewById(R.id.recetteListeTempsPreparation);
            recetteListeType = itemView.findViewById(R.id.recetteListeType);
            recetteListeImage = itemView.findViewById(R.id.recetteListeImage);
        }
    }
}