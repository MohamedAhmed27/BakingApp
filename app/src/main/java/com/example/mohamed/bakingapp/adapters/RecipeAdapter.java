package com.example.mohamed.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mohamed.bakingapp.Model.Recipes;
import com.example.mohamed.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by Mohamed on 8/14/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private Context context;
    public ArrayList<Recipes> recipes=new ArrayList<Recipes>();

    private final RecipeAdapteOnClickHandler mClickHandler;


    public interface RecipeAdapteOnClickHandler {
        void onClick(int adapterPosition);
    }


    public RecipeAdapter(RecipeAdapteOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageButton image;
        public final TextView rec_text;
        public RecipeAdapterViewHolder(View view) {
            super(view);
            rec_text=(TextView)view.findViewById(R.id.name_rec);
            image = (ImageButton) view.findViewById(R.id.image_rec);

            image.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            mClickHandler.onClick(adapterPosition);
        }
    }


    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder Holder, int position) {
        String recipeText = recipes.get(position).getName();
        Holder.rec_text.setText(recipeText);



    }


    @Override
    public int getItemCount() {
        if (null == recipes) return 0;
        return recipes.size();
    }



    public void setRecipesData(ArrayList<Recipes> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}

