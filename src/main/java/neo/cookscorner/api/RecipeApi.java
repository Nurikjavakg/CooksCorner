package neo.cookscorner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.recipe.PaginationResponse;
import neo.cookscorner.dto.recipe.RecipeRequest;
import neo.cookscorner.dto.recipe.RecipeResponse;
import neo.cookscorner.service.RecipeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
@Tag(name = "Recipe Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RecipeApi {
    private final RecipeService recipeService;


    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "You can save recipe")
    @PostMapping("/createRecipe")
    public SimpleResponse saveRecipe(@RequestPart ("dto")RecipeRequest recipeRequest, @RequestPart ("files") List<MultipartFile> images) {
        return recipeService.save(recipeRequest,images);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getByCategoryBreakfast")
    @Operation(summary = "You can get by category breakfast")
    PaginationResponse getByCategoryBreakfast(@RequestParam int currentPage,
                                              @RequestParam int pageSize) {
        return recipeService.getByCategoryBreakfast(currentPage, pageSize);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getByCategoryDinner")
    @Operation(summary = "You can get by category dinner")
    PaginationResponse getByCategoryDinner(@RequestParam int currentPage,
                                              @RequestParam int pageSize) {
        return recipeService.getByCategoryDinner(currentPage, pageSize);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getByCategoryLunch")
    @Operation(summary = "You can get by category lunch")
    PaginationResponse getByCategoryLunch(@RequestParam int currentPage,
                                              @RequestParam int pageSize) {
        return recipeService.getByCategoryLunch(currentPage, pageSize);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/getRecipeById/{recipeId}")
    @Operation(summary = "You can get recipe by id")
    public RecipeResponse getRecipeById(@PathVariable Long recipeId) {
        return recipeService.getRecipeById(recipeId);
    }
}