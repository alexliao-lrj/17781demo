package com.example.demo.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.model.Firefood;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

public class FirefoodAdapter extends FirestoreAdapter<FirefoodAdapter.ViewHolder> {

    public interface OnFoodSelectedListener{
        void onFoodSelected(DocumentSnapshot food);
    }

    private OnFoodSelectedListener mListener;

    public FirefoodAdapter(Query query, OnFoodSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.food_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    //a food item view
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodNameView;
        TextView totalCalView;
        TextView foodServing;
        TextView foodCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            totalCalView = itemView.findViewById(R.id.item_foodCal);
            foodNameView = itemView.findViewById(R.id.item_foodName);
            foodServing = itemView.findViewById(R.id.item_serving);
            foodCategory = itemView.findViewById(R.id.item_category);
        }

        public void bind(final DocumentSnapshot snapshot, final OnFoodSelectedListener listener){
            Firefood food = snapshot.toObject(Firefood.class);
            foodNameView.setText(food.getName());
            totalCalView.setText(String.valueOf(food.getTotalCal()) + " kcal");
            foodServing.setText(String.valueOf(food.getServing()) + " g");

            if(food.getCategory() == 1){
                foodCategory.setText("Breakfast");
                foodCategory.setTextColor(Color.rgb(255, 133, 102));
            }
            else if(food.getCategory() == 2){
                foodCategory.setText("Lunch");
                foodCategory.setTextColor(Color.rgb(133, 224, 133));
            }
            else if(food.getCategory() == 3){
                foodCategory.setText("Dinner");
                foodCategory.setTextColor(Color.rgb(255, 219, 77));
            }
            else if(food.getCategory() == 4){
                foodCategory.setText("Snack");
                foodCategory.setTextColor(Color.rgb(128, 179, 255));
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onFoodSelected(snapshot);
                    }
                }
            });
        }
    }
}
