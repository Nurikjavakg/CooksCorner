package neo.cookscorner.service;

import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.recipe.PaginationResponse;
import neo.cookscorner.dto.recipe.RecipeRequest;
import neo.cookscorner.dto.recipe.RecipeResponse;
import neo.cookscorner.enums.CategoryOfMeal;
import neo.cookscorner.enums.Difficulty;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface RecipeService {
    SimpleResponse save(RecipeRequest request, List<MultipartFile> images);
    PaginationResponse getByCategoryId(int categoryId, int currentPage, int pageSize);
    RecipeResponse getRecipeById(Long recipeId);
    PaginationResponse findRecipeByName(int currentPage, int pageSize, String recipeName);
}