package neo.cookscorner.repository;

import neo.cookscorner.dto.recipe.RecipeResponse;
import neo.cookscorner.entities.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("select i.names from Recipe r join r.ingredients i where r.recipeId = ?1")
    List<String> ingredientNames(Long recipeId);
    @Query("select i.quantities from Recipe r join r.ingredients i where r.recipeId = ?1")
    List<String> quantities(Long recipeId);
}