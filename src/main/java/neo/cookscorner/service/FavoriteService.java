package neo.cookscorner.service;

import neo.cookscorner.dto.SimpleResponse;

public interface FavoriteService {
    SimpleResponse favoriteRecipe(Long recipeId);
}
