package com.TD5.ingredient_srp.repository;

import com.TD5.ingredient_srp.entity.Dish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DishRepository {

    private final JdbcTemplate jdbc;

    public DishRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Dish> findAll() {
        String sql = "SELECT id, name, price FROM dish";

        return jdbc.query(sql, (rs, rowNum) -> {
            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setPrice(rs.getDouble("price"));
            return dish;
        });
    }

    public Optional<Dish> findById(int id) {
        String sql = "SELECT id, name, price FROM dish WHERE id = ?";

        List<Dish> result = jdbc.query(sql, (rs, rowNum) -> {
            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setPrice(rs.getDouble("price"));
            return dish;
        }, id);

        return result.isEmpty()
                ? Optional.empty()
                : Optional.of(result.get(0));
    }

    public void deleteIngredientsByDishId(int dishId) {
        String sql = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        jdbc.update(sql, dishId);
    }

    public void addIngredientToDish(int dishId, int ingredientId) {
        String sql = "INSERT INTO dish_ingredient (id_dish, id_ingredient) VALUES (?, ?)";
        jdbc.update(sql, dishId, ingredientId);
    }
}