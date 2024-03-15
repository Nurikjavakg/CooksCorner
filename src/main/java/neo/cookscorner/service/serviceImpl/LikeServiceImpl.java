package neo.cookscorner.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.entities.Like;
import neo.cookscorner.entities.Recipe;
import neo.cookscorner.entities.User;
import neo.cookscorner.exceptions.NotFoundException;
import neo.cookscorner.repository.*;
import neo.cookscorner.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class LikeServiceImpl implements LikeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

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
    public SimpleResponse likingRecipe(Long recipeId) {
        User user = getAuthFromUser();
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> {
                    log.info("Recipe with id: " + recipeId + " not found...");
                    return new NotFoundException(String.format("Recipe with id: %s not found...", recipeId));
                });

        List<Like> findAllLikes = likeRepository.findAll();
        for(Like like : findAllLikes){
            if(like.getUser().equals(user) && like.getRecipe().equals(recipe)){
                likeRepository.deleteById(like.getId());
                log.info("Disliked...");
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Disliked...")
                        .build();
            }
        }
        Like like = new Like();
        like.setUser(user);
        like.setRecipe(recipe);
        likeRepository.save(like);
        log.info("Liked...");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Liked...")
                .build();
    }
}
