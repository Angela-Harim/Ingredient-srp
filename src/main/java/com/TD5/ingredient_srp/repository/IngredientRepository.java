package com.TD5.ingredient_srp.repository;

import com.TD5.ingredient_srp.entity.Ingredient;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepository {
    private final JdbcTemplate jdbc;

    public IngredientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Ingredient> findAll() {
        return jdbc.query("SELECT * FROM ingredient", new BeanPropertyRowMapper<>(Ingredient.class));
    }

    public Optional<Ingredient> findById(int id) {
        try {
            Ingredient ing = jdbc.queryForObject("SELECT * FROM ingredient WHERE id = ?",
                    new BeanPropertyRowMapper<>(Ingredient.class), id);
            return Optional.ofNullable(ing);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
