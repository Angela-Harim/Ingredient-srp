package com.TD5.ingredient_srp.controller;


import com.TD5.ingredient_srp.entity.Dish;
import com.TD5.ingredient_srp.entity.Ingredient;
import com.TD5.ingredient_srp.exception.DishNotFoundException;
import com.TD5.ingredient_srp.repository.DishRepository;
import com.TD5.ingredient_srp.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishRepository repository;
    private final DishService dishService;

    public DishController(DishRepository repository, DishService dishService) {
        this.repository = repository;
        this.dishService = dishService;

    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return repository.findAll();
    }

    @GetMapping("/{id}/ingredients")
    public List<Ingredient> getDishIngredients(
            @PathVariable int id,
            @RequestParam(required = false) String ingredientName,
            @RequestParam(required = false) Double ingredientPriceAround) {

        return dishService.getIngredientsByDishWithFilters(id, ingredientName, ingredientPriceAround);
    }

}
