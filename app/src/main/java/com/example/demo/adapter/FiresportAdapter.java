package com.example.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initialize views in sport_item.xml
        }

        public void bind(final DocumentSnapshot snapshot, final FiresportAdapter.OnSportSelectedListener listener){
            Firesport sport = snapshot.toObject(Firesport.class);

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
