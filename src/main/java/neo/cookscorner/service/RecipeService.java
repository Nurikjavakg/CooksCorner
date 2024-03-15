package neo.cookscorner.service;

import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.recipe.PaginationResponse;
import neo.cookscorner.dto.recipe.RecipeRequest;
import neo.cookscorner.dto.recipe.RecipeResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface RecipeService {
    SimpleResponse save(RecipeRequest request, List<MultipartFile> images);
    PaginationResponse getByCategoryBreakfast(int currentPage, int pageSize);
    PaginationResponse getByCategoryDinner(int currentPage, int pageSize);
    PaginationResponse getByCategoryLunch(int currentPage, int pageSize);
    RecipeResponse getRecipeById(Long recipeId);
}
