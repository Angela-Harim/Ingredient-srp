package com.TD5.ingredient_srp.service;

import com.TD5.ingredient_srp.entity.Dish;
import com.TD5.ingredient_srp.entity.DishIngredient;
import com.TD5.ingredient_srp.entity.Ingredient;
import com.TD5.ingredient_srp.exception.DishNotFoundException;
import com.TD5.ingredient_srp.repository.DishRepository;
import com.TD5.ingredient_srp.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    public DishService(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Dish> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();

        for (Dish dish : dishes) {
            List<DishIngredient> ingredients = ingredientRepository.findByDishId(dish.getId());
            dish.setDishIngredients(ingredients);
        }

        return dishes;
    }

    public void updateDishIngredients(int dishId, List<Ingredient> ingredients) {

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException(
                        "Dish.id=" + dishId + " is not found"
                ));

        if (ingredients == null) {
            throw new IllegalArgumentException("Body is required");
        }

        dishRepository.deleteIngredientsByDishId(dishId);
        for (Ingredient ingredient : ingredients) {
            ingredientRepository.findById(ingredient.getId())
                    .ifPresent(i -> dishRepository.addIngredientToDish(dishId, i.getId()));
        }
    }
}
