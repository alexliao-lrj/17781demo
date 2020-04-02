package com.example.demo.adapter;

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

public class FirefoodAdapter extends FirestoreAdapter<FirefoodAdapter.ViewHolder> {

    public FirefoodAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.food_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodNameView;
        TextView totalCalView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            totalCalView = itemView.findViewById(R.id.item_foodCal);
            foodNameView = itemView.findViewById(R.id.item_foodName);
        }

        public void bind(final DocumentSnapshot snapshot){
            Firefood food = snapshot.toObject(Firefood.class);
            foodNameView.setText(food.getName());
            totalCalView.setText(String.valueOf(food.getTotalCal()));
        }
    }
}