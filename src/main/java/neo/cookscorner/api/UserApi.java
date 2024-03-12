package neo.cookscorner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.authentication.SignInRequest;
import neo.cookscorner.dto.authentication.SignInResponse;
import neo.cookscorner.dto.authentication.SignUpRequest;
import neo.cookscorner.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserApi {

    private final UserService userService;

    @PostMapping("/signIn")
    @Operation(summary = "Sign in to your account")
    public SignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/signUp")
    @Operation(summary = "Register", description = "Account registration")
    public SimpleResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }
}