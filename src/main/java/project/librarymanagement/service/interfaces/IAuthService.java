package project.librarymanagement.service.interfaces;

import project.librarymanagement.dto.request.LoginRequest;
import project.librarymanagement.dto.request.RegisterRequest;
import project.librarymanagement.dto.response.AuthResponse;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
