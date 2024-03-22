package neo.cookscorner.service;

import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.recipe.PaginationResponse;
import neo.cookscorner.dto.user.PaginationResponseUser;
import neo.cookscorner.dto.user.UserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    SimpleResponse manageProfile(UserRequest userRequest, List<MultipartFile> images);
    PaginationResponse getUserById(Long authorId, int currentPage, int pageSize);
    SimpleResponse following(Long userId);
    PaginationResponseUser findChefByName(int currentPage, int pageSize, String chefName);

}
