package neo.cookscorner.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.user.UserRequest;
import neo.cookscorner.entities.Image;
import neo.cookscorner.entities.User;
import neo.cookscorner.exceptions.NotFoundException;
import neo.cookscorner.repository.ImageRepository;
import neo.cookscorner.repository.IngredientRepository;
import neo.cookscorner.repository.UserRepository;
import neo.cookscorner.service.CloudinaryService;
import neo.cookscorner.service.UserService;
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
