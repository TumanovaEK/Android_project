package com.mirea.kt.ribo.recipeviewer.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeRepository {

    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> allRecipes;
    private ExecutorService executorService;

    public RecipeRepository(Application application) {
        RecipeDatabase database = RecipeDatabase.getInstance(application);
        recipeDao = database.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insert(Recipe recipe) {
        executorService.execute(() -> {
            if (recipeDao.findByName(recipe.getName()) == null) {
                recipeDao.insert(recipe);
            }
        });
    }

    public void delete(Recipe recipe) {
        executorService.execute(() -> recipeDao.delete(recipe));
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }
}
