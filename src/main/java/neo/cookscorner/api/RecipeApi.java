package neo.cookscorner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.recipe.PaginationResponse;
import neo.cookscorner.dto.recipe.RecipeRequest;
import neo.cookscorner.dto.recipe.RecipeResponse;
import neo.cookscorner.enums.CategoryOfMeal;
import neo.cookscorner.enums.Difficulty;
import neo.cookscorner.service.RecipeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
@Tag(name = "Recipe Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RecipeApi {
    private final RecipeService recipeService;


    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "You can save recipe")
    @PostMapping("/create_recipe")
    public SimpleResponse saveRecipe(@RequestPart("dto") RecipeRequest recipeRequest,
                                     @RequestPart("files") List<MultipartFile> images) {
        return recipeService.save(recipeRequest, images);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/get_by_category_id")
    @Operation(summary = "Get recipes by category ID")
    public PaginationResponse getByCategoryId(@RequestParam int categoryId,
                                              @RequestParam int currentPage,
                                              @RequestParam int pageSize) {

       return recipeService.getByCategoryId(categoryId,currentPage,pageSize);
    }


    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/get_recipe_by_id/{recipeId}")
    @Operation(summary = "You can get recipe by id")
    public RecipeResponse getRecipeById(@PathVariable Long recipeId) {
        return recipeService.getRecipeById(recipeId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/find_recipe_by_name")
    @Operation(summary = "You can find recipe by name")
    PaginationResponse findRecipeByName(@RequestParam int currentPage,
                                        @RequestParam int pageSize,
                                        @RequestParam String recipeName) {
        return recipeService.findRecipeByName(currentPage, pageSize, recipeName);
    }
}