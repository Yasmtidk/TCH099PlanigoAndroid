// GroceryListAdapter.java
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

public class GroceryListAdapter extends ArrayAdapter<GroceryListAdapter.GroceryItem> {

    private Context context;
    private List<GroceryItem> groceryItems;

    public GroceryListAdapter(Context context, List<GroceryItem> groceryItems) {
        super(context, 0, groceryItems);
        this.context = context;
        this.groceryItems = groceryItems;
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

        GroceryItem groceryItem = groceryItems.get(position);
        if (groceryItem != null) {
            holder.nameTextView.setText(groceryItem.getName());
            holder.quantityTextView.setText(groceryItem.getQuantity() + " " + groceryItem.getUnit());
            //TODO Set the correct icon based on groceryItem.getIcon()
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

    public static class GroceryItem {
        private String name;
        private int quantity;
        private String unit;
        private String icon;

        public GroceryItem(String name, int quantity, String unit, String icon) {
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