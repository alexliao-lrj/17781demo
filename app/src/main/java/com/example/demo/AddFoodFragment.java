package com.example.demo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodFragment extends Fragment {

    private Button foodBtnSubmit;
    private NavController foodNavController;
    private TextView foodName;
    private TextView foodCal;

    private FoodListViewModel foodListViewModel;

    public AddFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        foodListViewModel = new ViewModelProvider(requireActivity()).get(FoodListViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_food, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();

        foodName = activity.findViewById(R.id.editTextFoodName);
        foodCal = activity.findViewById(R.id.editTextKCal);

        foodBtnSubmit = activity.findViewById(R.id.foodBtnSubmit);
        foodBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodNavController = Navigation.findNavController(v);
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Result");
                builder.setMessage(R.string.add_food_result);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FoodItem newFood = new FoodItem(foodName.getText().toString(), Integer.valueOf(foodCal.getText().toString()));
                        foodListViewModel.addFoodItem(newFood);
                        foodNavController.navigateUp();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

}
