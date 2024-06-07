package com.mirea.kt.ribo.recipeviewer.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kt.ribo.recipeviewer.R;
import com.mirea.kt.ribo.recipeviewer.database.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private List<Recipe> recipes = new ArrayList<>();
    private final OnItemClickListener itemClickListener;
    private final OnDeleteClickListener deleteClickListener;

    public RecipeAdapter(OnItemClickListener itemClickListener, OnDeleteClickListener deleteClickListener) {
        this.itemClickListener = itemClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new RecipeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        Recipe currentRecipe = recipes.get(position);
        holder.textViewTitle.setText(currentRecipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final Button deleteButton;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            deleteButton = itemView.findViewById(R.id.button_delete);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(recipes.get(position));
                }
            });
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (deleteClickListener != null && position != RecyclerView.NO_POSITION) {
                    deleteClickListener.onDeleteClick(recipes.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Recipe recipe);
    }
}
