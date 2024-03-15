package neo.cookscorner.repository.jdbcTemplate;

import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.recipe.RecipeResponse;
import neo.cookscorner.dto.user.UserResponse;
import neo.cookscorner.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecipeJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;

    public RecipeResponse getRecipe(ResultSet rs, int rowName) throws SQLException {
        return new RecipeResponse(
                rs.getLong("recipeId"),
                rs.getString("recipeName"),
                rs.getString("name"),
                rs.getInt("likeCount"),
                rs.getInt("favoriteCount"),
                rs.getString("imageUrl"),
                rs.getString("description"),
                rs.getString("preparationTime"),
                rs.getString("difficulty") == null ? null : Difficulty.valueOf(rs.getString("difficulty"))

        );

    }

    public List<RecipeResponse> getRecipeByCategoryBreakfast(ResultSet rs) throws SQLException {
        List<RecipeResponse> recipeResponses = new ArrayList<>();
        while (rs.next()) {
            RecipeResponse recipeResponse = new RecipeResponse(
                    rs.getLong("recipeId"),
                    rs.getString("recipeName"),
                    rs.getString("name"),
                    rs.getInt("likeCount"),
                    rs.getInt("favoriteCount"),
                    rs.getString("imageUrl")
            );
            recipeResponses.add(recipeResponse);
        }
        return recipeResponses;
    }

    public Page<RecipeResponse> getByCategoryBreakfast(Pageable pageable) {
        String sql = """
                select  r.recipe_id as recipeId,\s
                r.recipe_name recipeName,\s
                u.name as name,\s
                cast(count(l) as int) as likeCount,\s
                cast(count(f) as int) as favoriteCount,\s
                i.url_image as imageUrl
                    from recipes r\s
                    left join likes l on r.recipe_id = l.recipe_recipe_id
                    left join public.users u on u.user_id = r.owner_user_id
                    left join public.favorites f on r.recipe_id = f.recipe_recipe_id\s
                    left join recipes_images ri on r.recipe_id = ri.recipe_id
                    left join images i on ri.image_id = i.id
                where r.category_of_meal = 'Breakfast'
                group by r.recipe_id, u.name, r.recipe_name, i. url_image
                     """;
        List<RecipeResponse> recipeResponses = jdbcTemplate.query(sql, this::getRecipeByCategoryBreakfast);
        assert recipeResponses != null;
        return new PageImpl<>(recipeResponses, pageable, recipeResponses.size());
    }

    public Page<RecipeResponse> getByCategoryDinner(Pageable pageable) {
        String sql = """
                select  r.recipe_id as recipeId,\s
                r.recipe_name recipeName,\s
                u.name as name,\s
                cast(count(l) as int) as likeCount,\s
                cast(count(f) as int) as favoriteCount,\s
                i.url_image as imageUrl
                    from recipes r\s
                    left join likes l on r.recipe_id = l.recipe_recipe_id
                    left join public.users u on u.user_id = r.owner_user_id
                    left join public.favorites f on r.recipe_id = f.recipe_recipe_id\s
                    left join recipes_images ri on r.recipe_id = ri.recipe_id
                    left join images i on ri.image_id = i.id
                where r.category_of_meal = 'Dinner'
                group by r.recipe_id, u.name, r.recipe_name, i. url_image
                     """;
        List<RecipeResponse> recipeResponses = jdbcTemplate.query(sql, this::getRecipeByCategoryBreakfast);
        assert recipeResponses != null;
        return new PageImpl<>(recipeResponses, pageable, recipeResponses.size());
    }

    public Page<RecipeResponse> getByCategoryLunch(Pageable pageable) {
        String sql = """
                select  r.recipe_id as recipeId,\s
                r.recipe_name recipeName,\s
                u.name as name,\s
                cast(count(l) as int) as likeCount,\s
                cast(count(f) as int) as favoriteCount,\s
                i.url_image as imageUrl
                    from recipes r\s
                    left join likes l on r.recipe_id = l.recipe_recipe_id
                    left join public.users u on u.user_id = r.owner_user_id
                    left join public.favorites f on r.recipe_id = f.recipe_recipe_id\s
                    left join recipes_images ri on r.recipe_id = ri.recipe_id
                    left join images i on ri.image_id = i.id
                where r.category_of_meal = 'Lunch'
                group by r.recipe_id, u.name, r.recipe_name, i. url_image
                     """;
        List<RecipeResponse> recipeResponses = jdbcTemplate.query(sql, this::getRecipeByCategoryBreakfast);
        assert recipeResponses != null;
        return new PageImpl<>(recipeResponses, pageable, recipeResponses.size());
    }

    public RecipeResponse getRecipeById(Long recipeId) {
        String sql = """
            select  r.recipe_id as recipeId,
            r.recipe_name as recipeName,
            u.name as name,
            cast(count(l) as int) as likeCount,
            cast(count(f) as int) as favoriteCount,
            i.url_image as imageUrl,
            r.description as description, 
            r.preparation_time as preparationTime,
            r.difficulty as difficulty
            from recipes r
            left join likes l on r.recipe_id = l.recipe_recipe_id
            left join public.users u on u.user_id = r.owner_user_id
            left join public.favorites f on r.recipe_id = f.recipe_recipe_id
            left join recipes_images ri on r.recipe_id = ri.recipe_id
            left join images i on ri.image_id = i.id
            where r.recipe_id = ?
            group by r.recipe_id, u.name, r.recipe_name, i.url_image
            """;
        return jdbcTemplate.query(sql, this::getRecipe, recipeId)
                .stream()
                .findFirst()
                .orElse(new RecipeResponse());
    }

    public Page<RecipeResponse> getRecipesFromAuthor(Pageable pageable, Long authorId) {
        String sql = """
        select
            r.recipe_id as recipeId,
            r.recipe_name as recipeName,
            u.name as name,
            cast(count(l) as int) as likeCount,
            cast(count(f) as int) as favoriteCount,
            i.url_image as imageUrl
        from
            recipes r
            left join likes l on r.recipe_id = l.recipe_recipe_id
            left join public.users u on u.user_id = r.owner_user_id
            left join public.favorites f on r.recipe_id = f.recipe_recipe_id
            left join recipes_images ri on r.recipe_id = ri.recipe_id
            left join images i on ri.image_id = i.id
        where
            r.owner_user_id = ?
        group by
            r.recipe_id, u.name, r.recipe_name, i.url_image
    """;

        List<RecipeResponse> recipeResponses = jdbcTemplate.query(sql, this::getRecipeByCategoryBreakfast, authorId);
        assert recipeResponses != null;
        return new PageImpl<>(recipeResponses, pageable, recipeResponses.size());
    }
}