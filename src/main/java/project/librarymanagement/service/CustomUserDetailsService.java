package project.librarymanagement.service;

import project.librarymanagement.entity.Users;
import project.librarymanagement.repository.UsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public CustomUserDetailsService(
            UsersRepository usersRepository
    ) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        Users user = usersRepository.findUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new UsernameNotFoundException(
                    "User account is inactive"
            );
        }

        return new User(
                user.getUsername(),
                user.getPasswordHash(),
                true,
                true,
                true,
                true,
                user.getRoles()
                        .stream()
                        .map(role ->
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role.getRoleName().name()
                                )
                        )
                        .collect(Collectors.toList())
        );
    }
}