package neo.cookscorner.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.recipe.PaginationResponse;
import neo.cookscorner.dto.recipe.RecipeRequest;
import neo.cookscorner.dto.recipe.RecipeResponse;
import neo.cookscorner.entities.*;
import neo.cookscorner.exceptions.NotFoundException;
import neo.cookscorner.repository.*;
import neo.cookscorner.repository.jdbcTemplate.RecipeJDBCTemplate;
import neo.cookscorner.service.CloudinaryService;
import neo.cookscorner.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;
    private final IngredientRepository ingredientRepository;
    private final RecipeJDBCTemplate recipeJDBCTemplate;

    public User getAuthFromUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> {
                    log.info("User with email: " + email + " not found!");
                    return new NotFoundException(String.format("User with email address: %s not found!", email));
                });
    }

    @Override
    @Transactional
    public SimpleResponse save(RecipeRequest request, List<MultipartFile> images) {
        User user = getAuthFromUser();

        Recipe recipe = new Recipe();
        recipe.setRecipeName(request.recipeName());
        recipe.setDescription(request.description());
        recipe.setDifficulty(request.difficulty());
        recipe.setCategoryOfMeal(request.categoryOfMeal());
        recipe.setPreparationTime(request.preparationTime());
        List<Image> recipeImages = new ArrayList<>();
        iterateOverPhotos(images, recipeImages);
        recipe.setImages(recipeImages);

        List<Ingredient> ingredients = new ArrayList<>();

        List<String> ingredientNames = request.ingredientNames();
        List<String> quantities = request.quantities();

        if (ingredientNames.size() != quantities.size()) {
            throw new IllegalArgumentException("Ingredient names and quantities lists must have the same size");
        }

        for (int i = 0; i < ingredientNames.size(); i++) {
            Ingredient ingredient = new Ingredient();
            ingredient.setNames(ingredientNames.get(i));
            ingredient.setQuantities(quantities.get(i));
            ingredient.setRecipe(recipe);
            ingredients.add(ingredient);
        }

        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);
        ingredientRepository.saveAll(ingredients);

        recipe.setOwner(user);
        user.getRecipes().add(recipe);
        log.info("Recipe successfully saved in database...");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Recipe successfully saved...")
                .build();
    }

    @Override
    public PaginationResponse getByCategoryBreakfast(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<RecipeResponse> trips = recipeJDBCTemplate.getByCategoryBreakfast(pageable);
        return PaginationResponse.builder()
                .recipeResponseList(trips.getContent())
                .currentPage(trips.getNumber() + 1)
                .pageSize(trips.getTotalPages()).build();
    }

    @Override
    public PaginationResponse getByCategoryDinner(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<RecipeResponse> trips = recipeJDBCTemplate.getByCategoryDinner(pageable);
        return PaginationResponse.builder()
                .recipeResponseList(trips.getContent())
                .currentPage(trips.getNumber() + 1)
                .pageSize(trips.getTotalPages()).build();
    }

    @Override
    public PaginationResponse getByCategoryLunch(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<RecipeResponse> trips = recipeJDBCTemplate.getByCategoryLunch(pageable);
        return PaginationResponse.builder()
                .recipeResponseList(trips.getContent())
                .currentPage(trips.getNumber() + 1)
                .pageSize(trips.getTotalPages()).build();
    }

    @Override
    public RecipeResponse getRecipeById(Long recipeId) {
        RecipeResponse recipeResponse = recipeJDBCTemplate.getRecipeById(recipeId);
        List<String> ingredientNames = recipeRepository.ingredientNames(recipeId);
        List<String> quantities = recipeRepository.quantities(recipeId);
        recipeResponse.setIngredientNames(ingredientNames);
        recipeResponse.setQuantities(quantities);
        return recipeResponse;

    }

    private void iterateOverPhotos(List<MultipartFile> images, List<Image> tripImages) {
        for (MultipartFile image : images) {
            try {
                Image recipeImage = new Image();
                recipeImage.setUrl(cloudinaryService.uploadFile(image, "recipeImage"));
                imageRepository.save(recipeImage);
                tripImages.add(recipeImage);
            } catch (Exception e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }
    }
}