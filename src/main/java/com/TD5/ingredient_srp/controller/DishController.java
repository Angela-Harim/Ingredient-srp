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
    private final DishService dishService ;

    public DishController(DishRepository repository, DishService dishService) {
        this.repository = repository;
        this.dishService = dishService;

    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return repository.findAll();
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateIngredients(
            @PathVariable int id,
            @RequestBody(required = false) List<Ingredient> ingredients) {

        if (ingredients == null) {
            return ResponseEntity.badRequest()
                    .body("Request body is required");
        }

        if (ingredients.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Ingredient list must not be empty");
        }

        try {
            dishService.updateDishIngredients(id, ingredients);
            return ResponseEntity.ok().build();
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
