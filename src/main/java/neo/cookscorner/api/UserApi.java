package neo.cookscorner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.recipe.PaginationResponse;
import neo.cookscorner.dto.user.UserRequest;
import neo.cookscorner.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserApi {
    private final UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/createProfile")
    public SimpleResponse saveRecipe(@RequestPart("dto")UserRequest userRequest, @RequestPart ("files") List<MultipartFile> images) {
        return userService.createProfile(userRequest,images);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/getAuthorById")
    @Operation(summary = "You can get author by id")
    PaginationResponse getUserById(@RequestParam int currentPage,
                                   @RequestParam int pageSize,
                                   @RequestParam Long authorId) {
        return userService.getUserById(authorId, currentPage, pageSize);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{userId}")
    @Operation(summary = "Following to user")
    public SimpleResponse following(@PathVariable Long userId) {
        return userService.following(userId);
    }
}
