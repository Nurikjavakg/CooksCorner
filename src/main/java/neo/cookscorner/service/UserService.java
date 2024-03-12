package neo.cookscorner.service;

import neo.cookscorner.dto.SimpleResponse;
import neo.cookscorner.dto.authentication.SignInRequest;
import neo.cookscorner.dto.authentication.SignInResponse;
import neo.cookscorner.dto.authentication.SignUpRequest;

public interface UserService {
    SimpleResponse signUp(SignUpRequest authenticationSignUpRequest);
    SignInResponse signIn(SignInRequest signInRequest);
}