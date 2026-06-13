package project.librarymanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Roles {

  public enum RoleName {
    ADMIN,
    LIBRARIAN,
    MEMBER
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private long roleId;

  @Enumerated(EnumType.STRING)
  @Column(name = "role_name")
  private RoleName roleName;

  @JsonIgnore
  @ManyToMany(mappedBy = "roles")
  private Set<Users> users = new HashSet<>();
}