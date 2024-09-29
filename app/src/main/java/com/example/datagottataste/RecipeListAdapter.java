package com.example.datagottataste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private Context context;
    private List<RecipeBd> recipeList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(RecipeBd event);
    }

    public RecipeListAdapter(Context context, List<RecipeBd> recipeList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.recipeList = recipeList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_recipe, parent, false);
        return new RecipeViewHolder(view);
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
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView RecipeNameTextView;
        TextView CalTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            RecipeNameTextView = itemView.findViewById(R.id.textViewName);
            CalTextView = itemView.findViewById(R.id.textViewCal);
        }
    }
}