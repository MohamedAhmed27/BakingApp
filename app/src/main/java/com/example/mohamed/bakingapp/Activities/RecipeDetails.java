package com.example.mohamed.bakingapp.Activities;

import android.animation.ObjectAnimator;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mohamed.bakingapp.Model.RecipeSteps;
import com.example.mohamed.bakingapp.Model.Recipes;
import com.example.mohamed.bakingapp.R;
import com.example.mohamed.bakingapp.adapters.stepsAdapter;
import com.example.mohamed.bakingapp.fragments.video_detail_fragment;
import com.example.mohamed.bakingapp.widget.RecipeWidgetProvider;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetails extends AppCompatActivity implements stepsAdapter.stepsAdapterAdapteOnClickHandler,View.OnClickListener {

    public ArrayList<Recipes>recipes=new ArrayList<>();
    public static final String ADAPTER_POS_TAG="pos";
    public static final String ARRAY_TAG_TO_DEATIL_ACTIVITY="arraytodet";
    public static final String ARRAY_TAG_TO_VIDEO_ACTIVITY="arraytovideo";
    public int clickedPosition;
    public static Recipes oneRecipe;
    public static LinearLayoutManager layoutManager;
    public stepsAdapter mStepsAdapter;
    public final String STATE="state";
    public int state;
    @BindView(R.id.ingredients) TextView ingredients;
    @BindView(R.id.item_description_img)ImageView imageView;
    @BindView(R.id.recycler_view_steps)RecyclerView mStepsRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);
        if(savedInstanceState != null) {
          state=savedInstanceState.getInt(STATE);
        }

        Intent k=getIntent();
        if (k.hasExtra(ADAPTER_POS_TAG) && k.hasExtra(ARRAY_TAG_TO_DEATIL_ACTIVITY)) {
            clickedPosition = k.getIntExtra(ADAPTER_POS_TAG, 0);

            recipes = (ArrayList<Recipes>) k.getSerializableExtra(ARRAY_TAG_TO_DEATIL_ACTIVITY);

        }
        else
        {
            closeOnError();
        }

        oneRecipe=recipes.get(clickedPosition);
        if(oneRecipe==null)
        {
            closeOnError();
        }
        setIngredients();
        initializeRecyclerviewAndSetData(oneRecipe.getRecipeSteps());
       setTitle(oneRecipe.getName());
       imageView.setOnClickListener(this);




    }
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(state==0)
        {
            ingredients.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);

        }
        else
        {

            ingredients.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_expand_less_black_24dp);
        }
    }

    void collapseExpandTextView() {
        if (ingredients.getVisibility() == View.GONE) {
            ingredients.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.yaw), "Recipe Added To Widget", Snackbar.LENGTH_LONG);
            snackbar.show();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

            RecipeWidgetProvider.updateWidget(this,appWidgetManager,appWidgetIds,getIngredients(),oneRecipe.getName()+" "+"Ingredients");
            imageView.setImageResource(R.drawable.ic_expand_less_black_24dp);
        } else {
            ingredients.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }

        ObjectAnimator animation = ObjectAnimator.ofInt(ingredients, "maxLines", ingredients.getMaxLines());
        animation.setDuration(200).start();
    }
    String getIngredients()
    {
        String ingredients="";
        for(int i =0;i<oneRecipe.getRecipeIngredients().size();i++)
        {
            ingredients+=oneRecipe.getRecipeIngredients().get(i).getIngredient()+" "+
                    oneRecipe.getRecipeIngredients().get(i).getQuantity()+
                    oneRecipe.getRecipeIngredients().get(i).getMeasure()+"\n";

        }
        return ingredients;
    }
    void setIngredients()
    {
        for(int i =0;i<oneRecipe.getRecipeIngredients().size();i++)
        {
            ingredients.append(Html.fromHtml("<br/>"+"<b>"+oneRecipe.getRecipeIngredients().get(i).getIngredient()+"</b>"+"" +
                    "   ("+oneRecipe.getRecipeIngredients().get(i).getQuantity()+" "+oneRecipe.getRecipeIngredients().get(i).getMeasure() +")"));
        }

    }
    void initializeRecyclerviewAndSetData(ArrayList<RecipeSteps> recipeSteps)
    {
        layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mStepsRecyclerView.setLayoutManager(layoutManager);
        mStepsRecyclerView.setHasFixedSize(false);
        mStepsAdapter = new stepsAdapter(this);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsAdapter.setRecipeStepsDataData(recipeSteps);
    }



    @Override
        public void onClick(int adapterPosition) {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if(tabletSize) {
            Bundle arguments = new Bundle();
            arguments.putInt(ADAPTER_POS_TAG, adapterPosition);
            arguments.putSerializable("yaw", oneRecipe);
            video_detail_fragment fragment = new video_detail_fragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, fragment)
                    .commit();
        }
        else {


            Intent i = new Intent(this, VideoActivity.class);
            i.putExtra(ADAPTER_POS_TAG, adapterPosition);
            i.putExtra(ARRAY_TAG_TO_VIDEO_ACTIVITY, oneRecipe);
            startActivity(i);
        }
    }


    @Override
    public void onClick(View view) {

        collapseExpandTextView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (ingredients.getVisibility() == View.GONE) {
            outState.putInt(STATE, 0);
        }
        else
        {
            outState.putInt(STATE,1);
        }
        super.onSaveInstanceState(outState);

    }
}
