package com.mirea.kt.ribo.recipeviewer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipe_table")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe_table WHERE name = :name LIMIT 1")
    Recipe findByName(String name);
}
