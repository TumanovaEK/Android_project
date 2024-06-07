package com.mirea.kt.ribo.recipeviewer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.kt.ribo.recipeviewer.database.Recipe;
import com.mirea.kt.ribo.recipeviewer.recipe.RecipeViewModel;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textViewRecipeName;
    private TextView textViewIngredients;
    private TextView textViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        textViewRecipeName = findViewById(R.id.text_view_recipe_name);
        textViewIngredients = findViewById(R.id.text_view_ingredients);
        textViewDescription = findViewById(R.id.text_view_description);
        Button buttonShare = findViewById(R.id.button_share);

        RecipeViewModel recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        recipeViewModel.getAllRecipes().observe(this, recipes -> {
            for (Recipe recipe : recipes) {
                if (recipe.getId() == recipeId) {
                    textViewRecipeName.setText(recipe.getName());
                    textViewIngredients.setText(recipe.getIngredients());
                    textViewDescription.setText(recipe.getDescription());

                    buttonShare.setOnClickListener(v -> {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, recipe.getDescription());
                        startActivity(Intent.createChooser(shareIntent, "Share Recipe"));
                    });

                    break;
                }
            }
        });
    }
}
