package neo.cookscorner.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.recipe.PaginationResponse;
import neo.cookscorner.dto.recipe.RecipeResponse;
import neo.cookscorner.dto.user.UserRequest;
import neo.cookscorner.dto.user.UserResponse;
import neo.cookscorner.entities.Image;
import neo.cookscorner.entities.User;
import neo.cookscorner.exceptions.AlreadyExistException;
import neo.cookscorner.exceptions.BadCredentialException;
import neo.cookscorner.exceptions.NotFoundException;
import neo.cookscorner.repository.ImageRepository;
import neo.cookscorner.repository.UserRepository;
import neo.cookscorner.repository.jdbcTemplate.RecipeJDBCTemplate;
import neo.cookscorner.repository.jdbcTemplate.UserJDBCTemplate;
import neo.cookscorner.service.CloudinaryService;
import neo.cookscorner.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;
    private final UserJDBCTemplate userJDBCTemplate;
    private final RecipeJDBCTemplate recipeJDBCTemplate;

    public User getAuthFromUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> {
                    log.info("User with email: " + email + " not found!");
                    return new NotFoundException(String.format("User with email address: %s not found!", email));
                });
    }

    @Override
    public SimpleResponse createProfile(UserRequest userRequest, List<MultipartFile> images) {
        User user = getAuthFromUser();
        user.setName(userRequest.userName());
        user.setBiography(userRequest.biography());

        List<Image> userImages = new ArrayList<>();
        iterateOverPhotos(images, userImages);
        user.setImages(userImages);
        log.info("User profile successfully created...");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("User profile successfully created...")
                .build();
    }

    @Override
    public PaginationResponse getUserById(Long authorId, int currentPage, int pageSize) {
       UserResponse userResponse = userJDBCTemplate.getAuthorById(authorId);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<RecipeResponse> recipes = recipeJDBCTemplate.getRecipesFromAuthor(pageable,authorId);

        return PaginationResponse.builder()
                .recipeResponseList(recipes.getContent())
                .currentPage(recipes.getNumber() + 1)
                .pageSize(recipes.getTotalPages())
                .userResponse(userResponse).build();
    }

    @Override
    public SimpleResponse following(Long userId) {
        User following = getAuthFromUser();
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.info("User with userId: " + userId + " not found...");
                    return new NotFoundException(String.format("User with userId: %s не not found...", userId));
                });

        if (user.getFollowers().contains(following.getUserId())) {
            log.info("User already exists in your friend list");
            throw new AlreadyExistException("User already exists in your following list");
        }
        if (following.getUserId().equals(user.getUserId())) {
            log.info("You have not permission sent request yourself");
            throw new BadCredentialException("You have not permission sent request yourself");
        }

        user.addFollowers(following);
        following.addFollowing(user);

        userRepository.save(user);
        userRepository.save(following);
        log.info("Your following successfully sand");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Your following successfully sand")
                .build();
    }

    private void iterateOverPhotos(List<MultipartFile> images, List<Image> tripImages) {
        for (MultipartFile image : images) {
            try {
                Image recipeImage = new Image();
                recipeImage.setUrl(cloudinaryService.uploadFile(image, "userImage"));
                imageRepository.save(recipeImage);
                tripImages.add(recipeImage);
            } catch (Exception e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }
    }
}
