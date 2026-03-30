package com.TD5.ingredient_srp.service;


import com.TD5.ingredient_srp.dto.StockResponse;
import com.TD5.ingredient_srp.entity.Ingredient;
import com.TD5.ingredient_srp.repository.IngredientRepository;
import com.TD5.ingredient_srp.repository.StockMovementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository repository;
    private final StockMovementRepository stockMovementRepository;

    public IngredientService(IngredientRepository repository,
                             StockMovementRepository stockMovementRepository) {
        this.repository = repository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public List<Ingredient> getAll() {
        return repository.findAll();
    }

    public Ingredient getById(int id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Ingredient.id=" + id + " is not found"
                        )
                );
    }

    public StockResponse getStock(int id, String at, String unit) {

        if (at == null || unit == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Either mandatory query parameter `at` or `unit` is not provided."
            );
        }

        repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Ingredient.id=" + id + " is not found"
                        )
                );

        Instant date = Instant.parse(at);
        double stockValue = stockMovementRepository.getStock(id, date);

        return new StockResponse(unit, stockValue);
    }
}
