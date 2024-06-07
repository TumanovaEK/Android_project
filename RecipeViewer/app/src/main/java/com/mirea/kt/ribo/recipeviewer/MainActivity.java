package com.mirea.kt.ribo.recipeviewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kt.ribo.recipeviewer.database.Recipe;
import com.mirea.kt.ribo.recipeviewer.recipe.RecipeAdapter;
import com.mirea.kt.ribo.recipeviewer.recipe.RecipeViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecipeAdapter recipeAdapter;
    private RecipeViewModel recipeViewModel;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(
                recipe -> {
                    Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                    intent.putExtra("RECIPE_ID", recipe.getId());
                    startActivity(intent);
                },
                recipe -> recipeViewModel.delete(recipe)
        );
        recyclerView.setAdapter(recipeAdapter);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.getAllRecipes().observe(this, recipes -> {
            recipeAdapter.setRecipes(recipes);
        });

        Button addRecipeButton = findViewById(R.id.button_add_recipe);
        addRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });

        // Загрузка данных при первом запуске
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("recipeData")) {
            String recipeData = intent.getStringExtra("recipeData");
            loadDataFromServer(recipeData);
        }
    }

    private void loadDataFromServer(String recipeData) {
        executor.execute(() -> {
            try {
                JSONArray jsonArray = new JSONArray(recipeData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String description = jsonObject.getString("description");
                    String ingredients = jsonObject.getString("ingredients");
                    Recipe recipe = new Recipe(name, ingredients, description);
                    recipeViewModel.insert(recipe);
                }
                handler.post(() -> {
                    // Данные уже добавлены в базу данных, обновление UI не требуется, так как LiveData обновит UI автоматически
                });
            } catch (JSONException e) {
                Log.e("MainActivity", "Error parsing JSON data", e);
            }
        });
    }
}
