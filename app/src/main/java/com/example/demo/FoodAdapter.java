package com.example.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    //private List<FoodItem> foodList;
    private FoodListViewModel foodListViewModel;

    public FoodAdapter(FoodListViewModel foodListViewModel){
        //this.foodList = foodList;
        this.foodListViewModel = foodListViewModel;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent,false);
        FoodViewHolder foodViewHolder = new FoodViewHolder(itemView);
        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem curItem = foodListViewModel.getFoodList().getValue().get(position);
        holder.foodName.setText(curItem.getFoodName());
        holder.foodCal.setText(curItem.getFoodCal() + " kcal");
    }

    @Override
    public int getItemCount() {
        return foodListViewModel.getFoodList().getValue().size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        public TextView foodName;
        public TextView foodCal;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.item_foodName);
            foodCal = itemView.findViewById(R.id.item_foodCal);
        }
    }
}
