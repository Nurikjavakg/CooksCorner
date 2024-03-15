package neo.cookscorner.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.cookscorner.dto.recipe.PaginationResponse;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private String userName;
    private int sumRecipe;
    private String biography;
    private String userImageUrl;
    private PaginationResponse paginationResponse;

    public UserResponse(String userName, int sumRecipe, String biography, String userImageUrl) {
        this.userName = userName;
        this.sumRecipe = sumRecipe;
        this.biography = biography;
        this.userImageUrl = userImageUrl;
    }
}