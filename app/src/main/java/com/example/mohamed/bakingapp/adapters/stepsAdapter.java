package com.example.mohamed.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mohamed.bakingapp.Model.RecipeSteps;
import com.example.mohamed.bakingapp.Model.Recipes;
import com.example.mohamed.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by Mohamed on 8/16/2018.
 */

public class stepsAdapter extends RecyclerView.Adapter<stepsAdapter.RecipeStepsAdapterViewHolder>
{
    private Context context;
    public ArrayList<RecipeSteps> recipesSteps=new ArrayList<RecipeSteps>();

    private final stepsAdapter.stepsAdapterAdapteOnClickHandler mClickHandler;


    public interface stepsAdapterAdapteOnClickHandler {
        void onClick(int adapterPosition);
    }


    public stepsAdapter(stepsAdapter.stepsAdapterAdapteOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class RecipeStepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView step_number;
        public final TextView step_text;
        public final CardView cardView;
        public RecipeStepsAdapterViewHolder(View view) {
            super(view);
            step_number=(TextView)view.findViewById(R.id.step_number);
            step_text =(TextView) view.findViewById(R.id.step_text);
            cardView=view.findViewById(R.id.card_view);

            cardView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            mClickHandler.onClick(adapterPosition);
        }
    }


    @Override
    public stepsAdapter.RecipeStepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new stepsAdapter.RecipeStepsAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(stepsAdapter.RecipeStepsAdapterViewHolder holder, int position) {
        holder.step_number.setText(String.valueOf(recipesSteps.get(position).getId()+1));
        holder.step_text.setText(recipesSteps.get(position).getShortDescription());

    }


    @Override
    public int getItemCount() {
        if (null == recipesSteps) return 0;
        return recipesSteps.size();
    }



    public void setRecipeStepsDataData(ArrayList<RecipeSteps> recipesSteps) {
        this.recipesSteps = recipesSteps;
        notifyDataSetChanged();
    }

}
