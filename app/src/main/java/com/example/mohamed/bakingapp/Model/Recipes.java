package com.example.mohamed.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mohamed on 8/14/2018.
 */

public class Recipes implements Serializable {

    int id;
    String name;
    ArrayList<RecipeIngredients>recipeIngredients;
    ArrayList<RecipeSteps>recipeSteps;

    public Recipes()
    {

    }
    public Recipes(String Name,int id, ArrayList<RecipeIngredients> recipeIngredients, ArrayList<RecipeSteps> recipeSteps) {
        this.id = id;
        this.name=Name;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<RecipeIngredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(ArrayList<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public void setRecipeSteps(ArrayList<RecipeSteps> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public ArrayList<RecipeSteps> getRecipeSteps() {
        return recipeSteps;
    }

    public Recipes(int id) {
        this.id = id;

    }

    public void setId(int id) {
        this.id = id;
    }





    public int getId() {
        return id;
    }


}

