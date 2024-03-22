package neo.cookscorner.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class PaginationResponseUser {
    private List<UserResponse> userResponse;
    private int currentPage;
    private int pageSize;
}