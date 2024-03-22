package neo.cookscorner.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.cookscorner.dto.recipe.PaginationResponse;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private Long userId;
    private String userName;
    private int sumRecipe;
    private int sumFollowing;
    private int sumFollowers;
    private String biography;
    private String userImageUrl;
    private PaginationResponse paginationResponse;

    public UserResponse(String userName, int sumRecipe, int sumFollowing, int sumFollowers, String biography, String userImageUrl) {
        this.userName = userName;
        this.sumRecipe = sumRecipe;
        this.sumFollowing = sumFollowing;
        this.sumFollowers = sumFollowers;
        this.biography = biography;
        this.userImageUrl = userImageUrl;
    }

    public UserResponse(Long userId, String userName, String userImageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userImageUrl = userImageUrl;
    }
}