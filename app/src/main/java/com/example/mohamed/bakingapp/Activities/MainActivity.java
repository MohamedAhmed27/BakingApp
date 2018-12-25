package com.example.mohamed.bakingapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.bakingapp.Model.Recipes;
import com.example.mohamed.bakingapp.R;
import com.example.mohamed.bakingapp.adapters.RecipeAdapter;
import com.example.mohamed.bakingapp.utilities.JsonUtilities;
import com.example.mohamed.bakingapp.utilities.NetworkUtil;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapteOnClickHandler{

    public static ArrayList<Recipes>recipes= recipes=new ArrayList<>();
    private String jsonResult;
    private RecipeAdapter mRecipeAdapter;
    public static final String ADAPTER_POS_TAG="pos";
    public static final String ARRAY_TAG_TO_DEATIL_ACTIVITY="arraytodet";
    @BindView(R.id.recycler_view)RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display)TextView mErrorMessageDisplay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(	"#303F9F")));

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
           GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
        }
        else
        {

                 LinearLayoutManager linearLayoutManager   = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }


        mRecyclerView.setHasFixedSize(false);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        loadRecipeData();

    }
    private void showRecipeDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void loadRecipeData() {
        showRecipeDataView();

        new FetchRecipe().execute();
    }
    private void showErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int adapterPosition) {
        Intent i = new Intent(this,RecipeDetails.class);
        i.putExtra(ADAPTER_POS_TAG,adapterPosition);
        i.putExtra(ARRAY_TAG_TO_DEATIL_ACTIVITY, recipes);
        startActivity(i);
    }

    public class FetchRecipe extends AsyncTask<String, Void, ArrayList<Recipes>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<Recipes> doInBackground(String... params) {




            URL RecipeUrl = NetworkUtil.buildUrl();


            try {
                jsonResult = NetworkUtil
                        .getResponseFromHttpUrl(RecipeUrl);

                recipes = JsonUtilities.ParseRecipesWithJson(jsonResult);


                return recipes;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipes> recipes) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (recipes != null) {
                showRecipeDataView();
                mRecipeAdapter.setRecipesData(recipes);
            }
            else
                {
                    showErrorMessage();
                }

        }
    }

}
