package neo.cookscorner.dto.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.cookscorner.enums.Difficulty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeResponse{
        private Long recipeId;
        private String recipeName;
        private String ownerName;
        private int likeCount;
        private int favoriteCount;
        private String imageUrl;

    private String description;;
    private String preparationTime;
    private Difficulty difficulty;;
    List<String> ingredientNames;
    List<String> quantities;

    public RecipeResponse(Long recipeId, String recipeName, String ownerName,
                          int likeCount, int favoriteCount, String imageUrl, String description,
                          String preparationTime, Difficulty difficulty) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ownerName = ownerName;
        this.likeCount = likeCount;
        this.favoriteCount = favoriteCount;
        this.imageUrl = imageUrl;
        this.description = description;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
    }

    public RecipeResponse(Long recipeId, String recipeName, String ownerName, int likeCount, int favoriteCount, String imageUrl) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ownerName = ownerName;
        this.likeCount = likeCount;
        this.favoriteCount = favoriteCount;
        this.imageUrl = imageUrl;
    }
}