package com.example.demo;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodListFragment extends Fragment {

    private FloatingActionButton addFoodBtn;
    private FloatingActionButton backMainBtn;
    private FloatingActionButton clearAllBtn;
    private NavController foodNavController;
    private RecyclerView foodRecyclerView;
    private FoodAdapter foodListAdapter;
    private RecyclerView.LayoutManager foodLayoutManager;

    private FoodListViewModel foodListViewModel;

    public FoodListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        final FragmentActivity activity = requireActivity();
        foodListViewModel = new ViewModelProvider(activity).get(FoodListViewModel.class);

        foodRecyclerView = activity.findViewById(R.id.foodListRecyclerView);

        buildRecyclerView(activity);

        backMainBtn = activity.findViewById(R.id.floatingBackButton);
        backMainBtn.setOnClickListener((v) -> {
            Intent homeIntent = new Intent(activity, MainActivity.class);
            startActivity(homeIntent);
        });

        addFoodBtn = activity.findViewById(R.id.floatingAddButton);
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodNavController = Navigation.findNavController(v);
                foodNavController.navigate(R.id.addFoodFragment);
            }
        });

        clearAllBtn = activity.findViewById(R.id.floatingClearButton);
        clearAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListViewModel.clearAllFood();
            }
        });

        foodListViewModel.getAllFoodsLive().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foodItems) {
                foodListAdapter.setFoodList(foodItems);
                foodListAdapter.notifyDataSetChanged();
            }
        });
    }


    public void buildRecyclerView(FragmentActivity activity){
        foodLayoutManager = new LinearLayoutManager(activity);
        foodListAdapter = new FoodAdapter(foodListViewModel);
        foodRecyclerView.setLayoutManager(foodLayoutManager);
        foodRecyclerView.setAdapter(foodListAdapter);
    }

}
