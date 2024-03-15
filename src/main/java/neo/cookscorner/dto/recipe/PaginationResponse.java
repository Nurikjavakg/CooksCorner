package neo.cookscorner.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import neo.cookscorner.dto.user.UserResponse;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class PaginationResponse {
    List<RecipeResponse> recipeResponseList;
    private int currentPage;
    private int pageSize;
    private UserResponse userResponse;
}
