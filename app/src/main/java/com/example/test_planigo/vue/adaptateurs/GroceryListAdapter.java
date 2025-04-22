package com.example.test_planigo.vue.adaptateurs;

import android.content.Context;
import android.graphics.Paint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.test_planigo.R;
import com.example.test_planigo.modeles.entitees.Produit;

import java.util.List;

public class GroceryListAdapter extends ArrayAdapter<Produit> {

    private Context context;
    private List<Produit> groceryItems;
    private SparseBooleanArray checkedState = new SparseBooleanArray();
    public GroceryListAdapter(Context context, List<Produit> groceryItems) {
        super(context, 0, groceryItems);
        this.context = context;
        this.groceryItems = groceryItems;
    }

    static class ViewHolder {
        CheckBox checkBox;
        ImageView iconImageView;
        TextView nameTextView;
        TextView quantityTextView;
        ImageView editImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_grocery, parent, false);
            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.itemCheckBox);
            holder.iconImageView = convertView.findViewById(R.id.itemIconImageView);
            holder.nameTextView = convertView.findViewById(R.id.itemNameTextView);
            holder.quantityTextView = convertView.findViewById(R.id.itemQuantityTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Produit groceryItem = groceryItems.get(position);
        if (groceryItem != null) {
            holder.nameTextView.setText(groceryItem.getName());
            String quantityString = String.valueOf(groceryItem.getQuantity());
            if (quantityString.endsWith(".0")) {
                quantityString = quantityString.substring(0, quantityString.length() - 2);
            }
            holder.quantityTextView.setText(quantityString + " " + groceryItem.getUnit());
            //TODO Set the correct icon based on groceryItem.getIcon()
            holder.iconImageView.setImageResource(R.drawable.planigologo);

            boolean isChecked = checkedState.get(position, false);
            holder.checkBox.setChecked(isChecked);
            updateTextStyle(holder.nameTextView, isChecked);

            holder.checkBox.setOnClickListener(v -> {
                boolean newState = !checkedState.get(position, false);
                checkedState.put(position, newState);
                updateTextStyle(holder.nameTextView, newState);
            });
        }

        return convertView;
    }

    private void updateTextStyle(TextView textView, boolean isChecked) {
        if (isChecked) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textView.setTextColor(ContextCompat.getColor(context, R.color.hint_text_color));
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            textView.setTextColor(ContextCompat.getColor(context, R.color.black_text));
        }
    }
}