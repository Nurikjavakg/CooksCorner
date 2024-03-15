package neo.cookscorner.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.service.LikeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
@Tag(name = "Like Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LikeApi {
    private final LikeService likingRecipe;

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "You can like or dislike recipe")
    @PostMapping("/LikeOrDislike/{recipeId}")
    public SimpleResponse LikeOrDislike(@PathVariable Long recipeId) {
        return likingRecipe.likingRecipe(recipeId);
    }
}