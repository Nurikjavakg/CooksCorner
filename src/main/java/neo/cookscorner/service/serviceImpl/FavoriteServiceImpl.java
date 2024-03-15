package neo.cookscorner.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.entities.Favorite;
import neo.cookscorner.entities.Recipe;
import neo.cookscorner.entities.User;
import neo.cookscorner.exceptions.NotFoundException;
import neo.cookscorner.repository.FavoriteRepository;
import neo.cookscorner.repository.RecipeRepository;
import neo.cookscorner.repository.UserRepository;
import neo.cookscorner.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

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
    public SimpleResponse favoriteRecipe(Long recipeId) {
        User user = getAuthFromUser();
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> {
                    log.info("Recipe with id: " + recipeId + " not found...");
                    return new NotFoundException(String.format("Recipe with id: %s not found...", recipeId));
                });
        List<Favorite> favorites = favoriteRepository.findAll();
        for (Favorite favorite : favorites) {
            if (favorite.getUser().equals(user) && favorite.getRecipe().equals(recipe)) {
                favoriteRepository.deleteById(favorite.getId());
                log.info("Unfavorited...");
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Unfavorited...")
                        .build();
            }
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        favoriteRepository.save(favorite);
        log.info("favorited...");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("favorited...")
                .build();
    }
}