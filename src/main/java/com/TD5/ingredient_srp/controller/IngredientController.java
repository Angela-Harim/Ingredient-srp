package com.TD5.ingredient_srp.controller;

import com.TD5.ingredient_srp.dto.StockResponse;
import com.TD5.ingredient_srp.entity.Ingredient;
import com.TD5.ingredient_srp.repository.StockMovementRepository;
import com.TD5.ingredient_srp.repository.IngredientRepository;
import com.TD5.ingredient_srp.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository repository;
    private final StockMovementRepository stockRepo;
    private final IngredientService service;

    public IngredientController(IngredientRepository repository, StockMovementRepository stockRepo, IngredientService service) {
        this.repository = repository;
        this.stockRepo = stockRepo;
        this.service = service;
    }

    @GetMapping
    public List<Ingredient> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return repository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Ingredient.id=" + id + " is not found"));
    }


    @GetMapping("/{id}/stock")
    public StockResponse getStock(
            @PathVariable int id,
            @RequestParam String at,
            @RequestParam String unit
    ) {
        return service.getStock(id, at, unit);
    }
}
