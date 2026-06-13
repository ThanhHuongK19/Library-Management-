package project.librarymanagement.service.implementations;

import project.librarymanagement.config.JwtUtil;
import project.librarymanagement.dto.request.LoginRequest;
import project.librarymanagement.dto.request.RegisterRequest;
import project.librarymanagement.dto.response.AuthResponse;
import project.librarymanagement.entity.Roles;
import project.librarymanagement.entity.Roles.RoleName;
import project.librarymanagement.entity.Users;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.repository.RolesRepository;
import project.librarymanagement.repository.UsersRepository;
import project.librarymanagement.service.interfaces.IAuthService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService implements IAuthService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(
            UsersRepository usersRepository,
            RolesRepository rolesRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {

        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (usersRepository.existsUserByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (usersRepository.existsUserByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Roles memberRole = rolesRepository.findRoleByRoleName(RoleName.USER)
                .orElseThrow(() ->
                        new BadRequestException("Default role USER not found")
                );

        Set<Roles> roles = new HashSet<>();
        roles.add(memberRole);

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRoles(roles);

        usersRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(token, "Bearer");
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getUsername());

        return new AuthResponse(token, "Bearer");
    }
}