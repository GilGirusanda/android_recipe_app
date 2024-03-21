package com.example.myapplication;

import java.util.Collections;
import java.util.Optional;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentSecondBinding;
import com.example.myapplication.models.RecipeImageModel;
import com.example.myapplication.models.RecipeModel;
import com.example.myapplication.services.ImageService;
import com.example.myapplication.services.RecipeService;

import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private List<RecipeModel> recipes;
    private List<RecipeImageModel> images;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        RecipeService recipeService = new RecipeService(getContext());
        ImageService imageService = new ImageService(getContext());

        recipes = recipeService.getAll();
        images = imageService.getAll();

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecondNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentRecipe();
            }
        });

        binding.buttonSecondPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        setCurrentRecipe();

        binding.recipeDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setCurrentRecipe() {
        Collections.shuffle(recipes);
        Optional<RecipeModel> currentRecipe = recipes.stream().findAny();
        Optional<RecipeImageModel> currentImage = images.stream()
                .filter(image -> image.getRecipeId().equals(currentRecipe.get().getId()))
                .findFirst();

        if (currentImage.isPresent()) {
            binding.recipeImage.setImageBitmap(currentImage.get().getImage());
        }

        binding.recipeTitle.setText(currentRecipe.get().getTitle());
        binding.recipeDescription.setText(currentRecipe.get().getDescription());
    }
}