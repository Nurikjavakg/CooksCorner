package neo.cookscorner.repository.jdbcTemplate;

import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.user.UserResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class UserJDBCTemplate {

    private final JdbcTemplate jdbcTemplate;

    public UserResponse getUserById(ResultSet rs, int rowName) throws SQLException {
        return new UserResponse(
                rs.getString("userName"),
                rs.getInt("sumRecipe"),
                rs.getString("biography"),
                rs.getString("userImageUrl")
        );

    }

    public UserResponse getAuthorById(Long authorId) {
        String sql = """
        
        select u.name as userName, 
                count(r.recipe_id) as sumRecipe, 
                u.biography, i.url_image as userImageUrl
        from public.users u
        left join recipes r on u.user_id = r.owner_user_id
        left join public.users_images ui on u.user_id = ui.user_id
        left join images i on ui.image_id = i.id
        where u.user_id = ?
        group by u.name, u.biography, i.url_image
        """;
        return jdbcTemplate.query(sql, this::getUserById, authorId)
                .stream()
                .findFirst()
                .orElse(new UserResponse());
    }
}