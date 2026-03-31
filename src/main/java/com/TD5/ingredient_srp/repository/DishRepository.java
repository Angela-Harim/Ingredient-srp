package com.TD5.ingredient_srp.repository;

import com.TD5.ingredient_srp.entity.Dish;
import com.TD5.ingredient_srp.entity.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<Ingredient> findIngredientsByDishWithFilters(int dishId, String name, Double price) {
        List<Ingredient> ingredients = new ArrayList<>();


        DataSource dataSource = jdbc.getDataSource();
        if (dataSource == null) {
            throw new RuntimeException("DataSource non disponible");
        }

        StringBuilder sql = new StringBuilder("""
            SELECT i.id, i.name, i.price 
            FROM ingredient i
            JOIN dish_ingredient di ON i.id = di.id_ingredient
            WHERE di.id_dish = ?
        """);

        if (name != null && !name.isEmpty()) {
            sql.append(" AND i.name ILIKE ?");
        }
        if (price != null) {
            sql.append(" AND i.price BETWEEN ? AND ?");
        }


        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, dishId);

            if (name != null && !name.isEmpty()) {
                ps.setString(paramIndex++, "%" + name + "%");
            }
            if (price != null) {
                ps.setDouble(paramIndex++, price - 50);
                ps.setDouble(paramIndex++, price + 50);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setPrice(rs.getDouble("price"));
                    ingredients.add(ing);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du filtrage des ingrédients", e);
        }
        return ingredients;
    }
}