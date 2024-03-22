package neo.cookscorner.repository.jdbcTemplate;

import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.recipe.RecipeResponse;
import neo.cookscorner.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;

    public UserResponse getUserById(ResultSet rs, int rowName) throws SQLException {
        return new UserResponse(
                rs.getString("userName"),
                rs.getInt("sumRecipe"),
                rs.getInt("sumFollowing"),
                rs.getInt("sumFollowers"),
                rs.getString("biography"),
                rs.getString("userImageUrl")
        );
    }

    public UserResponse getUserByName(ResultSet rs, int rowName) throws SQLException {
        return new UserResponse(
                rs.getLong("userId"),
                rs.getString("userName"),
                rs.getString("userImageUrl")
        );

    }

    public UserResponse getAuthorById(Long authorId) {
        String sql = """
                           
                   select u.name as userName,
                count(distinct r.recipe_id) as sumRecipe,
                u.biography,
                i.url_image as userImageUrl,
                count(distinct uf.following) as sumFollowing,
                count(distinct f.followers) as sumFollowers
                from users u
                left join recipes r on u.user_id = r.owner_user_id
                left join users_images ui on u.user_id = ui.user_id
                left join images i on ui.image_id = i.id
                left join user_following uf on u.user_id = uf.user_user_id
                left join users f1 on f1.user_id = uf.following
                left join user_followers f on u.user_id = f.user_user_id
                left join users f2 on f2.user_id = f.followers
                where u.user_id = ? group by u.name, u.biography, i.url_image;
                   """;
        return jdbcTemplate.query(sql, this::getUserById, authorId)
                .stream()
                .findFirst()
                .orElse(new UserResponse());
    }


    public Page<UserResponse> findChefByName(Pageable pageable, String userName) {
        String sql = """
        select
            u.user_id as userId,
            u.name as userName,
            i.url_image as userImageUrl
        from
            users u 
            left join users_images ui on u.user_id = ui.user_id
            left join images i on ui.image_id = i.id
        where
            u.name ILIKE '%' || ? || '%'
        group by
            u.user_id, u.name, i.url_image
    """;
        List<UserResponse> userResponses = jdbcTemplate.query(sql, this::getUserByName, userName);
        return new PageImpl<>(userResponses, pageable, userResponses.size());
    }
}