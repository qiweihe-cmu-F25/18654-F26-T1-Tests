package userprofile.domain;

import jakarta.persistence.*;

import java.util.UUID;

/** Domain models represent the core business entities. These are typically simple Java classes annotated with JPA (Java Persistence API) annotations.
 *  Spring Boot annotations:
 *  {@code @Entity}: is used at the class level and marks the class as a persistent entity. It signals to the JPA provider that the class should be treated as a table in the database.
 *  {@code @Id}: indicates the member field below is the primary key of the current entity.
 *  {@code @GeneratedValue} can be used with parameters alongside {@code @Id} to designate how an entity's unique ID value will be generated. If no parameters are provided, the ID will be generated according to the default algorithm used by the underlying database.
 */
@Entity
@Table(name = "users")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String email;

    private String passwordHash;

    public UserProfile() {}

    public UserProfile(Long id, String username, String email, String passwordHash){
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public UserProfile(String username, String email, String passwordHash){
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

}
