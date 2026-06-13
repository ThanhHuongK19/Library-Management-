package project.librarymanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private Boolean isActive;
    private Set<String> roles;
}