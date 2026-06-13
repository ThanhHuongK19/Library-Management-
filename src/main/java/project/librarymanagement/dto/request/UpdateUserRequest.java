package project.librarymanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank
    @Size(max = 50)
    private String username;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Size(max = 155)
    private String fullName;

    private Boolean isActive;
}
