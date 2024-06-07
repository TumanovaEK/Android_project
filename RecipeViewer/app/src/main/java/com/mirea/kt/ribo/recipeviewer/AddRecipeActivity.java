package com.mirea.kt.ribo.recipeviewer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.kt.ribo.recipeviewer.database.Recipe;
import com.mirea.kt.ribo.recipeviewer.recipe.RecipeViewModel;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextIngredients;
    private EditText editTextDescription;
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        editTextName = findViewById(R.id.edit_text_name);
        editTextIngredients = findViewById(R.id.edit_text_ingredients);
        editTextDescription = findViewById(R.id.edit_text_description);
        Button buttonSave = findViewById(R.id.button_save);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String ingredients = editTextIngredients.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            if (name.isEmpty() || ingredients.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                Recipe recipe = new Recipe(name, ingredients, description);
                recipeViewModel.insert(recipe);
                finish();
            }
        });
    }
}
