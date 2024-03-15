package neo.cookscorner.dto.recipe;

import neo.cookscorner.entities.Image;
import neo.cookscorner.entities.Ingredient;
import neo.cookscorner.enums.CategoryOfMeal;
import neo.cookscorner.enums.Difficulty;
import java.util.List;

public record RecipeRequest(
        String recipeName,
        String description,
        Difficulty difficulty,
        CategoryOfMeal categoryOfMeal,
        String preparationTime,
        List<String> ingredientNames,
        List<String> quantities
) {
}