package com.example.mohamed.bakingapp.utilities;

import android.util.Log;

import com.example.mohamed.bakingapp.Model.RecipeIngredients;
import com.example.mohamed.bakingapp.Model.RecipeSteps;
import com.example.mohamed.bakingapp.Model.Recipes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohamed on 8/14/2018.
 */

public class JsonUtilities {
    public static final String TAG_ARRAY_INGREDIENTS="ingredients";
    public static final String TAG_ARRAY_STEPS="steps";
    public static ArrayList<Recipes> ParseRecipesWithJson(String JSON) throws JSONException {
        ArrayList <Recipes> recipes =new ArrayList<Recipes>();

        JSONArray main=new JSONArray(JSON);

        for(int i=0;i<main.length();i++) {
            ArrayList <RecipeIngredients>recipeIngredients  =new ArrayList<RecipeIngredients>();
            ArrayList <RecipeSteps> recipeSteps =new ArrayList<RecipeSteps>();
            JSONObject recipe = main.getJSONObject(i);
            JSONArray ingredients = recipe.getJSONArray(TAG_ARRAY_INGREDIENTS);
            JSONArray steps = recipe.getJSONArray(TAG_ARRAY_STEPS);

            for (int j = 0; j < ingredients.length(); j++){
                JSONObject ingredientsObject=ingredients.getJSONObject(j);
                recipeIngredients.add(j,new RecipeIngredients(ingredientsObject.optString("quantity"),ingredientsObject.optString("measure"),ingredientsObject.optString("ingredient")));
        }
        for(int k=0;k<steps.length();k++ )
        {
            JSONObject stepsObject=steps.getJSONObject(k);
            recipeSteps.add(k,new RecipeSteps(stepsObject.optInt("id"),stepsObject.optString("shortDescription"),stepsObject.optString("description"),stepsObject.optString("videoURL"),stepsObject.optString("thumbnailURL")));

        }
        recipes.add(i,new Recipes(recipe.optString("name"),recipe.optInt("id"),recipeIngredients,recipeSteps));



        }

        return recipes;
    }



}
