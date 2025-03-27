package com.yasmi.tch099planigo.vue.adaptateurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yasmi.tch099planigo.R;

import java.util.List;

public class StockIngredientAdapter extends ArrayAdapter<StockIngredientAdapter.StockIngredient> {

    private Context context;
    private List<StockIngredient> ingredients;

    public StockIngredientAdapter(Context context, List<StockIngredient> ingredients) {
        super(context, 0, ingredients);
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.itemCheckBox);
            holder.iconImageView = convertView.findViewById(R.id.itemIconImageView);
            holder.nameTextView = convertView.findViewById(R.id.itemNameTextView);
            holder.quantityTextView = convertView.findViewById(R.id.itemQuantityTextView);
            holder.deleteImageView = convertView.findViewById(R.id.itemDeleteImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StockIngredient ingredient = ingredients.get(position);
        if (ingredient != null) {
            holder.nameTextView.setText(ingredient.getName());
            holder.quantityTextView.setText(ingredient.getQuantity() + " " + ingredient.getUnit());
            //TODO Set the correct icon based on ingredients.getIcon()
            holder.iconImageView.setImageResource(R.drawable.planigologo);
            holder.deleteImageView.setImageResource(R.drawable.trash);
        }

        return convertView;
    }

    static class ViewHolder {
        CheckBox checkBox;
        ImageView iconImageView;
        TextView nameTextView;
        TextView quantityTextView;
        ImageView deleteImageView;
    }

    public static class StockIngredient {
        private String name;
        private int quantity;
        private String unit;
        private String icon;

        public StockIngredient(String name, int quantity, String unit, String icon) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getUnit() {
            return unit;
        }

        public String getIcon() {
            return icon;
        }
    }
}