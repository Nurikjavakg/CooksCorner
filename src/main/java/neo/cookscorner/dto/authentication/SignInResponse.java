package neo.cookscorner.dto.authentication;

import lombok.Builder;
import neo.cookscorner.enums.Role;

@Builder
public record SignInResponse(
    Long id,
    String accessToken,
    String refreshToken,
    String email,
    Role role
){
}