package com.example.demo.roomfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foodList = new ArrayList<>();
    private FoodListViewModel foodListViewModel;

    public FoodAdapter(FoodListViewModel foodListViewModel){
        this.foodListViewModel = foodListViewModel;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent,false);
        FoodViewHolder foodViewHolder = new FoodViewHolder(itemView);
        foodViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = v.findViewById(R.id.item_foodName);
                TextView cal = v.findViewById(R.id.item_foodCal);
                System.out.println("Name: " + name.getText());
                System.out.println("Cal: " + cal.getText());
            }
        });

        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food curItem = foodList.get(position);
        holder.foodName.setText(curItem.getFoodName());
        holder.foodCal.setText(curItem.getFoodCal() + " kcal");
    }

    @Override
    public int getItemCount() {
        return foodList.size();
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
