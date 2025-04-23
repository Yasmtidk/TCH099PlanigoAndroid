package com.example.test_planigo.vue.adaptateurs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.AccueilNavigationItem;
import java.util.List;

public class AccueilAdapter extends RecyclerView.Adapter<AccueilAdapter.ViewHolder> {

    private List<AccueilNavigationItem> navigationItems;
    private Context context;

    public AccueilAdapter(Context context, List<AccueilNavigationItem> navigationItems) {
        this.context = context;
        this.navigationItems = navigationItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_accueil_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccueilNavigationItem item = navigationItems.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.iconImageView.setImageResource(item.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, item.getTargetActivity());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return navigationItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.accueilCardIcon);
            titleTextView = itemView.findViewById(R.id.accueilCardTitle);
        }
    }
}