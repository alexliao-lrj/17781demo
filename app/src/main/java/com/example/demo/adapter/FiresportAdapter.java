package com.example.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.model.Firesport;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class FiresportAdapter extends FirestoreAdapter<FiresportAdapter.ViewHolder> {

    public interface OnSportSelectedListener{
        void onSportSelected(DocumentSnapshot sport);
    }

    private FiresportAdapter.OnSportSelectedListener mListener;

    public FiresportAdapter(Query query, FiresportAdapter.OnSportSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FiresportAdapter.ViewHolder(inflater.inflate(R.layout.sport_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    //a sport item view
    static class ViewHolder extends RecyclerView.ViewHolder{
        //声明xml中的views
        TextView sportName;
        TextView sportCalorie;
        ImageView sportImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initialize views in sport_item.xml
            sportName = itemView.findViewById(R.id.item_sportName);
            sportCalorie = itemView.findViewById(R.id.item_sportCalorie);
            sportImg = itemView.findViewById(R.id.item_sportImg);
        }

        public void bind(final DocumentSnapshot snapshot, final FiresportAdapter.OnSportSelectedListener listener){
            Firesport sport = snapshot.toObject(Firesport.class);
            sportName.setText(sport.getName());
            sportCalorie.setText(String.valueOf(sport.getTotalCal()) + " kcal");

            if(sport.getCategory() == 1){
                sportImg.setImageResource(R.drawable.rope_skipping);
            }else if(sport.getCategory() == 2){
                sportImg.setImageResource(R.drawable.walk);
            }else if(sport.getCategory() == 3){
                sportImg.setImageResource(R.drawable.running);
            }else if(sport.getCategory() == 4){
                sportImg.setImageResource(R.drawable.stairs);
            }else if(sport.getCategory() == 5){
                sportImg.setImageResource(R.drawable.swim);
            }else if(sport.getCategory() == 6){
                sportImg.setImageResource(R.drawable.bike);
            }else if(sport.getCategory() == 7){
                sportImg.setImageResource(R.drawable.dance);
            }else if(sport.getCategory() == 8){
                sportImg.setImageResource(R.drawable.yoga);
            }else if(sport.getCategory() == 9){
                sportImg.setImageResource(R.drawable.ball_game);
            }else if(sport.getCategory() == 11){
                sportImg.setImageResource(R.drawable.ski);
            }else if(sport.getCategory() == 10){
                sportImg.setImageResource(R.drawable.skate_board);
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSportSelected(snapshot);
                    }
                }
            });
        }
    }
}
