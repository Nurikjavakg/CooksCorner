package neo.cookscorner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.service.FavoriteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
@Tag(name = "Favorite Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavoriteApi {
    private final FavoriteService favoriteService;

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "You can favorite or unfavorite recipe")
    @PostMapping("/favoriteOrUnFavorite/{recipeId}")
    public SimpleResponse favoriteOrUnFavorite(@PathVariable Long recipeId) {
        return favoriteService.favoriteRecipe(recipeId);
    }
}