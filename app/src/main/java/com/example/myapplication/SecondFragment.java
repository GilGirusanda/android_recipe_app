package com.example.myapplication;

import java.util.Random;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentSecondBinding;
import com.example.myapplication.models.RecipeModel;
import com.example.myapplication.services.ImageService;
import com.example.myapplication.services.RecipeService;

import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private List<RecipeModel> meals;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // INIT SERVICES
        RecipeService recipeService = new RecipeService(getContext());
        ImageService imageService = new ImageService(getContext());

        meals = recipeService.getAll();

//        String myString = getArguments().getString("mealType");
//        Log.d("meals", "meals --> " + meals);
//        Toast.makeText(getContext(), getArguments().getString("mealType"), Toast.LENGTH_LONG).show();


        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecondNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentMeal();
            }
        });

        binding.buttonSecondPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        setCurrentMeal();

        binding.recipeDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setCurrentMeal() {
        Random rand = new Random();

        int currentMealInd = rand.nextInt(meals.size());

        binding.recipeTitle.setText(meals.get(currentMealInd).getTitle());
        binding.recipeDescription.setText(meals.get(currentMealInd).getDescription());
    }
}