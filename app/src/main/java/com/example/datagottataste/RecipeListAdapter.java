package com.example.datagottataste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;


import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private Context context;
    private List<RecipeBd> recipeList;
    //private buttonClickListener buttonClickListener;
    Button button;
    final RecycleViewInterface recycleViewInterface;
    public RecipeListAdapter(Context context, List<RecipeBd> recipeList, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.recipeList = recipeList;
        this.recycleViewInterface = recycleViewInterface;
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_recipe, parent, false);
        return new RecipeViewHolder(view, recycleViewInterface, recipeList);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeBd recipe = recipeList.get(position);
        holder.RecipeNameTextView.setText(recipe.getName());
        holder.CalTextView.setText(recipe.getCal());
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            Picasso.get().load(recipe.getImageUrl()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }
        //holder.BtnAddToDietDay.setOnClickListener(v -> buttonClickListener.onButtonClick(position));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView RecipeNameTextView;
        TextView CalTextView;
        Button BtnAddToDietDay;
        RecycleViewInterface recycleViewInterface;
        private List<RecipeBd> recipeList;
        public RecipeViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface, List<RecipeBd> recipeList) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            RecipeNameTextView = itemView.findViewById(R.id.textViewName);
            CalTextView = itemView.findViewById(R.id.textViewCal);
            BtnAddToDietDay = itemView.findViewById(R.id.addToDietDayBtn);
            int pos = getAdapterPosition();
            this.recycleViewInterface = recycleViewInterface;
            this.recipeList = recipeList;
            BtnAddToDietDay.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if ( recycleViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            RecipeBd recipe = recipeList.get(pos);
                            recycleViewInterface.onClickDelete(pos,recipe);
                        }
                    }
                    return true;
                }
            });
        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                RecipeBd recipe = recipeList.get(pos); // Get the recipe based on the position
                recycleViewInterface.onItemClick(recipe); // Pass the recipe object
            }
        }
    }
}