package neo.cookscorner.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.cookscorner.config.JwtService;
import neo.cookscorner.config.senderConfig.EmailSenderConfig;
import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.authentication.SignInRequest;
import neo.cookscorner.dto.authentication.SignInResponse;
import neo.cookscorner.dto.authentication.SignUpRequest;
import neo.cookscorner.entities.User;
import neo.cookscorner.enums.Role;
import neo.cookscorner.exceptions.AlreadyExistException;
import neo.cookscorner.exceptions.BadCredentialException;
import neo.cookscorner.exceptions.NotFoundException;
import neo.cookscorner.repository.UserRepository;
import neo.cookscorner.service.AuthService;
import org.thymeleaf.context.Context;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailSenderConfig emailSenderConfig;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;

    @Override
    public SimpleResponse signUp(SignUpRequest request) {
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new AlreadyExistException("The user with the email address " + request.getEmail() + " already exists.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (request.getPassword().equals(request.getVerifyPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);

            Context context = new Context();
            sendRegistryEmail(user.getEmail(),context, jwtToken);

            refreshTokenServiceImpl.saveUserToken(user,jwtToken);
            log.info("User successfully saved with the identifier:" + user.getEmail());
            return  SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Success! Please, check your email for the confirmation"+ user.getEmail() +"the link access only for 5 min")
                    .build();
        } else {
            throw new BadCredentialException("The password and password confirmation do not match.");
        }
    }
    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getUserByEmail(signInRequest.email())
                .orElseThrow(() -> {
                    log.info("User with email:" + signInRequest.email() + " not found!");
                    return new NotFoundException("The user with the email address " + signInRequest.email() + " was not found!");
                });

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.email(),
                        signInRequest.password()));
        String accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenServiceImpl.saveUserToken(user, accessToken);
        return SignInResponse.builder()
                .id(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }


    private void sendRegistryEmail(String email, Context context, String jwtToken) {
        context.setVariable("userEmail", email);
        context.setVariable("registry", "https://neobis-front-auth-mu.vercel.app/"+ jwtToken);
        sendConfirmationEmail(email, "Authorization", context);
    }

    private void sendConfirmationEmail(String email, String subject, Context context) {
        emailSenderConfig.sendEmailWithHTMLTemplate(email,"nurmukhamedalymbaiuulu064@gmail.com", subject, "userRegistry", context);
    }
}