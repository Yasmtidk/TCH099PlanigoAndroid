package com.example.test_planigo.vue.adaptateurs;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test_planigo.R;
import java.util.List;
import java.util.Locale;

public class ItemStepAdapter extends RecyclerView.Adapter<ItemStepAdapter.StepViewHolder> {
    private List<String> listeEtapes;

    public ItemStepAdapter(List<String> listeEtapes) {
        this.listeEtapes = listeEtapes;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_step_detail, parent, false);
        return new StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        if (listeEtapes != null && position < listeEtapes.size()) {
            String etape = listeEtapes.get(position);
            holder.stepTextView.setText(String.format(Locale.getDefault(), "%d. %s", position + 1, etape));

            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(false);
            updateTextStyle(holder.stepTextView, false);

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                updateTextStyle(holder.stepTextView, isChecked);
            });
        }
    }

    private void updateTextStyle(TextView textView, boolean isChecked) {
        int colorRes = isChecked ? R.color.hint_text_color : R.color.black_text;
        int color = ContextCompat.getColor(textView.getContext(), colorRes);
        int paintFlags = textView.getPaintFlags();

        textView.setTextColor(color);

        if (isChecked) {
            textView.setPaintFlags(paintFlags | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(paintFlags & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return listeEtapes != null ? listeEtapes.size() : 0;
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView stepTextView;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.stepCheckBox);
            stepTextView = itemView.findViewById(R.id.stepTextView);
        }
    }
}