package neo.cookscorner.service;

import neo.cookscorner.dto.SimpleResponse;

public interface LikeService {
    SimpleResponse likingRecipe(Long recipeId);
}
